package panda.ioc.wea;

import java.lang.reflect.Method;

import panda.ioc.Ioc;
import panda.lang.Exceptions;
import panda.lang.reflect.Creator;

public class BeanMethodCreator<T> implements Creator<T> {
	private Ioc ioc;
	private String name;
	private Method method;

	public BeanMethodCreator(Ioc ioc, String name, Method method) {
		this.ioc = ioc;
		this.name = name;
		this.method = method;
	}

	@SuppressWarnings("unchecked")
	public T create(Object... args) {
		try {
			Object obj = ioc.get(null, name);
			return (T)method.invoke(obj, args);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
