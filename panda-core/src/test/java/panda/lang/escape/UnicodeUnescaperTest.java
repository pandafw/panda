package panda.lang.escape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link UnicodeEscaper}.
 */
public class UnicodeUnescaperTest {

	// Requested in LANG-507
	@Test
	public void testUPlus() {
		final UnicodeUnescaper uu = new UnicodeUnescaper();

		final String input = "\\u+0047";
		assertEquals("Failed to unescape Unicode characters with 'u+' notation", "G", uu.translate(input));
	}

	@Test
	public void testUuuuu() {
		final UnicodeUnescaper uu = new UnicodeUnescaper();

		final String input = "\\uuuuuuuu0047";
		final String result = uu.translate(input);
		assertEquals("Failed to unescape Unicode characters with many 'u' characters", "G", result);
	}

	@Test
	public void testLessThanFour() {
		final UnicodeUnescaper uu = new UnicodeUnescaper();

		final String input = "\\0047\\u006";
		try {
			uu.translate(input);
			fail("A lack of digits in a Unicode escape sequence failed to throw an exception");
		}
		catch (final IllegalArgumentException iae) {
			// expected
		}
	}
}
