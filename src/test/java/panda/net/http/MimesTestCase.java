package panda.net.http;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import panda.net.http.Mimes;

/**
 * Use the online <a href="http://dogmamix.com/MimeHeadersDecoder/">MimeHeadersDecoder</a> to
 * validate expected values.
 */
public final class MimesTestCase {

	@Test
	public void noNeedToDecode() throws Exception {
		assertEncoded("abc", "abc");
	}

	@Test
	public void decodeUtf8QuotedPrintableEncoded() throws Exception {
		assertEncoded(" hé! àèôu !!!", "=?UTF-8?Q?_h=C3=A9!_=C3=A0=C3=A8=C3=B4u_!!!?=");
	}

	@Test
	public void decodeUtf8Base64Encoded() throws Exception {
		assertEncoded(" hé! àèôu !!!", "=?UTF-8?B?IGjDqSEgw6DDqMO0dSAhISE=?=");
	}

	@Test
	public void decodeIso88591Base64Encoded() throws Exception {
		assertEncoded("If you can read this you understand the example.",
			"=?ISO-8859-1?B?SWYgeW91IGNhbiByZWFkIHRoaXMgeW8=?= =?ISO-8859-2?B?dSB1bmRlcnN0YW5kIHRoZSBleGFtcGxlLg==?=\"\r\n");
	}

	@Test
	public void decodeIso88591Base64EncodedWithWhiteSpace() throws Exception {
		assertEncoded(
			"If you can read this you understand the example.",
			"=?ISO-8859-1?B?SWYgeW91IGNhbiByZWFkIHRoaXMgeW8=?=\t  \r\n   =?ISO-8859-2?B?dSB1bmRlcnN0YW5kIHRoZSBleGFtcGxlLg==?=\"\r\n");
	}

	private static void assertEncoded(String expected, String encoded) throws Exception {
		assertEquals(expected, Mimes.decodeText(encoded));
	}

	@Test(expected = UnsupportedEncodingException.class)
	public void decodeInvalidEncoding() throws Exception {
		Mimes.decodeText("=?invalid?B?xyz-?=");
	}
}
