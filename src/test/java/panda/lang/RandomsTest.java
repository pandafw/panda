package panda.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 */
public class RandomsTest {
	@Test
	public void testRandom() {
		for (int i = 0; i < 100; i++) {
			int r = Randoms.randInt(0, 5);
			Assert.assertTrue("0 <= " + r + " <= 5", (0 <= r && r <= 5));
		}
	}
}
