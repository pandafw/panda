package panda.dao.handlers;

import java.util.List;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.DataHandler;
import panda.lang.Asserts;


/**
 * get the property value from the data, and add it to the list 
 * @param <T> data type
 * @author yf.frank.wang@gmail.com
 */
public class ListDataHandler<T> implements DataHandler<T> {
	private List list;
	private String prop;
	private BeanHandler<T> bh;
	
	/**
	 * @param type bean type
	 * @param list the list to store the property value
	 * @param prop property name
	 */
	public ListDataHandler(Class<T> type, List list, String prop) {
		this(Beans.i().getBeanHandler(type), list, prop);
	}

	/**
	 * @param bh BeanHandler
	 * @param list the list to store the property value
	 * @param prop property name
	 */
	public ListDataHandler(BeanHandler<T> bh, List list, String prop) {
		Asserts.notNull(prop, "The parameter bean handler is null");
		Asserts.notNull(list, "The parameter list is null");
		Asserts.notEmpty(prop, "The parameter prop is empty");

		this.list = list;
		this.prop = prop;
		this.bh = bh;
	}

	/**
	 * handle data
	 * @param data data
	 * @return false to stop the process
	 */
	@SuppressWarnings("unchecked")
	public boolean handle(T data) throws Exception {
		Object v = bh.getPropertyValue(data, prop);
		list.add(v);

		return true;
	}
}
