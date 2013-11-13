package panda.net.http;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import panda.net.http.URLHelper;

@SuppressWarnings("unchecked")
public class URLHelperTest {

	private static Map params = new TreeMap();
	
	static {
		params.put("a", "s");
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
	public void testGetURLRoot() throws Exception {
		assertEquals("http://www.test.com", URLHelper.getURLRoot("http://www.test.com"));
		assertEquals("http://www.test.com", URLHelper.getURLRoot("http://www.test.com/"));
		assertEquals("http://www.test.com", URLHelper.getURLRoot("http://www.test.com/app"));
		assertEquals(null, URLHelper.getURLRoot(null));
		assertEquals(null, URLHelper.getURLRoot(""));
		assertEquals(null, URLHelper.getURLRoot("/app"));
		assertEquals(null, URLHelper.getURLRoot("app"));
	}

	@Test
	public void testConcatURL() throws Exception {
		assertEquals(null, URLHelper.concatURL(null, "*"));
		assertEquals(null, URLHelper.concatURL("", "*"));
		assertEquals("http://a.b.c", URLHelper.concatURL("http://a.b.c", null));
		assertEquals("http://a.b.c", URLHelper.concatURL("http://a.b.c", ""));
		assertEquals("http://x.y.z", URLHelper.concatURL("http://a.b.c", "http://x.y.z"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c", "/x"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c/", "/x"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c/d", "/x"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c/d/e", "/x"));
		assertEquals(null, URLHelper.concatURL("http://a.b.c", "../x"));
		assertEquals(null, URLHelper.concatURL("http://a.b.c/", "../x"));
		assertEquals(null, URLHelper.concatURL("http://a.b.c/d", "../x"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c/d/", "../x"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c/d/e", "../x"));
		assertEquals("http://a.b.c/d/x", URLHelper.concatURL("http://a.b.c/d/e/", "../x"));
		assertEquals("http://a.b.c/d/x", URLHelper.concatURL("http://a.b.c/d/e/f", "../x"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c", "x"));
		assertEquals("http://a.b.c/x?a=1", URLHelper.concatURL("http://a.b.c", "x?a=1"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c/", "x"));
		assertEquals("http://a.b.c/x", URLHelper.concatURL("http://a.b.c/d", "x"));
		assertEquals("http://a.b.c/x?b=1", URLHelper.concatURL("http://a.b.c/d?a=1", "x?b=1"));
		assertEquals("http://a.b.c/d/x", URLHelper.concatURL("http://a.b.c/d/", "x"));
		assertEquals("http://a.b.c/d/x", URLHelper.concatURL("http://a.b.c/d/e", "x"));
	}

	@Test
	public void testBuildParametersString() throws Exception {
		assertEquals("", URLHelper.buildParametersString(Collections.emptyMap()));
		assertEquals("a=s&n=100", URLHelper.buildParametersString(params));
	}
	
	@Test
	public void testBuildURL() throws Exception {
		assertEquals("test.com?a=s&n=100", URLHelper.buildURL("test.com", params));
		assertEquals("test.com?a=s&n=100", URLHelper.buildURL("test.com?", params));
		assertEquals("test.com?0&a=s&n=100", URLHelper.buildURL("test.com?0", params));
	}
}
