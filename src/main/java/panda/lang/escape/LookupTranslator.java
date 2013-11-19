package panda.lang.escape;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Translates a value using a lookup table.
 * 
 */
public class LookupTranslator extends CharSequenceTranslator {
	private final Map<String, String> lookupMap;
	private int shortest;
	private int longest;

	/**
	 * Define the lookup table to be used in translation Note that, as of Lang 3.1, the key to the
	 * lookup table is converted to a java.lang.String, while the value remains as a
	 * java.lang.CharSequence. This is because we need the key to support hashCode and
	 * equals(Object), allowing it to be the key for a HashMap. See LANG-882.
	 * 
	 * @param lookup CharSequence[][] table of size [*][2]
	 */
	public LookupTranslator(final Map<String, String> lookup) {
		lookupMap = lookup;
		init();
	}

	/**
	 * Define the lookup table to be used in translation Note that, as of Lang 3.1, the key to the
	 * lookup table is converted to a java.lang.String, while the value remains as a
	 * java.lang.CharSequence. This is because we need the key to support hashCode and
	 * equals(Object), allowing it to be the key for a HashMap. See LANG-882.
	 * 
	 * @param lookup CharSequence[][] table of size [*][2]
	 */
	public LookupTranslator(final String[] ... lookup) {
		lookupMap = new HashMap<String, String>();
		if (lookup != null) {
			for (final String[] seq : lookup) {
				this.lookupMap.put(seq[0].toString(), seq[1]);
			}
		}
		init();
	}

	private void init() {
		int _shortest = Integer.MAX_VALUE;
		int _longest = 0;
		for (Entry<String, String> en : lookupMap.entrySet()) {
			final int sz = en.getKey().length();
			if (sz < _shortest) {
				_shortest = sz;
			}
			if (sz > _longest) {
				_longest = sz;
			}
		}

		shortest = _shortest;
		longest = _longest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int translate(final CharSequence input, final int index, final Appendable out) throws IOException {
		int max = longest;
		if (index + longest > input.length()) {
			max = input.length() - index;
		}
		// descend so as to get a greedy algorithm
		for (int i = max; i >= shortest; i--) {
			final CharSequence subSeq = input.subSequence(index, index + i);
			final CharSequence result = lookupMap.get(subSeq.toString());
			if (result != null) {
				out.append(result);
				return i;
			}
		}
		return 0;
	}
}
