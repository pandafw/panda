package panda.log.log4j;

import java.io.PrintStream;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class ConsoleAppender extends AbstractAppender {
	/**
	 * Construct
	 */
	public ConsoleAppender() {
	}

	protected PrintStream getOutputStream(LoggingEvent event) {
		return event.getLevel().isGreaterOrEqual(Level.WARN) ? System.err : System.out;
	}
	
	@Override
	protected void subAppend(LoggingEvent event) {
		PrintStream ps = getOutputStream(event);

		outputLogEvent(ps, event);

		if (ps == System.err) {
			ps.flush();
		}
	}
}
