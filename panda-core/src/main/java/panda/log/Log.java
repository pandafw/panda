package panda.log;

/**
 */
public interface Log {
	boolean isTraceEnabled();
	boolean isInfoEnabled();
	boolean isDebugEnabled();
	boolean isWarnEnabled();
	boolean isErrorEnabled();
	boolean isFatalEnabled();

	void log(LogLevel level, String msg, Throwable tx);

	void trace(Object message);
	void trace(Object message, Throwable t);
	void tracef(String fmt, Object... args);

	void debug(Object message);
	void debug(Object message, Throwable t);
	void debugf(String fmt, Object... args);

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
