package panda.lang.comparator;

import java.util.Comparator;

import panda.lang.Numbers;


/**
 * string comparator for string
 */
public class NumberComparator implements Comparator<Number> {
	private final static NumberComparator i = new NumberComparator();
	
	public final static NumberComparator i() {
		return i;
	}
	
	private NumberComparator() {
	}

	/**
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Number o1, Number o2) {
		return Numbers.compare(o1, o2);
	}
}
