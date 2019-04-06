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
		request.setRequestURI("/register");
		servlet.service(request, response);
		assertEquals("/jsp/user/information.do?id=373", response.getHeader("Location"));
	}

	@Test
	public void testP_in_path() throws Throwable {
		request.setRequestURI("/login");
		request.addParameter("name", "panda");
		request.addParameter("password", "123456");
		servlet.service(request, response);
		assertEquals("/jsp/user/panda", response.getHeader("Location"));
	}
}
