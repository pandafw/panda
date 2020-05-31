package panda.lang.escape;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

/**
 * Unit tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

	@Test
	public void testBasicLookup() throws IOException {
		final LookupTranslator lt = new LookupTranslator(new String[][] { { "one", "two" } });
		final StringWriter out = new StringWriter();
		final int result = lt.translateChar("one", 0, out);
		assertEquals("Incorrect codepoint consumption", 3, result);
		assertEquals("Incorrect value", "two", out.toString());
	}
}
