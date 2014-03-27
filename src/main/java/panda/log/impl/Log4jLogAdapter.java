package panda.log.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import panda.log.Log;
import panda.log.LogAdapter;

/**
 * Apache log4j adapter
 * 
 * <b>Log4J 1.2.11 does not support TRACE, use DEBUG for TRACE</b>
 * 
 */
public class Log4jLogAdapter implements LogAdapter {
	public Log getLogger(String name) {
		return new Log4JLogger(name);
	}

	static class Log4JLogger extends AbstractLog {

		public static final String SUPER_FQCN = AbstractLog.class.getName();
		public static final String SELF_FQCN = Log4JLogger.class.getName();

		private Logger logger;

		private static boolean hasTrace;

		static {
			try {
				Level.class.getDeclaredField("TRACE");
				hasTrace = true;
			}
			catch (Throwable e) {
			}
		}

		Log4JLogger(String className) {
			logger = Logger.getLogger(className);
		}

		public boolean isFatalEnabled() {
			return logger.isEnabledFor(Level.FATAL);
		}

		public boolean isErrorEnabled() {
			return logger.isEnabledFor(Level.ERROR);
		}

		public boolean isWarnEnabled() {
			return logger.isEnabledFor(Level.WARN);
		}

		public boolean isInfoEnabled() {
			return logger.isEnabledFor(Level.INFO);
		}

		public boolean isDebugEnabled() {
			return logger.isEnabledFor(Level.DEBUG);
		}

		public boolean isTraceEnabled() {
			return hasTrace ? logger.isEnabledFor(Level.TRACE) : false;
		}
		
		public void debug(Object msg, Throwable t) {
			if (isDebugEnabled()) {
				logger.log(SELF_FQCN, Level.DEBUG, msg, t);
			}
		}

		public void error(Object msg, Throwable t) {
			if (isErrorEnabled())
				logger.log(SELF_FQCN, Level.ERROR, msg, t);

		}

		public void fatal(Object msg, Throwable t) {
			if (isFatalEnabled())
				logger.log(SELF_FQCN, Level.FATAL, msg, t);
		}

		public void info(Object msg, Throwable t) {
			if (isInfoEnabled())
				logger.log(SELF_FQCN, Level.INFO, msg, t);
		}

		public void trace(Object msg, Throwable t) {
			if (isTraceEnabled())
				logger.log(SELF_FQCN, Level.TRACE, msg, t);
			else if ((!hasTrace) && isDebugEnabled())
				logger.log(SELF_FQCN, Level.DEBUG, msg, t);
		}

		public void warn(Object msg, Throwable t) {
			if (isWarnEnabled())
				logger.log(SELF_FQCN, Level.WARN, msg, t);
		}

		@Override
		protected void log(int level, Object msg, Throwable tx) {
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
				if (hasTrace) {
					logger.log(SUPER_FQCN, Level.TRACE, msg, tx);
				}
				else {
					logger.log(SUPER_FQCN, Level.DEBUG, msg, tx);
				}
				break;
			default:
				break;
			}
		}
	}
}
