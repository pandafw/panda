package panda.lang.collection;

import panda.lang.Arrays;

/**
 */
public class MultiKey {
	private final Object[] keys;
	private int hashCode;

	/**
	 * Constructs an instance of <code>MultipartKey</code> to hold the specified objects.
	 * 
	 * @param keys the set of objects that make up the key. Each key may be null.
	 */
	public MultiKey(final Object... keys) {
		this.keys = keys;
		for (final Object key : keys) {
			if (key != null) {
				hashCode = hashCode * 7 + key.hashCode();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		// Eliminate the usual boilerplate because
		// this inner static class is only used in a generic ConcurrentHashMap
		// which will not compare against other Object types
		return Arrays.equals(keys, ((MultiKey)obj).keys);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}
}
