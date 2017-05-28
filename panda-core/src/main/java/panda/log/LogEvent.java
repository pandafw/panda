package panda.log;

import panda.lang.Arrays;
import panda.lang.Strings;

public class LogEvent {
	/** The fully qualified name of the fqcn class. */
	private String fqcn;

	/** the log name */
	private String name;
	
	/** thread id */
	private long threadId;

	/** Event time in milliseconds since 1970 */
	private long millis;

	/** log level */
	private LogLevel level;
	
	/** log message */
	private String message;

	/** log exception */
	private Throwable error;

	/** the class that issued logging call */
	private String callClass;

	/** the callMethod that issued logging call */
	private String callMethod;

	/** the line number that issued logging call */
	private int callLineNo;

	public LogEvent() {
	}

	public LogEvent(String fqcn, String name, LogLevel level, String message) {
		this(fqcn, name, level, message, (Throwable)null);
	}

	public LogEvent(String fqcn, String name, LogLevel level, String message, Throwable error) {
		this(fqcn, name, level, message, new Object[] { error });
	}

	public LogEvent(String fqcn, String name, LogLevel level, String format, Object... args) {
		this.fqcn = fqcn;
		this.name = name;
		this.level = level;
		this.threadId = Thread.currentThread().getId();
		this.millis = System.currentTimeMillis();

		if (format == null) {
			message = "<NULL>";
		}
		else {
			message = format;
			if (args != null && args.length > 0) {
				try {
					message = String.format(format, args);
				}
				catch (Throwable e) {
					LogLog.warn("String format fail in log , format = " + format + " , args = " + Arrays.toString(args), e);
				}
			}
		}

		if (args != null && args.length > 0 && args[args.length - 1] instanceof Throwable) {
			error = (Throwable)args[args.length - 1];
		}
	}

	/**
	 * infer the caller's class and callMethod names
	 */
	public void inferCaller() {
		if (fqcn == null || Strings.isNotEmpty(callClass)) {
			return;
		}

		Throwable ex = new Throwable();
		StackTraceElement ss[] = ex.getStackTrace();

		for (int i = ss.length - 2; i >= 0; i--) {
			StackTraceElement s = ss[i];
			if (fqcn.equals(s.getClassName())) {
				s = ss[i + 1];
				callClass = s.getClassName();
				callMethod = s.getMethodName();
				callLineNo = s.getLineNumber();
				return;
			}
		}

		callClass = "<UNKNOWN CLASS>";
		callMethod = "<UNKNOWN METHOD>";
	}
	
	/**
	 * @return the fqcn
	 */
	public String getFQCN() {
		return fqcn;
	}

	/**
	 * @param fqcn the fqcn to set
	 */
	public void setFQCN(String fqcn) {
		this.fqcn = fqcn;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the level
	 */
	public LogLevel getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(LogLevel level) {
		this.level = level;
	}

	/**
	 * @return the threadId
	 */
	public long getThreadId() {
		return threadId;
	}

	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	/**
	 * @return the millis
	 */
	public long getMillis() {
		return millis;
	}

	/**
	 * @param millis the millis to set
	 */
	public void setMillis(long millis) {
		this.millis = millis;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the error
	 */
	public Throwable getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Throwable error) {
		this.error = error;
	}

	/**
	 * @return the callClass
	 */
	public String getCallClass() {
		return callClass;
	}

	/**
	 * @param callClass the callClass to set
	 */
	public void setCallClass(String callClass) {
		this.callClass = callClass;
	}

	/**
	 * @return the callMethod
	 */
	public String getCallMethod() {
		return callMethod;
	}

	/**
	 * @param callMethod the callMethod to set
	 */
	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}

	/**
	 * @return the callLineNo
	 */
	public int getCallLineNo() {
		return callLineNo;
	}

	/**
	 * @param callLineNo the callLineNo to set
	 */
	public void setCallLineNo(int callLineNo) {
		this.callLineNo = callLineNo;
	}
}
