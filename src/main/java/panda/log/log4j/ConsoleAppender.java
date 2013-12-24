package panda.log.log4j;

import java.io.PrintStream;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class ConsoleAppender extends AbstractAppender {
	/**
	 * Construct
	 */
	public ConsoleAppender() {
	}

	/**
	 * The WriterAppender requires a layout. Hence, this method returns
	 * <code>true</code>.
	 */
	public boolean requiresLayout() {
		return true;
	}

	protected PrintStream getOutputStream(LoggingEvent event) {
		return event.getLevel().isGreaterOrEqual(Level.WARN) ? System.err : System.out;
	}
	
	@Override
	protected void subAppend(LoggingEvent event) {
		PrintStream ps = getOutputStream(event);

		ps.print(layout.format(event));
		if (layout.ignoresThrowable()) {
			String[] ss = event.getThrowableStrRep();
			if (ss != null) {
				ps.print(Layout.LINE_SEP);
				for (String s : ss) {
					ps.print(s);
					ps.print(Layout.LINE_SEP);
				}
			}
		}

		if (ps == System.err) {
			ps.flush();
		}
	}
}
