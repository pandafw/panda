package panda.codec;

import org.junit.Test;

/**
 */
public abstract class BinaryEncoderTestCase {

	protected abstract BinaryEncoder makeEncoder();

	@Test
	public void testEncodeEmpty() throws Exception {
		final BinaryEncoder encoder = makeEncoder();
		encoder.encode(new byte[0]);
	}

	@Test
	public void testEncodeNull() throws Exception {
		final BinaryEncoder encoder = makeEncoder();
		try {
			encoder.encode(null);
		}
		catch (final EncoderException ee) {
			// An exception should be thrown
		}
	}
}
