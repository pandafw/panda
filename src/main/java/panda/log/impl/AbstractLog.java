package panda.log.impl;


import panda.lang.Arrays;
import panda.log.Log;
import panda.log.LogLevel;
import panda.log.Logs;


public abstract class AbstractLog implements Log {
	private static final LogInfo LOGINFO_ERROR = new LogInfo("<ERROR>");
	private static final LogInfo LOGINFO_NULL = new LogInfo("<NULL>");
	
	protected static class LogInfo {
		String msg;
		Throwable ex;

		public LogInfo() {
		}

		public LogInfo(String msg) {
			this.msg = msg;
		}

		public LogInfo(String msg, Throwable ex) {
			this.msg = msg;
			this.ex = ex;
		}
	}

	protected LogLevel level;

	protected AbstractLog(String name) {
		level = Logs.getLogLevel(name);
	}

	protected void log(LogLevel level, LogInfo info) {
		log(level, info.msg, info.ex);
	}

	public abstract void log(LogLevel level, String msg, Throwable tx);
	
	private LogInfo makeInfo(Object obj, Throwable ex) {
		return new LogInfo(String.valueOf(obj), ex);
	}
	
	/**
	 * Create a LogInfo object
	 * <p/>
	 * <code>log.warn(e)</code>
	 * <p/>
	 * <code>log.warnf("User(name=%s) login fail",username,e)</code>
	 */
	private LogInfo makeInfo(Object obj, Object... args) {
		if (obj == null)
			return LOGINFO_NULL;

		try {
			LogInfo info = new LogInfo();
			if (obj instanceof Throwable) {
				info.ex = (Throwable)obj;
				info.msg = info.ex.getMessage();
			}
			else if (args == null || args.length == 0) {
				info.msg = obj.toString();
			}
			else {
				info.msg = String.format(obj.toString(), args);
				if (args[args.length - 1] instanceof Throwable)
					info.ex = (Throwable)args[args.length - 1];
			}
			return info;
		}
		catch (Throwable e) {
			if (isWarnEnabled()) {
				warn("String format fail in log , fmt = " + obj + " , args = " + Arrays.toString(args), e);
			}
			return LOGINFO_ERROR;
		}
	}

	@Override
	public boolean isFatalEnabled() {
		return level.ordinal() <= LogLevel.FATAL.ordinal();
	}

	@Override
	public boolean isErrorEnabled() {
		return level.ordinal() <= LogLevel.ERROR.ordinal();
	}

	@Override
	public boolean isWarnEnabled() {
		return level.ordinal() <= LogLevel.WARN.ordinal();
	}

	@Override
	public boolean isInfoEnabled() {
		return level.ordinal() <= LogLevel.INFO.ordinal();
	}

	@Override
	public boolean isDebugEnabled() {
		return level.ordinal() <= LogLevel.DEBUG.ordinal();
	}

	@Override
	public boolean isTraceEnabled() {
		return level.ordinal() <= LogLevel.TRACE.ordinal();
	}
	
	@Override
	public void trace(Object msg) {
		if (isTraceEnabled()) {
			log(LogLevel.TRACE, makeInfo(msg));
		}
	}

	@Override
	public void trace(Object msg, Throwable t) {
		if (isTraceEnabled()) {
			log(LogLevel.TRACE, makeInfo(msg, t));
		}
	}

	@Override
	public void tracef(String fmt, Object... args) {
		if (isTraceEnabled()) {
			log(LogLevel.TRACE, makeInfo(fmt, args));
		}
	}

	@Override
	public void debug(Object msg) {
		if (isDebugEnabled()) {
			log(LogLevel.DEBUG, makeInfo(msg));
		}
	}

	@Override
	public void debug(Object msg, Throwable t) {
		if (isDebugEnabled()) {
			log(LogLevel.DEBUG, makeInfo(msg, t));
		}
	}

	@Override
	public void debugf(String fmt, Object... args) {
		if (isDebugEnabled()) {
			log(LogLevel.DEBUG, makeInfo(fmt, args));
		}
	}

	@Override
	public void info(Object msg) {
		if (isInfoEnabled()) {
			log(LogLevel.INFO, makeInfo(msg));
		}
	}

	@Override
	public void info(Object msg, Throwable t) {
		if (isInfoEnabled()) {
			log(LogLevel.INFO, makeInfo(msg, t));
		}
	}

	@Override
	public void infof(String fmt, Object... args) {
		if (isInfoEnabled()) {
			log(LogLevel.INFO, makeInfo(fmt, args));
		}
	}

	@Override
	public void warn(Object msg) {
		if (isWarnEnabled()) {
			log(LogLevel.WARN, makeInfo(msg));
		}
	}

	@Override
	public void warn(Object msg, Throwable t) {
		if (isWarnEnabled()) {
			log(LogLevel.WARN, makeInfo(msg, t));
		}
	}

	@Override
	public void warnf(String fmt, Object... args) {
		if (isWarnEnabled()) {
			log(LogLevel.WARN, makeInfo(fmt, args));
		}
	}

	@Override
	public void error(Object msg) {
		if (isErrorEnabled()) {
			log(LogLevel.ERROR, makeInfo(msg));
		}
	}

	@Override
	public void error(Object msg, Throwable t) {
		if (isErrorEnabled()) {
			log(LogLevel.ERROR, makeInfo(msg, t));
		}
	}

	@Override
	public void errorf(String fmt, Object... args) {
		if (isErrorEnabled()) {
			log(LogLevel.ERROR, makeInfo(fmt, args));
		}
	}

	@Override
	public void fatal(Object msg) {
		if (isFatalEnabled()) {
			log(LogLevel.FATAL, makeInfo(msg));
		}
	}

	@Override
	public void fatal(Object msg, Throwable t) {
		if (isFatalEnabled()) {
			log(LogLevel.FATAL, makeInfo(msg, t));
		}
	}

	@Override
	public void fatalf(String fmt, Object... args) {
		if (isFatalEnabled()) {
			log(LogLevel.FATAL, makeInfo(fmt, args));
		}
	}
}
