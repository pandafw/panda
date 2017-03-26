package panda.wing.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import panda.io.ReloadableSettings;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.Logs;
import panda.wing.AppConstants;
import panda.wing.constant.SC;

@IocBean(type=Settings.class)
public class AppSettings extends ReloadableSettings {
	private static final Log log = Logs.getLog(AppSettings.class);

	@IocInject(required=false)
	protected ServletContext servlet;

	@IocInject(value=AppConstants.SETTINGS_RELOAD_PATH, required=false)
	protected String runtime;

	@IocInject(value=AppConstants.SETTINGS_RELOAD_INTERVAL, required=false)
	public void setInterval(long interval) {
		super.setInterval(interval);
	}

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

		if (Strings.isNotEmpty(runtime)) {
			load(new File(runtime));
		}
		log.info("Version: " + getAppVersion());
	}
	
	public String getAppVersion() {
		return get(SC.APP_VERSION);
	}

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
