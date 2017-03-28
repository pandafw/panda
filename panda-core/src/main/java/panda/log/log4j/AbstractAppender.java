package panda.log.log4j;

import java.io.IOException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import panda.lang.Exceptions;

public abstract class AbstractAppender extends AppenderSkeleton {
	/**
	 * Construct
	 */
	public AbstractAppender() {
	}

	/**
	 * The WriterAppender requires a layout. Hence, this method returns
	 * <code>true</code>.
	 */
	public boolean requiresLayout() {
		return true;
	}

	/**
	 * Activate the specified options, such as the smtp host, the recipient, from, etc.
	 */
	public void activateOptions() {
		if (layout == null) {
			setLayout(new PatternLayout("%m%n"));
		}
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
	
	protected abstract void subAppend(LoggingEvent event);
	
	protected void outputLogEvent(Appendable writer, LoggingEvent event) {
		try {
			writer.append(layout.format(event));
			if (layout.ignoresThrowable()) {
				String[] ss = event.getThrowableStrRep();
				if (ss != null) {
					writer.append(Layout.LINE_SEP);
					for (String s : ss) {
						writer.append(s);
						writer.append(Layout.LINE_SEP);
					}
				}
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	protected String formatLogEvent(LoggingEvent event) {
		StringBuilder sb = new StringBuilder();
		outputLogEvent(sb, event);
		return sb.toString();
	}

	/**
	 * This method determines if there is a sense in attempting to append.
	 * 
	 * <p>
	 * It checks whether there is a set output target and also if there is a set
	 * layout. If these checks fail, then the boolean value <code>false</code>
	 * is returned.</p>
	 * 
	 * @return check result
	 */
	protected boolean checkEntryConditions() {
		if (this.closed) {
			LogLog.warn("Not allowed to write to a closed appender.");
			return false;
		}

		if (this.layout == null) {
			errorHandler.error("No layout set for the appender named [" + name + "].");
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
