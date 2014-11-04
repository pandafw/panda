package panda.filepool.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.DataHandler;
import panda.filepool.FileItem;
import panda.filepool.FilePool;
import panda.filepool.NullFileItem;
import panda.io.FileNames;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.time.DateTimes;
import panda.mvc.MvcConstants;
import panda.mvc.adaptor.multipart.FileItemStream;

@IocBean(type=FilePool.class)
public class DaoFilePool implements FilePool {
	@IocInject
	protected DaoClient daoClient;

	@IocInject(value=MvcConstants.FILEPOOL_DAO_BLOCK_SIZE, required=false)
	protected int blockSize = Integer.MAX_VALUE;

	/**
	 * milliseconds since last modified. (default: 1h) 
	 */
	@IocInject(value=MvcConstants.FILEPOOL_EXPIRES, required=false)
	protected long expires = 60 * 60 * 1000;

	/**
	 * @return the daoClient
	 */
	public DaoClient getDaoClient() {
		return daoClient;
	}

	/**
	 * @param daoClient the daoClient to set
	 */
	public void setDaoClient(DaoClient daoClient) {
		this.daoClient = daoClient;
	}

	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * @param blockSize the blockSize to set
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	protected FileItem saveFile(FileItem file, final InputStream body) throws IOException {
		final byte[] data = Streams.toByteArray(body);
		return saveFile(file, data);
	}
	
	protected FileItem saveFile(FileItem file, final byte[] data) throws IOException {
		final DaoFileItem fi = (DaoFileItem)file;
		
		fi.setDaoFilePool(this);
		fi.setDate(DateTimes.getDate());
		fi.setData(data);
		fi.setSize(data.length);
		
		try {
			final Dao dao = getDaoClient().getDao();
			dao.exec(new Runnable() {
				public void run() {
					dao.update(fi);
					saveData(dao, fi, data);
				}
			});
		}
		catch (DaoException e) {
			throw new IOException("Failed to save file " + fi.getName());
		}
		return fi;
	}

	public FileItem saveFile(String name, final InputStream data, boolean temporary) throws IOException {
		return saveFile(name, Streams.toByteArray(data), temporary);
	}
	
	public FileItem saveFile(String name, final byte[] data, boolean temporary) throws IOException {
		final DaoFileItem fi = new DaoFileItem();
		
		fi.setDaoFilePool(this);
		fi.setName(name);
		fi.setDate(DateTimes.getDate());
		fi.setData(data);
		fi.setSize(data.length);
		fi.setFlag(temporary ? DaoFileItem.TEMPORARY : DaoFileItem.ARCHIVE);
		
		try {
			final Dao dao = getDaoClient().getDao();
			dao.exec(new Runnable() {
				public void run() {
					dao.insert(fi);
	
					FileDataQuery fdq = new FileDataQuery();
					fdq.fid().equalTo(fi.getId());
					dao.deletes(fdq);
	
					saveData(dao, fi, data);
				}
			});
		}
		catch (DaoException e) {
			throw new IOException("Failed to save file " + name);
		}
		return fi;
	}

	private void saveData(Dao dao, FileItem fi, byte[] data) {
		int len = data.length;
		for (int i = 0; i < len; i += blockSize) {
			DaoFileData fd = new DaoFileData();
			fd.setFid(fi.getId());
			fd.setBno(i);
			int bs = blockSize;
			if (i + bs > len) {
				bs = len- i;
			}

			byte[] buf = new byte[bs];
			System.arraycopy(data, i, buf, 0, bs);

			fd.setSize(bs);
			fd.setData(buf);

			dao.insert(fd);
		}
	}

	public FileItem saveFile(FileItemStream fis, boolean temporary) throws IOException {
		String name = FileNames.getName(fis.getName());
		return saveFile(name, fis.openStream(), temporary);
	}

	public FileItem findFile(Long id) {
		Dao dao = getDaoClient().getDao();
		DaoFileItem fi = dao.fetch(DaoFileItem.class, id);
		return fi == null ? new NullFileItem(id) : fi;
	}
	
	protected byte[] readFile(DaoFileItem fi) throws IOException {
		try {
			final byte[] buf = new byte[fi.getSize()];
	
			if (fi.getSize() > 0) {
				Dao dao = getDaoClient().getDao();
				FileDataQuery fdq = new FileDataQuery();
				
				fdq.fid().equalTo(fi.getId()).bno().asc();
				
				dao.select(fdq, new DataHandler<DaoFileData>() {
					private int len = 0;
		
					public boolean handle(DaoFileData data) throws Exception {
						System.arraycopy(data.getData(), 0, buf, len, data.getData().length);
						len += data.getData().length;
						return true;
					}
				});
			}
			
			fi.setData(buf);
			
			return buf;
		}
		catch (DaoException e) {
			throw new IOException("Failed to read file: " + fi.getId());
		}
	}

	private static class DeleteFile implements Runnable {
		private Dao dao;
		private FileItem file;
		
		public DeleteFile(Dao dao, FileItem file) {
			this.dao = dao;
			this.file = file;
		}
		
		public void run() {
			FileDataQuery fdq = new FileDataQuery();
			fdq.fid().equalTo(file.getId());
			dao.deletes(fdq);
			
			dao.delete(file);
		}
	}

	protected void deleteFile(final DaoFileItem file) throws IOException {
		try {
			final Dao dao = getDaoClient().getDao();
			dao.exec(new DeleteFile(dao, file));
	
			file.setExists(false);
			file.setData(null);
		}
		catch (DaoException e) {
			throw new IOException("Failed to delete file: " + file.getId());
		}
	}
	
	public void clean() throws IOException {
		final Dao dao = getDaoClient().getDao();
		final Date time = new Date(System.currentTimeMillis() - expires);
		
		FileItemQuery fiq = new FileItemQuery();
		fiq.flag().equalTo(DaoFileItem.TEMPORARY).date().lessThan(time);
		
		final List<DaoFileItem> fis = dao.select(fiq);
		if (Collections.isEmpty(fis)) {
			return;
		}
		
		for (DaoFileItem fi : fis) {
			try {
				dao.exec(new DeleteFile(dao, fi));
			}
			catch (DaoException e) {
				throw new IOException("Failed to delete file: " + fi.getId());
			}
		}
	}
}
