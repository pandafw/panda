package panda.log;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface Log {
	public static final int LEVEL_TRACE = 0;
	public static final int LEVEL_DEBUG = 10;
	public static final int LEVEL_INFO = 20;
	public static final int LEVEL_WARN = 30;
	public static final int LEVEL_ERROR = 40;
	public static final int LEVEL_FATAL = 50;


	boolean isTraceEnabled();
	boolean isInfoEnabled();
	boolean isDebugEnabled();
	boolean isWarnEnabled();
	boolean isErrorEnabled();
	boolean isFatalEnabled();

	void trace(Object message);
	void trace(Object message, Throwable t);
	void tracef(String fmt, Object... args);

	void debug(Object message);
	void debugf(String fmt, Object... args);
	void debug(Object message, Throwable t);

	void info(Object message);
	void info(Object message, Throwable t);
	void infof(String fmt, Object... args);

	void warn(Object message);
	void warn(Object message, Throwable t);
	void warnf(String fmt, Object... args);

	void error(Object message);
	void error(Object message, Throwable t);
	void errorf(String fmt, Object... args);

	void fatal(Object message);
	void fatal(Object message, Throwable t);
	void fatalf(String fmt, Object... args);
}
