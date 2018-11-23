package panda.lang.comparator;

import java.util.Comparator;


/**
 * string comparator for string case insensitive
 */
public class IgnoreCaseStringComparator implements Comparator<String> {
	private final static IgnoreCaseStringComparator i = new IgnoreCaseStringComparator();
	
	public final static IgnoreCaseStringComparator i() {
		return i;
	}
	
	private IgnoreCaseStringComparator() {
	}

	/**
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 */
	@Override
	public int compare(String o1, String o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return o1.compareToIgnoreCase(o2);
	}
}
