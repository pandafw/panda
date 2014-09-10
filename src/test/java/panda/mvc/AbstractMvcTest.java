package panda.mvc;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import panda.mock.web.MockHttpServletRequest;
import panda.mock.web.MockHttpServletResponse;
import panda.mock.web.MockHttpSession;
import panda.mock.web.MockServletConfig;
import panda.mock.web.MockServletContext;
import panda.mvc.mock.Mock;

@Ignore
public abstract class AbstractMvcTest {

	protected MvcServlet servlet;

	protected MockHttpServletRequest request;

	protected MockHttpServletResponse response;

	protected MockHttpSession session;

	protected MockServletContext servletContext;

	protected MockServletConfig servletConfig;

	@Before
	public void init() throws Throwable {
		servletContext = Mock.servlet.context();
		servletConfig = new MockServletConfig(servletContext, "test");
		initServletConfig();
		servlet = new MvcServlet();
		servlet.init(servletConfig);

		session = Mock.servlet.session(servletContext);
		newreq();
	}

	protected void newreq() {
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		request.setContextPath("");
		request.setSession(session);
		response = new MockHttpServletResponse();
	}

	protected abstract void initServletConfig();

	@After
	public void destroy() {
		if (servlet != null)
			servlet.destroy();
	}

}
