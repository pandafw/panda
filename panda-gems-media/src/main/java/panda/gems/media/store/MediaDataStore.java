package panda.gems.media.store;

import panda.dao.Dao;
import panda.gems.media.entity.Media;
import panda.gems.media.entity.MediaData;

public interface MediaDataStore {
	public MediaData find(Dao dao, Media m);

	public MediaData find(Dao dao, Media m, int sz);

	public void save(Dao dao, Media m);
	
	public void delete(Dao dao, Media... m);
}
