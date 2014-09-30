package panda.el;

import panda.bean.BeanHandler;
import panda.bean.Beans;

public class ElContext {
	private Object context;
	private BeanHandler handler;

	/**
	 * Constructor
	 * 
	 * @param context context object
	 */
	public ElContext(Object context) {
		this(context, Beans.i().getBeanHandler(context == null ? null : context.getClass()));
	}

	/**
	 * @param context
	 * @param handler
	 */
	public ElContext(Object context, BeanHandler handler) {
		this.context = context;
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
		if (name == null) {
			return null;
		}
		return handler.getBeanValue(context, name);
	}

	/**
	 * set property value
	 * 
	 * @param name property name
	 * @param value value
	 */
	@SuppressWarnings("unchecked")
	public void set(String name, Object value) {
		handler.setBeanValue(context, name, value);
	}
	
	/**
	 * @return context object
	 */
	public Object context() {
		return context;
	}
}
