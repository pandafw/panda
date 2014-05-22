package panda.ioc.aop.config.impl;

import panda.aop.InterceptorChain;
import panda.aop.MethodInterceptor;

public class MyMI implements MethodInterceptor {

	private int time;

	public void filter(InterceptorChain chain) throws Throwable {
		time++;
		chain.doChain();
	}

	public int getTime() {
		return time;
	}
}
