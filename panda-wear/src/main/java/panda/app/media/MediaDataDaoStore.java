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
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

@IocBean(type=MediaDataStore.class)
public class MediaDataDaoStore extends AbstractMediaDataStore {
	private static final Log log = Logs.getLog(MediaDataDaoStore.class);

	@IocInject
	private DaoClient daoClient;

	@Override
	public MediaData find(Media m, int sz) {
		Dao dao = daoClient.getDao();

		MediaData md = dao.fetch(MediaData.class, m.getId(), sz);
		if (md == null && sz != Medias.ORIGINAL) {
			MediaData mo = dao.fetch(MediaData.class, m.getId(), Medias.ORIGINAL);
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
				md.setMid(m.getId());
				md.setMsz(sz);
				md.setSize(data.length);
				md.setData(data);
				dao.insert(md);
			}
			catch (Exception e) {
				log.error("Failed to save data of media [" + m.getId() + "] (" + sz + ")", e);
			}
		}
		return md;
	}

	@Override
	public void save(Media m) {
		try {
			Dao dao = daoClient.getDao();

			MediaData md = new MediaData();
			md.setMid(m.getId());
			md.setMsz(Medias.ORIGINAL);
			md.setSize(m.getFile().getSize());
			md.setData(m.getFile().data());
			dao.save(md);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to save media data of [" + m.getId() + "]", e);
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
}
