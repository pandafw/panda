package panda.app.media;

import panda.app.entity.Media;
import panda.ioc.annotation.IocBean;

@IocBean
public interface MediaDataStore {
	public MediaData find(Media m);

	public MediaData find(Media m, int sz);

	public void save(Media m);
	
	public void delete(String... mid);

	public void delete(Media... m);

}
