package panda.vfs.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.DaoIterator;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.mutable.MutableInt;
import panda.lang.time.DateTimes;
import panda.vfs.FileItem;
import panda.vfs.FilePool;
import panda.vfs.NullFileItem;

public class DaoFilePool implements FilePool {
	private DaoClient daoClient;

	private int blockSize = Integer.MAX_VALUE;

	/**
	 * milliseconds since last modified. (default: 1 day) 
	 */
	private long expires = DateTimes.MS_DAY;

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

	/**
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return (int)(expires / 1000);
	}

	/**
	 * @param maxage the maxage to set
	 */
	public void setMaxAge(int maxage) {
		this.expires = maxage * 1000L;
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
			throw new IOException("Failed to save file " + fi.getName(), e);
		}
		return fi;
	}

	@Override
	public Class<? extends FileItem> getItemType() {
		return DaoFileItem.class;
	}
	
	@Override
	public FileItem saveFile(String name, final InputStream data) throws IOException {
		return saveFile(name, Streams.toByteArray(data));
	}
	
	@Override
	public FileItem saveFile(String name, final byte[] data) throws IOException {
		name = Strings.right(name, FileNames.MAX_FILENAME_LENGTH);

		final DaoFileItem fi = new DaoFileItem();
		fi.setDaoFilePool(this);
		fi.setName(name);
		fi.setDate(DateTimes.getDate());
		fi.setData(data);
		fi.setSize(data.length);
		
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
			throw new IOException("Failed to save file " + name, e);
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
				bs = len - i;
			}

			byte[] buf;
			if (i == 0 && bs == len) {
				buf = data;
			}
			else {
				buf = new byte[bs];
				System.arraycopy(data, i, buf, 0, bs);
			}

			fd.setSize(bs);
			fd.setData(buf);

			dao.insert(fd);
		}
	}

	@Override
	public FileItem findFile(Long id) {
		Dao dao = getDaoClient().getDao();
		DaoFileItem fi = dao.fetch(DaoFileItem.class, id);
		if (fi == null) {
			return new NullFileItem(id);
		}
		fi.setDaoFilePool(this);
		return fi;
	}
	
	protected byte[] readFile(DaoFileItem fi) throws IOException {
		try {
			final byte[] buf = new byte[fi.getSize()];
	
			if (fi.getSize() > 0) {
				Dao dao = getDaoClient().getDao();
				FileDataQuery fdq = new FileDataQuery();
				
				fdq.fid().equalTo(fi.getId()).bno().asc();
				
				DaoIterator<DaoFileData> it = dao.iterate(fdq);
				try {
					int len = 0;
					while (it.hasNext()) {
						DaoFileData data = it.next();
						System.arraycopy(data.getData(), 0, buf, len, data.getData().length);
						len += data.getData().length;
					}
				}
				finally {
					it.close();
				}
			}
			
			fi.setData(buf);
			
			return buf;
		}
		catch (DaoException e) {
			throw new IOException("Failed to read file: " + fi.getId(), e);
		}
	}

	private static class DeleteFile implements Runnable {
		private Dao dao;
		private FileItem file;
		private MutableInt count;
		
		public DeleteFile(Dao dao, FileItem file) {
			this.dao = dao;
			this.file = file;
		}
		
		public DeleteFile(Dao dao, FileItem file, MutableInt count) {
			this.dao = dao;
			this.file = file;
			this.count = count;
		}
		
		public void run() {
			FileDataQuery fdq = new FileDataQuery();
			fdq.fid().equalTo(file.getId());
			dao.deletes(fdq);
			
			int cnt = dao.delete(file);
			if (count != null) {
				count.add(cnt);
			}
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
			throw new IOException("Failed to delete file: " + file.getId(), e);
		}
	}
	
	@Override
	public int clean() throws IOException {
		final Dao dao = getDaoClient().getDao();
		final Date time = new Date(System.currentTimeMillis() - expires);
		
		FileItemQuery fiq = new FileItemQuery();
		fiq.date().lessThan(time);
		
		final List<DaoFileItem> fis = dao.select(fiq);
		if (Collections.isEmpty(fis)) {
			return 0;
		}
		
		MutableInt cnt = new MutableInt();
		for (DaoFileItem fi : fis) {
			try {
				dao.exec(new DeleteFile(dao, fi, cnt));
			}
			catch (DaoException e) {
				throw new IOException("Failed to delete file: " + fi.getId(), e);
			}
		}
		return cnt.intValue();
	}
}
