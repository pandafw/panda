package panda.bean.handlers;

import java.lang.reflect.Type;

import panda.bean.Beans;
import panda.lang.reflect.Types;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public abstract class AbstractJavaBeanHandler<T> extends AbstractBeanHandler<T> {
	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public AbstractJavaBeanHandler(Beans beans, Type type) {
		super(beans, type);
	}

	/**
	 * create bean object
	 * @return bean instance 
	 */
	public T createObject() {
		return Types.born(Types.getDefaultImplType(type));
	}

//	/**
//	 * @param propertyName property name
//	 * @return NoSuchPropertyException
//	 */
//	protected RuntimeException noSuchPropertyException(String propertyName) {
//		return new NoSuchPropertyException("Unknown property: " + propertyName + " [" + type + "].");
//	}
//	
//	/**
//	 * @param propertyName property name
//	 * @return No getter method Exception
//	 */
//	protected RuntimeException noGetterMethodException(String propertyName) {
//		return new IncorrectAccessException("No getter method for property: " 
//				+ propertyName + " [" + type + "].");
//	}
//	
//	/**
//	 * @param propertyName property name
//	 * @return No setter method Exception
//	 */
//	protected RuntimeException noSetterMethodException(String propertyName) {
//		return new IncorrectAccessException("No setter method for property: " 
//				+ propertyName + " [" + type + "].");
//	}
	
	/**
	 * @return a string representation of the object.
	 */
	public String toString() {
		return getClass().getName() + ": " + type;
	}
	
}
