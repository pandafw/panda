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
import panda.lang.Randoms;
import panda.net.ssl.SSLProtocols;

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
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("class", new String[] { "a", "b"} );
		params.put("version", Panda.VERSION);
		String response = HttpClient.post("https://panda-demo.appspot.com/test/json", params).getContentText();
		
		assertNotNull(response);
		assertTrue(response.length() > 0);
		
		JsonObject jo = JsonObject.fromJson(response);
		JsonObject ps = jo.getJsonObject("params");
		assertEquals(Jsons.toJson(params.get("class")), Jsons.toJson(ps.get("class")));
		assertEquals(Jsons.toJson(params.get("version")), Jsons.toJson(ps.get("version")));
	}

	@Test
	public void testPostByGzip() throws Exception {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("class", HttpClientTest.class.getName());
		params.put("random", Randoms.randString(2048));

		HttpClient hc = new HttpClient();
		hc.getRequest().setDefault();
		hc.getRequest().setContentEncoding(HttpHeader.CONTENT_ENCODING_GZIP);
		hc.getRequest().setParams(params);
		hc.getRequest().setUrl("https://panda-demo.azurewebsites.net/test/json");
//		hc.getRequest().setUrl("https://panda-demo.appspot.com/test/json");
//		hc.getRequest().setUrl("https://localhost/test/json");
		String response = hc.doPost().getContentText();
		
		assertNotNull(response);
		assertTrue(response.length() > 0);
		
		JsonObject jo = JsonObject.fromJson(response);
		JsonObject ps = jo.getJsonObject("params");
		assertEquals(Jsons.toJson(params.get("class")), Jsons.toJson(ps.get("class")));
		assertEquals(Jsons.toJson(params.get("random")), Jsons.toJson(ps.get("random")));
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
		HttpClient hc = new HttpClient();
		hc.setSslProtocols(SSLProtocols.TLSv1_2);
		hc.getRequest().setUrl("https://github.com");
		HttpResponse response = hc.doGet();
		assertTrue(response.getStatusCode() == 200);
		assertTrue(response.getContentText().indexOf("github.com") >= 0);
	}

	@Test
	public void testGetHttpsTrust() throws Exception {
		HttpClient hc = new HttpClient();
		hc.setSslProtocols(SSLProtocols.TLSv1_2);
		hc.setSslHostnameCheck(false);
		hc.getRequest().setUrl("https://xxx.orange1.net");
		HttpResponse response = hc.doGet();

		assertTrue(response.getStatusCode() == 200);
	}
}
