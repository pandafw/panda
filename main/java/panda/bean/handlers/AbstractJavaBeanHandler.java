package panda.bean.handlers;

import java.lang.reflect.Type;

import panda.bean.Beans;
import panda.bean.IncorrectAccessException;
import panda.bean.NoSuchPropertyException;
import panda.lang.Arrays;
import panda.lang.Types;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public abstract class AbstractJavaBeanHandler<T> extends AbstractBeanHandler<T> {

	protected Type type;

	/**
	 * Constructor
	 * @param factory bean handler factory
	 * @param type bean type
	 */
	public AbstractJavaBeanHandler(Beans factory, Type type) {
		super(factory);
		this.type = type;
	}

	/**
	 * create bean object
	 * @return bean instance 
	 */
	public T createObject() {
		try {
			return Types.newInstance(Types.getDefaultImplType(type));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * is the property readable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T beanObject, String propertyName) {
		return Arrays.contains(getReadPropertyNames(beanObject), propertyName);
	}

	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T beanObject, String propertyName) {
		return Arrays.contains(getWritePropertyNames(beanObject), propertyName);
	}
	
	/**
	 * @param propertyName property name
	 * @return NoSuchPropertyException
	 */
	protected RuntimeException noSuchPropertyException(String propertyName) {
		return new NoSuchPropertyException("Unknown property: " + propertyName + " [" + type + "].");
	}
	
	/**
	 * @param propertyName property name
	 * @return No getter method Exception
	 */
	protected RuntimeException noGetterMethodException(String propertyName) {
		return new IncorrectAccessException("No getter method for property: " 
				+ propertyName + " [" + type + "].");
	}
	
	/**
	 * @param propertyName property name
	 * @return No setter method Exception
	 */
	protected RuntimeException noSetterMethodException(String propertyName) {
		return new IncorrectAccessException("No setter method for property: " 
				+ propertyName + " [" + type + "].");
	}
	
	/**
	 * @return a string representation of the object.
	 */
	public String toString() {
		return getClass().getName() + ": " + type;
	}
	
}
