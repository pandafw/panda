package panda.log.impl;

import java.util.logging.Level;

import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;


public class JavaLogAdapter extends AbstractLogAdapter {

	@Override
	protected Log getLogger(String name) {
		return new JavaLog(this, name);
	}

	protected void write(LogEvent event) {
		java.util.logging.Logger jLogger = java.util.logging.Logger.getLogger(event.getName());
		Level jLevel = getJavaLogLevel(event.getLevel());
		if (jLogger.isLoggable(jLevel)) {
			jLogger.log(jLevel, event.getMessage(), event.getError());
		}
	}

	protected Level getJavaLogLevel(LogLevel level) {
		if (level.isGreaterOrEqual(LogLevel.ERROR)) {
			return Level.SEVERE;
		}
		if (level.isGreaterOrEqual(LogLevel.WARN)) {
			return Level.WARNING;
		}
		if (level.isGreaterOrEqual(LogLevel.INFO)) {
			return Level.INFO;
		}
		if (level.isGreaterOrEqual(LogLevel.DEBUG)) {
			return Level.FINE;
		}
		if (level.isGreaterOrEqual(LogLevel.TRACE)) {
			return Level.FINER;
		}
		return Level.FINEST;
	}

	protected static class JavaLog extends AbstractLog {
		protected JavaLogAdapter adapter;
		protected String name;
		
		protected JavaLog(JavaLogAdapter adapter, String name) {
			super(name, adapter.threshold);
			this.adapter = adapter;
			this.name = name;
		}

		@Override
		protected void write(LogEvent event) {
			adapter.write(event);
		}
	}
}
