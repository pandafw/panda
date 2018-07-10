package panda.app.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.util.MvcSettings;

@IocBean(type=Settings.class)
public class AppSettings extends MvcSettings {
	private static final Log log = Logs.getLog(AppSettings.class);

	@IocInject(value=MVC.SETTINGS_SYSTEM, required=false)
	protected boolean system = true;

	@IocInject(value=MVC.SETTINGS_ENVIRONMENT, required=false)
	protected boolean environment = true;

	@IocInject(value=MVC.SETTINGS_RUNTIME_PATH, required=false)
	protected String runtime;

	@IocInject(value=MVC.SETTINGS_RUNTIME_DELAY, required=false)
	public void setDelay(long delay) {
		super.setDelay(delay);
	}

	public AppSettings() throws IOException {
		load("app.properties");
		
		try {
			load("env.properties");
		}
		catch (FileNotFoundException e) {
			log.warn(e.getMessage());
		}

		if (environment) {
			putAll(System.getenv());
		}
		if (system) {
			putAll(System.getProperties());
		}

		if (Strings.isNotEmpty(runtime)) {
			setReloadable(new File(runtime));
		}

		try {
			load("test.properties");
		}
		catch (IOException e) {
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
}
