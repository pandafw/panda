package panda.log.impl;

import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.LogAdapter;


public class ConsoleLogAdapter implements LogAdapter {

	private final static ConsoleLog log = new ConsoleLog();
	private static int level = Log.LEVEL_INFO;

	public Log getLogger(String className) {
		return log;
	}

	/**
	 * @return the level
	 */
	public static int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public static void setLevel(int level) {
		ConsoleLogAdapter.level = level;
	}

	/**
	 * Console log to System.out and System.err
	 */
	public static class ConsoleLog extends AbstractLog {
		private ConsoleLog() {
		}

		public boolean isFatalEnabled() {
			return level >= Log.LEVEL_FATAL;
		}

		public boolean isErrorEnabled() {
			return level >= Log.LEVEL_ERROR;
		}

		public boolean isWarnEnabled() {
			return level >= Log.LEVEL_WARN;
		}

		public boolean isInfoEnabled() {
			return level >= Log.LEVEL_INFO;
		}

		public boolean isDebugEnabled() {
			return level >= Log.LEVEL_DEBUG;
		}

		public boolean isTraceEnabled() {
			return level >= Log.LEVEL_TRACE;
		}
		
		public void fatal(Object message, Throwable t) {
			if (isFatalEnabled()) {
				errorOut("FATAL", message, t);
			}
		}

		public void warn(Object message, Throwable t) {
			if (isWarnEnabled()) {
				errorOut("WARN", message, t);
			}
		}

		public void error(Object message, Throwable t) {
			if (isErrorEnabled()) {
				errorOut("ERROR", message, t);
			}
		}

		public void info(Object message, Throwable t) {
			if (isInfoEnabled()) {
				printOut("INFO", message, t);
			}
		}

		public void debug(Object message, Throwable t) {
			if (isDebugEnabled()) {
				printOut("DEBUG", message, t);
			}
		}

		public void trace(Object message, Throwable t) {
			if (isTraceEnabled()) {
				printOut("TRACE", message, t);
			}
		}

		private void printOut(String level, Object message, Throwable t) {
			System.out.printf("%s %s [%s] %s\n", 
				DateTimes.timeFormat().format(DateTimes.getDate()), 
				level, 
				Thread.currentThread().getName(), 
				message);
			if (t != null) {
				t.printStackTrace(System.out);
			}
		}

		private void errorOut(String level, Object message, Throwable t) {
			System.err.printf("%s %s [%s] %s\n", 
				DateTimes.timeFormat().format(DateTimes.getDate()), 
				level, 
				Thread.currentThread().getName(), 
				message);
			if (t != null) {
				t.printStackTrace(System.err);
			}
		}

		@Override
		protected void log(int level, Object message, Throwable tx) {
			switch (level) {
			case LEVEL_FATAL:
				fatal(message, tx);
				break;
			case LEVEL_ERROR:
				error(message, tx);
				break;
			case LEVEL_WARN:
				warn(message, tx);
				break;
			case LEVEL_INFO:
				info(message, tx);
				break;
			case LEVEL_DEBUG:
				debug(message, tx);
				break;
			case LEVEL_TRACE:
				trace(message, tx);
				break;
			}
		}
	}
}
