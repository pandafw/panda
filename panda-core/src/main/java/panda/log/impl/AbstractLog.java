package panda.log.impl;


import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;
import panda.log.Logs;


public abstract class AbstractLog implements Log {
	protected String name;
	protected LogLevel level;

	protected AbstractLog(Logs logs, String name, LogLevel level) {
		this.name = name;
		this.level = logs.getLogLevel(name);
		if (level != null && this.level.isLessOrEqual(level)) {
			this.level = level;
		}
	}

	
	/**
	 * @return the FQCN
	 */
	public String getFQCN() {
		return AbstractLog.class.getName();
	}
	
	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the level
	 */
	@Override
	public LogLevel getLevel() {
		return level;
	}

	@Override
	public void log(LogLevel level, String message) {
		if (level.isGreaterOrEqual(this.level)) {
			write(new LogEvent(getFQCN(), name, level, message));
		}
	}

	@Override
	public void log(LogLevel level, String message, Throwable error) {
		if (level.isGreaterOrEqual(this.level)) {
			write(new LogEvent(getFQCN(), name, level, message, error));
		}
	}

	@Override
	public void logf(LogLevel level, String message, Object... args) {
		if (level.isGreaterOrEqual(this.level)) {
			write(new LogEvent(getFQCN(), name, level, message, args));
		}
	}

	@Override
	public void log(LogEvent event) {
		if (event.getLevel().isGreaterOrEqual(this.level)) {
			write(event);
		}
	}

	protected abstract void write(LogEvent event);

	protected boolean isLogLevelEnabled(LogLevel level) {
		return this.level.isLessOrEqual(level);
	}
	
	@Override
	public boolean isFatalEnabled() {
		return isLogLevelEnabled(LogLevel.FATAL);
	}

	@Override
	public boolean isErrorEnabled() {
		return isLogLevelEnabled(LogLevel.ERROR);
	}

	@Override
	public boolean isWarnEnabled() {
		return isLogLevelEnabled(LogLevel.WARN);
	}

	@Override
	public boolean isInfoEnabled() {
		return isLogLevelEnabled(LogLevel.INFO);
	}

	@Override
	public boolean isDebugEnabled() {
		return isLogLevelEnabled(LogLevel.DEBUG);
	}

	@Override
	public boolean isTraceEnabled() {
		return isLogLevelEnabled(LogLevel.TRACE);
	}
	
	@Override
	public void trace(String message) {
		log(LogLevel.TRACE, message);
	}

	@Override
	public void trace(String message, Throwable error) {
		log(LogLevel.TRACE, message, error);
	}

	@Override
	public void tracef(String format, Object... args) {
		logf(LogLevel.TRACE, format, args);
	}

	@Override
	public void debug(String message) {
		log(LogLevel.DEBUG, message);
	}

	@Override
	public void debug(String message, Throwable error) {
		log(LogLevel.DEBUG, message, error);
	}

	@Override
	public void debugf(String format, Object... args) {
		logf(LogLevel.DEBUG, format, args);
	}

	@Override
	public void info(String message) {
		log(LogLevel.INFO, message);
	}

	@Override
	public void info(String message, Throwable error) {
		log(LogLevel.INFO, message, error);
	}

	@Override
	public void infof(String format, Object... args) {
		logf(LogLevel.INFO, format, args);
	}

	@Override
	public void warn(String message) {
		log(LogLevel.WARN, message);
	}

	@Override
	public void warn(String message, Throwable error) {
		log(LogLevel.WARN, message, error);
	}

	@Override
	public void warnf(String format, Object... args) {
		logf(LogLevel.WARN, format, args);
	}

	@Override
	public void error(String message) {
		log(LogLevel.ERROR, message);
	}

	@Override
	public void error(String message, Throwable error) {
		log(LogLevel.ERROR, message, error);
	}

	@Override
	public void errorf(String format, Object... args) {
		logf(LogLevel.ERROR, format, args);
	}

	@Override
	public void fatal(String message) {
		log(LogLevel.FATAL, message);
	}

	@Override
	public void fatal(String message, Throwable error) {
		log(LogLevel.FATAL, message, error);
	}

	@Override
	public void fatalf(String format, Object... args) {
		logf(LogLevel.FATAL, format, args);
	}
}
