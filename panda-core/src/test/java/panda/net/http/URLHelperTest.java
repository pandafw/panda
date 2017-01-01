package panda.net.http;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import panda.net.URLHelper;

@SuppressWarnings("unchecked")
public class URLHelperTest {

	private static Map params = new TreeMap();
	
	static {
		params.put("a", "s");
		params.put("b", null);
		params.put("n", 100);
	}

	@Test
	public void testGetURLRootLength() throws Exception {
		assertEquals("http://www.test.com".length(), URLHelper.getURLRootLength("http://www.test.com"));
		assertEquals("http://www.test.com".length(), URLHelper.getURLRootLength("http://www.test.com/"));
		assertEquals("http://www.test.com".length(), URLHelper.getURLRootLength("http://www.test.com/app"));
		assertEquals(-1, URLHelper.getURLRootLength(null));
		assertEquals(-1, URLHelper.getURLRootLength(""));
		assertEquals(-1, URLHelper.getURLRootLength("/app"));
		assertEquals(-1, URLHelper.getURLRootLength("app"));
	}


	@Test
	public void testGetURLDomain() throws Exception {
		assertEquals("www.test.com", URLHelper.getURLDomain("http://www.test.com"));
		assertEquals("www.test.com", URLHelper.getURLDomain("http://www.test.com/"));
		assertEquals("www.test.com", URLHelper.getURLDomain("http://www.test.com/app"));
		assertEquals(null, URLHelper.getURLDomain(null));
		assertEquals("", URLHelper.getURLDomain(""));
		assertEquals("", URLHelper.getURLDomain("/app"));
		assertEquals("app", URLHelper.getURLDomain("app"));
	}

	@Test
	public void testGetURLRoot() throws Exception {
		assertEquals("http://www.test.com", URLHelper.getURLRoot("http://www.test.com"));
		assertEquals("http://www.test.com", URLHelper.getURLRoot("http://www.test.com/"));
		assertEquals("http://www.test.com", URLHelper.getURLRoot("http://www.test.com/app"));
		assertEquals(null, URLHelper.getURLDomain(null));
		assertEquals("", URLHelper.getURLDomain(""));
		assertEquals("", URLHelper.getURLDomain("/app"));
		assertEquals("app", URLHelper.getURLDomain("app"));
	}

	@Test
	public void testNormalize() throws Exception {
		assertEquals("http://a.b.c/a/b", URLHelper.normalize("http://a.b.c//a///b"));
	}
	
	@Test
	public void testResolveURL() throws Exception {
		assertEquals("*", URLHelper.resolveURL(null, "*"));
		assertEquals("*", URLHelper.resolveURL("", "*"));
		assertEquals("http://a.b.c", URLHelper.resolveURL("http://a.b.c", null));
		assertEquals("http://a.b.c", URLHelper.resolveURL("http://a.b.c", ""));
		assertEquals("http://x.y.z", URLHelper.resolveURL("http://a.b.c", "http://x.y.z"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c", "/x"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c/", "/x"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c/d", "/x"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c/d/e", "/x"));
		assertEquals(null, URLHelper.resolveURL("http://a.b.c", "../x"));
		assertEquals(null, URLHelper.resolveURL("http://a.b.c/", "../x"));
		assertEquals(null, URLHelper.resolveURL("http://a.b.c/d", "../x"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c/d/", "../x"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c/d/e", "../x"));
		assertEquals("http://a.b.c/d/x", URLHelper.resolveURL("http://a.b.c/d/e/", "../x"));
		assertEquals("http://a.b.c/d/x", URLHelper.resolveURL("http://a.b.c/d/e/f", "../x"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c", "x"));
		assertEquals("http://a.b.c/x?a=1", URLHelper.resolveURL("http://a.b.c", "x?a=1"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c/", "x"));
		assertEquals("http://a.b.c/x", URLHelper.resolveURL("http://a.b.c/d", "x"));
		assertEquals("http://a.b.c/x?b=1", URLHelper.resolveURL("http://a.b.c/d?a=1", "x?b=1"));
		assertEquals("http://a.b.c/d/x", URLHelper.resolveURL("http://a.b.c/d/", "x"));
		assertEquals("http://a.b.c/d/x", URLHelper.resolveURL("http://a.b.c/d/e", "x"));

		assertEquals("/d/x", URLHelper.resolveURL("/d/e", "x"));
		assertEquals("/x", URLHelper.resolveURL("/d/e", "../x"));
	}
}
