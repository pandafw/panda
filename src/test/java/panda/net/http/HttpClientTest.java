package panda.net.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import panda.Panda;
import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.net.http.HttpClient;
import panda.net.http.HttpResponse;

public class HttpClientTest {

	@Test
	public void testGet() throws Exception {
		HttpResponse response = HttpClient.get("http://www.msn.com/");
		assertNotNull(response);
		assertNotNull(response.getContent());
		assertNotNull(response.getStatusReason());
		assertNotNull(response.getHeader());
		assertNotNull(response.getProtocol());
		assertEquals(response.getStatusCode(), 200);
		assertNotNull(response.getStream());
	}

	@Test
	public void testEmptyHeadValue() throws Exception {
		HttpClient hc = new HttpClient();
		hc.getRequest().getHeader().set("X-Empty", "");
		hc.getRequest().setUrl("http://www.msn.com/");
		HttpResponse response = hc.send();

		assertNotNull(response);
		assertEquals(response.getStatusCode(), 200);
		assertNotNull(response.getContent());
	}

	@Test
	public void testPost() throws Exception {
		Map<String, Object> parms = new LinkedHashMap<String, Object>();
		parms.put("class", new Object[] { HttpClientTest.class.getName() });
		parms.put("version", new Object[] { Panda.VERSION });
		String response = HttpClient.post("http://pdemo.foolite.com/debug/json", parms).getContentText();
		
		assertNotNull(response);
		assertTrue(response.length() > 0);
		
		JsonObject jo = JsonObject.fromJson(response);
		assertEquals(Jsons.toJson(parms), jo.getJsonObject("params").toString());
	}

	@Test
	public void testEncode() throws Exception {
		// detect charset
		HttpResponse response = HttpClient.get("www.baidu.com");
		assertEquals("utf-8", response.getContentCharset().toLowerCase());
		assertTrue(response.getContentText().indexOf("百度") > 0);

		// supply the charset
		response = HttpClient.get("www.exam8.com/SiteMap/Article1.htm");
		assertTrue(response.getContentText("GBK").indexOf("考试吧") > 0);
	}

	@Test(expected = IOException.class)
	public void testTimeout() throws Exception {
		HttpResponse response = HttpClient.get("www.baidu.com", 10 * 1000);
		assertTrue(response.getStatusCode() == 200);

		// timeout exception
		HttpClient.get("www.baidu.com", 1);
	}

	@Test
	public void testGetHttps() throws Exception {
		HttpResponse response = HttpClient.get("https://github.com");
		assertTrue(response.getStatusCode() == 200);
		assertTrue(response.getContentText().indexOf("github.com") >= 0);
	}
}
