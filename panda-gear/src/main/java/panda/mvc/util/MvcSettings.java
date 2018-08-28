package panda.mvc.util;

import javax.servlet.ServletContext;

import panda.io.RuntimeSettings;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean(type=Settings.class)
public class MvcSettings extends RuntimeSettings {

	@IocInject(required=false)
	private ServletContext servlet;

	public String getPropertyAsPath(String name) {
		return getPropertyAsPath(name, null);
	}

	public String getPropertyAsPath(String name, String defv) {
		String dir = getProperty(name, defv);
		if (dir != null && dir.startsWith("web://")) {
			if (servlet == null) {
				throw new IllegalStateException("Null servlet!");
			}
			dir = servlet.getRealPath(dir.substring(6));
		}
		return dir;
	}

}
