package panda.mvc.init;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;
import panda.mvc.init.module.MainModule;

public class MvcBaseTest extends AbstractMvcTestCase {

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", MainModule.class.getName());
	}

	@Test
	public void testAnotherModule() throws Throwable {
		request.setRequestURI("/two/abc");
		servlet.service(request, response);
		assertEquals("haha", response.getContentAsString());
	}

	@Test
	public void testRequestParms() throws Throwable {
		request.setRequestURI("/two/login");
		request.addParameter("username", "wendal");
		request.addParameter("password", "123456");
		request.addParameter("authCode", "236475");
		servlet.service(request, response);
		assertEquals("true", response.getContentAsString());
	}

	@Test
	public void test_NotPublicClass() throws Throwable {
		request.setRequestURI("/here");
		servlet.service(request, response);
		assertEquals("", response.getContentAsString());
	}

	@Test
	public void test_PathMeOne() throws Throwable {
		request.setRequestURI("/two/pathme/123");
		servlet.service(request, response);
		assertEquals("123", response.getContentAsString());
	}

	@Test
	public void test_PathMeTwo() throws Throwable {
		request.setRequestURI("/two/pathme/123/456");
		servlet.service(request, response);
		assertEquals("123/456", response.getContentAsString());
	}

	@Test
	public void test_PathTwoTwo() throws Throwable {
		request.setRequestURI("/two/pathtwo/123/456");
		servlet.service(request, response);
		assertEquals("123+456", response.getContentAsString());
	}
}
