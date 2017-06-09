package panda.log.impl;

import java.util.Properties;

import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogEvent;
import panda.log.LogLevel;

/**
 * log nothing
 */
public class NopLog implements Log, LogAdapter {
	public static final NopLog INSTANCE = new NopLog();
	
	public void init(String name, Properties props) {
	}
	
	public Log getLog(String name) {
		return this;
	}
	
	protected NopLog() {
	}
	

	@Override
	public String getName() {
		return "";
	}

	@Override
	public LogLevel getLevel() {
		return null;
	}

	@Override
	public void log(LogLevel level, String message) {
	}

	@Override
	public void log(LogLevel level, String message, Throwable error) {
	}

	@Override
	public void logf(LogLevel level, String message, Object... args) {
	}

	@Override
	public void log(LogEvent event) {
	}

	@Override
	public void warnf(String format, Object... args) {}
	
	@Override
	public void warn(String message, Throwable t) {}
	
	@Override
	public void warn(String message) {}
	
	@Override
	public void tracef(String format, Object... args) {}
	
	@Override
	public void trace(String message, Throwable t) {}
	
	@Override
	public void trace(String message) {}
	
	@Override
	public boolean isWarnEnabled() {
		return false;
	}
	
	@Override
	public boolean isTraceEnabled() {
		return false;
	}
	
	@Override
	public boolean isInfoEnabled() {
		return false;
	}
	
	@Override
	public boolean isFatalEnabled() {
		return false;
	}
	
	@Override
	public boolean isErrorEnabled() {
		return false;
	}
	
	@Override
	public boolean isDebugEnabled() {
		return false;
	}
	
	@Override
	public void infof(String format, Object... args) {
	}
	
	@Override
	public void info(String message, Throwable t) {
	}
	
	@Override
	public void info(String message) {
	}
	
	@Override
	public void fatalf(String format, Object... args) {
	}
	
	@Override
	public void fatal(String message, Throwable t) {
	}
	
	@Override
	public void fatal(String message) {
	}
	
	@Override
	public void errorf(String format, Object... args) {
	}
	
	@Override
	public void error(String message, Throwable t) {
	}
	
	@Override
	public void error(String message) {
	}
	
	@Override
	public void debugf(String format, Object... args) {
	}
	
	@Override
	public void debug(String message, Throwable t) {
	}
	
	@Override
	public void debug(String message) {
	}
}
