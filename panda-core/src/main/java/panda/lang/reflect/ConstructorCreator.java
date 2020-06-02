package panda.lang.reflect;

import java.lang.reflect.Constructor;

import panda.lang.Exceptions;

public class ConstructorCreator<T> implements Creator<T> {
	private Constructor<T> constructor;

	/**
	 * @param constructor the constructor
	 */
	public ConstructorCreator(Constructor<T> constructor) {
		this.constructor = constructor;
	}

	@Override
	public T create(Object... args) {
		try {
			return constructor.newInstance(args);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + constructor;
	}
}
