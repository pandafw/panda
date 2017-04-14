package panda.net.http;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import panda.net.URLBuilder;

@SuppressWarnings("unchecked")
public class URLBuilderTest {

	private static Map params = new TreeMap();
	
	static {
		params.put("a", "s");
		params.put("b", null);
		params.put("n", 100);
	}

	private String build(String url, Object params) {
		URLBuilder ub = new URLBuilder();
		ub.setPath(url);
		ub.setParams(params);
		return ub.build();
	}

	@Test
	public void testBuildParametersString() throws Exception {
		assertEquals("", build(null, Collections.emptyMap()));
		assertEquals("a=s&n=100", build(null, params));
	}
	
	@Test
	public void testBuildURL() throws Exception {
		assertEquals("test.com?a=s&n=100", build("test.com", params));
		assertEquals("test.com?a=s&n=100", build("test.com?", params));
		assertEquals("test.com?0&a=s&n=100", build("test.com?0", params));
	}
}
