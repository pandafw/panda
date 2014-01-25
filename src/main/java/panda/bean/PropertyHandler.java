package panda.bean;

import java.lang.reflect.Type;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public interface PropertyHandler<T> {
	/**
	 * get object type
	 */
	Type getType();
	
	/**
	 * get read property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	String[] getReadPropertyNames(T beanObject);

	/**
	 * get read property names
	 * @return property names
	 */
	String[] getReadPropertyNames();

	/**
	 * get write property names
	 * @return property names
	 */
	String[] getWritePropertyNames();

	/**
	 * get write property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	String[] getWritePropertyNames(T beanObject);
	
	/**
	 * get property type
	 * @param propertyName property name
	 * @return property type
	 */
	Type getPropertyType(String propertyName);

	/**
	 * get property type
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	Type getPropertyType(T beanObject, String propertyName);

	/**
	 * is the property readable
	 * @param propertyName property name
	 * @return true if property is readable
	 */
	boolean canReadProperty(String propertyName);

	/**
	 * is the property readable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return true if property is readable
	 */
	boolean canReadProperty(T beanObject, String propertyName);
	
	/**
	 * is the property writable
	 * @param beanName bean name
	 * @return true if property is writable
	 */
	boolean canWriteProperty(String propertyName);
	
	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return true if property is writable
	 */
	boolean canWriteProperty(T beanObject, String propertyName);
	
	/**
	 * get property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @return property value
	 */
	Object getPropertyValue(T beanObject, String propertyName);
	
	/**
	 * set property value 
	 * @param beanObject bean object
	 * @param propertyName property name
	 * @param value value
	 * @return true if set value successfully
	 */
	boolean setPropertyValue(T beanObject, String propertyName, Object value);
}
