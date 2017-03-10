package panda.lang;

import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

import panda.io.Streams;

/**
 * test class for ClassUtils
 */
public class CharsetsTest {
	@Test
	public void testToCharset() {
		Assert.assertEquals(Charset.defaultCharset(), Charsets.toCharset((String)null));
		Assert.assertEquals(Charset.defaultCharset(), Charsets.toCharset((Charset)null));
		Assert.assertEquals(Charset.defaultCharset(), Charsets.toCharset(Charset.defaultCharset()));
		Assert.assertEquals(Charset.forName("UTF-8"), Charsets.toCharset(Charset.forName("UTF-8")));
	}

	@Test
	public void testIso8859_1() {
		Assert.assertEquals("ISO-8859-1", Charsets.ISO_8859_1);
		Assert.assertEquals("ISO-8859-1", Charsets.CS_ISO_8859_1.name());
	}

	@Test
	public void testUsAscii() {
		Assert.assertEquals("US-ASCII", Charsets.US_ASCII);
		Assert.assertEquals("US-ASCII", Charsets.CS_US_ASCII.name());
	}

	@Test
	public void testUtf16() {
		Assert.assertEquals("UTF-16", Charsets.UTF_16);
		Assert.assertEquals("UTF-16", Charsets.CS_UTF_16.name());
	}

	@Test
	public void testUtf16Be() {
		Assert.assertEquals("UTF-16BE", Charsets.UTF_16BE);
		Assert.assertEquals("UTF-16BE", Charsets.CS_UTF_16BE.name());
	}

	@Test
	public void testUtf16Le() {
		Assert.assertEquals("UTF-16LE", Charsets.UTF_16LE);
		Assert.assertEquals("UTF-16LE", Charsets.CS_UTF_16LE.name());
	}

	@Test
	public void testUtf8() {
		Assert.assertEquals("UTF-8", Charsets.UTF_8);
		Assert.assertEquals("UTF-8", Charsets.CS_UTF_8.name());
	}

	@Test
	public void testDetectCharsets() throws Exception {
		Assert.assertEquals("Shift_JIS,GB18030", Strings.join(Charsets.detectCharsets(Streams.toByteArray(getClass().getResource("Shift-JIS.txt"))), ","));
	}

}
