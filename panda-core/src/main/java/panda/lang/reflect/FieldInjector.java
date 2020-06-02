package panda.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import panda.lang.Exceptions;

public class FieldInjector implements Injector {
	private Field field;

	/**
	 * @param field the field
	 */
	public FieldInjector(Field field) {
		this.field = field;
		
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	@Override
	public Type type(Object obj) {
		return Fields.getFieldType(field);
	}

	@Override
	public Type[] types(Object obj) {
		return new Type[] { Fields.getFieldType(field) };
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
	
	/**
	 * @param obj object
	 * @param value value
	 */
	public void injects(Object obj, Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + field;
	}
}
