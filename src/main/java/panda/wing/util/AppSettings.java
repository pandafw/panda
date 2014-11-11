package panda.wing.util;

import java.io.IOException;

import javax.servlet.ServletContext;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.Logs;

@IocBean(type=Settings.class)
public class AppSettings extends Settings {
	private static final Log log = Logs.getLog(AppSettings.class);

	public static final String APP_VERSION = "app.version";
	
	@IocInject(required=false)
	protected ServletContext servlet;
	
	public AppSettings() throws IOException {
		putAll(System.getProperties());

		load("app.properties");
		
		if (Systems.IS_OS_APPENGINE) {
			try {
				load("gae.properties");
			}
			catch (IOException e) {
				log.warn(e.getMessage());
			}
		}

		if ("true".equals(System.getenv("panda.env"))) {
			putAll(System.getenv());
		}
		
		try {
			load("test.properties");
		}
		catch (IOException e) {
		}

		log.info("Version: " + getAppVersion());
	}
	
	public String getAppVersion() {
		return get(APP_VERSION);
	}

	public String getPropertyAsPath(String name) {
		return getPropertyAsPath(name, null);
	}

	public String getPropertyAsPath(String name, String defv) {
		String dir = getProperty(name, defv);
		if (dir != null && dir.startsWith("web://") && servlet != null) {
			dir = servlet.getRealPath(dir.substring(6));
		}
		return dir;
	}
}
