package panda.dao.handler;

import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.DataHandler;
import panda.lang.Asserts;


/**
 * DataHandler for save data to map
 * @param <T> data type
 */
@SuppressWarnings("rawtypes")
public class MapDataHandler<T> implements DataHandler<T> {
	private Map map;
	private String key;
	private String val;
	private BeanHandler<T> bh;
	
	/**
	 * @param type bean type
	 * @param map the map to store data
	 * @param key the key property name
	 */
	public MapDataHandler(Class<T> type, Map map, String key) {
		this(type, map, key, null);
	}

	/**
	 * @param type bean type
	 * @param map the map to store data
	 * @param key the key property name
	 * @param val the val property name
	 */
	public MapDataHandler(Class<T> type, Map map, String key, String val) {
		this(Beans.i().getBeanHandler(type), map, key, val);
	}

	/**
	 * @param bh bean handler
	 * @param map the map to store data
	 * @param key the key property name
	 */
	public MapDataHandler(BeanHandler<T> bh, Map map, String key) {
		this(bh, map, key, null);
	}

	/**
	 * @param bh bean handler
	 * @param map the map to store data
	 * @param key the key property name
	 * @param val the val property name
	 */
	public MapDataHandler(BeanHandler<T> bh, Map map, String key, String val) {
		Asserts.notNull(bh, "The parameter bean handler is null");
		Asserts.notNull(map, "The parameter map is null");
		Asserts.notEmpty(key, "The parameter key is empty");

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
	public boolean handle(T data) {
		Object kv = bh.getPropertyValue(data, key);
		Object vv;
		
		if (val != null) {
			vv = bh.getPropertyValue(data, val);
		}
		else {
			vv = data;
		}
		map.put(kv, vv);

		return true;
	}
}
