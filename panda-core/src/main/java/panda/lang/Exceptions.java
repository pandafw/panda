package panda.lang;

import panda.io.stream.StringBuilderWriter;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Utility class for exception
 * @author yf.frank.wang@gmail.com
 */
public abstract class Exceptions {
	public static RuntimeException impossible() {
		return new RuntimeException("Are you kidding me?! It is impossible!");
	}

	public static RuntimeException unsupported() {
		throw new UnsupportedOperationException();
	}
	
	public static RuntimeException unsupported(String msg) {
		throw new UnsupportedOperationException(msg);
	}

	public static IllegalArgumentException illegalArgument(String format, Object ... args) {
		throw new IllegalArgumentException(String.format(format, args));
	}
	
	/**
	 * 根据格式化字符串，生成运行时异常
	 * 
	 * @param format 格式
	 * @param args 参数
	 * @return RuntimeException
	 */
	public static RuntimeException makeThrow(String format, Object... args) {
		return new RuntimeException(String.format(format, args));
	}

	/**
	 * 根据格式化字符串，生成一个指定的异常。
	 * 
	 * @param type 异常类型， 需要有一个字符串为参数的构造函数
	 * @param format 格式
	 * @param args 参数
	 * @return 异常对象
	 */
	public static <T extends Throwable> T makeThrow(Class<T> type, String format, Object... args) {
		return Classes.born(type, String.format(format, args), String.class);
	}

	public static Throwable unwrapThrow(Throwable e) {
		if (e == null) {
			return null;
		}
		if (e instanceof InvocationTargetException) {
			InvocationTargetException itE = (InvocationTargetException)e;
			if (itE.getTargetException() != null) {
				return unwrapThrow(itE.getTargetException());
			}
		}
		if (e instanceof RuntimeException && e.getCause() != null) {
			return unwrapThrow(e.getCause());
		}
		return e;
	}

	/**
	 * wrap a exception
	 * 
	 * @param e exception
	 * @return RuntimeException
	 */
	public static RuntimeException wrapThrow(Throwable e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException)e;
		}
		
		if (e instanceof InvocationTargetException) {
			return wrapThrow(((InvocationTargetException)e).getTargetException());
		}
		return new RuntimeException(e);
	}

	/**
	 * wrap a exception with supplied message
	 * 
	 * @param e exception
	 * @param fmt format
	 * @param args arguments
	 * @return runtime exception
	 */
	public static RuntimeException wrapThrow(Throwable e, String fmt, Object... args) {
		return new RuntimeException(String.format(fmt, args), e);
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the
	 * target method.
	 * <p>Rethrows the underlying exception cast to an {@link RuntimeException} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 * @param ex the exception to rethrow
	 * @throws RuntimeException the rethrown exception
	 */
	public static void rethrowRuntime(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		if (ex instanceof InvocationTargetException) {
			rethrowRuntime(((InvocationTargetException)ex).getTargetException());
		}
		throw new RuntimeException(ex);
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the
	 * target method.
	 * <p>Rethrows the underlying exception cast to an {@link Exception} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 * @param ex the exception to rethrow
	 * @throws Exception the rethrown exception (in case of a checked exception)
	 */
	public static void rethrowException(Throwable ex) throws Exception {
		if (ex instanceof Exception) {
			throw (Exception) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new IllegalStateException(ex);
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the stack trace from a Throwable as a String.
	 * </p>
	 * <p>
	 * The result of this method vary by JDK version as this method uses
	 * {@link Throwable#printStackTrace(java.io.PrintWriter)}. On JDK1.3 and earlier, the cause
	 * exception will not be shown unless the specified throwable alters printStackTrace.
	 * </p>
	 * 
	 * @param throwable the <code>Throwable</code> to be examined
	 * @return the stack trace as generated by the exception's
	 *         <code>printStackTrace(PrintWriter)</code> method
	 */
	public static String getStackTrace(Throwable throwable) {
		StringBuilderWriter sw = new StringBuilderWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * <p>
	 * Captures the stack trace associated with the specified <code>Throwable</code> object,
	 * decomposing it into a list of stack frames.
	 * </p>
	 * <p>
	 * The result of this method vary by JDK version as this method uses
	 * {@link Throwable#printStackTrace(java.io.PrintWriter)}. On JDK1.3 and earlier, the cause
	 * exception will not be shown unless the specified throwable alters printStackTrace.
	 * </p>
	 * 
	 * @param throwable the <code>Throwable</code> to examine, may be null
	 * @return an array of strings describing each stack frame, never null
	 */
	public static String[] getStackFrames(Throwable throwable) {
		if (throwable == null) {
			return Arrays.EMPTY_STRING_ARRAY;
		}
		return getStackFrames(getStackTrace(throwable));
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Returns an array where each element is a line from the argument.
	 * </p>
	 * <p>
	 * The end of line is determined by the value of {@link Systems#LINE_SEPARATOR}.
	 * </p>
	 * 
	 * @param stackTrace a stack trace String
	 * @return an array where each element is a line from the argument
	 */
	static String[] getStackFrames(String stackTrace) {
		String linebreak = Systems.LINE_SEPARATOR;
		StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
		List<String> list = new ArrayList<String>();
		while (frames.hasMoreTokens()) {
			list.add(frames.nextToken());
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * <p>
	 * Produces a <code>List</code> of stack frames - the message is not included. Only the trace of
	 * the specified exception is returned, any caused by trace is stripped.
	 * </p>
	 * <p>
	 * This works in most cases - it will only fail if the exception message contains a line that
	 * starts with: <code>&quot;&nbsp;&nbsp;&nbsp;at&quot;.</code>
	 * </p>
	 * 
	 * @param t is any throwable
	 * @return List of stack frames
	 */
	static List<String> getStackFrameList(Throwable t) {
		String stackTrace = getStackTrace(t);
		String linebreak = Systems.LINE_SEPARATOR;
		StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
		List<String> list = new ArrayList<String>();
		boolean traceStarted = false;
		while (frames.hasMoreTokens()) {
			String token = frames.nextToken();
			// Determine if the line starts with <whitespace>at
			int at = token.indexOf("at");
			if (at != -1 && token.substring(0, at).trim().length() == 0) {
				traceStarted = true;
				list.add(token);
			}
			else if (traceStarted) {
				break;
			}
		}
		return list;
	}

	// -----------------------------------------------------------------------
	/**
	 * Gets a short message summarising the exception.
	 * <p>
	 * The message returned is of the form {ClassNameWithoutPackage}: {ThrowableMessage}
	 * 
	 * @param th the throwable to get a message for, null returns empty string
	 * @return the message, non-null
	 */
	public static String getMessage(Throwable th) {
		if (th == null) {
			return "";
		}
		String cln = th.getClass().getSimpleName();
		String msg = th.getMessage();
		return cln + ": " + Strings.defaultString(msg);
	}
}