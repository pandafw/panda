package panda.dao.handler;

import java.util.Collection;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.DataHandler;
import panda.lang.Asserts;


/**
 * get the property value from the data, and add it to the collection 
 * @param <T> data type
 * @author yf.frank.wang@gmail.com
 */
public class CollectionDataHandler<T> implements DataHandler<T> {
	private BeanHandler<T> bh;
	private Collection coll;
	private String prop;
	
	/**
	 * @param type bean type
	 * @param coll the list to store the property value
	 * @param prop property name
	 */
	public CollectionDataHandler(Class<T> type, Collection<?> coll, String prop) {
		this(Beans.i().getBeanHandler(type), coll, prop);
	}

	/**
	 * @param bh BeanHandler
	 * @param coll the collection to store the property value
	 * @param prop property name
	 */
	public CollectionDataHandler(BeanHandler<T> bh, Collection<?> coll, String prop) {
		Asserts.notNull(bh, "The parameter bean handler is null");
		Asserts.notNull(coll, "The parameter coll is null");
		Asserts.notEmpty(prop, "The parameter prop is empty");

		this.coll = coll;
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
		coll.add(v);

		return true;
	}
}
