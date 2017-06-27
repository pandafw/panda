package panda.el;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ElTemplateTest {
	@Test
	public void testPrefix() {
		assertEquals("abc", ElTemplate.evaluate("a${'bc'}"));
		assertEquals("abc", ElTemplate.evaluate("a%{'bc'}"));
	}

	@Test
	public void testNoClose() {
		assertEquals("a${'bc'", ElTemplate.evaluate("a${'bc'"));
		assertEquals("a%{'bc'", ElTemplate.evaluate("a%{'bc'"));
	}

	@Test
	public void testNoPrefix() {
		assertEquals("a$bc", new ElTemplate("a${'bc'}", new char[0]).evaluate());
	}
}
