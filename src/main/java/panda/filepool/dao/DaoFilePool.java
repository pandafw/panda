package panda.filepool.dao;

import java.io.IOException;
import java.io.InputStream;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DataHandler;
import panda.filepool.FileItem;
import panda.filepool.FilePool;
import panda.filepool.NullFileItem;
import panda.io.FileNames;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.time.DateTimes;
import panda.mvc.adaptor.multipart.FileItemStream;

@IocBean(type=FilePool.class)
public class DaoFilePool implements FilePool {
	@IocInject
	protected DaoClient daoClient;

	@IocInject(value="ref:filepool.data.blocksize", required=false)
	protected int blockSize = Integer.MAX_VALUE;

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

	public FileItem saveFile(String name, final InputStream body, boolean temporary) throws IOException {
		final DaoFileItem fi = new DaoFileItem();
		
		final byte[] data = Streams.toByteArray(body);
		
		fi.setDaoFilePool(this);
		fi.setName(name);
		fi.setDate(DateTimes.getDate());
		fi.setData(data);
		fi.setSize(data.length);
		fi.setFlag(temporary ? FileItem.TEMPORARY : FileItem.ARCHIVE);
		
		final Dao dao = getDaoClient().getDao();
		dao.exec(new Runnable() {
			public void run() {
				dao.insert(fi);

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
		});
		return fi;
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
	
	protected byte[] readFile(DaoFileItem fi) {
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
}
