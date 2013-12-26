package panda.dao.handlers;

import java.util.List;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.DataHandler;
import panda.lang.Asserts;


/**
 * @param <T> data type
 * @author yf.frank.wang@gmail.com
 */
public class ListDataHandler<T> implements DataHandler<T> {
	private List list;
	private String prop;
	private BeanHandler<T> bh;
	
	/**
	 * @param type bean type
	 * @param list
	 * @param prop
	 */
	public ListDataHandler(Class<T> type, List list, String prop) {
		this(list, prop, Beans.i().getBeanHandler(type));
	}

	/**
	 * @param list
	 * @param prop
	 * @param bh
	 */
	public ListDataHandler(List list, String prop, BeanHandler<T> bh) {
		Asserts.notNull(list, "The parameter list is null");
		Asserts.notEmpty(prop, "The parameter prop is empty");
		Asserts.notNull(prop, "The parameter bean handler is null");

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
