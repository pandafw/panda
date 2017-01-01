package panda.io.stream;

import junit.framework.TestCase;

/**
 * Really not a lot to do here, but checking that no Exceptions are thrown.
 * 
 */
public class NullWriterTest extends TestCase {

	public NullWriterTest(final String name) {
		super(name);
	}

	public void testNull() {
		final char[] chars = new char[] { 'A', 'B', 'C' };
		final NullWriter writer = new NullWriter();
		writer.write(1);
		writer.write(chars);
		writer.write(chars, 1, 1);
		writer.write("some string");
		writer.write("some string", 2, 2);
		writer.flush();
		writer.close();
	}

}
