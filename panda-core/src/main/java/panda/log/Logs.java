package panda.log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import panda.Panda;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Booleans;
import panda.lang.ClassLoaders;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.ex.JavaLogRedirectHandler;
import panda.log.impl.ConsoleLog;
import panda.log.impl.ConsoleLogAdapter;
import panda.log.impl.ComboLogAdapter;

public final class Logs {
	private static final String CONFIG = "panda-logging.properties";

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
	}

	private static LogConfig rootlc = new LogConfig(LogLevel.INFO, null);

	private static Map<String, LogConfig> configs = new HashMap<String, LogConfig>();

	private static LogAdapter adapter;

	static {
		if (Systems.IS_OS_ANDROID) {
			LogLog.output = System.out;
			ConsoleLog.output = System.out;
		}

		init();
		
		try {
			getLog(Panda.class).debug("Panda is licensed under the GNU General Public License 3. " 
				+ "Report bugs: https://github.com/pandafw/panda/issues");
		}
		catch (Throwable e) {
			// just pass!!
		}
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
		return getLogger(adapter, name);
	}

	/**
	 * get a log by caller's class name
	 * 
	 * @return the Log
	 */
	public static Log get() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (Systems.IS_OS_ANDROID) {
			for (int i = 0; i < sts.length; i++) {
				if (sts[i].getClassName().equals(Logs.class.getName())) {
					return adapter.getLog(sts[i + 1].getClassName());
				}
			}
		}
		return getLogger(adapter, sts[2].getClassName());
	}

	/**
	 * get the log level by the name
	 * @param name name
	 * @return log level
	 */
	public static LogLevel getLogLevel(String name) {
		return getLogConfig(name).level;
	}

	/**
	 * get the loggers by the name
	 * @param name name
	 * @return loggers
	 */
	public static Set<String> getLogLoggers(String name) {
		LogConfig lc = getLogConfig(name);
		return Collections.isEmpty(lc.loggers) ? rootlc.loggers : lc.loggers;
	}
	
	/**
	 * get the LogConfig by the name
	 * @param name name
	 * @return LogConfig
	 */
	private static LogConfig getLogConfig(String name) {
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

	private static Log getLogger(LogAdapter adapter, String name) {
		Log log;
		try {
			log = adapter.getLog(name);
		}
		catch (Throwable e) {
			LogLog.error("Failed to getLogger(" + adapter.getClass() + ", " + name + ")");
			log =  new ConsoleLog(name);
		}
		return log;
	}

	/**
	 * is the specified logger enabled for the name
	 * @param adapter  adapter name
	 * @param name     logger name
	 * @return true if the adapter is enabled for the logger
	 */
	public static boolean isLoggerEnabled(String adapter, String name) {
		if (Strings.isEmpty(adapter)) {
			return true;
		}
		
		Set<String> loggers = getLogLoggers(name);
		if (Collections.isEmpty(loggers)) {
			return true;
		}
		return loggers.contains(adapter);
	}
	
	private static void init() {
		Properties props = new Properties();

		InputStream is = ClassLoaders.getResourceAsStream(CONFIG);
		if (is != null) {
			try {
				props.load(is);
				
				// level settings
				for (Entry<Object, Object> en : props.entrySet()) {
					String key = en.getKey().toString();
					if (key.startsWith("level.")) {
						key = key.substring("level.".length());
						LogConfig lc = LogConfig.parse(en.getValue().toString());
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

				// java logging redirect
				String v = props.getProperty("panda.java.logging.redirect");
				if (Booleans.toBoolean(v)) {
					JavaLogRedirectHandler.redirect();
				}

				// initialize adapter
				String name = "";
				String impl = props.getProperty(LogAdapter.class.getName());
				if (Strings.isEmpty(impl)) {
					adapter = new ComboLogAdapter();
				}
				else {
					int d = impl.indexOf(':');
					if (d > 0) {
						name = impl.substring(0, d);
						impl = impl.substring(d + 1);
					}
					adapter = (LogAdapter)Classes.newInstance(impl);
				}
				adapter.init(name, props);
				adapter.getLog("panda");
			}
			catch (Throwable e) {
				LogLog.error("Failed to initialize log", e);
				adapter = null;
			}
			finally {
				Streams.safeClose(is);
			}
		}

		if (adapter == null) {
			adapter = new ConsoleLogAdapter();
			adapter.init(adapter.getClass().getName(), props);
		}
	}
}
