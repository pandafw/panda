package panda.app.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.io.ReloadableSettings;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.Logs;

@IocBean(type=Settings.class)
public class AppSettings extends ReloadableSettings {
	private static final Log log = Logs.getLog(AppSettings.class);

	@IocInject(required=false)
	protected ServletContext servlet;

	@IocInject(value=MVC.SETTINGS_RUNTIME_PATH, required=false)
	protected String runtime;

	@IocInject(value=MVC.SETTINGS_RUNTIME_DELAY, required=false)
	public void setDelay(long delay) {
		super.setDelay(delay);
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
		String av = get(SET.APP_VERSION);
		if (Strings.isEmpty(av)) {
			av = getProperty(SET.PRJ_VERSION, "");
			String rv = get(SET.PRJ_REVISION);
			if (Strings.isNotEmpty(rv)) {
				av += "." + rv;
			}
		}
		return av;
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
