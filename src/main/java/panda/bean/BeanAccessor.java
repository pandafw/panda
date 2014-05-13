package panda.bean;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class BeanAccessor {
	private Object bean;
	private BeanHandler handler;

	/**
	 * Constructor
	 * 
	 * @param bean bean object
	 */
	public BeanAccessor(Object bean) {
		this(bean, Beans.i().getBeanHandler(bean == null ? null : bean.getClass()));
	}

	/**
	 * @param bean
	 * @param handler
	 */
	public BeanAccessor(Object bean, BeanHandler handler) {
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
		return handler.getBeanValue(bean, name);
	}

	/**
	 * set property value
	 * 
	 * @param name property name
	 * @param value value
	 */
	@SuppressWarnings("unchecked")
	public void set(String name, Object value) {
		handler.setBeanValue(bean, name, value);
	}
}
