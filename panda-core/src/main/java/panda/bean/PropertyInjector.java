package panda.bean;

import java.lang.reflect.Type;

import panda.lang.reflect.Injector;

@SuppressWarnings("rawtypes")
public class PropertyInjector implements Injector {
	private String field;
	private PropertyHandler handler;


	/**
	 * Constructor
	 * 
	 * @param field field name
	 * @param handler property handler
	 */
	public PropertyInjector(String field, PropertyHandler handler) {
		this.field = field;
		this.handler = handler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Type type(Object obj) {
		return handler.getPropertyType(obj, field);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Type[] types(Object obj) {
		return new Type[] { handler.getPropertyType(obj, field) };
	}

	@Override
	@SuppressWarnings("unchecked")
	public void inject(Object obj, Object value) {
		if (!handler.setPropertyValue(obj, field, value)) {
			throw new RuntimeException("Failed to inject " + field + " of " + obj.getClass() + " : " + value);
		}
	}
	
	/**
	 * @param obj object
	 * @param value value
	 */
	public void injects(Object obj, Object[] values) {
		throw new UnsupportedOperationException();
	}
}
