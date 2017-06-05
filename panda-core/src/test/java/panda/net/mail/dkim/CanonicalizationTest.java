package panda.net.mail.dkim;

import org.junit.Assert;
import org.junit.Test;

public class CanonicalizationTest {

	@Test
	public void testSimpleCanonicalization() {
		Assert.assertEquals("name: value   ", Canonicalization.SIMPLE.canonicalizeHeader("name", "value   "));

		String s = "  abc \t\u000b\u000c def\r\n\r\r\n\n";
		String e = "  abc \t\u000b\u000c def\r\n";
		String a = Canonicalization.SIMPLE.canonicalizeBody(s);
		Assert.assertEquals(e, a);
	}

	@Test
	public void testRelaxedCanonicalization() {
		Assert.assertEquals("name: value", Canonicalization.RELAXED.canonicalizeHeader("name", "value   "));

		String s = "  abc \t\u000b\u000c  def   \r\n\r\r\n\n";
		String e = " abc def \r\n";
		String a = Canonicalization.RELAXED.canonicalizeBody(s);
		Assert.assertEquals(e, a);
	}
}
