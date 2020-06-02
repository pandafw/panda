package panda.lang.reflect;

import java.lang.reflect.Method;

import panda.lang.Exceptions;

public class MethodCreator<T> implements Creator<T> {
	private Object object;
	private Method method;

	/**
	 * @param method the method
	 */
	public MethodCreator(Method method) {
		this.method = method;
	}

	/**
	 * @param object the object
	 * @param method the method
	 */
	public MethodCreator(Object object, Method method) {
		this.object = object;
		this.method = method;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T create(Object... args) {
		try {
			return (T)method.invoke(object, args);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
