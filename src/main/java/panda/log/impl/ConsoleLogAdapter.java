package panda.log.impl;

import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.LogAdapter;


public class ConsoleLogAdapter implements LogAdapter {

	private final static ConsoleLog log = new ConsoleLog();
	private static int level = Log.LEVEL_INFO;

	public Log getLogger(String name) {
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
		
		public void fatal(Object msg, Throwable t) {
			if (isFatalEnabled()) {
				errorOut("FATAL", msg, t);
			}
		}

		public void warn(Object msg, Throwable t) {
			if (isWarnEnabled()) {
				errorOut("WARN", msg, t);
			}
		}

		public void error(Object msg, Throwable t) {
			if (isErrorEnabled()) {
				errorOut("ERROR", msg, t);
			}
		}

		public void info(Object msg, Throwable t) {
			if (isInfoEnabled()) {
				printOut("INFO", msg, t);
			}
		}

		public void debug(Object msg, Throwable t) {
			if (isDebugEnabled()) {
				printOut("DEBUG", msg, t);
			}
		}

		public void trace(Object msg, Throwable t) {
			if (isTraceEnabled()) {
				printOut("TRACE", msg, t);
			}
		}

		private void printOut(String level, Object msg, Throwable t) {
			System.out.printf("%s %s [%s] %s\n", 
				DateTimes.timeFormat().format(DateTimes.getDate()), 
				level, 
				Thread.currentThread().getName(), 
				msg);
			if (t != null) {
				t.printStackTrace(System.out);
			}
		}

		private void errorOut(String level, Object msg, Throwable t) {
			System.err.printf("%s %s [%s] %s\n", 
				DateTimes.timeFormat().format(DateTimes.getDate()), 
				level, 
				Thread.currentThread().getName(), 
				msg);
			if (t != null) {
				t.printStackTrace(System.err);
			}
		}

		@Override
		protected void log(int level, Object msg, Throwable tx) {
			switch (level) {
			case LEVEL_FATAL:
				fatal(msg, tx);
				break;
			case LEVEL_ERROR:
				error(msg, tx);
				break;
			case LEVEL_WARN:
				warn(msg, tx);
				break;
			case LEVEL_INFO:
				info(msg, tx);
				break;
			case LEVEL_DEBUG:
				debug(msg, tx);
				break;
			case LEVEL_TRACE:
				trace(msg, tx);
				break;
			}
		}
	}
}
