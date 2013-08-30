package panda.algorithm;

import panda.lang.Strings;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class Combinater {
	private int[] index;
	private long count;
	
	public Combinater() {
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

	/**
	 * C(n, m)
	 * @param n array size
	 * @param m combination number
	 */
	public long count(int n, int m) {
		long count = 1;
		for (int i = n - m + 1; i <= n; i++) {
			count *= i;
		}
		for (int i = 2; i <= m; i++) {
			count /= i;
		}
		return count;
	}
	
	/**
	 * C(n, m)
	 * @param n array size
	 * @param m combination number
	 */
	public long combinate(int n, int m) {
		index = new int[m];
		count = 0;
		_combinate(n, m);
		return count;
	}

	protected boolean _combinate(int n, int m) { 
		for (int i = n; i >= m; i--) {
			index[m - 1] = i - 1;
			if (m > 1) {
				if (!_combinate(i - 1, m - 1)) {
					return false;
				}
			} 
			else {
				count++;
				return handle();
			}
		}
		return true;
	} 
}
