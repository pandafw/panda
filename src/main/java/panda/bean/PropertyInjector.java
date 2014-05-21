package panda.bean;

import panda.lang.Injector;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class PropertyInjector implements Injector {
	private String field;
	private PropertyHandler handler;


	/**
	 * @param handler
	 * @param field
	 */
	public PropertyInjector(String field, PropertyHandler handler) {
		this.field = field;
		this.handler = handler;
	}


	@Override
	@SuppressWarnings("unchecked")
	public void inject(Object obj, Object value) {
		handler.setPropertyValue(obj, field, value);
	}
}
