package panda.bean;


import java.lang.reflect.Type;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> class type
 */
public interface BeanHandler<T> extends PropertyHandler<T> {
	/**
	 * create bean object
	 * @return bean instance 
	 */
	T createObject();

	/**
	 * get bean type
	 * @param beanName bean name
	 * @return bean type
	 */
	Type getBeanType(String beanName);

	/**
	 * get bean type
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return bean type
	 */
	Type getBeanType(T beanObject, String beanName);

	/**
	 * is the bean readable
	 * @param beanName bean name
	 * @return true if bean is readable
	 */
	boolean canReadBean(String beanName);

	/**
	 * is the bean readable
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return true if bean is readable
	 */
	boolean canReadBean(T beanObject, String beanName);
	
	/**
	 * is the bean writable
	 * @param beanName bean name
	 * @return true if bean is writable
	 */
	boolean canWriteBean(String beanName);
	
	/**
	 * is the bean writable
	 * @param beanObject bean object (can be null)
	 * @param beanName bean name
	 * @return true if bean is writable
	 */
	boolean canWriteBean(T beanObject, String beanName);
	
	/**
	 * get bean value 
	 * @param beanObject bean object
	 * @param beanName bean name
	 * @return bean value
	 */
	Object getBeanValue(T beanObject, String beanName);
	
	/**
	 * set bean value 
	 * @param beanObject bean object
	 * @param beanName bean name
	 * @param value value
	 */
	void setBeanValue(T beanObject, String beanName, Object value);

}
