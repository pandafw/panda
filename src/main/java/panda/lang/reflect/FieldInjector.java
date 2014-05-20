package panda.lang.reflect;

import java.lang.reflect.Field;

import panda.lang.Exceptions;
import panda.lang.Injector;

public class FieldInjector implements Injector {
	private Field field;

	/**
	 * @param field
	 */
	public FieldInjector(Field field) {
		this.field = field;
	}

	@Override
	public void inject(Object obj, Object value) {
		try {
			field.set(obj, value);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

}
