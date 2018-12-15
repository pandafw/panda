package panda.app.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import panda.app.entity.Media;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.FilePools;

@IocBean(type=MediaDataSaver.class)
public class MediaDataDaoSaver implements MediaDataSaver {
	private static final Log log = Logs.getLog(MediaDataDaoSaver.class);

	private static final int ORIGINAL = 0;

	@IocInject
	private DaoClient daoClient;
	

	public MediaData find(long id) {
		return daoClient.getDao().fetch(MediaData.class, id, ORIGINAL);
	}

	public MediaData find(long id, int sz) {
		Dao dao = daoClient.getDao();

		MediaData md = dao.fetch(MediaData.class, id, sz);
		if (md == null) {
			MediaData mo = dao.fetch(MediaData.class, id, ORIGINAL);
			if (mo == null) {
				return null;
			}

			try {
				// resize
				ImageWrapper iw = Images.i().read(mo.getData());
				iw = iw.resize(sz);
				byte[] data = iw.getData();

				// save
				md = new MediaData();
				md.setMid(id);
				md.setMsz(sz);
				md.setSize(data.length);
				md.setData(data);
				dao.insert(md);
			}
			catch (Exception e) {
				log.error("Failed to save data of media [" + id + "] (" + sz + ")", e);
			}
		}
		return md;
	}

	public void save(Media m) {
		try {
			Dao dao = daoClient.getDao();

			MediaData md = new MediaData();
			md.setMid(m.getId());
			md.setMsz(ORIGINAL);
			md.setSize(m.getFile().getSize());
			md.setData(m.getFile().data());
			dao.save(md);

			FilePools.safeDelete(m.getFile());
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	public void delete(Media m) {
		try {
			Dao dao = daoClient.getDao();

			MediaDataQuery mdq = new MediaDataQuery();
			mdq.mid().equalTo(m.getId());
			dao.deletes(mdq);
		}
		catch (Exception e) {
			log.error("Failed to delete media data for " + m.getId(), e);
		}
	}

	public void deletes(List<Media> ms) {
		List<Long> ids = new ArrayList<Long>(ms.size());
		for (Media m : ms) {
			ids.add(m.getId());
		}

		try {
			Dao dao = daoClient.getDao();

			MediaDataQuery mdq = new MediaDataQuery();
			mdq.mid().in(ids);
			dao.deletes(mdq);
		}
		catch (Exception e) {
			log.error("Failed to delete media data for " + Strings.join(ids, ", "), e);
		}
	}
}
