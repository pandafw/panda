package panda.app.media;

import java.io.IOException;

import panda.app.entity.Media;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.FileStores;

@IocBean(type=MediaDataSaver.class)
public class MediaDataDaoSaver implements MediaDataSaver {
	private static final Log log = Logs.getLog(MediaDataDaoSaver.class);

	@IocInject
	private DaoClient daoClient;
	

	@Override
	public MediaData find(String id) {
		return daoClient.getDao().fetch(MediaData.class, id, Medias.ORIGINAL);
	}

	@Override
	public MediaData find(String id, int sz) {
		Dao dao = daoClient.getDao();

		MediaData md = dao.fetch(MediaData.class, id, sz);
		if (md == null) {
			MediaData mo = dao.fetch(MediaData.class, id, Medias.ORIGINAL);
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
			md.setMsz(Medias.ORIGINAL);
			md.setSize(m.getFile().getSize());
			md.setData(m.getFile().data());
			dao.save(md);

			FileStores.safeDelete(m.getFile());
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	@Override
	public void delete(String... mids) {
		if (Arrays.isEmpty(mids)) {
			return;
		}
		
		try {
			Dao dao = daoClient.getDao();

			MediaDataQuery mdq = new MediaDataQuery();
			mdq.mid().in(mids);
			dao.deletes(mdq);
		}
		catch (Exception e) {
			log.error("Failed to delete media data for " + Strings.join(mids, ", "), e);
		}
	}

	@Override
	public void delete(Media... ms) {
		if (Arrays.isEmpty(ms)) {
			return;
		}
		
		String[] ids = new String[ms.length];
		for (int i = 0; i < ms.length; i++) {
			ids[i] = ms[i].getId();
		}

		delete(ids);
	}
}
