package panda.bean;

/**
 * 
 */
public class BeanAccessor {
	private Object bean;
	private BeanHandler handler;

	/**
	 * Constructor
	 * 
	 * @param handler bean handler
	 * @param bean bean object
	 */
	public BeanAccessor(BeanHandler handler, Object bean) {
		this.bean = bean;
		this.handler = handler;
	}

	/**
	 * get property value
	 * 
	 * @param name property name
	 * @return value
	 */
	@SuppressWarnings("unchecked")
	public Object get(String name) {
		return handler == null ? null : handler.getBeanValue(bean, name);
	}

	/**
	 * set property value
	 * 
	 * @param name property name
	 * @param value value
	 */
	@SuppressWarnings("unchecked")
	public void set(String name, Object value) {
		if (handler != null) {
			handler.setBeanValue(bean, name, value);
		}
	}
}
