package panda.log.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public abstract class AbstractAppender extends AppenderSkeleton {
	/**
	 * Construct
	 */
	public AbstractAppender() {
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
