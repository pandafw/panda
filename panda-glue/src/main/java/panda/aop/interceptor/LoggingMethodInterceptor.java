package panda.aop.interceptor;

import panda.aop.AopCallback;
import panda.aop.InterceptorChain;
import panda.aop.MethodInterceptor;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * 为方法添加Log
 * 
 * @author wendal(wendal1985@gmail.com)
 */
public class LoggingMethodInterceptor implements MethodInterceptor {

	private static final Log LOG = Logs.getLog(LoggingMethodInterceptor.class);

	protected boolean logBeforeInvoke;
	protected boolean logAfterInvoke;
	protected boolean logWhenException;
	protected boolean logWhenError;

	public LoggingMethodInterceptor() {
		this.logBeforeInvoke = LOG.isDebugEnabled();
		this.logAfterInvoke = LOG.isDebugEnabled();
		this.logWhenException = LOG.isDebugEnabled();
		this.logWhenError = LOG.isDebugEnabled();
	}

	public void setLogEvent(boolean logBeforeInvoke, boolean logAfterInvoke, boolean logWhenException,
			boolean logWhenError) {
		this.logBeforeInvoke = logBeforeInvoke && LOG.isDebugEnabled();
		this.logAfterInvoke = logAfterInvoke && LOG.isDebugEnabled();
		this.logWhenException = logWhenException && LOG.isDebugEnabled();
		this.logWhenError = logWhenError && LOG.isDebugEnabled();
	}

	public void filter(InterceptorChain chain) {
		try {
			if (logBeforeInvoke)
				LOG.debugf("[beforeInvoke] Obj = %s , Method = %s , args = %s", toString(chain.getCallingObj()),
					chain.getCallingMethod(), str(chain.getArgs()));
			chain.doChain();
		}
		catch (Exception e) {
			if (logWhenException)
				LOG.debugf("[whenException] Obj = %s , Throwable = %s , Method = %s , args = %s",
					toString(chain.getCallingObj()), e, chain.getCallingMethod(), str(chain.getArgs()));
			throw Exceptions.wrapThrow(e);
		}
		catch (Throwable e) {
			if (logWhenError)
				LOG.debugf("[whenError] Obj = %s , Throwable = %s , Method = %s , args = %s",
					toString(chain.getCallingObj()), e, chain.getCallingMethod(), str(chain.getArgs()));
			throw Exceptions.wrapThrow(e);
		}
		finally {
			if (logAfterInvoke)
				LOG.debugf("[afterInvoke] Obj = %s , Return = %s , Method = %s , args = %s",
					toString(chain.getCallingObj()), chain.getReturn(), chain.getCallingMethod(), str(chain.getArgs()));
		}
	}

	protected static final String toString(Object object) {
		if (object != null) {
			if (object instanceof AopCallback) {
				return "[" + object.getClass().getName() + "]"; 
			}
		}
		
		String str = String.valueOf(object);
		if (str.length() > 100) {
			str = str.substring(0, 97) + "...";
		}
		return str;
	}

	protected static final String str(Object... args) {
		if (args == null || args.length == 0) {
			return "[]";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Object object : args) {
			sb.append(toString(object)).append(",");
		}
		
		sb.replace(sb.length() - 1, sb.length(), "]");
		return sb.toString();
	}

}
