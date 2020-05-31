package panda.lang.escape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

	@Test
	public void testSupplementaryUnescaping() {
		final NumericEntityUnescaper neu = new NumericEntityUnescaper();
		final String input = "&#68642;";
		final String expected = "\uD803\uDC22";

		final String result = neu.translate(input);
		assertEquals("Failed to unescape numeric entities supplementary characters", expected, result);
	}

	@Test
	public void testOutOfBounds() {
		final NumericEntityUnescaper neu = new NumericEntityUnescaper();

		assertEquals("Failed to ignore when last character is &", "Test &", neu.translate("Test &"));
		assertEquals("Failed to ignore when last character is &", "Test &#", neu.translate("Test &#"));
		assertEquals("Failed to ignore when last character is &", "Test &#x", neu.translate("Test &#x"));
		assertEquals("Failed to ignore when last character is &", "Test &#X", neu.translate("Test &#X"));
	}

	@Test
	public void testUnfinishedEntity() {
		// parse it
		NumericEntityUnescaper neu = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
		String input = "Test &#x30 not test";
		String expected = "Test \u0030 not test";

		String result = neu.translate(input);
		assertEquals("Failed to support unfinished entities (i.e. missing semi-colon)", expected, result);

		// ignore it
		neu = new NumericEntityUnescaper();
		input = "Test &#x30 not test";
		expected = input;

		result = neu.translate(input);
		assertEquals("Failed to ignore unfinished entities (i.e. missing semi-colon)", expected, result);

		// fail it
		neu = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
		input = "Test &#x30 not test";

		try {
			result = neu.translate(input);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException iae) {
			// expected
		}
	}

}
