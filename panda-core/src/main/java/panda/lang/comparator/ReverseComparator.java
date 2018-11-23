package panda.lang.comparator;

import java.util.Comparator;

import panda.lang.Asserts;

/**
 * reverse order comparator
 * @param <T> the type of objects that may be compared by this comparator
 */
public class ReverseComparator<T> implements Comparator<T> {
	private Comparator<T> comparator;
	
	/**
	 * constructor
	 * @param comparator comparator
	 */
	public ReverseComparator(Comparator<T> comparator) {
		Asserts.notNull(comparator);
		this.comparator = comparator;
	}
	
	/**
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T o1, T o2) {
		return - comparator.compare(o1, o2);
	}
}
