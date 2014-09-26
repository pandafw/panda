package panda.wing.util;

import java.io.IOException;

import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.Logs;

@IocBean(type=Settings.class)
public class AppSettings extends Settings {
	private static final Log log = Logs.getLog(AppSettings.class);

	public static final String APP_VERSION = "app.version";
	
	public AppSettings() throws IOException {
		putAll(System.getProperties());

		load("system.properties");
		
		if (Systems.IS_OS_APPENGINE) {
			try {
				load("appengine.properties");
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
}
