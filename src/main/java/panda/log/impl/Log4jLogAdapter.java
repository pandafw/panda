package panda.log.impl;

import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import panda.log.Log;
import panda.log.LogAdapter;

/**
 * Apache log4j adapter
 * 
 */
public class Log4jLogAdapter implements LogAdapter {
	public void init(Properties props) {
	}
	
	public Log getLogger(String name) {
		return new Log4JLogger(name);
	}

	static class Log4JLogger extends AbstractLog {
		public static final String SUPER_FQCN = AbstractLog.class.getName();
		public static final String SELF_FQCN = Log4JLogger.class.getName();

		private Logger logger;

		Log4JLogger(String className) {
			logger = Logger.getLogger(className);
			
			if (logger.isEnabledFor(Level.TRACE)) {
				level = Log.LEVEL_TRACE;
			}
			else if (logger.isEnabledFor(Level.DEBUG)) {
				level = Log.LEVEL_DEBUG;
			}
			else if (logger.isEnabledFor(Level.INFO)) {
				level = Log.LEVEL_INFO;
			}
			else if (logger.isEnabledFor(Level.WARN)) {
				level = Log.LEVEL_WARN;
			}
			else if (logger.isEnabledFor(Level.ERROR)) {
				level = Log.LEVEL_ERROR;
			}
			else if (logger.isEnabledFor(Level.FATAL)) {
				level = Log.LEVEL_FATAL;
			}
			else {
				level = Log.LEVEL_TRACE;
			}
		}

		@Override
		protected void log(int level, String msg, Throwable tx) {
			switch (level) {
			case LEVEL_FATAL:
				logger.log(SUPER_FQCN, Level.FATAL, msg, tx);
				break;
			case LEVEL_ERROR:
				logger.log(SUPER_FQCN, Level.ERROR, msg, tx);
				break;
			case LEVEL_WARN:
				logger.log(SUPER_FQCN, Level.WARN, msg, tx);
				break;
			case LEVEL_INFO:
				logger.log(SUPER_FQCN, Level.INFO, msg, tx);
				break;
			case LEVEL_DEBUG:
				logger.log(SUPER_FQCN, Level.DEBUG, msg, tx);
				break;
			case LEVEL_TRACE:
				logger.log(SUPER_FQCN, Level.TRACE, msg, tx);
				break;
			default:
				break;
			}
		}
	}
}
