package panda.algorithm;

import java.util.Comparator;
import java.util.List;

import panda.lang.Arrays;
import panda.lang.Collections;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class Sorter {
	protected Comparator comparator;
	protected boolean ascend;
	
	public Sorter() {
		this(null, true);
	}
	
	public Sorter(boolean ascend) {
		this(null, ascend);
	}
	
	public Sorter(Comparator comparator) {
		this(comparator, true);
	}
	
	public Sorter(Comparator comparator, boolean ascend) {
		this.comparator = comparator;
		this.ascend = ascend;
	}
	
	/**
	 * @return the ascend
	 */
	public boolean isAscend() {
		return ascend;
	}

	/**
	 * @param ascend the ascend to set
	 */
	public void setAscend(boolean ascend) {
		this.ascend = ascend;
	}

	/**
	 * Sorts the list
	 * 
	 * @param list the list to be sorted
	 * @param from the index of the first element (inclusive) to be sorted
	 * @param to the index of the last element (exclusive) to be sorted
	 */
	public abstract void sort(List<?> list, int from, int to);

	/**
	 * Sorts the array
	 * 
	 * @param array the array to be sorted
	 * @param from the index of the first element (inclusive) to be sorted
	 * @param to the index of the last element (exclusive) to be sorted
	 */
	public <T> void sort(T[] array, int from, int to) {
		sort(Arrays.asList(array), from, to);
	}

	public void sort(List<?> list) {
		sort(list, 0, list.size());
	}

	public <T> void sort(T[] array) {
		sort(Arrays.asList(array), 0, array.length);
	}
	
	@SuppressWarnings("unchecked")
	protected int compare(List list, int x, int y) {
		Object a = list.get(x);
		Object b = list.get(y);
		if (comparator == null) {
			return ascend ? ((Comparable)a).compareTo(b) : ((Comparable)b).compareTo(a);
		}
		else {
			return comparator.compare(a, b);
		}
	}
	
	protected void swap(List<?> list, int x, int y) {
		Collections.swap(list, x, y);
	}

	/**
	 * @param src the source array.
	 * @param srcPos starting position in the source array.
	 * @param des the destination array.
	 * @param desPos starting position in the destination data.
	 * @param length the number of array elements to be copied.
	 */
	@SuppressWarnings("unchecked")
	protected void copy(List src, int srcPos, List des, int desPos, int length) {
		Collections.copy(src, srcPos, des, desPos, length);
	}

	public boolean verify(List<?> list) {
		return verify(list, 0, list.size());
	}

	public boolean verify(List<?> list, int from, int to) {
		for (int i = from; i < to; i++) {
			for (int j = from; j < to; j++) {
				if (i != j) {
					int c = compare(list, i, j);
					if ((i < j && c > 0) || (i > j && c < 0)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public <T> boolean verify(T[] array) {
		return verify(array, 0, array.length);
	}
	
	public <T> boolean verify(T[] array, int from, int to) {
		return verify(Arrays.asList(array), from, to);
	}
}
