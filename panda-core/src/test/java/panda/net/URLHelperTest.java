package panda.net;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;

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

	@Test
	public void testParseQueryString() {
		Map<String, Object> qparams = new LinkedHashMap<String, Object>();

		qparams.put("q", Arrays.toList("0", "1"));
		Assert.assertEquals(qparams, URLHelper.parseQueryString("q=0&q=1"));

		qparams.clear();
		qparams.put("q", Arrays.toList(null, "1"));
		Assert.assertEquals(qparams, URLHelper.parseQueryString("q&q=1"));

		qparams.clear();
		qparams.put("a", "-1");
		qparams.put("q", Arrays.toList(null, "1"));
		Assert.assertEquals(qparams, URLHelper.parseQueryString("a=-1&q&q=1"));
	}
}
