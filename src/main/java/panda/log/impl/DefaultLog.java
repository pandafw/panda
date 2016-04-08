package panda.log.impl;

import java.io.PrintStream;

import panda.lang.time.DateTimes;
import panda.log.LogLevel;

public class DefaultLog extends AbstractLog {
	public DefaultLog(String name) {
		super(name);
	}

	@Override
	public void log(LogLevel level, String msg, Throwable t) {
		PrintStream out = (level.isGreaterOrEqual(LogLevel.WARN) ? System.err : System.out);
		output(out, level.toString(), msg, t);
	}

	private void output(PrintStream out, String level, Object msg, Throwable t) {
		out.printf("%s %s [%s] %s\n", DateTimes.timeFormat().format(DateTimes.getDate()), level, Thread.currentThread()
			.getName(), msg);
		if (t != null) {
			t.printStackTrace(out);
		}
	}
}
