package panda.log.impl;

import panda.log.Log;
import panda.log.LogAdapter;


public class AndroidLogAdapter implements LogAdapter {

	public Log getLogger(String name) {
		return new AndroidLog(name);
	}

	/**
	 * Console log to System.out and System.err
	 */
	public static class AndroidLog extends AbstractLog {
		private String tag;
		
		private AndroidLog(String name) {
			tag = name;
		}

		public boolean isFatalEnabled() {
			return android.util.Log.isLoggable(tag, android.util.Log.ASSERT);
		}

		public boolean isErrorEnabled() {
			return android.util.Log.isLoggable(tag, android.util.Log.ERROR);
		}

		public boolean isWarnEnabled() {
			return android.util.Log.isLoggable(tag, android.util.Log.WARN);
		}

		public boolean isInfoEnabled() {
			return android.util.Log.isLoggable(tag, android.util.Log.INFO);
		}

		public boolean isDebugEnabled() {
			return android.util.Log.isLoggable(tag, android.util.Log.DEBUG);
		}

		public boolean isTraceEnabled() {
			return android.util.Log.isLoggable(tag, android.util.Log.VERBOSE);
		}
		
		public void fatal(Object msg, Throwable t) {
			if (t != null) {
				msg = String.valueOf(msg) + "\n" + android.util.Log.getStackTraceString(t);
			}
			android.util.Log.println(android.util.Log.ASSERT, tag, String.valueOf(msg));
		}

		public void warn(Object msg, Throwable t) {
			android.util.Log.w(tag, String.valueOf(msg), t);
		}

		public void error(Object msg, Throwable t) {
			android.util.Log.e(tag, String.valueOf(msg), t);
		}

		public void info(Object msg, Throwable t) {
			android.util.Log.i(tag, String.valueOf(msg), t);
		}

		public void debug(Object msg, Throwable t) {
			android.util.Log.d(tag, String.valueOf(msg), t);
		}

		public void trace(Object msg, Throwable t) {
			android.util.Log.v(tag, String.valueOf(msg), t);
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
