package panda.lang.escape;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link UnicodeEscaper}.
 */
public class UnicodeEscaperTest  {

	@Test
	public void testBelow() {
		final UnicodeEscaper ue = UnicodeEscaper.below('F');

		final String input = "ADFGZ";
		final String result = ue.translate(input);
		assertEquals("Failed to escape Unicode characters via the below method", "\\u0041\\u0044FGZ", result);
	}

	@Test
	public void testBetween() {
		final UnicodeEscaper ue = UnicodeEscaper.between('F', 'L');

		final String input = "ADFGZ";
		final String result = ue.translate(input);
		assertEquals("Failed to escape Unicode characters via the between method", "AD\\u0046\\u0047Z", result);
	}

	@Test
	public void testAbove() {
		final UnicodeEscaper ue = UnicodeEscaper.above('F');

		final String input = "ADFGZ";
		final String result = ue.translate(input);
		assertEquals("Failed to escape Unicode characters via the above method", "ADF\\u0047\\u005A", result);
	}
}
