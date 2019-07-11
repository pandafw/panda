package panda.mvc.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.mvc.ActionContext;

@IocBean(scope=Scope.REQUEST)
public class ActionConsts implements Map<String, Object> {
	@IocInject
	protected ActionContext context;

	protected Map<String, Object> cache = new HashMap<String, Object>();


	public List getList(String key) {
		return (List)get(key);
	}

	public Map getMap(String key) {
		return (Map)get(key);
	}
	
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

		v = context.getText().getTextAsList(key, def);
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

		v = context.getText().getTextAsMap(key, def);
		if (v == null) {
			v = Objects.NULL;
		}
		cache.put(key, v);
		return v == Objects.NULL ? def : (Map)v;
	}
	
	@Override
	public int size() {
		return cache.size();
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return cache.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		Object v = cache.get(key);
		if (v == null && key instanceof String) {
			String k = "const-" + Texts.uncamelWord((String)key, '-');
			if (Strings.endsWith(k, "-map")) {
				v = context.getText().getTextAsMap(k, Collections.EMPTY_MAP);
			}
			else if (Strings.endsWith(k, "-list")) {
				v = context.getText().getTextAsList(k, Collections.EMPTY_LIST);
			}
			else {
				v = context.getText().getText(k);
			}
			if (v == null) {
				v = Objects.NULL;
			}
			cache.put((String)key, v);
		}
		return v == Objects.NULL ? null : v;
	}

	@Override
	public Object put(String key, Object value) {
		return cache.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return cache.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		cache.putAll(m);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public Set<String> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<Object> values() {
		return cache.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return cache.entrySet();
	}
}
