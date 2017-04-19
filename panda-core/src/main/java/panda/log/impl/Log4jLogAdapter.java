package panda.log.impl;

import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import panda.log.Log;
import panda.log.LogAdapter;
import panda.log.LogLevel;

/**
 * Apache log4j adapter
 * 
 */
public class Log4jLogAdapter implements LogAdapter {
	@Override
	public void init(Properties props) {
	}
	
	@Override
	public Log getLogger(String name) {
		return new Log4JLogger(name);
	}

	private static class Log4JLogger extends AbstractLog {
		public static final String SUPER_FQCN = AbstractLog.class.getName();

		private Logger logger;

		Log4JLogger(String className) {
			super(className, null);
			
			logger = Logger.getLogger(className);
			
			if (logger.isEnabledFor(Level.TRACE)) {
				level = LogLevel.TRACE;
			}
			else if (logger.isEnabledFor(Level.DEBUG)) {
				level = LogLevel.DEBUG;
			}
			else if (logger.isEnabledFor(Level.INFO)) {
				level = LogLevel.INFO;
			}
			else if (logger.isEnabledFor(Level.WARN)) {
				level = LogLevel.WARN;
			}
			else if (logger.isEnabledFor(Level.ERROR)) {
				level = LogLevel.ERROR;
			}
			else if (logger.isEnabledFor(Level.FATAL)) {
				level = LogLevel.FATAL;
			}
			else {
				level = LogLevel.TRACE;
			}
		}

		protected Level toLog4jLevel(LogLevel level) {
			if (level == LogLevel.DEBUG) {
				return Level.DEBUG;
			}
			if (level == LogLevel.INFO) {
				return Level.INFO;
			}
			if (level == LogLevel.WARN) {
				return Level.WARN;
			}
			if (level == LogLevel.ERROR) {
				return Level.ERROR;
			}
			if (level == LogLevel.FATAL) {
				return Level.FATAL;
			}
			return Level.TRACE;
		}
		
		@Override
		public void log(LogLevel level, String msg, Throwable tx) {
			Level lvl = toLog4jLevel(level);
			logger.log(SUPER_FQCN, lvl, msg, tx);
		}
	}
}
