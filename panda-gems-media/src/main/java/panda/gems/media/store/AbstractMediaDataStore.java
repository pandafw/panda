package panda.gems.media.store;

import panda.dao.Dao;
import panda.gems.media.Medias;
import panda.gems.media.entity.Media;
import panda.gems.media.entity.MediaData;

public abstract class AbstractMediaDataStore implements MediaDataStore {
	@Override
	public MediaData find(Dao dao, Media m) {
		return find(dao, m, Medias.ORIGINAL);
	}
}
