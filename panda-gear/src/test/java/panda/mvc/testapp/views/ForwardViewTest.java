package panda.mvc.testapp.views;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import panda.mvc.testapp.BaseWebappTest;

public class ForwardViewTest extends BaseWebappTest {

	@Test
	public void test_for1() throws IOException {
		get("/views/for1?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());
	}

	@Test
	public void test_for2() throws IOException {
		get("/views/for2?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());
	}

	@Test
	public void test_for3() throws IOException {
		get("/views/for3?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());
	}

	@Test
	public void test_for4() throws IOException {
		String exp = "/views/for.4";
		get(exp + "?to=base");
		assertEquals(200, resp.getStatusCode());
		assertEquals(exp, resp.getContentText());
	}
}
