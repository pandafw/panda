package panda.log.impl;

import java.io.PrintStream;

import panda.lang.time.DateTimes;
import panda.log.Log;


public class ConsoleLogAdapter extends AbstractLogAdapter {

	public Log getLogger(String name) {
		return new ConsoleLog(name);
	}

	/**
	 * Console log to System.out and System.err
	 */
	public class ConsoleLog extends AbstractLog {
		ConsoleLog(String name) {
			level = getLogLevel(name);
		}

		@Override
		protected void log(int level, String msg, Throwable t) {
			switch (level) {
			case LEVEL_FATAL:
				output(System.err, "FATAL", msg, t);
				break;
			case LEVEL_ERROR:
				output(System.err, "ERROR", msg, t);
				break;
			case LEVEL_WARN:
				output(System.err, "WARN", msg, t);
				break;
			case LEVEL_INFO:
				output(System.out, "INFO", msg, t);
				break;
			case LEVEL_DEBUG:
				output(System.out, "DEBUG", msg, t);
				break;
			case LEVEL_TRACE:
				output(System.out, "TRACE", msg, t);
				break;
			}
		}

		private void output(PrintStream out, String level, Object msg, Throwable t) {
			out.printf("%s %s [%s] %s\n", 
				DateTimes.timeFormat().format(DateTimes.getDate()), 
				level, 
				Thread.currentThread().getName(), 
				msg);
			if (t != null) {
				t.printStackTrace(out);
			}
		}
	}
}
