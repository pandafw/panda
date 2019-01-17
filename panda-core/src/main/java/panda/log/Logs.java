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
import panda.log.impl.RuntimeLog;

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

	private static Logs logs = new Logs();

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
	public static void initialize(Map<String, String> props) {
		logs = new Logs(props);
	}
	

	/**
	 * configure logs
	 * @param props properties
	 */
	public static void configure(Map<String, String> props) {
		logs.init(props);
	}
	
	/**
	 * @return the configure time
	 */
	public static long getConfigureTime() {
		return logs.getConfigTime();
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

	//-------------------------------------------------------------------------
	private LogAdapter adapter;
	private Map<String, LogConfig> configs;
	private LogConfig rootlc = new LogConfig(LogLevel.INFO, null);
	private long configTime;
	private boolean runtime;
	
	protected Logs() {
		adapter = new ConsoleLogAdapter();
	}

	protected Logs(Map<String, String> props) {
		runtime = Booleans.toBoolean(props.get("panda.log.runtime"));
		
		init(props);
	}

	/**
	 * @return the configTime
	 */
	public long getConfigTime() {
		return configTime;
	}

	/**
	 * @return the adapter
	 */
	public LogAdapter getAdapter() {
		return adapter;
	}

	protected void init(Map<String, String> props) {
		// java logging redirect
		String v = props.get("panda.java.logging.redirect");
		JavaLogRedirectHandler.redirect(Booleans.toBoolean(v));

		// level settings
		initLogLevels(props);
		
		// initialize adapter
		initLogAdapter(props);
		
		// set configure timestamp
		configTime = System.currentTimeMillis();
	}
	
	protected void initLogLevels(Map<String, String> props) {
		String LEVEL = "level.";

		Map<String, LogConfig> configs = new HashMap<String, LogConfig>();
		for (Entry<String, String> en : props.entrySet()) {
			String key = en.getKey();
			if (key.startsWith(LEVEL)) {
				key = key.substring(LEVEL.length());
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
		
		this.configs = configs;
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
				LogLog.error("Failed to create LogAdapter(" + impl + "): " + e.getMessage());
				adapter = new ComboLogAdapter();
			}
		}
		adapter.init(this, name, props);
		adapter.getLog("panda");
	}

	/**
	 * get the Log by the name
	 * @param name name
	 * @return Log
	 */
	protected Log getLogger(String name) {
		Log log = newLogger(name);
		if (runtime) {
			log = new RuntimeLog(log, this);
		}
		return log;
	}
	
	/**
	 * get the Logger by the name
	 * @param name name
	 * @return Log
	 */
	public Log newLogger(String name) {
		Log log;
		try {
			log = adapter.getLog(name);
		}
		catch (Throwable e) {
			LogLog.error("Failed to getLogger(" + name + "): " + e.getMessage());
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
		if (Strings.isEmpty(name)) {
			return rootlc;
		}
		
		Map<String, LogConfig> configs = this.configs;
		if (Collections.isEmpty(configs)) {
			return rootlc;
		}

		LogConfig lc = rootlc;

		String key = "";
		for (Entry<String, LogConfig> en : configs.entrySet()) {
			if (name.startsWith(en.getKey())) {
				if (en.getKey().length() > key.length()) {
					key = en.getKey();
					lc = en.getValue();
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
