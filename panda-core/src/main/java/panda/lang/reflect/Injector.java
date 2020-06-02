package panda.lang.reflect;

import java.lang.reflect.Type;

public interface Injector {
	/**
	 * @param obj object
	 * @return the field type
	 */
	Type type(Object obj);
	
	/**
	 * @param obj object
	 * @param value value
	 */
	void inject(Object obj, Object value);

	/**
	 * @param obj object
	 * @return the field type
	 */
	Type[] types(Object obj);

	/**
	 * @param obj object
	 * @param value value
	 */
	void injects(Object obj, Object[] values);
}
