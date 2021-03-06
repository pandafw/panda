package panda.bean.handler;

import java.lang.reflect.Type;

import panda.bean.BeanHandler;

/**
 * 
 * @param <T> class type
 */
public abstract class AbstractSimpleBeanHandler<T> implements BeanHandler<T> {
	protected Type type;

	/**
	 * Constructor
	 * @param type bean type
	 */
	public AbstractSimpleBeanHandler(Type type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * get bean type
	 * @param beanName bean name
	 * @return bean type
	 */
	public Type getBeanType(String beanName) {
		return getPropertyType(beanName);
	}

	/**
	 * get bean type
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return bean type
	 */
	public Type getBeanType(T beanObject, String beanName) {
		return getPropertyType(beanObject, beanName);
	}

	/**
	 * is the bean readable
	 * @param beanName bean name
	 * @return true if bean is readable
	 */
	public boolean canReadBean(String beanName) {
		return canReadProperty(beanName);
	}

	/**
	 * is the bean readable
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return true if bean is readable
	 */
	public boolean canReadBean(T beanObject, String beanName) {
		return canReadProperty(beanObject, beanName);
	}
	
	/**
	 * is the property writable
	 * @param beanName bean name
	 * @return true if bean is writable
	 */
	public boolean canWriteBean(String beanName) {
		return canWriteProperty(beanName);
	}
	
	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return true if bean is writable
	 */
	public boolean canWriteBean(T beanObject, String beanName) {
		return canWriteProperty(beanObject, beanName);
	}
	
	/**
	 * get property value 
	 * @param beanObject bean object
	 * @param beanName bean name
	 * @return bean value
	 */
	public Object getBeanValue(T beanObject, String beanName) {
		return getPropertyValue(beanObject, beanName);
	}
	
	/**
	 * set bean value 
	 * @param beanObject bean object
	 * @param beanName bean name
	 * @param value value
	 * @return true if set value successfully
	 */
	public boolean setBeanValue(T beanObject, String beanName, Object value) {
		return setPropertyValue(beanObject, beanName, value);
	}
}
