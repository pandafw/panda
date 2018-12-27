package panda.app.media;

import panda.app.entity.Media;
import panda.ioc.annotation.IocBean;

@IocBean
public interface MediaDataSaver {
	public MediaData find(String id);

	public MediaData find(String id, int sz);

	public void save(Media m);
	
	public void delete(String... mid);

	public void delete(Media... m);

}
