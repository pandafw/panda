package panda.log.ex;

import java.io.Serializable;

import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogLevel;
import panda.log.Logs;

public class CommonLoggingLogger implements org.apache.commons.logging.Log, Serializable {

	/** Serializable version identifier. */
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------- Attributes

	/** The fully qualified name of the class. */
	private static final String FQCN = CommonLoggingLogger.class.getName();

	/** Log to this logger */
	private transient volatile Log logger = null;

	/** Logger name */
	private final String name;

	// ------------------------------------------------------------ Constructor

	public CommonLoggingLogger() {
		name = null;
	}

	/**
	 * Base constructor.
	 */
	public CommonLoggingLogger(String name) {
		this.name = name;
		this.logger = getLogger();
	}

	/**
	 * For use with a log factory.
	 */
	public CommonLoggingLogger(Log logger) {
		if (logger == null) {
			throw new IllegalArgumentException("Warning - null logger in constructor.");
		}
		this.name = logger.getName();
		this.logger = logger;
	}

	/**
	 * Log a message with trace log level.
	 *
	 * @param message to log
	 * @see org.apache.commons.logging.Log#trace(Object)
	 */
	public void trace(Object message) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.TRACE, String.valueOf(message)));
	}

	/**
	 * Log a message with trace log level.
	 *
	 * @param message to log
	 * @param t log this cause
	 * @see org.apache.commons.logging.Log#trace(Object, Throwable)
	 */
	public void trace(Object message, Throwable t) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.TRACE, String.valueOf(message), t));
	}

	/**
	 * Log a message with debug log level.
	 *
	 * @param message to log
	 * @see org.apache.commons.logging.Log#debug(Object)
	 */
	public void debug(Object message) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.DEBUG, String.valueOf(message)));
	}

	/**
	 * Log a message with debug log level.
	 *
	 * @param message to log
	 * @param t log this cause
	 * @see org.apache.commons.logging.Log#debug(Object, Throwable)
	 */
	public void debug(Object message, Throwable t) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.DEBUG, String.valueOf(message), t));
	}

	/**
	 * Log a message with info log level.
	 *
	 * @param message to log
	 * @see org.apache.commons.logging.Log#info(Object)
	 */
	public void info(Object message) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.INFO, String.valueOf(message)));
	}

	/**
	 * Log a message with info log level.
	 *
	 * @param message to log
	 * @param t log this cause
	 * @see org.apache.commons.logging.Log#info(Object, Throwable)
	 */
	public void info(Object message, Throwable t) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.INFO, String.valueOf(message), t));
	}

	/**
	 * Log a message with info warn level.
	 *
	 * @param message to log
	 * @see org.apache.commons.logging.Log#warn(Object)
	 */
	public void warn(Object message) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.WARN, String.valueOf(message)));
	}

	/**
	 * Log a message with info warn level.
	 *
	 * @param message to log
	 * @param t log this cause
	 * @see org.apache.commons.logging.Log#warn(Object, Throwable)
	 */
	public void warn(Object message, Throwable t) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.WARN, String.valueOf(message), t));
	}

	/**
	 * Log a message with info warn level.
	 *
	 * @param message to log
	 * @see org.apache.commons.logging.Log#error(Object)
	 */
	public void error(Object message) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.ERROR, String.valueOf(message)));
	}

	/**
	 * Log a message with info error level.
	 *
	 * @param message to log
	 * @param t log this cause
	 * @see org.apache.commons.logging.Log#error(Object, Throwable)
	 */
	public void error(Object message, Throwable t) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.ERROR, String.valueOf(message), t));
	}

	/**
	 * Log a message with info error level.
	 *
	 * @param message to log
	 * @see org.apache.commons.logging.Log#fatal(Object)
	 */
	public void fatal(Object message) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.FATAL, String.valueOf(message)));
	}

	/**
	 * Log a message with info fatal level.
	 *
	 * @param message to log
	 * @param t log this cause
	 * @see org.apache.commons.logging.Log#fatal(Object, Throwable)
	 */
	public void fatal(Object message, Throwable t) {
		getLogger().log(new LogEvent(FQCN, name, LogLevel.FATAL, String.valueOf(message), t));
	}

	/**
	 * Return the native Logger instance we are using.
	 */
	public Log getLogger() {
		Log result = logger;
		if (result == null) {
			synchronized (this) {
				result = logger;
				if (result == null) {
					logger = result = Logs.getLog(name);
				}
			}
		}
		return result;
	}

	/**
	 * Is debug logging currently enabled?
	 */
	public boolean isDebugEnabled() {
		return getLogger().isDebugEnabled();
	}

	/**
	 * Is error logging currently enabled?
	 */
	public boolean isErrorEnabled() {
		return getLogger().isErrorEnabled();
	}

	/**
	 * Is fatal logging currently enabled?
	 */
	public boolean isFatalEnabled() {
		return getLogger().isFatalEnabled();
	}

	/**
	 * Is info logging currently enabled?
	 */
	public boolean isInfoEnabled() {
		return getLogger().isInfoEnabled();
	}

	/**
	 * Is trace logging currently enabled?
	 */
	public boolean isTraceEnabled() {
		return getLogger().isTraceEnabled();
	}

	/**
	 * Check whether the Logger used is enabled for <code>WARN</code> priority.
	 */
	public boolean isWarnEnabled() {
		return getLogger().isWarnEnabled();
	}
}
