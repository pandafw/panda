package panda.log.impl;

import java.util.Properties;

import panda.log.Log;
import panda.log.LogAdapter;

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
	
	public void warnf(String fmt, Object... args) {}
	
	public void warn(Object msg, Throwable t) {}
	
	public void warn(Object msg) {}
	
	public void tracef(String fmt, Object... args) {}
	
	public void trace(Object msg, Throwable t) {}
	
	public void trace(Object msg) {}
	
	public boolean isWarnEnabled() {
		return false;
	}
	
	public boolean isTraceEnabled() {
		return false;
	}
	
	public boolean isInfoEnabled() {
		return false;
	}
	
	public boolean isFatalEnabled() {
		return false;
	}
	
	public boolean isErrorEnabled() {
		return false;
	}
	
	public boolean isDebugEnabled() {
		return false;
	}
	
	public void infof(String fmt, Object... args) {
	}
	
	public void info(Object msg, Throwable t) {
	}
	
	public void info(Object msg) {
	}
	
	public void fatalf(String fmt, Object... args) {
	}
	
	public void fatal(Object msg, Throwable t) {
	}
	
	public void fatal(Object msg) {
	}
	
	public void errorf(String fmt, Object... args) {
	}
	
	public void error(Object msg, Throwable t) {
	}
	
	public void error(Object msg) {
	}
	
	public void debugf(String fmt, Object... args) {
	}
	
	public void debug(Object msg, Throwable t) {
	}
	
	public void debug(Object msg) {
	}
}
