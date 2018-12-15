package panda.app.media;

import java.util.List;

import panda.app.entity.Media;
import panda.ioc.annotation.IocBean;

@IocBean
public interface MediaDataSaver {
	public MediaData find(long id);

	public MediaData find(long id, int sz);

	public void save(Media m);
	
	public void delete(Media m);

	public void deletes(List<Media> ms);
}
