package panda.lang.collection;

import java.util.Map;

/**
 * @param <K> the key type
 * @param <V> the value type
 */
public class MapEntry<K, V> extends KeyValue<K, V> {
	protected Map<K, V> map;
	
	/**
	 * Constructs a new pair with the specified key and given value.
	 * 
	 * @param map the map
	 * @param key the key for the entry, may be null
	 * @param value the value for the entry, may be null
	 */
	public MapEntry(Map<K, V> map, K key, V value) {
		super(key, value);
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public V setValue(V value) {
		this.value = value;
		return map.put(key, value);
	}
}
