package panda.lang.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * 
 */
public class ExpireMap<K, V> implements Map<K, V> {
	private Map<K, Long> kts;
	private Map<K, V> map;
	private long expire;

	/**
	 * @param map a map
	 * @param expire expire time (millisecond)
	 */
	public ExpireMap(Map<K, V> map, long expire) {
		this.map = map;
		this.expire = expire;
		this.kts = new HashMap<K, Long>();
	}

	/**
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return (int)(expire / 1000L);
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		this.expire = maxAge * 1000L;
	}

	public void clear() {
		kts.clear();
		map.clear();
	}

	public boolean containsKey(Object key) {
		if (map.containsKey(key)) {
			return true;
		}
		
		kts.remove(key);
		return false;
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public V get(Object key) {
		Long t = kts.get(key);
		if (t != null) {
			if (System.currentTimeMillis() - t < expire) {
				return map.get(key);
			}
			
			kts.remove(key);
			map.remove(key);
		}
		return null;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public V put(K key, V value) {
		kts.put(key, System.currentTimeMillis());
		return map.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> en : m.entrySet()) {
			put(en.getKey(), en.getValue());
		}
	}

	public V remove(Object key) {
		kts.remove(key);
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		return map.values();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public String toString() {
		return map.toString();
	}
}
