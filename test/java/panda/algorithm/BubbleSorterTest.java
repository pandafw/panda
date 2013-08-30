package panda.algorithm;

import java.util.Random;

import panda.algorithm.BubbleSorter;
import junit.framework.TestCase;

/**
 * @author yf.frank.wang@gmail.com
 */
public class BubbleSorterTest extends TestCase {
	
	protected void randomTest(int n) {
		Random r = new Random();
		
		Integer[] a = new Integer[n]; 
		for (int i = 0; i < a.length; i++) {
			a[i] = r.nextInt();
		}
		
//		System.out.println(StringUtils.join(a, ", "));

		BubbleSorter bs = new BubbleSorter();
		bs.sort(a, 1, a.length - 1);
		
//		System.out.println(StringUtils.join(a, ", "));

		assertTrue(bs.verify(a, 1, a.length - 1));
	}
	
	public void testRandom() {
		for (int i = 1; i < 100; i++) {
			randomTest(i);
		}
	}
}
