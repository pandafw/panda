package panda.log;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface Log {
	public static final int LEVEL_FATAL = 50;
	public static final int LEVEL_ERROR = 40;
	public static final int LEVEL_WARN = 30;
	public static final int LEVEL_INFO = 20;
	public static final int LEVEL_DEBUG = 10;
	public static final int LEVEL_TRACE = 0;


	boolean isFatalEnabled();

	void fatal(Object message);

	void fatalf(String fmt, Object... args);

	void fatal(Object message, Throwable t);

	boolean isErrorEnabled();

	void error(Object message);

	void errorf(String fmt, Object... args);

	void error(Object message, Throwable t);

	boolean isWarnEnabled();

	void warn(Object message);

	void warnf(String fmt, Object... args);

	void warn(Object message, Throwable t);

	boolean isInfoEnabled();

	void info(Object message);

	void infof(String fmt, Object... args);

	void info(Object message, Throwable t);

	boolean isDebugEnabled();

	void debug(Object message);

	void debugf(String fmt, Object... args);

	void debug(Object message, Throwable t);

	boolean isTraceEnabled();

	void trace(Object message);

	void tracef(String fmt, Object... args);

	void trace(Object message, Throwable t);
}
