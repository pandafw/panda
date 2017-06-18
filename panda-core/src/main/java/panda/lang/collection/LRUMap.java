package panda.lang.collection;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple LRU cache that implements the <code>Map</code> interface. Instances
 * are not thread-safe and should be synchronized externally, for instance by
 * using {@link java.util.Collections#synchronizedMap}.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class LRUMap<K,V> extends LinkedHashMap<K,V> implements Serializable {
	private static final long serialVersionUID = 1L;

	private final int maxEntries;

	public LRUMap(int maxEntries) {
		super( maxEntries, .75f, true );
		this.maxEntries = maxEntries;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return ( size() > maxEntries );
	}
}