package panda.mvc.testapp.views;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import panda.mvc.testapp.BaseWebappTest;
import panda.net.http.HttpHeader;

public class ServerRedirectViewTest extends BaseWebappTest {

	@Test
	public void test_simple() throws IOException {
		get("/views/red?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());

		get("/views/red2?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());

		get("/views/red3?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());
	}

	@Test
	public void test_toslash() throws IOException {
		get("/views", false);
		assertEquals(302, resp.getStatusCode());
		assertEquals("/mvctest/views/", resp.getHeader().getString(HttpHeader.LOCATION));
	}
}
