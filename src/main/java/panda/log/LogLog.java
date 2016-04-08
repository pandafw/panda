package panda.log;

/**
 * This class used to output log statements from within the log package.
 */
public class LogLog {
	/**
	 * Defining this value makes log4j print log internal debug statements to
	 * <code>System.out</code>.
	 * <p>
	 * The value of this string is <b>panda.log.debug</b>.
	 * <p>
	 * Note that the search for all option names is case sensitive.
	 */
	public static final String DEBUG_KEY = "panda.log.debug";

	protected static boolean debugEnabled = false;

	/**
	 * In quietMode not even errors generate any output.
	 */
	private static boolean quietMode = false;

	private static final String PREFIX = "log: ";
	private static final String ERR_PREFIX = "log:ERROR ";
	private static final String WARN_PREFIX = "log:WARN ";

	static {
		String key = System.getProperty(DEBUG_KEY);
		if ("true".equalsIgnoreCase(key)) {
			debugEnabled = true;
		}
	}

	/**
	 * Allows to enable/disable log4j internal logging.
	 */
	static public void setInternalDebugging(boolean enabled) {
		debugEnabled = enabled;
	}

	/**
	 * This method is used to output log4j internal debug statements. Output goes to
	 * <code>System.out</code>.
	 */
	public static void debug(String msg) {
		if (debugEnabled && !quietMode) {
			System.out.println(PREFIX + msg);
		}
	}

	/**
	 * This method is used to output log4j internal debug statements. Output goes to
	 * <code>System.out</code>.
	 */
	public static void debug(String msg, Throwable t) {
		if (debugEnabled && !quietMode) {
			System.out.println(PREFIX + msg);
			if (t != null)
				t.printStackTrace(System.out);
		}
	}

	/**
	 * This method is used to output log4j internal error statements. There is no way to disable
	 * error statements. Output goes to <code>System.err</code>.
	 */
	public static void error(String msg) {
		if (quietMode)
			return;
		System.err.println(ERR_PREFIX + msg);
	}

	/**
	 * This method is used to output log4j internal error statements. There is no way to disable
	 * error statements. Output goes to <code>System.err</code>.
	 */
	public static void error(String msg, Throwable t) {
		if (quietMode)
			return;

		System.err.println(ERR_PREFIX + msg);
		if (t != null) {
			t.printStackTrace();
		}
	}

	/**
	 * In quite mode no LogLog generates strictly no output, not even for errors.
	 * 
	 * @param quietMode A true for not
	 */
	public static void setQuietMode(boolean quietMode) {
		LogLog.quietMode = quietMode;
	}

	/**
	 * This method is used to output log internal warning statements. There is no way to disable
	 * warning statements. Output goes to <code>System.err</code>.
	 */
	public static void warn(String msg) {
		if (quietMode)
			return;

		System.err.println(WARN_PREFIX + msg);
	}

	/**
	 * This method is used to output log internal warnings. There is no way to disable warning
	 * statements. Output goes to <code>System.err</code>.
	 */
	public static void warn(String msg, Throwable t) {
		if (quietMode)
			return;

		System.err.println(WARN_PREFIX + msg);
		if (t != null) {
			t.printStackTrace();
		}
	}
}
