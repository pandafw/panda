package panda.log;

import panda.Panda;
import panda.lang.Classes;
import panda.lang.Systems;
import panda.log.impl.ConsoleLogAdapter;
import panda.log.impl.NopLog;

/**
 * @author yf.frank.wang@gmail.com
 */
public final class Logs {
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
		String[] adapters = new String[] {
				ConsoleLogAdapter.class.getPackage().getName() + ".Log4jLogAdapter"
		};

		for (String a : adapters) {
			try {
				adapter = (LogAdapter)Classes.newInstance(a);
				adapter.getLogger("panda");
				break;
			}
			catch (Throwable e) {
				adapter = null;
			}
		}

		if (adapter == null) {
			adapter = new ConsoleLogAdapter();
		}
	}

	/**
	 * set the log adapter, null to disable log
	 * 
	 * @param adapter customer LogAdapter
	 */
	public static void setAdapter(LogAdapter adapter) {
		if (adapter == null) {
			adapter = NopLog.NOP;
		}
		Logs.adapter = adapter;
	}
}
