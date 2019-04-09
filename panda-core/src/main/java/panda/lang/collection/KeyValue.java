package panda.lang.collection;

import java.util.Map;
import java.util.Map.Entry;

import panda.lang.Objects;

/**
 * @param <K> the Key Type
 * @param <V> the Value Type
 */
public class KeyValue<K, V> implements Entry<K, V> {

	/** The key */
	protected K key;
	
	/** The value */
	protected V value;

	/**
	 * Constructs a new pair with the specified key and given value.
	 * 
	 * @param key the key for the entry, may be null
	 * @param value the value for the entry, may be null
	 */
	public KeyValue(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * Gets the key from the pair.
	 * 
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * Gets the value from the pair.
	 * 
	 * @return the value
	 */
	public V getValue() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	public V setValue(V value) {
		V old = value;
		this.value = value;
		return old;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Map.Entry)) {
			return false;
		}

		Map.Entry<?, ?> rhs = (Map.Entry<?, ?>)obj;
		return Objects.equalsBuilder().append(key, rhs.getKey()).append(value, rhs.getValue()).isEquals();
	}

	/**
	 * Gets a debugging String view of the pair.
	 * 
	 * @return a String view of the entry
	 */
	@Override
	public String toString() {
		return key + "=" + value;
	}
}
