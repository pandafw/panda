package panda.log.impl;

import java.io.PrintStream;

import panda.lang.time.DateTimes;
import panda.log.LogLevel;

public class ConsoleLog extends AbstractLog {
	public static PrintStream console;
	
	public ConsoleLog(String name, LogLevel level) {
		super(name, level);
	}

	/**
	 * set log level
	 * @param level log level
	 */
	public void setLogLevel(LogLevel level) {
		this.level = level;
	}
	
	@Override
	public void log(LogLevel level, String msg, Throwable t) {
		PrintStream out ;
		if (console == null) {
			out = (level.isGreaterOrEqual(LogLevel.WARN) ? System.err : System.out);
		}
		else {
			out = console;
		}
		output(out, level.toString(), msg, t);
	}

	private void output(PrintStream out, String level, Object msg, Throwable t) {
		out.printf("%s %s [%s] %s\n", 
			DateTimes.timestampFormat().format(DateTimes.getDate()), 
			level, 
			Thread.currentThread().getName(), 
			msg);
		if (t != null) {
			t.printStackTrace(out);
		}
	}
}
