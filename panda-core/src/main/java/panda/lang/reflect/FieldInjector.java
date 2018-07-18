package panda.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import panda.lang.Exceptions;
import panda.lang.Injector;
import panda.lang.Objects;

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
	public void inject(Object obj, Object value) {
		try {
			field.set(obj, value);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("field", field)
				.toString();
	}
}
