package panda.lang.escape;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link NumericEntityEscaper}.
 */
public class NumericEntityEscaperTest {

	@Test
	public void testBelow() {
		final NumericEntityEscaper nee = NumericEntityEscaper.below('F');

		final String input = "ADFGZ";
		final String result = nee.translate(input);
		assertEquals("Failed to escape numeric entities via the below method", "&#65;&#68;FGZ", result);
	}

	@Test
	public void testBetween() {
		final NumericEntityEscaper nee = NumericEntityEscaper.between('F', 'L');

		final String input = "ADFGZ";
		final String result = nee.translate(input);
		assertEquals("Failed to escape numeric entities via the between method", "AD&#70;&#71;Z", result);
	}

	@Test
	public void testAbove() {
		final NumericEntityEscaper nee = NumericEntityEscaper.above('F');

		final String input = "ADFGZ";
		final String result = nee.translate(input);
		assertEquals("Failed to escape numeric entities via the above method", "ADF&#71;&#90;", result);
	}

	// See LANG-617
	@Test
	public void testSupplementary() {
		final NumericEntityEscaper nee = new NumericEntityEscaper();
		final String input = "\uD803\uDC22";
		final String expected = "&#68642;";

		final String result = nee.translate(input);
		assertEquals("Failed to escape numeric entities supplementary characters", expected, result);

	}

}
