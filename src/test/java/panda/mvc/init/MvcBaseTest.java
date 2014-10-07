package panda.mvc.init;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;
import panda.mvc.RequestPath;
import panda.mvc.init.module.MainModule;

public class MvcBaseTest extends AbstractMvcTestCase {

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", MainModule.class.getName());
	}

	@Test
	public void testAnotherModule() throws Throwable {
		request.setPathInfo("/two/abc");
		servlet.service(request, response);
		assertEquals("haha", response.getContentAsString());
	}

	@Test
	public void testPointPath() throws Throwable {
		request.setPathInfo("/1.2/say.do");
		RequestPath path = RequestPath.getRequestPathObject(request);
		assertNotNull(path);
		assertEquals("/1.2/say", path.getPath());
		assertEquals("do", path.getSuffix());

		request.setPathInfo("/1.2/say");
		path = RequestPath.getRequestPathObject(request);
		assertNotNull(path);
		assertEquals("/1.2/say", path.getPath());

		request.setPathInfo("/1.2/say.po/");
		path = RequestPath.getRequestPathObject(request);
		assertNotNull(path);
		assertEquals("/1.2/say.po/", path.getPath());

		request.setPathInfo("/1.2/say.po/.nut");
		path = RequestPath.getRequestPathObject(request);
		assertNotNull(path);
		assertEquals("/1.2/say.po/", path.getPath());
	}

	@Test
	public void testRequestParms_error() throws Throwable {
		request.setPathInfo("/two/login.do");
		request.addParameter("username", "wendal");
		request.addParameter("password", "123456");
		request.addParameter("authCode", "dummy");
		servlet.service(request, response);
		String resp = response.getContentAsString();
		System.out.println(resp);
		assertTrue(resp.indexOf("NumberFormatException") > -1);
	}

	@Test
	public void testRequestParms() throws Throwable {
		request.setPathInfo("/two/login.do");
		request.addParameter("username", "wendal");
		request.addParameter("password", "123456");
		request.addParameter("authCode", "236475");
		servlet.service(request, response);
		assertEquals("true", response.getContentAsString());
	}

	@Test
	public void test_CheckSession() throws Throwable {
		request.setPathInfo("/two/need.do");
		servlet.service(request, response);
		assertEquals("/two/abc", response.getHeader("Location"));
	}

	@Test
	public void test_NotPublicClass() throws Throwable {
		request.setPathInfo("/here");
		servlet.service(request, response);
		assertEquals("", response.getContentAsString());
	}

	@Test
	public void test_PathMeOne() throws Throwable {
		request.setPathInfo("/two/pathme/123.do");
		servlet.service(request, response);
		assertEquals("123+0", response.getContentAsString());
	}

	@Test
	public void test_PathMeTwo() throws Throwable {
		request.setPathInfo("/two/pathme/123/456.do");
		servlet.service(request, response);
		assertEquals("123+456", response.getContentAsString());
	}

	@Test
	public void test_PathTwoTwo() throws Throwable {
		request.setPathInfo("/two/pathtwo/123/456.do");
		servlet.service(request, response);
		assertEquals("123+456", response.getContentAsString());
	}
}
