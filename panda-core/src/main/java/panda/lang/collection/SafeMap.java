package panda.lang.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class SafeMap<K, V> implements Map<K, V> {
	private final static Log log = Logs.getLog(SafeMap.class);

	private Map<K, V> map;

	public SafeMap(Map<K, V> map) {
		this.map = map;
	}

	public void clear() {
		try {
			map.clear();
		}
		catch (Throwable ex) {
			log.warn("clear", ex);
		}
	}

	public boolean containsKey(Object key) {
		try {
			return map.containsKey(key);
		}
		catch (Throwable ex) {
			log.warn("containsKey", ex);
			return false;
		}
	}

	public boolean containsValue(Object value) {
		try {
			return map.containsValue(value);
		}
		catch (Throwable ex) {
			log.warn("containsValue", ex);
			return false;
		}
	}

	public Set<Entry<K, V>> entrySet() {
		try {
			return map.entrySet();
		}
		catch (Throwable ex) {
			log.warn("entrySet", ex);
			return new HashSet<Entry<K, V>>();
		}
	}

	public V get(Object key) {
		try {
			return map.get(key);
		}
		catch (Throwable ex) {
			log.warn("get", ex);
			return null;
		}
	}

	public boolean isEmpty() {
		try {
			return map.isEmpty();
		}
		catch (Throwable ex) {
			log.warn("isEmpty", ex);
			return false;
		}
	}

	public Set<K> keySet() {
		try {
			return map.keySet();
		}
		catch (Throwable ex) {
			log.warn("keySet", ex);
			return new HashSet<K>();
		}
	}

	public V put(K key, V value) {
		try {
			return map.put(key, value);
		}
		catch (Throwable ex) {
			log.warn("put", ex);
			return null;
		}
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		try {
			map.putAll(m);
		}
		catch (Throwable ex) {
			log.warn("putAll", ex);
		}
	}

	public V remove(Object key) {
		try {
			return map.remove(key);
		}
		catch (Throwable ex) {
			log.warn("remove", ex);
			return null;
		}
	}

	public int size() {
		try {
			return map.size();
		}
		catch (Throwable ex) {
			log.warn("size", ex);
			return 0;
		}
	}

	public Collection<V> values() {
		try {
			return map.values();
		}
		catch (Throwable ex) {
			log.warn("values", ex);
			return new ArrayList<V>();
		}
	}

	public boolean equals(Object o) {
		try {
			return map.equals(o);
		}
		catch (Throwable ex) {
			log.warn("equals", ex);
			return false;
		}
	}

	public int hashCode() {
		try {
			return map.hashCode();
		}
		catch (Throwable ex) {
			log.warn("hashCode", ex);
			return 0;
		}
	}

	public String toString() {
		try {
			return map.toString();
		}
		catch (Throwable ex) {
			log.warn("toString", ex);
			return Strings.EMPTY;
		}
	}
}
