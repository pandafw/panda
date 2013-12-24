package panda.log.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

public class JavaLogAppender extends AbstractAppender {
	/**
	 * Construct
	 */
	public JavaLogAppender() {
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
	@Override
	protected void subAppend(LoggingEvent event) {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(event.getLoggerName());
		java.util.logging.Level level = getJavaLogLevel(event);
		if (logger.isLoggable(level)) {
			StringBuilder sb = new StringBuilder();
			sb.append(layout.format(event));

			if (layout.ignoresThrowable()) {
				String[] ss = event.getThrowableStrRep();
				if (ss != null) {
					for (String s : ss) {
						sb.append(s);
						sb.append(Layout.LINE_SEP);
					}
				}
			}
			logger.log(level, sb.toString());
		}
	}

	protected java.util.logging.Level getJavaLogLevel(LoggingEvent event) {
		Level level = event.getLevel();
		if (level.isGreaterOrEqual(Level.ERROR)) {
			return java.util.logging.Level.SEVERE;
		}
		if (level.isGreaterOrEqual(Level.WARN)) {
			return java.util.logging.Level.WARNING;
		}
		if (level.isGreaterOrEqual(Level.INFO)) {
			return java.util.logging.Level.INFO;
		}
		if (level.isGreaterOrEqual(Level.DEBUG)) {
			return java.util.logging.Level.FINE;
		}
		if (level.isGreaterOrEqual(Level.TRACE)) {
			return java.util.logging.Level.FINER;
		}
		return java.util.logging.Level.FINEST;
	}
}
