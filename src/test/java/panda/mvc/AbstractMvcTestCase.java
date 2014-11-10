package panda.mvc;

import org.junit.After;
import org.junit.Before;

import panda.mvc.ioc.IocRequestListener;
import panda.mvc.mock.Mock;
import panda.servlet.mock.MockHttpServletRequest;
import panda.servlet.mock.MockHttpServletResponse;
import panda.servlet.mock.MockHttpSession;
import panda.servlet.mock.MockServletConfig;
import panda.servlet.mock.MockServletContext;

public abstract class AbstractMvcTestCase {

	protected MvcServlet servlet;

	protected MockHttpServletRequest request;

	protected MockHttpServletResponse response;

	protected MockHttpSession session;

	protected MockServletContext servletContext;

	protected MockServletConfig servletConfig;
	
	protected IocRequestListener iocRequestListener;

	@Before
	public void init() throws Throwable {
		servletContext = Mock.servletContext();
		servletConfig = Mock.servletConfig(servletContext, "test");
		iocRequestListener = new IocRequestListener();
		
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
		if (servlet != null) {
			servlet.destroy();
		}
	}

}
