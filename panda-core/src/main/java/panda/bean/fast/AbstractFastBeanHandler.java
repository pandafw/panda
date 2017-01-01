package panda.bean.fast;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import panda.bean.Beans;
import panda.bean.handler.AbstractJavaBeanHandler;


/**
 * @param <T> class type
 */
public abstract class AbstractFastBeanHandler<T> extends AbstractJavaBeanHandler<T> {
	public static class PropertyInfo {
		public int index;
		public boolean readable;
		public boolean writable;
	}
	
	protected String[] rpns;
	protected String[] wpns;
	protected Map<String, PropertyInfo> mm = new HashMap<String, PropertyInfo>();

	/**
	 * Constructor
	 * @param beans bean handler factory
	 * @param type bean type
	 */
	public AbstractFastBeanHandler(Beans beans, Type type) {
		super(beans, type);
		init();
	}

	protected abstract void init();
	
	/**
	 * is the property readable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property type
	 */
	public boolean canReadProperty(T beanObject, String propertyName) {
		PropertyInfo pi = mm.get(propertyName);
		return pi == null ? false : pi.readable;
	}

	/**
	 * is the property writable
	 * @param beanObject bean object (can be null)
	 * @param propertyName property name
	 * @return property writable
	 */
	public boolean canWriteProperty(T beanObject, String propertyName) {
		PropertyInfo pi = mm.get(propertyName);
		return pi == null ? false : pi.writable;
	}
	
	/**
	 * get read property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	public String[] getReadPropertyNames(T beanObject) {
		return rpns;
	}

	/**
	 * get write property names
	 * @param beanObject bean object (can be null)
	 * @return property names
	 */
	public String[] getWritePropertyNames(T beanObject) {
		return wpns;
	}
}
