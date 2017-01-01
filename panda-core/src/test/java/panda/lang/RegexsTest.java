package panda.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * test class for Digests
 */
public class RegexsTest {

	@Test
	public void testIsEmail() {
		Assert.assertTrue(Regexs.isEmail("a@a.co"));
		Assert.assertTrue(Regexs.isEmail("a.b.c@a.co"));
		Assert.assertTrue(Regexs.isEmail("a-b-c@a.co"));
		Assert.assertTrue(Regexs.isEmail("a_F@a.co"));

		Assert.assertFalse(Regexs.isEmail("a/_F@a.co"));
	}

	@Test
	public void testIsURL() {
		Assert.assertTrue(Regexs.isURL("http://a.co"));
		Assert.assertTrue(Regexs.isURL("HttpS://a.co.com"));
		Assert.assertTrue(Regexs.isURL("HttpS://a.co.com/x?a&=cb+c%20"));

		Assert.assertFalse(Regexs.isURL("HttpSS://a.co.com/x?a&=cb+c%20"));
	}


	@Test
	public void testIsFileName() {
		Assert.assertTrue(Regexs.isFileName("a.txT"));
		Assert.assertTrue(Regexs.isFileName("_"));

		Assert.assertFalse(Regexs.isFileName("a.tx/t"));
		Assert.assertFalse(Regexs.isFileName("a.tx\\t"));
		Assert.assertFalse(Regexs.isFileName("a.tx*t"));
		Assert.assertFalse(Regexs.isFileName("a.tx?t"));
	}
}
