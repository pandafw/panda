package panda.lang;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.time.StopWatch;

/**
 */
public class RandomsTest {
	@Test
	public void testRandInt() {
		for (int i = 0; i < 100; i++) {
			int r = Randoms.randInt(0, 5);
			Assert.assertTrue("0 <= " + r + " <= 5", (0 <= r && r <= 5));
		}
	}

	@Test
	public void testRandString() {
		StopWatch sw = new StopWatch();
		for (int i = 0; i < 1000; i++) {
			String s = Randoms.randString(36);
			Assert.assertEquals(36, s.length());
		}
		System.out.println("randString: " + sw.toString());
	}

	@Test
	public void testRandUUID() {
		StopWatch sw = new StopWatch();
		for (int i = 0; i < 1000; i++) {
			String s = Randoms.randUUID();
			Assert.assertEquals(36, s.length());
		}
		System.out.println("randUUID: " + sw.toString());
	}
}
