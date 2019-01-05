package panda.app.util;

import panda.app.constant.SET;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.vfs.MvcLocalFileStore;
import panda.vfs.FileStore;

@IocBean(type=FileStore.class)
public class AppLocalFileStore extends MvcLocalFileStore {
	@IocInject
	private Settings settings;
	
	public AppLocalFileStore() {
	}

	@Override
	public String getPath() {
		String path = settings.getProperty(SET.FILESTORE_LOCAL_PATH);
		if (Strings.isNotEmpty(path)) {
			return path;
		}
		return super.getPath();
	}
}
