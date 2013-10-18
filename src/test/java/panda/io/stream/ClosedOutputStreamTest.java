package panda.io.stream;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * JUnit Test Case for {@link ClosedOutputStream}.
 */
public class ClosedOutputStreamTest extends TestCase {

	/**
	 * Test the <code>write(b)</code> method.
	 */
	public void testRead() {
		try {
			new ClosedOutputStream().write('x');
			fail("write(b)");
		}
		catch (final IOException e) {
			// expected
		}
	}

}
