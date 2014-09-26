package panda.mvc.adaptor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import panda.lang.Charsets;
import panda.lang.Strings;
import panda.mvc.AbstractMvcTestCase;
import panda.mvc.adaptor.meta.BaseModule;

public class JsonAdaptorTest extends AbstractMvcTestCase {

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", BaseModule.class.getName());
	}

	private void initreq(String path, String json) {
		request.setPathInfo(path);
		request.setContent(Strings.getBytes(json, Charsets.UTF_8));
	}

	@Test
	public void test_mapobj() throws ServletException, IOException {
		String path = "/json/map/obj";
		String json = "{map:{a:{name:'a'},b:{name:'b'},c:{name:'c'}}}";
		initreq(path, json);
		servlet.service(request, response);
		assertEquals(3, response.getContentAsString());
	}

	@Test
	public void test_array() throws ServletException, IOException {
		String path = "/json/array";
		String json = "[{name:'a'},{name:'b'},{name:'c'}]";
		initreq(path, json);
		servlet.service(request, response);
		assertEquals(3, response.getContentAsString());
	}

	@Test
	public void test_list() throws ServletException, IOException {
		String path = "/json/list";
		String json = "[{name:'a'},{name:'b'},{name:'c'}]";
		initreq(path, json);
		servlet.service(request, response);
		assertEquals(3, response.getContentAsString());
	}

	@Test
	public void test_hello() throws ServletException, IOException {
		String path = "/json/hello";
		String json = "{pet : {name:'测试'}}";
		initreq(path, json);
		servlet.service(request, response);
		assertEquals("\"!!测试!!\"", response.getContentAsString());
	}

	@Test
	public void test_map() throws ServletException, IOException {
		String path = "/json/map";
		String json = "{a:3,b:4,e:5}";
		initreq(path, json);
		servlet.service(request, response);
		assertEquals(3, response.getContentAsString());
	}

}
