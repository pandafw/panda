package panda.gems.media.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import panda.dao.Dao;
import panda.gems.media.V;
import panda.gems.media.entity.Media;
import panda.gems.media.entity.MediaData;
import panda.gems.media.entity.query.MediaDataQuery;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class MediaDataDaoStore extends AbstractMediaDataStore {
	private static final Log log = Logs.getLog(MediaDataDaoStore.class);

	@Override
	public MediaData find(Dao dao, Media m, int sz) {
		MediaData md = dao.fetch(MediaData.class, m.getId(), sz);
		if (md == null && sz != V.ORIGINAL) {
			MediaData mo = dao.fetch(MediaData.class, m.getId(), V.ORIGINAL);
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
	public void save(Dao dao, Media m) {
		try {
			MediaData md = new MediaData();
			md.setMid(m.getId());
			md.setMsz(V.ORIGINAL);
			md.setSize(m.getFile().getSize());
			md.setData(m.getFile().data());
			dao.save(md);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to save media data of [" + m.getId() + "]", e);
		}
	}

	@Override
	public void delete(Dao dao, Media... ms) {
		if (Arrays.isEmpty(ms)) {
			return;
		}

		List<Long> mids = new ArrayList<Long>();
		for (Media m : ms) {
			if (m != null) {
				mids.add(m.getId());
			}
		}
		if (mids.isEmpty()) {
			return;
		}
		
		try {
			
			MediaDataQuery mdq = new MediaDataQuery();
			mdq.mid().in(mids);
			dao.deletes(mdq);
		}
		catch (Exception e) {
			log.error("Failed to delete media data for " + Strings.join(mids, ", "), e);
		}
	}
}
