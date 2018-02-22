package panda.codec.net;

import org.junit.Test;

import panda.codec.net.Utils;

/**
 * Tests Utils.
 * <p>
 * Methods currently get 100%/100% line/branch code coverage from other tests classes.
 * </p>
 */
public class UtilsTest {

	/**
	 * We could make the constructor private but there does not seem to be a point to jumping
	 * through extra code hoops to restrict instantiation right now.
	 */
	@Test
	public void testConstructor() {
		new Utils();
	}

}
