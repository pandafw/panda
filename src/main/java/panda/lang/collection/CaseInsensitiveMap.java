package panda.lang.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a <code>Map</code> that ignores the case of the
 * keys. if you do
 * <br>
 * <br>
 * <code>put("test", "1");</code>
 * <br>
 * <code>put("TEST", "2");</code>
 * <br>
 * <br>
 * the second <code>put</code> overwrites the value of the first one,
 * because the keys are considered to be equal. With
 * <br>
 * <br>
 * <code>get("TesT");</code>
 * <br>
 * <br>
 * you'll get the result <code>"2"</code>.
 * If you iterate through the keys (using either <code>keySet</code> or
 * <code>entrySet</code>), you'll get the first added version of the key,
 * in the above case, you'll get <code>"test"</code>.
 * It is allowed to use non-strings as keys. In this case the <code>Map</code>
 * behaves like a usual <code>HashMap</code>.<br>
 * Note: This class is similar to a <code>TreeMap(String.CASE_INSENSITIVE_ORDER)</code>
 *       except that non-strings do not throw a <code>ClassCastException</code>
 *       and that keys are not sorted.
 *       
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * 
 * @author yf.frank.wang@gmail.com
 */
public class CaseInsensitiveMap<K, V> implements Map<K, V> {
	private Map<K, V> map;
	
	public CaseInsensitiveMap(Map<K, V> map) {
		this.map = map;
	}

	private Object toCompareKey(Object key) {
		return key instanceof String ? ((String)key).toLowerCase() : key; 
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(toCompareKey(key));
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public V get(Object key) {
		return map.get(toCompareKey(key));
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		return map.put((K)toCompareKey(key), value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> en : m.entrySet()) {
			put(en.getKey(), en.getValue());
		}
	}

	public V remove(Object key) {
		return map.remove(toCompareKey(key));
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
