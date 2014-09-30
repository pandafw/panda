package panda.mvc.testapp.views;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import panda.mvc.testapp.BaseWebappTest;

public class ServerRedirectViewTest extends BaseWebappTest {

	@Test
	public void test_simple() throws IOException {
		get("/views/red?to=redbase");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());

		get("/views/red2?to=redbase");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());

		get("/views/red3?to=redbase");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());
	}
}
