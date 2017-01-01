package panda.io.stream;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * Really not a lot to do here, but checking that no Exceptions are thrown.
 * 
 * @version $Id: NullOutputStreamTest.java 1415850 2012-11-30 20:51:39Z ggregory $
 */

public class NullOutputStreamTest extends TestCase {

	public NullOutputStreamTest(final String name) {
		super(name);
	}

	public void testNull() throws IOException {
		final NullOutputStream nos = new NullOutputStream();
		nos.write("string".getBytes());
		nos.write("some string".getBytes(), 3, 5);
		nos.write(1);
		nos.write(0x0f);
		nos.flush();
		nos.close();
		nos.write("allowed".getBytes());
		nos.write(255);
	}

}
