package panda.algorithm;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class MergeSorter extends Sorter {
	/**
	 * Tuning parameter: list size at or below which insertion sort will be used in preference to
	 * mergesort or quicksort.
	 */
	private static final int INSERTIONSORT_THRESHOLD = 7;

	public void sort(List<?> list, int from, int to) {
		List<Object> src = new ArrayList<Object>(list);
		mergeSort(src, list, from, to, -from);
	}

	/**
	 * Src is the source array that starts at index 0 Dest is the (possibly larger) array
	 * destination with a possible offset low is the index in dest to start sorting high is the end
	 * index in dest to end sorting off is the offset into src corresponding to low in dest
	 */
	@SuppressWarnings("unchecked")
	private void mergeSort(List src, List dest, int low, int high, int off) {
		int length = high - low;

		// Insertion sort on smallest arrays
		if (length < INSERTIONSORT_THRESHOLD) {
			for (int i = low; i < high; i++)
				for (int j = i; j > low && compare(dest, j - 1, j) > 0; j--)
					swap(dest, j, j - 1);
			return;
		}

		// Recursively sort halves of dest into src
		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = (low + high) >>> 1;
		mergeSort(dest, src, low, mid, -off);
		mergeSort(dest, src, mid, high, -off);

		// If list is already sorted, just copy from src to dest. This is an
		// optimization that results in faster sorts for nearly ordered lists.
		if (compare(src, mid - 1, mid) <= 0) {
			copy(src, low, dest, destLow, length);
			return;
		}

		// Merge sorted halves (now in src) into dest
		for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid && compare(src, p, q) <= 0)
				dest.set(i, src.get(p++));
			else
				dest.set(i, src.get(q++));
		}
	}
}
