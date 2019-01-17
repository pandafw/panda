package panda.log.impl;

import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;
import panda.log.Logs;

public class RuntimeLog implements Log {
	// real log
	private Log log;

	// logs
	private Logs logs;
	
	// log creation time
	private long time;
	
	public RuntimeLog(Log log, Logs logs) {
		this.log = log;
		this.logs = logs;
		this.time = logs.getConfigTime();
	}

	private void reload() {
		if (time < logs.getConfigTime()) {
			log = logs.newLogger(getName());
			time = logs.getConfigTime();
		}
	}

	private void write(LogEvent event) {
		log.log(event);
	}

	/**
	 * @return the FQCN
	 */
	public String getFQCN() {
		return RuntimeLog.class.getName();
	}
	
	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return log.getName();
	}

	/**
	 * @return the level
	 */
	@Override
	public LogLevel getLevel() {
		reload();
		return log.getLevel();
	}

	@Override
	public void log(LogLevel level, String message) {
		if (level.isGreaterOrEqual(getLevel())) {
			write(new LogEvent(getFQCN(), getName(), level, message));
		}
	}

	@Override
	public void log(LogLevel level, String message, Throwable error) {
		if (level.isGreaterOrEqual(getLevel())) {
			write(new LogEvent(getFQCN(), getName(), level, message, error));
		}
	}

	@Override
	public void logf(LogLevel level, String message, Object... args) {
		if (level.isGreaterOrEqual(getLevel())) {
			write(new LogEvent(getFQCN(), getName(), level, message, args));
		}
	}

	@Override
	public void log(LogEvent event) {
		if (event.getLevel().isGreaterOrEqual(getLevel())) {
			write(event);
		}
	}

	@Override
	public boolean isFatalEnabled() {
		return getLevel().isLessOrEqual(LogLevel.FATAL);
	}

	@Override
	public boolean isErrorEnabled() {
		return getLevel().isLessOrEqual(LogLevel.ERROR);
	}

	@Override
	public boolean isWarnEnabled() {
		return getLevel().isLessOrEqual(LogLevel.WARN);
	}

	@Override
	public boolean isInfoEnabled() {
		return getLevel().isLessOrEqual(LogLevel.INFO);
	}

	@Override
	public boolean isDebugEnabled() {
		return getLevel().isLessOrEqual(LogLevel.DEBUG);
	}

	@Override
	public boolean isTraceEnabled() {
		return getLevel().isLessOrEqual(LogLevel.TRACE);
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
