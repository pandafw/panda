package panda.app.media;

import panda.app.entity.Media;
import panda.dao.Dao;
import panda.ioc.annotation.IocBean;

@IocBean
public interface MediaDataStore {
	public MediaData find(Dao dao, Media m);

	public MediaData find(Dao dao, Media m, int sz);

	public void save(Dao dao, Media m);
	
	public void delete(Dao dao, String... mid);

	public void delete(Dao dao, Media... m);
}
