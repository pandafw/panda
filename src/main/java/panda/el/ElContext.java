package panda.el;

import panda.bean.BeanHandler;
import panda.bean.Beans;

public class ElContext {
	private boolean strict;
	private Object context;
	private BeanHandler handler;

	/**
	 * Constructor
	 */
	public ElContext() {
		this(null);
	}

	/**
	 * @param context
	 */
	public ElContext(Object context) {
		this.context = context;
		this.handler = context == null ? null : Beans.i().getBeanHandler(context.getClass());
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
		if (handler == null) {
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
		if (handler != null) {
			handler.setBeanValue(context, name, value);
		}
	}
	
	/**
	 * @return context object
	 */
	public Object context() {
		return context;
	}
	
	/**
	 * @return the strict
	 */
	public boolean isStrict() {
		return strict;
	}

	/**
	 * @param strict the strict to set
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
}
