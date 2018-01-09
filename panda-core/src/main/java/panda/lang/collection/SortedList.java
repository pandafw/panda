package panda.lang.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * A simple auto sorted list
 * <p>
 * This class implements a sorted list. It is constructed with a comparator that can compare two
 * objects and sort objects accordingly. When you add an object to the list, it is inserted in the
 * correct place. Object that are equal according to the comparator, will be in the list in the
 * order that they were added to this list. Add only objects that the comparator can compare.
 * </p>
 * @param <E> element type
 */
public class SortedList<E> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;

	private final Comparator<E> comparator;

	/**
	 * <p>
	 * Constructs a new sorted list. The objects in the list will be sorted according to the
	 * specified comparator.
	 * </p>
	 *
	 * @param comparator a comparator
	 */
	public SortedList(Comparator<E> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @param comparator a comparator
	 * @param initialCapacity the initial capacity of the list
	 * @throws IllegalArgumentException if the specified initial capacity is negative
	 */
	public SortedList(int initialCapacity, Comparator<E> comparator) {
		super(initialCapacity);
		this.comparator = comparator;
	}

	/**
	 * <p>
	 * This method call {@link #add(Object)}
	 * </p>
	 */
	@Override
	public void add(int index, E element) {
		add(element);
	}

	/**
	 * <p>
	 * Adds an object to the list. The object will be inserted in the correct place so that the
	 * objects in the list are sorted. When the list already contains objects that are equal
	 * according to the comparator, the new object will be inserted immediately after these other
	 * objects.
	 * </p>
	 *
	 * @param o the object to be added
	 */
	@Override
	public boolean add(E o) {
		int i = 0;
		for (; i < size(); i++) {
			if (comparator.compare(o, get(i)) < 0) {
				break;
			}
		}
		super.add(i, o);
		return true;
	}

	/**
	 * Appends all of the elements in the specified collection to the end of this list, in the order
	 * that they are returned by the specified collection's Iterator. The behavior of this operation
	 * is undefined if the specified collection is modified while the operation is in progress.
	 * (This implies that the behavior of this call is undefined if the specified collection is this
	 * list, and this list is nonempty.)
	 *
	 * @param c collection containing elements to be added to this list
	 * @return <tt>true</tt> if this list changed as a result of the call
	 * @throws NullPointerException if the specified collection is null
	 */
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			add(e);
		}
		return true;
	}

	/**
	 * Inserts all of the elements in the specified collection into this list, starting at the
	 * specified position. Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (increases their indices). The new elements will appear in the list in
	 * the order that they are returned by the specified collection's iterator.
	 *
	 * @param index index at which to insert the first element from the specified collection
	 * @param c collection containing elements to be added to this list
	 * @return <tt>true</tt> if this list changed as a result of the call
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws NullPointerException if the specified collection is null
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		return addAll(c);
	}
}
