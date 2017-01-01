package panda.lang.codec;

import org.junit.Test;

import panda.lang.codec.BinaryEncoder;
import panda.lang.codec.EncoderException;

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
