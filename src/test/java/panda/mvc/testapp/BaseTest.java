package panda.mvc.testapp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import panda.net.http.HttpContentType;

public class BaseTest extends BaseWebappTest {

	@Test
	public void test_json_adaptor() throws IOException {
		post("/adaptor/json/pet/array", "{pets:[{name:'zzh'},{name:'wendal'}]}", HttpContentType.APP_JSON);
		assertEquals("pets(2) array", resp.getContentText());

		post("/adaptor/json/pet/list", "{pets:[{name:'zzh'},{name:'wendal'}]}", HttpContentType.APP_JSON);
		assertEquals("pets(2) list", resp.getContentText());
	}

	@Test
	public void test_base() throws IOException {
		get("/base.jsp");
		assertNotNull(resp);
		assertEquals(200, resp.getStatusCode());
		assertEquals(getContextPath(), resp.getContentText());
	}

	@Test
	public void test_pathargs() throws IOException {
		get("/common/pathArgs/Wendal");
		assertEquals("Wendal", resp.getContentText());

		get("/common/pathArgs2/Wendal/12345/123456789/123/123.00/200.9/true/n");
		assertEquals("Wendal12345123456789123123200truen", resp.getContentText());

		get("/common/pathArgs3/public/blog/200");
		assertEquals("public&200", resp.getContentText());
		get("/common/pathArgs32/puZ");
		assertEquals("puZ&Z", resp.getContentText());

		get("/common/pathArgs4/zzz?name=wendal");
		assertEquals("zzz&wendal", resp.getContentText());

		get("/common/pathArgs5/yyy?user.name=wendal&user2.name=zozoh");
		assertEquals("yyy&wendal&zozoh", resp.getContentText());
	}

	@Test
	public void test_param() throws IOException {
		get("/common/param?id=" + Long.MAX_VALUE);
		assertEquals("" + Long.MAX_VALUE, resp.getContentText());
	}

	@Test
	public void test_req_param() throws IOException {
		get("/common/path?key=base");
		assertEquals(getContextPath(), resp.getContentText());
	}

	// With EL
	@Test
	public void test_req_param2() throws IOException {
		get("/common/path2?key=base");
		assertEquals("base", resp.getContentText());
		get("/common/path2?key=T");
		assertEquals(getContextPath(), resp.getContentText());
	}

	@Test
	public void test_servlet_obj() {
		get("/common/servlet_obj");
		assertEquals(200, resp.getStatusCode());
	}
}
