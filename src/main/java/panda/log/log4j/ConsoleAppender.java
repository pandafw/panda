package panda.log.log4j;

import java.io.PrintStream;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class ConsoleAppender extends AppenderSkeleton {
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

	/**
	 * This method is called by the {@link AppenderSkeleton#doAppend} method.
	 * 
	 * <p>
	 * If the output stream exists and is writable then write a log statement to
	 * the output stream. Otherwise, write a single warning message to
	 * <code>System.err</code>.
	 * 
	 * <p>
	 * The format of the output will depend on this appender's layout.
	 */
	public void append(LoggingEvent event) {
		// Reminder: the nesting of calls is:
		//
		// doAppend()
		// - check threshold
		// - filter
		// - append();

		if (!checkEntryConditions()) {
			return;
		}
		
		subAppend(event);
	}
	
	protected PrintStream getOutputStream(LoggingEvent event) {
		return event.getLevel().isGreaterOrEqual(Level.WARN) ? System.err : System.out;
	}
	
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

	/**
	 * This method determines if there is a sense in attempting to append.
	 * 
	 * <p>
	 * It checks whether there is a set output target and also if there is a set
	 * layout. If these checks fail, then the boolean value <code>false</code>
	 * is returned.
	 */
	protected boolean checkEntryConditions() {
		if (this.closed) {
			LogLog.warn("Not allowed to write to a closed appender.");
			return false;
		}

		if (this.layout == null) {
			errorHandler.error("No layout set for the appender named [" + name
					+ "].");
			return false;
		}
		return true;
	}

	/**
	 * Release any resources allocated within the appender such as file handles,
	 * network connections, etc.
	 * 
	 * <p>
	 * It is a programming error to append to a closed appender.
	 * 
	 */
	public void close() {
		this.closed = true;
	}
}
