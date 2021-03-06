package panda.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import panda.codec.DecoderException;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Strings;

public class Base32Test {

	private static final String[][] BASE32_TEST_CASES = { // RFC 4648
			{ "", "" }, { "f", "MY======" }, { "fo", "MZXQ====" }, { "foo", "MZXW6===" }, { "foob", "MZXW6YQ=" },
			{ "fooba", "MZXW6YTB" }, { "foobar", "MZXW6YTBOI======" }, };

	private static final Object[][] BASE32_BINARY_TEST_CASES;

	// { null, "O0o0O0o0" }
	// BASE32_BINARY_TEST_CASES[2][0] = new Hex().decode("739ce739ce");

	static {
		final Hex hex = new Hex();
		try {
			BASE32_BINARY_TEST_CASES = new Object[][] {
					new Object[] { hex.decode("623a01735836e9a126e12fbf95e013ee6892997c"),
							"MI5AC42YG3U2CJXBF67ZLYAT5ZUJFGL4" },
					new Object[] { hex.decode("623a01735836e9a126e12fbf95e013ee6892997c"),
							"mi5ac42yg3u2cjxbf67zlyat5zujfgl4" },
					new Object[] { hex.decode("739ce42108"), "OOOOIIII" } };
		}
		catch (final DecoderException de) {
			throw new Error(":(", de);
		}
	}
	private static final String[][] BASE32HEX_TEST_CASES = { // RFC 4648
			{ "", "" }, { "f", "CO======" }, { "fo", "CPNG====" }, { "foo", "CPNMU===" }, { "foob", "CPNMUOG=" },
			{ "fooba", "CPNMUOJ1" }, { "foobar", "CPNMUOJ1E8======" }, };

	private static final String[][] BASE32_TEST_CASES_CHUNKED = { // Chunked
			{ "", "" }, { "f", "MY======\r\n" }, { "fo", "MZXQ====\r\n" }, { "foo", "MZXW6===\r\n" },
			{ "foob", "MZXW6YQ=\r\n" }, { "fooba", "MZXW6YTB\r\n" }, { "foobar", "MZXW6YTBOI======\r\n" }, };

	private static final String[][] BASE32_PAD_TEST_CASES = { // RFC 4648
			{ "", "" }, { "f", "MY%%%%%%" }, { "fo", "MZXQ%%%%" }, { "foo", "MZXW6%%%" }, { "foob", "MZXW6YQ%" },
			{ "fooba", "MZXW6YTB" }, { "foobar", "MZXW6YTBOI%%%%%%" }, };

	@Test
	public void testBase64AtBufferStart() {
		testBase64InBuffer(0, 100);
	}

	@Test
	public void testBase64AtBufferEnd() {
		testBase64InBuffer(100, 0);
	}

	@Test
	public void testBase64AtBufferMiddle() {
		testBase64InBuffer(100, 100);
	}

	private void testBase64InBuffer(final int startPasSize, final int endPadSize) {
		final Base32 codec = new Base32();
		for (final String[] element : BASE32_TEST_CASES) {
			final byte[] bytes = Strings.getBytesUtf8(element[0]);
			byte[] buffer = Arrays.addAll(bytes, new byte[endPadSize]);
			buffer = Arrays.addAll(new byte[startPasSize], buffer);
			assertEquals(element[1], Strings.newStringUtf8(codec.encode(buffer, startPasSize, bytes.length)));
		}
	}

	@Test
	public void testBase32Chunked() throws Exception {
		final Base32 codec = new Base32(20);
		for (final String[] element : BASE32_TEST_CASES_CHUNKED) {
			assertEquals(element[1], codec.encodeToString(element[0].getBytes(Charsets.UTF_8)));
		}
	}

	@Test
	public void testBase32HexSamples() throws Exception {
		final Base32 codec = new Base32(true);
		for (final String[] element : BASE32HEX_TEST_CASES) {
			assertEquals(element[1], codec.encodeToString(element[0].getBytes(Charsets.UTF_8)));
		}
	}

	@Test
	public void testBase32HexSamplesReverse() throws Exception {
		final Base32 codec = new Base32(true);
		for (final String[] element : BASE32HEX_TEST_CASES) {
			assertEquals(element[0], new String(codec.decode(element[1]), Charsets.UTF_8));
		}
	}

	@Test
	public void testBase32HexSamplesReverseLowercase() throws Exception {
		final Base32 codec = new Base32(true);
		for (final String[] element : BASE32HEX_TEST_CASES) {
			assertEquals(element[0], new String(codec.decode(element[1].toLowerCase()), Charsets.UTF_8));
		}
	}

	@Test
	public void testBase32Samples() throws Exception {
		final Base32 codec = new Base32();
		for (final String[] element : BASE32_TEST_CASES) {
			assertEquals(element[1], codec.encodeToString(element[0].getBytes(Charsets.UTF_8)));
		}
	}

	@Test
	public void testBase32BinarySamples() throws Exception {
		final Base32 codec = new Base32();
		for (final Object[] element : BASE32_BINARY_TEST_CASES) {
			String expected;
			if (element.length > 2) {
				expected = (String)element[2];
			}
			else {
				expected = (String)element[1];
			}
			assertEquals(expected.toUpperCase(), codec.encodeToString((byte[])element[0]));
		}
	}

	@Test
	public void testBase32BinarySamplesReverse() throws Exception {
		final Base32 codec = new Base32();
		for (final Object[] element : BASE32_BINARY_TEST_CASES) {
			assertArrayEquals((byte[])element[0], codec.decode((String)element[1]));
		}
	}

	@Test
	public void testBase32SamplesNonDefaultPadding() throws Exception {
		final Base32 codec = new Base32((byte)0x25); // '%' <=> 0x25

		for (final String[] element : BASE32_PAD_TEST_CASES) {
			assertEquals(element[1], codec.encodeToString(element[0].getBytes(Charsets.UTF_8)));
		}
	}

	@Test
	public void testCodec200() {
		final Base32 codec = new Base32(true, (byte)'W'); // should be allowed
		assertNotNull(codec);
	}

	@Test
	public void testRandomBytes() {
		for (int i = 0; i < 20; i++) {
			final Base32 codec = new Base32();
			final byte[][] b = Base32TestData.randomData(codec, i);
			assertEquals("" + i + " " + codec.lineLength, b[1].length, codec.getEncodedLength(b[0]));
			// assertEquals(b[0],codec.decode(b[1]));
		}
	}

	@Test
	public void testRandomBytesChunked() {
		for (int i = 0; i < 20; i++) {
			final Base32 codec = new Base32(10);
			final byte[][] b = Base32TestData.randomData(codec, i);
			assertEquals("" + i + " " + codec.lineLength, b[1].length, codec.getEncodedLength(b[0]));
			// assertEquals(b[0],codec.decode(b[1]));
		}
	}

	@Test
	public void testRandomBytesHex() {
		for (int i = 0; i < 20; i++) {
			final Base32 codec = new Base32(true);
			final byte[][] b = Base32TestData.randomData(codec, i);
			assertEquals("" + i + " " + codec.lineLength, b[1].length, codec.getEncodedLength(b[0]));
			// assertEquals(b[0],codec.decode(b[1]));
		}
	}

	@Test
	public void testSingleCharEncoding() {
		for (int i = 0; i < 20; i++) {
			Base32 codec = new Base32();
			final BaseNCodec.Context context = new BaseNCodec.Context();
			final byte unencoded[] = new byte[i];
			final byte allInOne[] = codec.encode(unencoded);
			codec = new Base32();
			for (int j = 0; j < unencoded.length; j++) {
				codec.encode(unencoded, j, 1, context);
			}
			codec.encode(unencoded, 0, -1, context);
			final byte singly[] = new byte[allInOne.length];
			codec.readResults(singly, 0, 100, context);
			if (!Arrays.equals(allInOne, singly)) {
				fail();
			}
		}
	}
}
