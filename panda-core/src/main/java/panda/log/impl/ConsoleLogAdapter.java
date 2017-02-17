package panda.log.impl;

import java.io.PrintStream;

import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.LogLevel;


public class ConsoleLogAdapter extends BaseLogAdapter {
	public Log getLogger(String name) {
		return new ConsoleLog(this, name);
	}

	private static class ConsoleLog extends BaseLog {
		public ConsoleLog(BaseLogAdapter adapter, String name) {
			super(adapter, name);
		}

		@Override
		public void log(LogLevel level, String msg, Throwable t) {
			PrintStream out = (level.isGreaterOrEqual(LogLevel.WARN) ? System.err : System.out);
			output(out, level.toString(), msg, t);
		}

		private void output(PrintStream out, String level, Object msg, Throwable t) {
			out.printf("%s %s [%s] %s\n", DateTimes.isoTimeFormat().format(DateTimes.getDate()), level, Thread.currentThread()
				.getName(), msg);
			if (t != null) {
				t.printStackTrace(out);
			}
		}
	}
}
