package panda.mvc.testapp.views;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import panda.mvc.testapp.BaseWebappTest;

public class ForwardViewTest extends BaseWebappTest {

	@Test
	public void test_simple() throws IOException {
		get("/views/for?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());

		get("/views/for2?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());

		get("/views/for3?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());
	}
}
