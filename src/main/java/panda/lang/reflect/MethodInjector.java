package panda.lang.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import panda.lang.Exceptions;
import panda.lang.Injector;

public class MethodInjector implements Injector {
	private Method method;

	/**
	 * @param method
	 */
	public MethodInjector(Method method) {
		this.method = method;

		if (!method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	@Override
	public Type type(Object obj) {
		return Methods.getParameterType(method, 0);
	}
	
	@Override
	public void inject(Object obj, Object value) {
		try {
			method.invoke(obj, value);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

}
