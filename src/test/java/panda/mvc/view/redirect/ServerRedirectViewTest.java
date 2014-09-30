package panda.mvc.view.redirect;

import static org.junit.Assert.*;

import org.junit.Test;

import panda.mvc.AbstractMvcTestCase;

public class ServerRedirectViewTest extends AbstractMvcTestCase {

	@Override
	protected void initServletConfig() {
		servletConfig.addInitParameter("modules", MainModule.class.getName());
	}

	@Test
	public void testRender() throws Throwable {
		request.setPathInfo("/register.do");
		servlet.service(request, response);
		System.out.println(response.getHeader("Location"));
		assertTrue(response.getHeader("Location").endsWith("/jsp/user/information.do?id=373"));
	}

	@Test
	public void testP_in_path() throws Throwable {
		request.setPathInfo("/login.do");
		request.addParameter("name", "wendal");
		request.addParameter("password", "123456");
		servlet.service(request, response);
		System.out.println(response.getHeader("Location"));
		assertTrue(response.getHeader("Location").endsWith("/jsp/user/wendal"));
	}
}
