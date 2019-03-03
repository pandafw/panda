package panda.aop.asm.test;

import java.lang.reflect.Method;

import panda.aop.interceptor.AbstractMethodInterceptor;
import panda.bind.json.Jsons;
import panda.log.Log;
import panda.log.Logs;

public class MyMethodInterceptor extends AbstractMethodInterceptor {

	private static final Log log = Logs.getLog(MyMethodInterceptor.class);
	
	public Object afterInvoke(Object obj, Object result, Method arg2, Object... objs) {
		log.debug("After..... " + arg2.getName());
		printArgs(objs);
		return result;
	}

	public boolean beforeInvoke(Object arg0, Method arg1, Object... objs) {
		log.debug("-----------------------------------------------------");
		log.debug("Before.... " + arg1.getName());
		printArgs(objs);
		return true;
	}

	public boolean whenError(Throwable e, Object arg1, Method arg2, Object... objs) {
		log.debug("抛出了Throwable " + e);
		printArgs(objs);
		return true;
	}

	public boolean whenException(Exception e, Object arg1, Method arg2, Object... objs) {
		log.debug("抛出了Exception " + e);
		printArgs(objs);
		return true;
	}

	void printArgs(Object... objs) {
		log.debug("参数 " + Jsons.toJson(objs));
	}

}
