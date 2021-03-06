package panda.bean.handler;

import java.lang.reflect.Type;

import panda.bean.PropertyHandler;

/**
 * 
 * @param <T> class type
 */
public abstract class AbstractPropertyHandler<T> implements PropertyHandler<T> {
	/**
	 * get read property names
	 * @return property names
	 */
	public String[] getReadPropertyNames() {
		return getReadPropertyNames(null);
	}

	/**
	 * get write property names
	 * @return property names
	 */
	public String[] getWritePropertyNames() {
		return getWritePropertyNames(null);
	}

	/**
	 * get property type
	 * @param propertyName property name
	 * @return property type
	 */
	public Type getPropertyType(String propertyName) {
		return getPropertyType(null, propertyName);
	}

	/**
	 * is the property readable
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(String propertyName) {
		return canReadProperty(null, propertyName);
	}

	/**
	 * is the property writable
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(String propertyName) {
		return canWriteProperty(null, propertyName);
	}
	
}
