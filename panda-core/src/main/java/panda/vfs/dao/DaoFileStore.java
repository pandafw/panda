package panda.vfs.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.DaoIterator;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.vfs.FileItem;
import panda.vfs.FileStore;

public class DaoFileStore implements FileStore {
	private DaoClient daoClient;

	private int blockSize = Integer.MAX_VALUE;

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

	@Override
	public Class<? extends FileItem> getItemType() {
		return DaoFileItem.class;
	}
	
	protected void saveFile(DaoFileItem file, InputStream body) throws IOException {
		saveFile(file, Streams.toByteArray(body));
	}
	
	protected void saveFile(final DaoFileItem fi, final byte[] data) throws IOException {
		fi.setDaoFileStore(this);
		fi.setDate(DateTimes.getDate());
		fi.setData(data);
		fi.setSize(data.length);
		
		try {
			final Dao dao = getDaoClient().getDao();
			dao.exec(new Runnable() {
				public void run() {
					if (fi.isExists()) {
						dao.update(fi);

						FileDataQuery fdq = new FileDataQuery();
						fdq.fnm().eq(fi.getName());
						dao.deletes(fdq);
					}
					else {
						dao.insert(fi);
					}
					saveData(dao, fi, data);
				}
			});
			fi.setExists(true);
		}
		catch (DaoException e) {
			throw new IOException("Failed to save file " + fi.getName(), e);
		}
	}

	private void saveData(Dao dao, FileItem fi, byte[] data) {
		int len = data.length;
		for (int i = 0; i < len; i += blockSize) {
			DaoFileData fd = new DaoFileData();
			fd.setFnm(fi.getName());
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
	public FileItem getFile(String name) throws IOException {
		try {
			Dao dao = getDaoClient().getDao();
			DaoFileItem fi = dao.fetch(DaoFileItem.class, name);
			if (fi == null) {
				fi = new DaoFileItem();
				fi.setName(name);
				fi.setExists(false);
				fi.setSize(0);
				fi.setData(Arrays.EMPTY_BYTE_ARRAY);
			}
			fi.setDaoFileStore(this);
			return fi;
		}
		catch (DaoException e) {
			throw new IOException("Failed to find file " + name, e);
		}
	}

	@Override
	public List<FileItem> listFiles() throws IOException {
		return listFiles();
	}
	
	@Override
	public List<FileItem> listFiles(String prefix, Date before) throws IOException {
		try {
			Dao dao = getDaoClient().getDao();

			FileItemQuery fiq = new FileItemQuery();
			if (Strings.isNotEmpty(prefix)) {
				fiq.name().startsWith(prefix);
			}
			if (before != null) {
				fiq.date().lt(before);
			}

			List<FileItem> list = new ArrayList<FileItem>();
			try (DaoIterator<DaoFileItem> it = dao.iterate(fiq)) {
				while (it.hasNext()) {
					DaoFileItem fi = it.next();
					fi.setDaoFileStore(this);
					list.add(fi);
				}
			}
			return list;
		}
		catch (DaoException e) {
			throw new IOException("Failed to list files (" + prefix + ", " + before + ")", e);
		}
	}
	
	protected byte[] readFile(DaoFileItem fi) throws IOException {
		try {
			final byte[] buf = new byte[fi.getSize()];
	
			if (fi.getSize() > 0) {
				Dao dao = getDaoClient().getDao();
				FileDataQuery fdq = new FileDataQuery();
				
				fdq.fnm().eq(fi.getName()).bno().asc();
				
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
			throw new IOException("Failed to read file " + fi.getName(), e);
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
			fdq.fnm().eq(file.getName());
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
			throw new IOException("Failed to delete file " + file.getName(), e);
		}
	}
}
