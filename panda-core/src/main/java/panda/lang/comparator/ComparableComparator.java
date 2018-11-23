package panda.lang.comparator;

import java.util.Comparator;

import panda.lang.Objects;

/**
 * comparator for comparable object
 * @param <T> comparable type
 */
public class ComparableComparator<T extends Comparable> implements Comparator<T> {
	private final static ComparableComparator i = new ComparableComparator();
	
	public static ComparableComparator i() {
		return i;
	}
	
	/**
	 * constructor
	 */
	public ComparableComparator() {
	}
	
	/**
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 *         equal to, or greater than the second.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Comparable o1, Comparable o2) {
		return Objects.compare(o1, o2);
	}
}
