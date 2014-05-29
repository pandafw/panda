package panda.log.impl;

import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.LogAdapter;


public class ConsoleLogAdapter implements LogAdapter {

	public Log getLogger(String name) {
		return new ConsoleLog(name);
	}

	/**
	 * Console log to System.out and System.err
	 */
	public static class ConsoleLog extends AbstractConfigLog {
		ConsoleLog(String name) {
			super(name);
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
	}
}
