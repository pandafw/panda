package panda.net;

import org.junit.Assert;
import org.junit.Test;

public class URLHelperTest {
	@Test
	public void testResolveURL() {
		Assert.assertEquals("http://a.b.c/", URLHelper.resolveURL("http://a.b.c/d", "./"));
		Assert.assertEquals("http://a.b.c/", URLHelper.resolveURL("http://a.b.c/d", "."));
		Assert.assertEquals("http://a.b.c/d/", URLHelper.resolveURL("http://a.b.c/d/", "./"));
		Assert.assertEquals("http://a.b.c/d/", URLHelper.resolveURL("http://a.b.c/d/", "."));
		Assert.assertEquals("http://a.b.c/e", URLHelper.resolveURL("http://a.b.c/d", "./e"));
		Assert.assertEquals("http://a.b.c/e", URLHelper.resolveURL("http://a.b.c/d", "e"));
		Assert.assertEquals("http://a.b.c/", URLHelper.resolveURL("http://a.b.c/d/e", "../"));
		Assert.assertEquals("http://a.b.c/f", URLHelper.resolveURL("http://a.b.c/d/e", "../f"));
	}
}
