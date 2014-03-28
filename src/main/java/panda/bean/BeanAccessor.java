package panda.bean;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class BeanAccessor {
	private Object bean;

	private BeanHandler beanHandler;

	/**
	 * Constructor
	 * 
	 * @param bean bean object
	 */
	public BeanAccessor(Object bean) {
		this.bean = bean;
		this.beanHandler = Beans.i().getBeanHandler(
			bean == null ? null : bean.getClass());
	}

	/**
	 * get property value
	 * 
	 * @param propertyName property name
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	public Object get(String propertyName) {
		return beanHandler.getBeanValue(bean, propertyName);
	}

	/**
	 * set property value
	 * 
	 * @param propertyName property name
	 * @param value value
	 */
	@SuppressWarnings("unchecked")
	public void set(String propertyName, Object value) {
		beanHandler.setBeanValue(bean, propertyName, value);
	}
}
