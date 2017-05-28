package panda.log;

public interface Log {
	String getName();
	LogLevel getLevel();
	
	boolean isTraceEnabled();
	boolean isInfoEnabled();
	boolean isDebugEnabled();
	boolean isWarnEnabled();
	boolean isErrorEnabled();
	boolean isFatalEnabled();

	void log(LogLevel level, String message);
	void log(LogLevel level, String message, Throwable error);
	void logf(LogLevel level, String message, Object... args);
	void log(LogEvent event);
	
	void trace(String message);
	void trace(String message, Throwable error);
	void tracef(String format, Object... args);

	void debug(String message);
	void debug(String message, Throwable error);
	void debugf(String format, Object... args);

	void info(String message);
	void info(String message, Throwable error);
	void infof(String format, Object... args);

	void warn(String message);
	void warn(String message, Throwable error);
	void warnf(String format, Object... args);

	void error(String message);
	void error(String message, Throwable error);
	void errorf(String format, Object... args);

	void fatal(String message);
	void fatal(String message, Throwable error);
	void fatalf(String format, Object... args);
}
