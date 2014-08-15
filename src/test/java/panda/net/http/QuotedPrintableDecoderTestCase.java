package panda.net.http;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public final class QuotedPrintableDecoderTestCase {

	private static final String US_ASCII_CHARSET = "US-ASCII";

	@Test
	public void emptyDecode() throws Exception {
		assertEncoded("", "");
	}

	@Test
	public void plainDecode() throws Exception {
		// spaces are allowed in encoded data
		// There are special rules for trailing spaces; these are not currently implemented.
		assertEncoded("The quick brown fox jumps over the lazy dog.", "The quick brown fox jumps over the lazy dog.");
	}

	@Test
	public void basicEncodeDecode() throws Exception {
		assertEncoded("= Hello there =\r\n", "=3D Hello there =3D=0D=0A");
	}

	@Test
	public void invalidQuotedPrintableEncoding() throws Exception {
		assertIOException("truncated escape sequence", "YWJjMTIzXy0uKn4hQCMkJV4mKCkre31cIlxcOzpgLC9bXQ==");
	}

	@Test
	public void unsafeDecode() throws Exception {
		assertEncoded("=\r\n", "=3D=0D=0A");
	}

	@Test
	public void unsafeDecodeLowerCase() throws Exception {
		assertEncoded("=\r\n", "=3d=0d=0a");
	}

	@Test(expected = IOException.class)
	public void invalidCharDecode() throws Exception {
		assertEncoded("=\r\n", "=3D=XD=XA");
	}

	/**
	 * This is NOT supported by Commons-Codec, see CODEC-121.
	 * 
	 * @throws Exception
	 * @see <a href="https://issues.apache.org/jira/browse/CODEC-121">CODEC-121</a>
	 */
	@Test
	public void softLineBreakDecode() throws Exception {
		assertEncoded(
			"If you believe that truth=beauty, then surely mathematics is the most beautiful branch of philosophy.",
			"If you believe that truth=3Dbeauty, then surely=20=\r\nmathematics is the most beautiful branch of philosophy.");
	}

	@Test
	public void invalidSoftBreak1() throws Exception {
		assertIOException("CR must be followed by LF", "=\r\r");
	}

	@Test
	public void invalidSoftBreak2() throws Exception {
		assertIOException("CR must be followed by LF", "=\rn");
	}

	@Test
	public void truncatedEscape() throws Exception {
		assertIOException("truncated", "=1");
	}

	private static void assertEncoded(String clearText, String encoded) throws Exception {
		byte[] expected = clearText.getBytes(US_ASCII_CHARSET);

		byte[] encodedData = encoded.getBytes(US_ASCII_CHARSET);
		byte[] actual = Mimes.decodeQuoted(encodedData);

		assertArrayEquals(expected, actual);
	}

	private static void assertIOException(String messageText, String encoded) throws UnsupportedEncodingException {
		byte[] encodedData = encoded.getBytes(US_ASCII_CHARSET);
		try {
			Mimes.decodeQuoted(encodedData);
			fail("Expected IOException");
		}
		catch (IOException e) {
			String em = e.getMessage();
			assertTrue("Expected to find " + messageText + " in '" + em + "'", em.contains(messageText));
		}
	}

}
