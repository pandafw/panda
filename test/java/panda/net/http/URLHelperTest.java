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
