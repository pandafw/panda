package panda.lang.comparator;

import java.util.Comparator;


/**
 * string comparator for string
 */
public class IgnoreCaesStringComparator implements Comparator<String> {
	private final static IgnoreCaesStringComparator i = new IgnoreCaesStringComparator();
	
	public final static IgnoreCaesStringComparator i() {
		return i;
	}
	
	private IgnoreCaesStringComparator() {
	}

	/**
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(String o1, String o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		if (o1 == o2) {
			return 0;
		}
		return o1.compareToIgnoreCase(o2);
	}
}
