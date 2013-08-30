package panda.algorithm;

import panda.lang.Arrays;
import panda.lang.Strings;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class Permutater {
	protected int[] index;
	protected long count;
	
	public Permutater() {
	}
	
	/**
	 * @return the count
	 */
	public long getCount() {
		return count;
	}

	protected boolean handle() {
		System.out.println(count + ": " + Strings.join(index, " "));
		return true;
	}
	
	public long count(int size) {
		long count = 1;
		for (int i = 2; i <= size; i++) {
			count *= i;
		}
		return count;
	}

	public long permutate(int size) {
		index = new int[size];
		for (int i = 0; i < size; i++) {
			index[i] = i;
		}
		
		count = 0;
		permutate(0, size);
		return count;
	}
	
	protected boolean permutate(int m, int len) {
		if (m < len - 1) {
			if (!permutate(m + 1, len)) {
				return false;
			}

			for (int i = m + 1; i < len; i++) {
				Arrays.swap(index, m, i);

				if (!permutate(m + 1, len)) {
					return false;
				}

				Arrays.swap(index, m, i);
			}
			return true;
		} 
		else {
			count++;
			return handle();
		}
	}
}
