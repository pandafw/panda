package panda.filepool.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DataHandler;
import panda.filepool.FileItem;
import panda.filepool.FilePool;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.time.DateTimes;
import panda.mvc.adaptor.multipart.FileItemStream;

public class DaoFilePool implements FilePool {
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

	public FileItem saveFile(String name, final InputStream body, boolean temporary) throws IOException {
		final FileItem fi = new FileItem();
		
		fi.setName(name);
		fi.setDate(DateTimes.getDate());
		fi.setData(Streams.toByteArray(body));
		fi.setFlag(temporary ? FileItem.TEMPORARY : FileItem.ARCHIVE);
		
		final Dao dao = getDaoClient().getDao();
		dao.exec(new Runnable() {
			public void run() {
				dao.insert(fi);

				int len = fi.getData().length;
				for (int i = 0; i < len; i += blockSize) {
					FileData fd = new FileData();
					fd.setFid(fi.getId());
					fd.setBno(i);
					int bs = blockSize;
					if (i + bs > len) {
						bs = len- i;
					}

					byte[] buf = new byte[bs];
					System.arraycopy(fi.getData(), i, buf, 0, bs);

					fd.setSize(bs);
					fd.setData(buf);
	
					dao.insert(fd);
				}
			}
		});
		return fi;
	}
	
	public FileItem saveFile(FileItemStream fis, boolean temporary) throws IOException {
		String name = FileNames.getBaseName(fis.getName());
		return saveFile(name, fis.openStream(), temporary);
	}

	public InputStream openFile(Long id) throws IOException {
		Dao dao = getDaoClient().getDao();
		FileItem fi = dao.fetch(FileItem.class, id);
		return openFile(fi);
	}
	
	public InputStream openFile(FileItem fi) throws IOException {
		final byte[] buf = new byte[fi.getSize()];

		Dao dao = getDaoClient().getDao();
		FileDataQuery fdq = new FileDataQuery();
		
		fdq.fid().equalTo(fi.getId()).bno().asc();
		
		dao.select(fdq, new DataHandler<FileData>() {
			private int len = 0;

			public boolean handle(FileData data) throws Exception {
				System.arraycopy(data.getData(), 0, buf, len, data.getData().length);
				len += data.getData().length;
				return true;
			}
		});
		
		fi.setData(buf);
		
		return new ByteArrayInputStream(buf);
	}
}
