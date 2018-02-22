package panda.codec.net;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import panda.codec.DecoderException;
import panda.codec.net.RFC1522Codec;
import panda.lang.Charsets;

/**
 * RFC 1522 compliant codec test cases
 */
public class RFC1522CodecTest {

	static class RFC1522TestCodec extends RFC1522Codec {

		@Override
		protected byte[] doDecoding(final byte[] bytes) {
			return bytes;
		}

		@Override
		protected byte[] doEncoding(final byte[] bytes) {
			return bytes;
		}

		@Override
		protected String getEncoding() {
			return "T";
		}

	}

	@Test
	public void testNullInput() throws Exception {
		final RFC1522TestCodec testcodec = new RFC1522TestCodec();
		assertNull(testcodec.decodeText(null));
		assertNull(testcodec.encodeText(null, Charsets.UTF_8));
	}

	private void assertExpectedDecoderException(final String s) throws Exception {
		final RFC1522TestCodec testcodec = new RFC1522TestCodec();
		try {
			testcodec.decodeText(s);
			fail("DecoderException should have been thrown");
		}
		catch (final DecoderException e) {
			// Expected.
		}
	}

	@Test
	public void testDecodeInvalid() throws Exception {
		assertExpectedDecoderException("whatever");
		assertExpectedDecoderException("=?");
		assertExpectedDecoderException("?=");
		assertExpectedDecoderException("==");
		assertExpectedDecoderException("=??=");
		assertExpectedDecoderException("=?stuff?=");
		assertExpectedDecoderException("=?UTF-8??=");
		assertExpectedDecoderException("=?UTF-8?stuff?=");
		assertExpectedDecoderException("=?UTF-8?T?stuff");
		assertExpectedDecoderException("=??T?stuff?=");
		assertExpectedDecoderException("=?UTF-8??stuff?=");
		assertExpectedDecoderException("=?UTF-8?W?stuff?=");
	}

}
