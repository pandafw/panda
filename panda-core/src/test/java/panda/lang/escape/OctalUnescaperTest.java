package panda.lang.escape;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link OctalUnescaper}.
 */
public class OctalUnescaperTest {

	@Test
	public void testBetween() {
		final OctalUnescaper oue = new OctalUnescaper(); // .between("1", "377");

		String input = "\\45";
		String result = oue.translate(input);
		assertEquals("Failed to unescape octal characters via the between method", "\45", result);

		input = "\\377";
		result = oue.translate(input);
		assertEquals("Failed to unescape octal characters via the between method", "\377", result);

		input = "\\377 and";
		result = oue.translate(input);
		assertEquals("Failed to unescape octal characters via the between method", "\377 and", result);

		input = "\\378 and";
		result = oue.translate(input);
		assertEquals("Failed to unescape octal characters via the between method", "\378 and", result);

		input = "\\378";
		result = oue.translate(input);
		assertEquals("Failed to unescape octal characters via the between method", "\378", result);

		input = "\\1";
		result = oue.translate(input);
		assertEquals("Failed to unescape octal characters via the between method", "\1", result);
	}

}
