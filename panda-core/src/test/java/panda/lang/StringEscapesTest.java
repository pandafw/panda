package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import panda.io.Streams;
import panda.io.stream.StringBuilderWriter;
import panda.lang.escape.CharSequenceTranslator;
import panda.lang.escape.NumericEntityEscaper;

/**
 * Unit tests for {@link StringEscapes}.
 */
public class StringEscapesTest {
    private final static String FOO = "foo";

	@Test
	public void testEscapeJava() throws IOException {
		assertEquals(null, StringEscapes.escapeJava(null));
		try {
			StringEscapes.ESCAPE_JAVA.translate(null, null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			StringEscapes.ESCAPE_JAVA.translate("", null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}

		assertEscapeJava("empty string", "", "");
		assertEscapeJava(FOO, FOO);
		assertEscapeJava("tab", "\\t", "\t");
		assertEscapeJava("backslash", "\\\\", "\\");
		assertEscapeJava("single quote should not be escaped", "'", "'");
		assertEscapeJava("\\\\\\b\\t\\r", "\\\b\t\r");
		assertEscapeJava("\\u1234", "\u1234");
		assertEscapeJava("\\u0234", "\u0234");
		assertEscapeJava("\\u00EF", "\u00ef");
		assertEscapeJava("\\u0001", "\u0001");
		assertEscapeJava("Should use capitalized Unicode hex", "\\uABCD", "\uabcd");

		assertEscapeJava("He didn't say, \\\"stop!\\\"", "He didn't say, \"stop!\"");
		assertEscapeJava("non-breaking space", "This space is non-breaking:" + "\\u00A0",
			"This space is non-breaking:\u00a0");
		assertEscapeJava("\\uABCD\\u1234\\u012C", "\uABCD\u1234\u012C");
	}

	/**
	 * Tests https://issues.apache.org/jira/browse/LANG-421
	 */
	@Test
	public void testEscapeJavaWithSlash() {
		final String input = "String with a slash (/) in it";

		final String expected = input;
		final String actual = StringEscapes.escapeJava(input);

		/**
		 * In 2.4 StringEscapes.escapeJava(String) escapes '/' characters, which are not a valid
		 * character to escape in a Java string.
		 */
		assertEquals(expected, actual);
	}

	private void assertEscapeJava(final String escaped, final String original) throws IOException {
		assertEscapeJava(null, escaped, original);
	}

	private void assertEscapeJava(String message, final String expected, final String original) throws IOException {
		final String converted = StringEscapes.escapeJava(original);
		message = "escapeJava(String) failed" + (message == null ? "" : (": " + message));
		assertEquals(message, expected, converted);

		final StringBuilderWriter writer = new StringBuilderWriter();
		StringEscapes.ESCAPE_JAVA.translate(original, writer);
		assertEquals(expected, writer.toString());
	}

	@Test
	public void testUnescapeJava() throws IOException {
		assertEquals(null, StringEscapes.unescapeJava(null));
		try {
			StringEscapes.UNESCAPE_JAVA.translate(null, null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			StringEscapes.UNESCAPE_JAVA.translate("", null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			StringEscapes.unescapeJava("\\u02-3");
			fail();
		}
		catch (final RuntimeException ex) {
		}

		assertUnescapeJava("", "");
		assertUnescapeJava("test", "test");
		assertUnescapeJava("\ntest\b", "\\ntest\\b");
		assertUnescapeJava("\u123425foo\ntest\b", "\\u123425foo\\ntest\\b");
		assertUnescapeJava("'\foo\teste\r", "\\'\\foo\\teste\\r");
		assertUnescapeJava("", "\\");
		// foo
		assertUnescapeJava("lowercase Unicode", "\uABCDx", "\\uabcdx");
		assertUnescapeJava("uppercase Unicode", "\uABCDx", "\\uABCDx");
		assertUnescapeJava("Unicode as final character", "\uABCD", "\\uabcd");
	}

	private void assertUnescapeJava(final String unescaped, final String original) throws IOException {
		assertUnescapeJava(null, unescaped, original);
	}

	private void assertUnescapeJava(final String message, final String unescaped, final String original)
			throws IOException {
		final String expected = unescaped;
		final String actual = StringEscapes.unescapeJava(original);

		assertEquals("unescape(String) failed" + (message == null ? "" : (": " + message)) + ": expected '"
				+ StringEscapes.escapeJava(expected) +
				// we escape this so we can see it in the error message
				"' actual '" + StringEscapes.escapeJava(actual) + "'", expected, actual);

		final StringBuilderWriter writer = new StringBuilderWriter();
		StringEscapes.UNESCAPE_JAVA.translate(original, writer);
		assertEquals(unescaped, writer.toString());

	}

	@Test
	public void testEscapeEcmaScript() {
		assertEquals(null, StringEscapes.escapeEcmaScript(null));
		try {
			StringEscapes.ESCAPE_ECMASCRIPT.translate(null, null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			StringEscapes.ESCAPE_ECMASCRIPT.translate("", null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}

		assertEquals("He didn\\'t say, \\\"stop!\\\"", StringEscapes.escapeEcmaScript("He didn't say, \"stop!\""));
		assertEquals("document.getElementById(\\\"test\\\").value = \\'<script>alert(\\'aaa\\');<\\/script>\\';",
			StringEscapes
				.escapeEcmaScript("document.getElementById(\"test\").value = '<script>alert('aaa');</script>';"));
	}

	// HTML and XML
	// --------------------------------------------------------------

	private static final String[][] HTML_ESCAPES = {
			{ "no escaping", "plain text", "plain text" },
			{ "no escaping", "plain text", "plain text" },
			{ "empty string", "", "" },
			{ "null", null, null },
			{ "ampersand", "bread &amp; butter", "bread & butter" },
			{ "quotes", "&quot;bread&quot; &amp; butter", "\"bread\" & butter" },
			{ "final character only", "greater than &gt;", "greater than >" },
			{ "first character only", "&lt; less than", "< less than" },
			{ "apostrophe", "Huntington&apos;s chorea", "Huntington's chorea" },
			{ "languages", "English,Fran&ccedil;ais,\u65E5\u672C\u8A9E (nihongo)",
					"English,Fran\u00E7ais,\u65E5\u672C\u8A9E (nihongo)" },
			{ "8-bit ascii shouldn't number-escape", "\u0080\u009F", "\u0080\u009F" }, };

	@Test
	public void testEscapeHtml() {
		for (int i = 0; i < HTML_ESCAPES.length; ++i) {
			final String message = HTML_ESCAPES[i][0];
			final String expected = HTML_ESCAPES[i][1];
			final String original = HTML_ESCAPES[i][2];
			assertEquals(message, expected, StringEscapes.escapeHtml4(original));
			final StringBuilderWriter sw = new StringBuilderWriter();
			try {
				StringEscapes.ESCAPE_HTML4.translate(original, sw);
			}
			catch (final IOException e) {
			}
			final String actual = original == null ? null : sw.toString();
			assertEquals(message, expected, actual);
		}
	}

	@Test
	public void testUnescapeHtml4() {
		for (int i = 0; i < HTML_ESCAPES.length; ++i) {
			final String message = HTML_ESCAPES[i][0];
			final String expected = HTML_ESCAPES[i][2];
			final String original = HTML_ESCAPES[i][1];
			assertEquals(message, expected, StringEscapes.unescapeHtml4(original));

			final StringBuilderWriter sw = new StringBuilderWriter();
			try {
				StringEscapes.UNESCAPE_HTML4.translate(original, sw);
			}
			catch (final IOException e) {
			}
			final String actual = original == null ? null : sw.toString();
			assertEquals(message, expected, actual);
		}
		// \u00E7 is a cedilla (c with wiggle under)
		// note that the test string must be 7-bit-clean (Unicode escaped) or else it will compile
		// incorrectly
		// on some locales
		assertEquals("funny chars pass through OK", "Fran\u00E7ais", StringEscapes.unescapeHtml4("Fran\u00E7ais"));

		assertEquals("Hello&;World", StringEscapes.unescapeHtml4("Hello&;World"));
		assertEquals("Hello&#;World", StringEscapes.unescapeHtml4("Hello&#;World"));
		assertEquals("Hello&# ;World", StringEscapes.unescapeHtml4("Hello&# ;World"));
		assertEquals("Hello&##;World", StringEscapes.unescapeHtml4("Hello&##;World"));
	}

	@Test
	public void testUnescapeHexCharsHtml() {
		// Simple easy to grok test
		assertEquals("hex number unescape", "\u0080\u009F", StringEscapes.unescapeHtml4("&#x80;&#x9F;"));
		assertEquals("hex number unescape", "\u0080\u009F", StringEscapes.unescapeHtml4("&#X80;&#X9F;"));
		// Test all Character values:
		for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
			final Character c1 = new Character(i);
			final Character c2 = new Character((char)(i + 1));
			final String expected = c1.toString() + c2.toString();
			final String escapedC1 = "&#x" + Integer.toHexString((c1.charValue())) + ";";
			final String escapedC2 = "&#x" + Integer.toHexString((c2.charValue())) + ";";
			assertEquals("hex number unescape index " + (int)i, expected,
				StringEscapes.unescapeHtml4(escapedC1 + escapedC2));
		}
	}

	@Test
	public void testUnescapeUnknownEntity() throws Exception {
		assertEquals("&zzzz;", StringEscapes.unescapeHtml4("&zzzz;"));
	}

	@Test
	public void testEscapeHtmlVersions() throws Exception {
		assertEquals("&Beta;", StringEscapes.escapeHtml4("\u0392"));
		assertEquals("\u0392", StringEscapes.unescapeHtml4("&Beta;"));

		// TODO: refine API for escaping/unescaping specific HTML versions
	}

	@Test
	public void testEscapeXml() throws Exception {
		assertEquals("&lt;abc&gt;", StringEscapes.escapeXml("<abc>"));
		assertEquals("<abc>", StringEscapes.unescapeXml("&lt;abc&gt;"));

		assertEquals("XML should not escape >0x7f values", "\u00A1", StringEscapes.escapeXml("\u00A1"));
		assertEquals("XML should be able to unescape >0x7f values", "\u00A0", StringEscapes.unescapeXml("&#160;"));
		assertEquals("XML should be able to unescape >0x7f values with one leading 0", "\u00A0",
			StringEscapes.unescapeXml("&#0160;"));
		assertEquals("XML should be able to unescape >0x7f values with two leading 0s", "\u00A0",
			StringEscapes.unescapeXml("&#00160;"));
		assertEquals("XML should be able to unescape >0x7f values with three leading 0s", "\u00A0",
			StringEscapes.unescapeXml("&#000160;"));

		assertEquals("ain't", StringEscapes.unescapeXml("ain&apos;t"));
		assertEquals("ain&apos;t", StringEscapes.escapeXml("ain't"));
		assertEquals("", StringEscapes.escapeXml(""));
		assertEquals(null, StringEscapes.escapeXml(null));
		assertEquals(null, StringEscapes.unescapeXml(null));

		StringBuilderWriter sw = new StringBuilderWriter();
		try {
			StringEscapes.ESCAPE_XML.translate("<abc>", sw);
		}
		catch (final IOException e) {
		}
		assertEquals("XML was escaped incorrectly", "&lt;abc&gt;", sw.toString());

		sw = new StringBuilderWriter();
		try {
			StringEscapes.UNESCAPE_XML.translate("&lt;abc&gt;", sw);
		}
		catch (final IOException e) {
		}
		assertEquals("XML was unescaped incorrectly", "<abc>", sw.toString());
	}

	/**
	 * Tests Supplementary characters.
	 * <p>
	 * From http://www.w3.org/International/questions/qa-escapes
	 * </p>
	 * <blockquote> Supplementary characters are those Unicode characters that have code points
	 * higher than the characters in the Basic Multilingual Plane (BMP). In UTF-16 a supplementary
	 * character is encoded using two 16-bit surrogate code points from the BMP. Because of this,
	 * some people think that supplementary characters need to be represented using two escapes, but
	 * this is incorrect - you must use the single, code point value for that character. For
	 * example, use &#x233B4; rather than &#xD84C;&#xDFB4;. </blockquote>
	 * 
	 * @see <a href="http://www.w3.org/International/questions/qa-escapes">Using character escapes
	 *      in markup and CSS</a>
	 * @see <a href="https://issues.apache.org/jira/browse/LANG-728">LANG-728</a>
	 */
	@Test
	public void testEscapeXmlSupplementaryCharacters() {
		final CharSequenceTranslator escapeXml = StringEscapes.ESCAPE_XML.with(NumericEntityEscaper.between(0x7f,
			Integer.MAX_VALUE));

		assertEquals("Supplementary character must be represented using a single escape", "&#144308;",
			escapeXml.translate("\uD84C\uDFB4"));
	}

	@Test
	public void testEscapeXmlAllCharacters() {
		// http://www.w3.org/TR/xml/#charsets says:
		// Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF] /* any
		// Unicode character,
		// excluding the surrogate blocks, FFFE, and FFFF. */
		final CharSequenceTranslator escapeXml = StringEscapes.ESCAPE_XML.with(NumericEntityEscaper.below(9),
			NumericEntityEscaper.between(0xB, 0xC), NumericEntityEscaper.between(0xE, 0x19),
			NumericEntityEscaper.between(0xD800, 0xDFFF), NumericEntityEscaper.between(0xFFFE, 0xFFFF),
			NumericEntityEscaper.above(0x110000));

		assertEquals("&#0;&#1;&#2;&#3;&#4;&#5;&#6;&#7;&#8;",
			escapeXml.translate("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u0008"));
		assertEquals("\t", escapeXml.translate("\t")); // 0x9
		assertEquals("\n", escapeXml.translate("\n")); // 0xA
		assertEquals("&#11;&#12;", escapeXml.translate("\u000B\u000C"));
		assertEquals("\r", escapeXml.translate("\r")); // 0xD
		assertEquals("Hello World! Ain&apos;t this great?", escapeXml.translate("Hello World! Ain't this great?"));
		assertEquals("&#14;&#15;&#24;&#25;", escapeXml.translate("\u000E\u000F\u0018\u0019"));
	}

	/**
	 * Reverse of the above.
	 * 
	 * @see <a href="https://issues.apache.org/jira/browse/LANG-729">LANG-729</a>
	 */
	@Test
	public void testUnescapeXmlSupplementaryCharacters() {
		assertEquals("Supplementary character must be represented using a single escape", "\uD84C\uDFB4",
			StringEscapes.unescapeXml("&#144308;"));
	}

	// Tests issue #38569
	// http://issues.apache.org/bugzilla/show_bug.cgi?id=38569
	@Test
	public void testStandaloneAmphersand() {
		assertEquals("<P&O>", StringEscapes.unescapeHtml4("&lt;P&O&gt;"));
		assertEquals("test & <", StringEscapes.unescapeHtml4("test & &lt;"));
		assertEquals("<P&O>", StringEscapes.unescapeXml("&lt;P&O&gt;"));
		assertEquals("test & <", StringEscapes.unescapeXml("test & &lt;"));
	}

	@Test
	public void testLang313() {
		assertEquals("& &", StringEscapes.unescapeHtml4("& &amp;"));
	}

	@Test
	public void testEscapeCsvString() throws Exception {
		assertEquals("foo.bar", StringEscapes.escapeCsv("foo.bar"));
		assertEquals("\"foo,bar\"", StringEscapes.escapeCsv("foo,bar"));
		assertEquals("\"foo\nbar\"", StringEscapes.escapeCsv("foo\nbar"));
		assertEquals("\"foo\rbar\"", StringEscapes.escapeCsv("foo\rbar"));
		assertEquals("\"foo\"\"bar\"", StringEscapes.escapeCsv("foo\"bar"));
		assertEquals("", StringEscapes.escapeCsv(""));
		assertEquals(null, StringEscapes.escapeCsv(null));
	}

	@Test
	public void testEscapeCsvWriter() throws Exception {
		checkCsvEscapeWriter("foo.bar", "foo.bar");
		checkCsvEscapeWriter("\"foo,bar\"", "foo,bar");
		checkCsvEscapeWriter("\"foo\nbar\"", "foo\nbar");
		checkCsvEscapeWriter("\"foo\rbar\"", "foo\rbar");
		checkCsvEscapeWriter("\"foo\"\"bar\"", "foo\"bar");
		checkCsvEscapeWriter("", null);
		checkCsvEscapeWriter("", "");
	}

	private void checkCsvEscapeWriter(final String expected, final String value) {
		try {
			final StringBuilderWriter writer = new StringBuilderWriter();
			StringEscapes.ESCAPE_CSV.translate(value, writer);
			assertEquals(expected, writer.toString());
		}
		catch (final IOException e) {
			fail("Threw: " + e);
		}
	}

	@Test
	public void testUnescapeCsvString() throws Exception {
		assertEquals("foo.bar", StringEscapes.unescapeCsv("foo.bar"));
		assertEquals("foo,bar", StringEscapes.unescapeCsv("\"foo,bar\""));
		assertEquals("foo\nbar", StringEscapes.unescapeCsv("\"foo\nbar\""));
		assertEquals("foo\rbar", StringEscapes.unescapeCsv("\"foo\rbar\""));
		assertEquals("foo\"bar", StringEscapes.unescapeCsv("\"foo\"\"bar\""));
		assertEquals("", StringEscapes.unescapeCsv(""));
		assertEquals(null, StringEscapes.unescapeCsv(null));

		assertEquals("\"foo.bar\"", StringEscapes.unescapeCsv("\"foo.bar\""));
	}

	@Test
	public void testUnescapeCsvWriter() throws Exception {
		checkCsvUnescapeWriter("foo.bar", "foo.bar");
		checkCsvUnescapeWriter("foo,bar", "\"foo,bar\"");
		checkCsvUnescapeWriter("foo\nbar", "\"foo\nbar\"");
		checkCsvUnescapeWriter("foo\rbar", "\"foo\rbar\"");
		checkCsvUnescapeWriter("foo\"bar", "\"foo\"\"bar\"");
		checkCsvUnescapeWriter("", null);
		checkCsvUnescapeWriter("", "");

		checkCsvUnescapeWriter("\"foo.bar\"", "\"foo.bar\"");
	}

	private void checkCsvUnescapeWriter(final String expected, final String value) {
		try {
			final StringBuilderWriter writer = new StringBuilderWriter();
			StringEscapes.UNESCAPE_CSV.translate(value, writer);
			assertEquals(expected, writer.toString());
		}
		catch (final IOException e) {
			fail("Threw: " + e);
		}
	}

	/**
	 * Tests // https://issues.apache.org/jira/browse/LANG-480
	 * @throws java.io.UnsupportedEncodingException if an error occurs
	 */
	@Test
	public void testEscapeHtmlHighUnicode() throws java.io.UnsupportedEncodingException {
		// this is the utf8 representation of the character:
		// COUNTING ROD UNIT DIGIT THREE
		// in Unicode
		// codepoint: U+1D362
		final byte[] data = new byte[] { (byte)0xF0, (byte)0x9D, (byte)0x8D, (byte)0xA2 };

		final String original = new String(data, "UTF8");

		final String escaped = StringEscapes.escapeHtml4(original);
		assertEquals("High Unicode should not have been escaped", original, escaped);

		final String unescaped = StringEscapes.unescapeHtml4(escaped);
		assertEquals("High Unicode should have been unchanged", original, unescaped);

		// TODO: I think this should hold, needs further investigation
		// String unescapedFromEntity = StringEscapes.unescapeHtml4( "&#119650;" );
		// assertEquals( "High Unicode should have been unescaped", original, unescapedFromEntity);
	}

	/**
	 * Tests https://issues.apache.org/jira/browse/LANG-339
	 */
	@Test
	public void testEscapeHiragana() {
		// Some random Japanese Unicode characters
		final String original = "\u304B\u304C\u3068";
		final String escaped = StringEscapes.escapeHtml4(original);
		assertEquals("Hiragana character Unicode behaviour should not be being escaped by escapeHtml4", original,
			escaped);

		final String unescaped = StringEscapes.unescapeHtml4(escaped);

		assertEquals("Hiragana character Unicode behaviour has changed - expected no unescaping", escaped, unescaped);
	}

	/**
	 * Tests https://issues.apache.org/jira/browse/LANG-708
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	@Test
	public void testLang708() throws IOException {
		final String input = Streams.toString(getClass().getResourceAsStream("lang-708-input.txt"), "UTF-8");
		final String escaped = StringEscapes.escapeEcmaScript(input);
		// just the end:
		assertTrue(escaped, escaped.endsWith("}]"));
		// a little more:
		assertTrue(escaped, escaped.endsWith("\"valueCode\\\":\\\"\\\"}]"));
	}

	/**
	 * Tests https://issues.apache.org/jira/browse/LANG-720
	 */
	@Test
	public void testLang720() {
		final String input = new StringBuilder("\ud842\udfb7").append("A").toString();
		final String escaped = StringEscapes.escapeXml(input);
		assertEquals(input, escaped);
	}

	@Test
	public void testEscapeJson() {
		assertEquals(null, StringEscapes.escapeJson(null));
		try {
			StringEscapes.ESCAPE_JSON.translate(null, null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			StringEscapes.ESCAPE_JSON.translate("", null);
			fail();
		}
		catch (final IOException ex) {
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}

		assertEquals("He didn't say, \\\"stop!\\\"", StringEscapes.escapeJson("He didn't say, \"stop!\""));

		String expected = "\\\"foo\\\" isn't \\\"bar\\\". specials: \\b\\r\\n\\f\\t\\\\\\/";
		String input = "\"foo\" isn't \"bar\". specials: \b\r\n\f\t\\/";

		assertEquals(expected, StringEscapes.escapeJson(input));
	}

}
