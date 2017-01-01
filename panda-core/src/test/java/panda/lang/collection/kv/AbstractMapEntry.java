package panda.lang.collection.kv;

import java.util.Map;

public abstract class AbstractMapEntry<K, V> extends AbstractKeyValue<K, V> implements Map.Entry<K, V> {

    /**
     * Constructs a new entry with the given key and given value.
     *
     * @param key  the key for the entry, may be null
     * @param value  the value for the entry, may be null
     */
    protected AbstractMapEntry(final K key, final V value) {
        super(key, value);
    }

    // Map.Entry interface
    //-------------------------------------------------------------------------
    /**
     * Sets the value stored in this <code>Map.Entry</code>.
     * <p>
     * This <code>Map.Entry</code> is not connected to a Map, so only the
     * local data is changed.
     *
     * @param value  the new value
     * @return the previous value
     */
    @Override
    public V setValue(final V value) {
        return super.setValue(value);
    }

    /**
     * Compares this <code>Map.Entry</code> with another <code>Map.Entry</code>.
     * <p>
     * Implemented per API documentation of {@link java.util.Map.Entry#equals(Object)}
     *
     * @param obj  the object to compare to
     * @return true if equal key and value
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry == false) {
            return false;
        }
        final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
        return
            (getKey() == null ? other.getKey() == null : getKey().equals(other.getKey())) &&
            (getValue() == null ? other.getValue() == null : getValue().equals(other.getValue()));
    }

    /**
     * Gets a hashCode compatible with the equals method.
     * <p>
     * Implemented per API documentation of {@link java.util.Map.Entry#hashCode()}
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode()) ^
               (getValue() == null ? 0 : getValue().hashCode());
    }

}
