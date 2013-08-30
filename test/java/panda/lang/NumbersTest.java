package panda.lang;

import panda.lang.Numbers;
import junit.framework.TestCase;

/**
 */
public class NumbersTest extends TestCase {
	/**
	 * test method: toInt
	 * @throws Exception if an error occurs
	 */
	public void testToInt() throws Exception {
		assertEquals(new Integer(123), Numbers.toInt("123"));
		assertEquals(new Integer(0), Numbers.toInt("", 0));
	}

	/**
	 * test method: toLong
	 * @throws Exception if an error occurs
	 */
	public void testToLong() throws Exception {
		assertEquals(new Long(123), Numbers.toLong("123"));
	}
}
