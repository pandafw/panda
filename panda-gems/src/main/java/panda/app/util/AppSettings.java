package panda.app.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.util.MvcSettings;

@IocBean(type=Settings.class, create="initialize")
public class AppSettings extends MvcSettings {
	private static final Log log = Logs.getLog(AppSettings.class);

	private static final String ENV = "$ENV";
	private static final String SYS = "$SYS";
	private static final String APP_PROPERTIES = "app.properties";
	private static final String ENV_PROPERTIES = "env.properties";
	private static final String TEST_PROPERTIES = "test.properties";

	@IocInject(value=MVC.SETTINGS, required=false)
	protected List<String> settings = Arrays.asList(
		ENV, SYS, APP_PROPERTIES, ENV_PROPERTIES);

	@IocInject(value=MVC.SETTINGS_RUNTIME_FILES, required=false)
	protected String[] runtimes;

	@IocInject(value=MVC.SETTINGS_RUNTIME_DELAY, required=false)
	public void setDelay(long delay) {
		super.setDelay(delay);
	}

	public AppSettings() {
	}

	public void putAll(Map<? extends String, ? extends String> map, String prefix, String from) {
		for (Entry<? extends String, ? extends String> en : map.entrySet()) {
			put(prefix + en.getKey(), en.getValue(), from);
		}
	}

	public void putAll(Properties ps, String prefix, String from) {
		for (Iterator<Entry<Object, Object>> i = ps.entrySet().iterator(); i.hasNext(); ) {
			Entry<Object, Object> e = i.next();
			put(prefix + e.getKey(), (String)e.getValue(), from);
		}
	}

	@Override
	public void initialize() throws IOException {
		super.initialize();

		for (String s : settings) {
			if (ENV.equalsIgnoreCase(s)) {
				log.info("Add environment variables to settings");
				putAll(System.getenv(), "env.", ENV);
			}
			else if (SYS.equalsIgnoreCase(s)) {
				log.info("Add system properties to settings");
				putAll(System.getProperties(), "sys.", SYS);
			}
			else {
				try {
					log.info("Loading " + s);
					load(s);
				}
				catch (FileNotFoundException e) {
					log.warn("Failed to load " + ENV_PROPERTIES + ": " + e.getMessage());
				}
			}
		}
		
		try {
			load(TEST_PROPERTIES);
			log.warn(TEST_PROPERTIES + " is loadded.");
		}
		catch (FileNotFoundException e) {
		}

		if (Arrays.isNotEmpty(runtimes)) {
			loadRuntimes(runtimes);
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
