package panda.el;

import panda.bean.BeanHandler;
import panda.bean.Beans;

public class ELContext {
	public static boolean defaultStrict;
	
	public static final ELContext DEFAULT = new ELContext();
	public static final ELContext STRICT = new ELContext(true);
	
	private boolean strict;
	private Object context;

	@SuppressWarnings("rawtypes")
	private BeanHandler handler;

	/**
	 * @param strict the default strict to set
	 */
	public static void setDefaultStrictMode(boolean strict) {
		defaultStrict = strict;
	}
	
	/**
	 * Constructor
	 */
	public ELContext() {
		this(null);
	}

	/**
	 * @param context context object
	 */
	public ELContext(Object context) {
		this(context, defaultStrict);
	}

	/**
	 * @param strict strict mode
	 */
	public ELContext(boolean strict) {
		this(null, strict);
	}

	/**
	 * @param context context object
	 * @param strict set this to true if you want NullPointException should be raised
	 */
	public ELContext(Object context, boolean strict) {
		this.context = context;
		this.strict = strict;
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
	public boolean strict() {
		return strict;
	}
	
	public String toString() {
		return (strict ? "strict: " : "") + (context == null ? "null" : context.getClass());
	}
}
