package panda.bean;

import panda.lang.Injector;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class BeanInjector implements Injector {
	private BeanHandler handler;
	private String field;


	/**
	 * @param handler
	 * @param field
	 */
	public BeanInjector(BeanHandler handler, String field) {
		this.handler = handler;
		this.field = field;
	}


	@Override
	@SuppressWarnings("unchecked")
	public void inject(Object obj, Object value) {
		// TODO Auto-generated method stub
		handler.setPropertyValue(obj, field, value);
	}
}
