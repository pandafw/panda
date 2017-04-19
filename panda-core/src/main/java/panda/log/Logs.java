package panda.log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import panda.Panda;
import panda.io.Streams;
import panda.lang.ClassLoaders;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.impl.ConsoleLogAdapter;
import panda.log.impl.ConsoleLog;

/**
 */
public final class Logs {
	private static final String CONFIG = "panda-log.properties";
	
	private static LogLevel rootLogLevel = LogLevel.TRACE;

	private static Map<String, LogLevel> levels = new HashMap<String, LogLevel>();

	private static LogAdapter adapter;

	static {
		if (Systems.IS_OS_ANDROID) {
			ConsoleLog.console = System.out;
		}

		init();
		
		try {
			getLog(Panda.class).debug("Panda is licensed under the GNU General Public License 3. " 
				+ "Report bugs: https://github.com/foolite/panda/issues");
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
	 * @param className the name of Log
	 * @return Log
	 */
	public static Log getLog(String className) {
		return getLogger(adapter, className);
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
					return adapter.getLogger(sts[i + 1].getClassName());
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
		LogLevel lvl = rootLogLevel;
		if (Collections.isNotEmpty(levels) && Strings.isNotEmpty(name)) {
			String key = "";
			for (Entry<String, LogLevel> en : levels.entrySet()) {
				if (name.startsWith(en.getKey())) {
					if (en.getKey().length() > key.length()) {
						key = en.getKey();
						lvl = en.getValue();
					}
				}
			}
		}
		return lvl;
	}

	/**
	 * @return the rootLogLevel
	 */
	public static LogLevel getRootLogLevel() {
		return rootLogLevel;
	}

	/**
	 * @param rootLogLevel the rootLogLevel to set
	 */
	public static void setRootLogLevel(LogLevel rootLogLevel) {
		Logs.rootLogLevel = rootLogLevel;
	}

	private static Log getLogger(LogAdapter adapter, String name) {
		Log log;
		try {
			log = adapter.getLogger(name);
		}
		catch (Throwable e) {
			LogLog.error("Failed to getLogger(" + adapter.getClass() + ", " + name + ")");
			log =  new ConsoleLog(name, null);
		}
		return log;
	}
	
	private static void init() {
		Properties props = new Properties();

		InputStream is = ClassLoaders.getResourceAsStream(CONFIG);
		if (is != null) {
			// load adapter from config
			try {
				props.load(is);
				for (Entry<Object, Object> en : props.entrySet()) {
					String key = en.getKey().toString();
					if (key.startsWith("log.")) {
						key = key.substring(4);
						LogLevel lvl = LogLevel.parse(en.getValue().toString());
						if ("*".equals(key)) {
							rootLogLevel = lvl;
						}
						else {
							levels.put(key, lvl);
						}
					}
				}
				
				String impl = props.getProperty(LogAdapter.class.getName());
				if (Strings.isNotEmpty(impl)) {
					adapter = (LogAdapter)Classes.newInstance(impl);
					adapter.init(props);
					adapter.getLogger("panda");
				}
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
			String[] adapters = new String[] {
					ConsoleLogAdapter.class.getPackage().getName() + ".Log4jLogAdapter",
					"panda.roid.log.LogCatAdapter"
			};

			ClassLoader cl = ClassLoaders.getClassLoader();
			for (String a : adapters) {
				try {
					adapter = (LogAdapter)Class.forName(a, true, cl).newInstance();
					adapter.init(props);
					adapter.getLogger("panda");
					break;
				}
				catch (Throwable e) {
					adapter = null;
				}
			}
		}

		if (adapter == null) {
			adapter = new ConsoleLogAdapter();
			adapter.init(props);
		}
	}
}
