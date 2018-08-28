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

@IocBean(type=Settings.class, create="initialize")
public class AppSettings extends MvcSettings {
	private static final Log log = Logs.getLog(AppSettings.class);

	private static final String APP_PROPERTIES = "app.properties";
	private static final String ENV_PROPERTIES = "env.properties";
	private static final String TEST_PROPERTIES = "test.properties";
	
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

	public AppSettings() {
	}
	
	public void initialize() throws IOException {
		log.info("Loading " + APP_PROPERTIES);
		load(APP_PROPERTIES);
		
		try {
			log.info("Loading " + ENV_PROPERTIES);
			load(ENV_PROPERTIES);
		}
		catch (FileNotFoundException e) {
			log.warn("Failed to load " + ENV_PROPERTIES + ": " + e.getMessage());
		}

		if (environment) {
			log.info("Add environment variables to settings");
			putAll(System.getenv(), "Enviroment");
		}
		if (system) {
			log.info("Add system properties to settings");
			putAll(System.getProperties(), "System.Properties");
		}

		if (Strings.isNotEmpty(runtime)) {
			setReloadable(new File(runtime));
		}

		try {
			load(TEST_PROPERTIES);
			log.warn(TEST_PROPERTIES + " Loaded!");
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
