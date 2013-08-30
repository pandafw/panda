package panda.bean.handlers;

import java.lang.reflect.Type;

import panda.lang.Strings;
import panda.lang.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public class AtomicBeanHandler<T> extends AbstractSimpleBeanHandler<T> {
	private final static String[] READ_PROPERTY_NAMES = { "" };
	
	private Type type;
	
	/**
	 * Constructor
	 * @param type bean type
	 */
	public AtomicBeanHandler(Type type) {
		this.type = type;
	}

	/**
	 * create bean object
	 * @return bean instance 
	 */
	public T createObject() {
		try {
			return Types.newInstance(type);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * get read property names
	 * @return property names
	 */
	public String[] getReadPropertyNames() {
		return READ_PROPERTY_NAMES;
	}

	/**
	 * get read property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	public String[] getReadPropertyNames(T beanObject) {
		return getReadPropertyNames();
	}

	/**
	 * get write property names
	 * @return property names
	 */
	public String[] getWritePropertyNames() {
		return Strings.EMPTY_ARRAY;
	}

	/**
	 * get write property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(T beanObject) {
		return getWritePropertyNames();
	}

	/**
	 * is the property readable
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(String propertyName) {
		return true;
	}

	/**
	 * is the property readable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T beanObject, String propertyName) {
		return true;
	}
	
	/**
	 * is the property writable
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canWriteProperty(String propertyName) {
		return false;
	}
	
	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canWriteProperty(T beanObject, String propertyName) {
		return false;
	}

	/**
	 * get property type
	 * @param propertyName property name
	 * @return property type
	 */
	public Type getPropertyType(String propertyName) {
		return type;
	}

	/**
	 * get property type
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public Type getPropertyType(T beanObject, String propertyName) {
		return type;
	}

	/**
	 * get property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @return value
	 */
	public Object getPropertyValue(T beanObject, String propertyName) {
		return beanObject;
	}

	/**
	 * set property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @param value value
	 */
	public void setPropertyValue(T beanObject, String propertyName, Object value) {
	}
}
