package panda.app.util;

import panda.app.constant.SET;
import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.vfs.MvcLocalFileStore;
import panda.vfs.FileStore;

@IocBean(type=FileStore.class)
public class AppLocalFileStore extends MvcLocalFileStore {
	@Override
	public String getLocation() {
		String loc = settings.getProperty(SET.FILESTORE_LOCATION);
		if (Strings.isNotEmpty(loc)) {
			return loc;
		}
		return super.getLocation();
	}
}
