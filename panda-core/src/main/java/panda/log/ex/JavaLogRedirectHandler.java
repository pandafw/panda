package panda.log.ex;

import java.io.ByteArrayInputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;
import panda.log.LogLog;
import panda.log.Logs;

public class JavaLogRedirectHandler extends Handler {
	public static void redirect(boolean redirect) {
		String CONFIG = "handlers = " + JavaLogRedirectHandler.class.getName() + "\n.level = " + (redirect ? "ALL" : "OFF");

		try {
			LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(CONFIG.getBytes()));
		}
		catch (Throwable e) {
			LogLog.error("Failed to redirect Java Logging", e);
		}
	}

	/**
	 * Publish a <tt>LogRecord</tt>.
	 * <p>
	 * The logging request was made initially to a <tt>Logger</tt> object, which initialized the
	 * <tt>LogRecord</tt> and forwarded it here.
	 * <p>
	 * The <tt>Handler</tt> is responsible for formatting the message, when and if necessary. The
	 * formatting should include localization.
	 *
	 * @param record description of the log event. A null record is silently ignored and is not
	 *            published
	 */
	public void publish(LogRecord record) {
		if (record == null) {
			return;
		}
		
		LogEvent evt = new LogEvent();
		evt.setName(record.getLoggerName());
		evt.setLevel(getLogLevel(record));
		evt.setMessage(record.getMessage());
		evt.setError(record.getThrown());
		evt.setCallClass(record.getSourceClassName());
		evt.setCallMethod(record.getSourceMethodName());
		evt.setMillis(record.getMillis());
		evt.setThreadId(record.getThreadID());

		Log log = Logs.getLog(evt.getName());
		log.log(evt);
	}

	private LogLevel getLogLevel(LogRecord record) {
		Level lvl = record.getLevel();
		if (Level.SEVERE.equals(lvl)) {
			return LogLevel.ERROR;
		}
		if (Level.WARNING.equals(lvl)) {
			return LogLevel.WARN;
		}
		if (Level.INFO.equals(lvl)) {
			return LogLevel.INFO;
		}
		if (Level.FINE.equals(lvl)) {
			return LogLevel.DEBUG;
		}
		if (Level.FINER.equals(lvl) || Level.FINEST.equals(lvl)) {
			return LogLevel.TRACE;
		}
		
		return null;
	}

	/**
	 * Check if this <tt>Handler</tt> would actually log a given <tt>LogRecord</tt>.
	 * <p>
	 * This method checks if the <tt>LogRecord</tt> has an appropriate level and whether it
	 * satisfies any <tt>Filter</tt>. It will also return false if no output stream has been
	 * assigned yet or the LogRecord is null.
	 * <p>
	 * 
	 * @param record a <tt>LogRecord</tt>
	 * @return true if the <tt>LogRecord</tt> would be logged.
	 */
	public boolean isLoggable(LogRecord record) {
		if (record == null) {
			return false;
		}
		
		Log log = Logs.getLog(record.getLoggerName());
		Level lvl = record.getLevel();
		if (Level.SEVERE.equals(lvl)) {
			return log.isErrorEnabled();
		}
		if (Level.WARNING.equals(lvl)) {
			return log.isWarnEnabled();
		}
		if (Level.INFO.equals(lvl)) {
			return log.isInfoEnabled();
		}
		if (Level.FINE.equals(lvl)) {
			return log.isDebugEnabled();
		}
		if (Level.FINER.equals(lvl) || Level.FINEST.equals(lvl)) {
			return log.isTraceEnabled();
		}
		
		return false;
	}

	/**
	 * Flush any buffered messages.
	 */
	public void flush() {
	}

	/**
	 * Close the <tt>Handler</tt> and free all associated resources.
	 * <p>
	 * The close method will perform a <tt>flush</tt> and then close the <tt>Handler</tt>. After
	 * close has been called this <tt>Handler</tt> should no longer be used. Method calls may either
	 * be silently ignored or may throw runtime exceptions.
	 *
	 * @exception SecurityException if a security manager exists and if the caller does not have
	 *                <tt>LoggingPermission("control")</tt>.
	 */
	public void close() throws SecurityException {
	}
}
