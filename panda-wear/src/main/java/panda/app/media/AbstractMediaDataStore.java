package panda.app.media;

import panda.app.entity.Media;
import panda.dao.Dao;
import panda.lang.Arrays;

public abstract class AbstractMediaDataStore implements MediaDataStore {
	@Override
	public MediaData find(Dao dao, Media m) {
		return find(dao, m, Medias.ORIGINAL);
	}

	@Override
	public void delete(Dao dao, Media... ms) {
		if (Arrays.isEmpty(ms)) {
			return;
		}
		
		String[] ids = new String[ms.length];
		for (int i = 0; i < ms.length; i++) {
			ids[i] = ms[i].getId();
		}

		delete(dao, ids);
	}
}
