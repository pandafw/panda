package panda.lang.reflect;

import java.lang.reflect.Array;

import panda.lang.Exceptions;

public class ArrayCreator<T> implements Creator<T> {
	private Class<T> clazz;
	private int length;

	/**
	 * @param clazz array clazz
	 * @param length
	 */
	public ArrayCreator(Class<T> clazz, int length) {
		this.clazz = clazz;
		this.length = length;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T create(Object... args) {
		try {
			return (T)Array.newInstance(clazz.getComponentType(), length);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
