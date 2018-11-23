package panda.lang.comparator;

import java.util.Comparator;


/**
 * length comparator for string
 */
public class StringLengthComparator implements Comparator<CharSequence> {
	private final static StringLengthComparator i = new StringLengthComparator();
	
	public static StringLengthComparator i() {
		return i;
	}
	
	private StringLengthComparator() {
	}
	
	/**
	 * @param o1 string1
	 * @param o2 string2
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 */
	@Override
	public int compare(CharSequence o1, CharSequence o2) {
		int len1 = o1 == null ? 0 : o1.length();
		int len2 = o2 == null ? 0 : o2.length();
		return len1 - len2;
	}
}
