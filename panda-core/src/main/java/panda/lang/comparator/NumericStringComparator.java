package panda.lang.comparator;

import java.util.Comparator;

import panda.lang.Numbers;

/**
 * number comparator for string
 */
public class NumericStringComparator implements Comparator<String> {
	private final static NumericStringComparator i = new NumericStringComparator();
	
	public static NumericStringComparator i() {
		return i;
	}
	
	
	/**
	 * constructor
	 */
	public NumericStringComparator() {
	}

	/**
	 * @param o1 string1
	 * @param o2 string2
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 */
	@Override
	public int compare(String o1, String o2) {
		Number n1 = Numbers.toNumber(o1);
		Number n2 = Numbers.toNumber(o2);

		return Numbers.compare(n1, n2);
	}
}
