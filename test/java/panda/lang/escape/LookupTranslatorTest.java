package panda.lang.escape;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import panda.lang.escape.LookupTranslator;

/**
 * Unit tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

	@Test
	public void testBasicLookup() throws IOException {
		final LookupTranslator lt = new LookupTranslator(new CharSequence[][] { { "one", "two" } });
		final StringWriter out = new StringWriter();
		final int result = lt.translate("one", 0, out);
		assertEquals("Incorrect codepoint consumption", 3, result);
		assertEquals("Incorrect value", "two", out.toString());
	}

	// Tests: https://issues.apache.org/jira/browse/LANG-882
	@Test
	public void testLang882() throws IOException {
		final LookupTranslator lt = new LookupTranslator(new CharSequence[][] { { new StringBuffer("one"),
				new StringBuffer("two") } });
		final StringWriter out = new StringWriter();
		final int result = lt.translate(new StringBuffer("one"), 0, out);
		assertEquals("Incorrect codepoint consumption", 3, result);
		assertEquals("Incorrect value", "two", out.toString());
	}

}
