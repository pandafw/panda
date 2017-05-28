package panda.log.impl;

import java.io.PrintStream;

import panda.log.LogEvent;
import panda.log.LogFormat;
import panda.log.LogLevel;
import panda.log.LogFormat.SimpleLogFormat;

public class ConsoleLog extends AbstractLog {
	public static PrintStream output;
	
	protected LogFormat format = SimpleLogFormat.DEFAULT;

	public ConsoleLog(String name) {
		super(name, null);
	}

	public ConsoleLog(String name, LogLevel level) {
		super(name, level);
	}

	public ConsoleLog(String name, LogLevel level, LogFormat format) {
		super(name, level);
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
			event.getError().printStackTrace(out);
		}
	}
}
