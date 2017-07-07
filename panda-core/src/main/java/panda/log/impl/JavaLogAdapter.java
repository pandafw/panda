package panda.log.impl;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.apache.log4j.helpers.LogLog;

import panda.io.Streams;
import panda.lang.ClassLoaders;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;

public class JavaLogAdapter extends AbstractLogAdapter {
	static {
		initJavaLogging();
	}

	private static final String JAVA_LOG_CONFIG = "java-logging.properties";
	
	private static void initJavaLogging() {
		if (Systems.IS_OS_APPENGINE) {
			return;
		}
		
		InputStream is = ClassLoaders.getResourceAsStream(JAVA_LOG_CONFIG);
		if (is == null) {
			return;
		}
		
		try {
			LogManager.getLogManager().readConfiguration(is);
		}
		catch (Throwable e) {
			LogLog.error("Failed to load " + JAVA_LOG_CONFIG, e);
		}
		finally {
			Streams.safeClose(is);
		}
	}

	@Override
	protected Log getLogger(String name) {
		return new JavaLog(this, name);
	}

	protected void write(LogEvent event) {
		java.util.logging.Logger jLogger = java.util.logging.Logger.getLogger(event.getName());
		Level jLevel = getJavaLogLevel(event.getLevel());
		if (jLogger.isLoggable(jLevel)) {
			event.inferCaller();
			if (event.getError() == null) {
				jLogger.logp(jLevel, event.getCallClass(), event.getCallMethod(), event.getMessage());
			}
			else {
				jLogger.logp(jLevel, event.getCallClass(), event.getCallMethod(), event.getMessage(), event.getError());
			}
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
			super(adapter.logs, name, adapter.threshold);
			this.adapter = adapter;
			this.name = name;
		}

		@Override
		protected void write(LogEvent event) {
			adapter.write(event);
		}
	}
}
