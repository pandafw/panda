package panda.mvc;

import org.junit.After;
import org.junit.Before;

import panda.mock.web.MockHttpServletRequest;
import panda.mock.web.MockHttpServletResponse;
import panda.mock.web.MockHttpSession;
import panda.mock.web.MockServletConfig;
import panda.mock.web.MockServletContext;
import panda.mvc.mock.Mock;

public abstract class AbstractMvcTestCase {

	protected MvcServlet servlet;

	protected MockHttpServletRequest request;

	protected MockHttpServletResponse response;

	protected MockHttpSession session;

	protected MockServletContext servletContext;

	protected MockServletConfig servletConfig;

	@Before
	public void init() throws Throwable {
		servletContext = Mock.servletContext();
		servletConfig = Mock.servletConfig(servletContext, "test");
		initServletConfig();
		servlet = new MvcServlet();
		servlet.init(servletConfig);

		session = Mock.servletSession(servletContext);
		newreq();
	}

	protected void newreq() {
		request = Mock.servletRequest(servletContext);
		request.setMethod("GET");
		request.setContextPath("");
		request.setSession(session);
		response = Mock.servletResponse();
	}

	protected abstract void initServletConfig();

	@After
	public void destroy() {
		if (servlet != null)
			servlet.destroy();
	}

}
