package panda.log.impl;

import java.io.PrintStream;

import panda.lang.Exceptions;
import panda.log.LogEvent;
import panda.log.LogFormat;
import panda.log.LogFormat.SimpleLogFormat;
import panda.log.LogLevel;
import panda.log.Logs;

public class ConsoleLog extends AbstractLog {
	public static PrintStream output;
	
	protected LogFormat format = SimpleLogFormat.DEFAULT;

	public ConsoleLog(Logs logs, String name) {
		super(logs, name, null);
	}

	public ConsoleLog(Logs logs, String name, LogLevel level) {
		super(logs, name, level);
	}

	public ConsoleLog(Logs logs, String name, LogLevel level, LogFormat format) {
		super(logs, name, level);
		this.format = format;
	}

	/**
	 * set log level
	 * @param level log level
	 */
	public void setLogLevel(LogLevel level) {
		this.level = level;
	}

	@Override
	@SuppressWarnings("resource")
	protected void write(LogEvent event) {
		PrintStream out ;
		if (output == null) {
			out = (level.isGreaterOrEqual(LogLevel.WARN) ? System.err : System.out);
		}
		else {
			out = output;
		}
		
		String msg = format.format(event);
		out.print(msg);
		if (event.getError() != null) {
			msg = Exceptions.getStackTrace(event.getError());
			out.print(msg);
		}
	}
}
