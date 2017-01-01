package panda.log.impl;

import java.util.Properties;

import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogLevel;

/**
 * log nothing
 */
public class NopLog implements Log, LogAdapter {
	public void init(Properties props) {
	}
	
	public Log getLogger(String name) {
		return this;
	}
	
	protected NopLog() {
	}
	
	@Override
	public void log(LogLevel level, String msg, Throwable tx) {}
	
	@Override
	public void warnf(String fmt, Object... args) {}
	
	@Override
	public void warn(Object msg, Throwable t) {}
	
	@Override
	public void warn(Object msg) {}
	
	@Override
	public void tracef(String fmt, Object... args) {}
	
	@Override
	public void trace(Object msg, Throwable t) {}
	
	@Override
	public void trace(Object msg) {}
	
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
	public void infof(String fmt, Object... args) {
	}
	
	@Override
	public void info(Object msg, Throwable t) {
	}
	
	@Override
	public void info(Object msg) {
	}
	
	@Override
	public void fatalf(String fmt, Object... args) {
	}
	
	@Override
	public void fatal(Object msg, Throwable t) {
	}
	
	@Override
	public void fatal(Object msg) {
	}
	
	@Override
	public void errorf(String fmt, Object... args) {
	}
	
	@Override
	public void error(Object msg, Throwable t) {
	}
	
	@Override
	public void error(Object msg) {
	}
	
	@Override
	public void debugf(String fmt, Object... args) {
	}
	
	@Override
	public void debug(Object msg, Throwable t) {
	}
	
	@Override
	public void debug(Object msg) {
	}
}
