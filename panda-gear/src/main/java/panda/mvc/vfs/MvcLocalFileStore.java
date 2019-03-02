package panda.mvc.vfs;

import java.io.File;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.mvc.MvcConstants;
import panda.mvc.util.MvcSettings;
import panda.vfs.FileStore;
import panda.vfs.local.LocalFileStore;

@IocBean(type=FileStore.class)
public class MvcLocalFileStore extends LocalFileStore {
	protected static final String DEFAULT_LOCATION = "${web.dir}/WEB-INF/_files";

	@IocInject
	protected MvcSettings settings;
	
	/**
	 * @param location the path to set
	 */
	@Override
	@IocInject(value=MvcConstants.FILESTORE_LOCATION, required=false)
	public void setLocation(String location) {
		super.setLocation(location);
	}

	@Override
	public String getLocation() {
		if (Strings.isEmpty(location)) {
			String s = Texts.translate(DEFAULT_LOCATION, settings);
			location = new File(s).getAbsolutePath();
		}
		return location;
	}
}
