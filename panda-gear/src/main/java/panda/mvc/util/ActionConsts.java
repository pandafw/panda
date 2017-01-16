package panda.mvc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;
import panda.lang.Objects;

@IocBean(scope=Scope.REQUEST)
public class ActionConsts extends ActionSupport {
	protected Map<String, Object> cache = new HashMap<String, Object>();
	
	/**
	 * getTextAsList
	 * @param name name
	 * @return map
	 */
	public List getTextAsList(String name) {
		return getTextAsList(name, Collections.EMPTY_LIST);
	}

	/**
	 * getTextAsList
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return list value
	 */
	public List getTextAsList(String key, List def) {
		Object v = cache.get(key);
		if (v == Objects.NULL) {
			return def;
		}
		if (v instanceof List) {
			return (List)v;
		}

		v = super.getTextAsList(key, def);
		if (v == null) {
			v = Objects.NULL;
		}
		cache.put(key, v);
		return v == Objects.NULL ? def : (List)v;
	}
	
	/**
	 * getTextAsMap
	 * @return map
	 */
	public Map getTextAsMap(String key) {
		return getTextAsMap(key, Collections.EMPTY_MAP);
	}
	
	/**
	 * getTextAsMap
	 * @return map
	 */
	public Map getTextAsMap(String key, Map def) {
		Object v = cache.get(key);
		if (v == Objects.NULL) {
			return def;
		}
		if (v instanceof Map) {
			return (Map)v;
		}

		v = super.getTextAsMap(key, def);
		if (v == null) {
			v = Objects.NULL;
		}
		cache.put(key, v);
		return v == Objects.NULL ? def : (Map)v;
	}
}
