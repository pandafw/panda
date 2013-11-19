package panda.doc.markdown;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.doc.markdown.Processor;

public class LineConventionTest {
	private static final String EXPECTED = "<p>a\nb\nc</p>\n";

	@Test
	public void testUnixLineConventions() {
		assertEquals(EXPECTED, Processor.process("a\nb\nc\n"));
	}

	@Test
	public void testWindowsLineConventions() {
		assertEquals(EXPECTED, Processor.process("a\r\nb\r\nc\r\n"));
	}

	@Test
	public void testMacLineConventions() {
		assertEquals(EXPECTED, Processor.process("a\rb\rc\r"));
	}
}
