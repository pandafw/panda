package panda.algorithm;

import java.util.List;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class BubbleSorter extends Sorter {
	public void sort(List<?> list, int from, int to) {
		boolean change = true;
		for (int i = to - 1; i >= from + 1 && change; i--) {
			change = false;
			for (int j = from; j < i; j++) {
				if (compare(list, j, j + 1) > 0) {
					swap(list, j, j + 1);
					change = true;
				}
			}
		}
	}
}
