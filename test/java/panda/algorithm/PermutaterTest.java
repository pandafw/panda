package panda.algorithm;

import panda.algorithm.Permutater;
import junit.framework.TestCase;

/**
 * @author yf.frank.wang@gmail.com
 */
public class PermutaterTest extends TestCase {
	public void test3() {
		Permutater p = new Permutater();
		p.permutate(6);
		
		assertEquals(p.count(6), p.getCount());
	}
}
