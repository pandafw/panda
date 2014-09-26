package panda.wing.mvc;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import panda.lang.Collections;
import panda.lang.Objects;
import panda.mvc.util.ActionSupport;

/**
 */
public class ActionConsts extends ActionSupport {
	protected Map<String, Object> cache = new HashMap<String, Object>();
	
	/**
	 * Constructor
	 */
	public ActionConsts() {
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

		v = getTextAsMap(key, def);
		if (v == null) {
			v = Objects.NULL;
		}
		cache.put(key, v);
		return v == Objects.NULL ? def : (Map)v;
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> getCharsets() {
		final String name = "charsets";
		
		Object v = cache.get(name);
		if (v instanceof Set) {
			return (Set<String>)v;
		}

		SortedMap<String,Charset> map = Charset.availableCharsets();
		v = map.keySet();
		cache.put(name, v);
		return (Set<String>)v;
	}

	/**
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getBooleanMap() {
		return getTextAsMap("const-booleans");
	}

	/**
	 * @return list
	 */
	public List getBloodTypeList() {
		return getTextAsList("const-bloodTypes");
	}
	
	/**
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getGenderMap() {
		return getTextAsMap("const-genders");
	}
}
