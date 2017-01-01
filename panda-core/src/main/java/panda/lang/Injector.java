package panda.lang;

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
}
