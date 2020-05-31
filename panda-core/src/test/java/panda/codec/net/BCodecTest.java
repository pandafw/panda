package panda.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.nio.charset.UnsupportedCharsetException;

import org.junit.Test;

import panda.codec.DecoderException;
import panda.codec.EncoderException;
import panda.lang.Charsets;

/**
 * Quoted-printable codec test cases
 */
public class BCodecTest {

	static final int SWISS_GERMAN_STUFF_UNICODE[] = { 0x47, 0x72, 0xFC, 0x65, 0x7A, 0x69, 0x5F, 0x7A, 0xE4, 0x6D, 0xE4 };

	static final int RUSSIAN_STUFF_UNICODE[] = { 0x412, 0x441, 0x435, 0x43C, 0x5F, 0x43F, 0x440, 0x438, 0x432, 0x435,
			0x442 };

	private String constructString(final int[] unicodeChars) {
		final StringBuilder buffer = new StringBuilder();
		if (unicodeChars != null) {
			for (final int unicodeChar : unicodeChars) {
				buffer.append((char)unicodeChar);
			}
		}
		return buffer.toString();
	}

	@Test
	public void testNullInput() throws Exception {
		final BCodec bcodec = new BCodec();
		assertNull(bcodec.doDecoding(null));
		assertNull(bcodec.doEncoding(null));
	}

	@Test
	public void testUTF8RoundTrip() throws Exception {

		final String ru_msg = constructString(RUSSIAN_STUFF_UNICODE);
		final String ch_msg = constructString(SWISS_GERMAN_STUFF_UNICODE);

		final BCodec bcodec = new BCodec(Charsets.UTF_8);

		assertEquals("=?UTF-8?B?0JLRgdC10Lxf0L/RgNC40LLQtdGC?=", bcodec.encode(ru_msg));
		assertEquals("=?UTF-8?B?R3LDvGV6aV96w6Rtw6Q=?=", bcodec.encode(ch_msg));

		assertEquals(ru_msg, bcodec.decode(bcodec.encode(ru_msg)));
		assertEquals(ch_msg, bcodec.decode(bcodec.encode(ch_msg)));
	}

	@Test
	public void testBasicEncodeDecode() throws Exception {
		final BCodec bcodec = new BCodec();
		final String plain = "Hello there";
		final String encoded = bcodec.encode(plain);
		assertEquals("Basic B encoding test", "=?UTF-8?B?SGVsbG8gdGhlcmU=?=", encoded);
		assertEquals("Basic B decoding test", plain, bcodec.decode(encoded));
	}

	@Test
	public void testEncodeDecodeNull() throws Exception {
		final BCodec bcodec = new BCodec();
		assertNull("Null string B encoding test", bcodec.encode((String)null));
		assertNull("Null string B decoding test", bcodec.decode((String)null));
	}

	@Test
	public void testEncodeStringWithNull() throws Exception {
		final BCodec bcodec = new BCodec();
		final String test = null;
		final String result = bcodec.encode(test, "charset");
		assertEquals("Result should be null", null, result);
	}

	@Test
	public void testDecodeStringWithNull() throws Exception {
		final BCodec bcodec = new BCodec();
		final String test = null;
		final String result = bcodec.decode(test);
		assertEquals("Result should be null", null, result);
	}

	@Test
	public void testEncodeObjects() throws Exception {
		final BCodec bcodec = new BCodec();
		final String plain = "what not";
		final String encoded = (String)bcodec.encode((Object)plain);

		assertEquals("Basic B encoding test", "=?UTF-8?B?d2hhdCBub3Q=?=", encoded);

		final Object result = bcodec.encode((Object)null);
		assertEquals("Encoding a null Object should return null", null, result);

		try {
			final Object dObj = new Double(3.0);
			bcodec.encode(dObj);
			fail("Trying to url encode a Double object should cause an exception.");
		}
		catch (final EncoderException ee) {
			// Exception expected, test segment passes.
		}
	}

	@Test(expected = UnsupportedCharsetException.class)
	public void testInvalidEncoding() {
		new BCodec("NONSENSE");
	}

	@Test
	public void testDecodeObjects() throws Exception {
		final BCodec bcodec = new BCodec();
		final String decoded = "=?UTF-8?B?d2hhdCBub3Q=?=";
		final String plain = (String)bcodec.decode((Object)decoded);
		assertEquals("Basic B decoding test", "what not", plain);

		final Object result = bcodec.decode((Object)null);
		assertEquals("Decoding a null Object should return null", null, result);

		try {
			final Object dObj = new Double(3.0);
			bcodec.decode(dObj);
			fail("Trying to url encode a Double object should cause an exception.");
		}
		catch (final DecoderException ee) {
			// Exception expected, test segment passes.
		}
	}
}
