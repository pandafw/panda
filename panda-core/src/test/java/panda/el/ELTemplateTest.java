package panda.el;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ELTemplateTest {
	@Test
	public void testPrefix() {
		assertEquals("abc", ELTemplate.evaluate("a${'bc'}"));
		assertEquals("abc", ELTemplate.evaluate("a%{'bc'}"));
	}

	@Test
	public void testNoClose() {
		assertEquals("a${'bc'", ELTemplate.evaluate("a${'bc'"));
		assertEquals("a%{'bc'", ELTemplate.evaluate("a%{'bc'"));
	}

	@Test
	public void testNoPrefix() {
		assertEquals("a$bc", new ELTemplate("a${'bc'}", new char[0]).evaluate());
	}
}
