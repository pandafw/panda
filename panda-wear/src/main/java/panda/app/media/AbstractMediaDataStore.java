package panda.app.media;

import panda.app.entity.Media;
import panda.dao.Dao;

public abstract class AbstractMediaDataStore implements MediaDataStore {
	@Override
	public MediaData find(Dao dao, Media m) {
		return find(dao, m, Medias.ORIGINAL);
	}
}
