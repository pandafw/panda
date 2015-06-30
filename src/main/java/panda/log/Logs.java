package panda.log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import panda.Panda;
import panda.lang.ClassLoaders;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.impl.ConsoleLogAdapter;

/**
 * @author yf.frank.wang@gmail.com
 */
public final class Logs {
	private static final String CONFIG = "panda-log.properties";
	
	private static LogLevel rootLvl = LogLevel.TRACE;

	private static Map<String, LogLevel> levels = new HashMap<String, LogLevel>();

	private static LogAdapter adapter;

	static {
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
		return adapter.getLogger(className);
	}

	/**
	 * get a log by caller's class name
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
		return adapter.getLogger(sts[2].getClassName());
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
							rootLvl = lvl;
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
				e.printStackTrace();
				adapter = null;
			}
			finally {
				try {
					is.close();
				}
				catch (Throwable e) {
				}
			}
		}

		if (adapter == null) {
			String[] adapters = new String[] {
					ConsoleLogAdapter.class.getPackage().getName() + ".Log4jLogAdapter",
					"panda.android.log.LogCatAdapter"
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

	public static LogLevel getLogLevel(String name) {
		if (Collections.isNotEmpty(levels) && Strings.isNotEmpty(name)) {
			String key = "";
			for (String k : levels.keySet()) {
				if (name.startsWith(k)) {
					if (k.length() > key.length()) {
						key = k;
					}
				}
			}

			LogLevel lvl = levels.get(key);
			if (lvl != null) {
				return lvl;
			}
		}
		return rootLvl;
	}
}
