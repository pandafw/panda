package panda.dao.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.DataHandler;
import panda.lang.Asserts;


/**
 * DataHandler
 * @param <T> data type
 * @author yf.frank.wang@gmail.com
 */
public class GroupDataHandler<T> implements DataHandler<T> {
	private Map map;
	private String key;
	private String val;
	private BeanHandler<T> bh;
	
	/**
	 * @param type bean type
	 * @param map
	 * @param key
	 */
	public GroupDataHandler(Class<T> type, Map map, String key) {
		this(type, map, key, null);
	}

	/**
	 * @param type bean type
	 * @param map
	 * @param key
	 * @param val
	 */
	public GroupDataHandler(Class<T> type, Map map, String key, String val) {
		this(map, key, val, Beans.i().getBeanHandler(type));
	}

	/**
	 * @param map
	 * @param key
	 * @param bh
	 */
	public GroupDataHandler(Map map, String key, BeanHandler<T> bh) {
		this(map, key, null, bh);
	}

	/**
	 * @param map
	 * @param key
	 * @param val
	 * @param bh
	 */
	public GroupDataHandler(Map map, String key, String val, BeanHandler<T> bh) {
		Asserts.notNull(map, "The parameter map is null");
		Asserts.notEmpty(key, "The parameter key is empty");
		Asserts.notNull(key, "The parameter bean handler is null");

		this.map = map;
		this.key = key;
		this.val = val;
		this.bh = bh;
	}

	/**
	 * handle data
	 * @param data data
	 * @return false to stop the process
	 */
	@SuppressWarnings("unchecked")
	public boolean handle(T data) throws Exception {
		Object kv = bh.getPropertyValue(data, key);
		Object vv;
		
		if (val != null) {
			vv = bh.getPropertyValue(data, val);
		}
		else {
			vv = data;
		}
		
		List vs = (List)map.get(kv);
		if (vs == null) {
			vs = new ArrayList();
			map.put(kv, vs);
		}
		vs.add(vv);

		return true;
	}
}
