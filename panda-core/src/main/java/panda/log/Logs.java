package panda.log;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.Panda;
import panda.io.Settings;
import panda.lang.Arrays;
import panda.lang.Booleans;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.ex.JavaLogRedirectHandler;
import panda.log.impl.ComboLogAdapter;
import panda.log.impl.ConsoleLog;
import panda.log.impl.ConsoleLogAdapter;

public final class Logs {
	public static final String CONFIG = "log.properties";

	private static class LogConfig {
		LogLevel level;
		Set<String> loggers;
		
		LogConfig(LogLevel level, Set<String> loggers) {
			this.level = level;
			this.loggers = loggers;
		}
		
		static LogConfig parse(String setting) {
			String[] ss = Strings.split(setting, " ,");
			if (Arrays.isEmpty(ss)) {
				return null;
			}
			
			LogLevel ll = LogLevel.parse(ss[0]);
			Set<String> gs = ss.length > 1 ? Arrays.toSet(Arrays.subarray(ss, 1, ss.length)) : null;
			return new LogConfig(ll, gs);
		}
		
		@Override
		public String toString() {
			return level + ": " + loggers;
		}
	}

	private static LogConfig rootlc = new LogConfig(LogLevel.INFO, null);
	
	private static Logs logs;

	static {
		if (Systems.IS_OS_ANDROID) {
			LogLog.output = System.out;
			ConsoleLog.output = System.out;
		}

		init();
		
		try {
			getLog(Panda.class).debug("Panda is licensed under the Apache License Version 2.0, please report bugs to https://github.com/pandafw/panda/issues");
		}
		catch (Throwable e) {
			// just pass!!
		}
	}

	public static Logs i() {
		return logs;
	}
	
	private static void init() {
		Settings props = new Settings();

		String file = Systems.getProperty(CONFIG, CONFIG);
		try {
			// load settings
			props.load(file);
		}
		catch (FileNotFoundException e) {
			LogLog.warn("Missing log config file: " + file);
		}
		catch (Throwable e) {
			LogLog.error("Failed to load log config file: " + file, e);
		}
		
		// create logs
		logs = new Logs(props);
	}

	/**
	 * initialize logs
	 * @param props properties
	 */
	public static void init(Map<String, String> props) {
		logs = new Logs(props);
	}
	
	/**
	 * Get a Log by Class
	 * 
	 * @param clazz your class
	 * @return Log
	 */
	public static Log getLog(Class<?> clazz) {
		return getLog(clazz.getName());
	}

	/**
	 * Get a Log by name
	 * 
	 * @param name the name of Log
	 * @return Log
	 */
	public static Log getLog(String name) {
		return logs.getLogger(name);
	}

	/**
	 * @return the rootLogLevel
	 */
	public static LogLevel getRootLogLevel() {
		return rootlc.level;
	}

	/**
	 * @param level the root LogLevel to set
	 */
	public static void setRootLogLevel(LogLevel level) {
		rootlc.level = level;
	}

	/**
	 * @return the root loggers
	 */
	public static Set<String> getRootLoggers() {
		return rootlc.loggers;
	}

	/**
	 * @param loggers the root loggers to set
	 */
	public static void setRootLoggers(Set<String> loggers) {
		rootlc.loggers = loggers;
	}

	//-------------------------------------------------------------------------
	private LogAdapter adapter;
	private Map<String, LogConfig> configs = new HashMap<String, LogConfig>();

	protected Logs() {
		adapter = new ConsoleLogAdapter();
	}

	protected Logs(Map<String, String> props) {
		// level settings
		initLogLevels(props);
		
		// java logging redirect
		String v = props.get("panda.java.logging.redirect");
		if (Booleans.toBoolean(v)) {
			JavaLogRedirectHandler.redirect();
		}

		// initialize adapter
		initLogAdapter(props);
	}

	protected void initLogLevels(Map<String, String> props) {
		for (Entry<String, String> en : props.entrySet()) {
			String key = en.getKey();
			if (key.startsWith("level.")) {
				key = key.substring("level.".length());
				LogConfig lc = LogConfig.parse(en.getValue());
				if (lc == null) {
					continue;
				}

				if ("*".equals(key)) {
					rootlc = lc;
				}
				else {
					configs.put(key, lc);
				}
			}
		}
	}
	
	protected void initLogAdapter(Map<String, String> props) {
		String name = "";
		String impl = props.get(LogAdapter.class.getName());
		if (Strings.isEmpty(impl)) {
			adapter = new ComboLogAdapter();
		}
		else {
			int d = impl.indexOf(':');
			if (d > 0) {
				name = impl.substring(0, d);
				impl = impl.substring(d + 1);
			}
			try {
				adapter = (LogAdapter)Classes.newInstance(impl);
			}
			catch (Throwable e) {
				LogLog.error("Failed to create LogAdapter: " + impl);
				adapter = new ComboLogAdapter();
			}
		}
		adapter.init(this, name, props);
		adapter.getLog("panda");
	}

	/**
	 * get the Logger by the name
	 * @param name name
	 * @return Log
	 */
	protected Log getLogger(String name) {
		Log log;
		try {
			log = adapter.getLog(name);
		}
		catch (Throwable e) {
			LogLog.error("Failed to getLogger(" + adapter.getClass() + ", " + name + ")");
			log =  new ConsoleLog(logs, name);
		}
		return log;
	}
	
	/**
	 * get the LogConfig by the name
	 * @param name name
	 * @return LogConfig
	 */
	protected LogConfig getLogConfig(String name) {
		LogConfig lc = rootlc;
		if (Collections.isNotEmpty(configs) && Strings.isNotEmpty(name)) {
			String key = "";
			for (Entry<String, LogConfig> en : configs.entrySet()) {
				if (name.startsWith(en.getKey())) {
					if (en.getKey().length() > key.length()) {
						key = en.getKey();
						lc = en.getValue();
					}
				}
			}
		}
		return lc;
	}

	/**
	 * get the log level by the name
	 * @param name name
	 * @return log level
	 */
	public LogLevel getLogLevel(String name) {
		return getLogConfig(name).level;
	}

	/**
	 * is the specified logger enabled for the name
	 * @param adapter  adapter name
	 * @param name     logger name
	 * @return true if the adapter is enabled for the logger
	 */
	public boolean isLoggerEnabled(String adapter, String name) {
		if (Strings.isEmpty(adapter)) {
			return true;
		}
		
		Set<String> loggers = getLogLoggers(name);
		if (Collections.isEmpty(loggers)) {
			return true;
		}
		return loggers.contains(adapter);
	}

	/**
	 * get the loggers by the name
	 * @param name name
	 * @return loggers
	 */
	public Set<String> getLogLoggers(String name) {
		LogConfig lc = getLogConfig(name);
		return Collections.isEmpty(lc.loggers) ? rootlc.loggers : lc.loggers;
	}
}
