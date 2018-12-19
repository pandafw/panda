package panda.app.media;

import panda.app.entity.Media;
import panda.ioc.annotation.IocBean;

@IocBean
public interface MediaDataSaver {
	public MediaData find(long id);

	public MediaData find(long id, int sz);

	public void save(Media m);
	
	public void delete(Long... mid);

	public void delete(Media... m);

}
