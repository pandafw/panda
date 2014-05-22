package panda.ioc.impl;

import java.lang.reflect.Method;

import panda.ioc.IocEventTrigger;
import panda.lang.Exceptions;

public class MethodEventTrigger implements IocEventTrigger<Object> {

	private Method method;

	public MethodEventTrigger(Method method) {
		this.method = method;
	}

	public void trigger(Object obj) {
		try {
			method.invoke(obj);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

}
