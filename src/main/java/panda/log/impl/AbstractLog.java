package panda.log.impl;


import panda.lang.Arrays;
import panda.log.Log;


public abstract class AbstractLog implements Log {
	protected abstract void log(int level, Object msg, Throwable tx);

	protected void log(int level, LogInfo info) {
		log(level, info.msg, info.ex);
	}

	private static final LogInfo LOGINFO_ERROR = new LogInfo();
	private static final LogInfo LOGINFO_NULL = new LogInfo();
	static {
		LOGINFO_ERROR.msg = "!!!!Log Fail!!";
		LOGINFO_NULL.msg = "null";
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
	public void trace(Object msg) {
		if (isTraceEnabled()) {
			log(LEVEL_TRACE, makeInfo(msg));
		}
	}

	@Override
	public void tracef(String fmt, Object... args) {
		if (isTraceEnabled()) {
			log(LEVEL_TRACE, makeInfo(fmt, args));
		}
	}

	@Override
	public void debug(Object msg) {
		if (isDebugEnabled()) {
			log(LEVEL_DEBUG, makeInfo(msg));
		}
	}

	@Override
	public void debugf(String fmt, Object... args) {
		if (isDebugEnabled())
			log(LEVEL_DEBUG, makeInfo(fmt, args));
	}

	@Override
	public void info(Object msg) {
		if (isInfoEnabled()) {
			log(LEVEL_INFO, makeInfo(msg));
		}
	}

	@Override
	public void infof(String fmt, Object... args) {
		if (isInfoEnabled()) {
			log(LEVEL_INFO, makeInfo(fmt, args));
		}
	}

	@Override
	public void warn(Object msg) {
		if (isWarnEnabled()) {
			log(LEVEL_WARN, makeInfo(msg));
		}
	}

	@Override
	public void warnf(String fmt, Object... args) {
		if (isWarnEnabled()) {
			log(LEVEL_WARN, makeInfo(fmt, args));
		}
	}

	@Override
	public void error(Object msg) {
		if (isErrorEnabled())
			log(LEVEL_ERROR, makeInfo(msg));
	}

	@Override
	public void errorf(String fmt, Object... args) {
		if (isErrorEnabled()) {
			log(LEVEL_ERROR, makeInfo(fmt, args));
		}
	}

	@Override
	public void fatal(Object msg) {
		if (isFatalEnabled()) {
			log(LEVEL_FATAL, makeInfo(msg));
		}
	}

	@Override
	public void fatalf(String fmt, Object... args) {
		if (isFatalEnabled()) {
			log(LEVEL_FATAL, makeInfo(fmt, args));
		}
	}
}
