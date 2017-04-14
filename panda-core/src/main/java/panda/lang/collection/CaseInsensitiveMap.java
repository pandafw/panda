package panda.lang.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
 */
public class CaseInsensitiveMap<K, V> implements Map<K, V>, Cloneable {
	private transient Map<K, K> keys;
	private transient Map<K, V> cmap;
	
	public CaseInsensitiveMap() {
		this.keys = new HashMap<K, K>();
		this.cmap = init();
	}
	
	public CaseInsensitiveMap(Map<K, V> map) {
		this();
		putAll(map);
	}

	protected Map<K, V> init() {
		return new HashMap<K, V>();
	}
	
	protected Object toCompareKey(Object key) {
		return key instanceof String ? ((String)key).toLowerCase() : key; 
	}

	@Override
	public void clear() {
		keys.clear();
		cmap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		Object ck = toCompareKey(key);
		if (keys.containsKey(ck)) {
			K mk = keys.get(ck);
			if (cmap.containsKey(mk)) {
				return true;
			}
			keys.remove(ck);
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return cmap.containsValue(value);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return cmap.entrySet();
	}

	@Override
	public V get(Object key) {
		Object ck = toCompareKey(key);
		if (keys.containsKey(ck)) {
			K mk = keys.get(ck);
			return cmap.get(mk);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return cmap.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return new Set<K>() {
			@Override
			public int size() {
				return cmap.keySet().size();
			}

			@Override
			public boolean isEmpty() {
				return cmap.keySet().isEmpty();
			}

			@Override
			public boolean contains(Object o) {
				return containsKey(o);
			}

			@Override
			public Iterator<K> iterator() {
				return cmap.keySet().iterator();
			}

			@Override
			public Object[] toArray() {
				return cmap.keySet().toArray();
			}

			@Override
			public <T> T[] toArray(T[] a) {
				return cmap.keySet().toArray(a);
			}

			@Override
			public boolean add(K e) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean remove(Object o) {
				Object ck = toCompareKey(o);
				if (keys.containsKey(ck)) {
					K mk = keys.remove(ck);
					cmap.remove(mk);
					return true;
				}
				return false;
			}

			@Override
			public boolean containsAll(Collection<?> c) {
				for (Object e : c) {
					if (!containsKey(e)) {
						return false;
					}
				}
				return true;
			}

			@Override
			public boolean addAll(Collection<? extends K> c) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				boolean modified = false;

				Set<Object> s = new CaseInsensitiveSet<Object>(c);

				Iterator<?> it = iterator();
				while (it.hasNext()) {
					if (!s.contains(it.next())) {
						it.remove();
						modified = true;
					}
				}
				return modified;
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				boolean modified = false;
				
				for (Object o : c) {
					if (remove(o)) {
						modified = true;
					}
				}
				return modified;
			}

			@Override
			public void clear() {
				keys.clear();
				cmap.clear();
			}

			@Override
			public int hashCode() {
				return cmap.keySet().hashCode();
			}

			@Override
			public boolean equals(Object o) {
				if (o == this)
					return true;

				if (!(o instanceof Set))
					return false;
				
				return cmap.keySet().equals(o);
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		Object ck = toCompareKey(key);
		if (keys.containsKey(ck)) {
			K mk = keys.get(ck);
			return cmap.put(mk, value);
		}

		keys.put((K)ck, key);
		return cmap.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> en : m.entrySet()) {
			put(en.getKey(), en.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		Object ck = toCompareKey(key);
		if (keys.containsKey(ck)) {
			K mk = keys.remove(ck);
			return cmap.remove(mk);
		}

		return null;
	}

	@Override
	public int size() {
		return cmap.size();
	}

	@Override
	public Collection<V> values() {
		return cmap.values();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Map)) {
			return false;
		}
		
		return cmap.equals(o);
	}

	@Override
	public int hashCode() {
		return cmap.hashCode();
	}

	@Override
	public String toString() {
		return cmap.toString();
	}

	@Override
	public CaseInsensitiveMap<K, V> clone() {
		CaseInsensitiveMap<K, V> m = new CaseInsensitiveMap<K, V>();
		m.putAll(this);
		return m;
	}
}
