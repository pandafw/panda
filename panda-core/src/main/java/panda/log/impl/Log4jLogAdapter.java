package panda.log.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;

/**
 * Apache log4j adapter
 */
public class Log4jLogAdapter extends AbstractLogAdapter {
	@Override
	protected Log getLogger(String name) {
		return new Log4JLogger(name, threshold);
	}

	private static class Log4JLogger extends AbstractLog {
		private Logger logger;

		Log4JLogger(String className, LogLevel level) {
			super(className, level);
			
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

			if (this.level.isLessOrEqual(level)) {
				this.level = level;
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
		protected void write(LogEvent event) {
			Level lvl = toLog4jLevel(event.getLevel());
			logger.log(event.getFQCN(), lvl, event.getMessage(), event.getError());
		}
	}
}
