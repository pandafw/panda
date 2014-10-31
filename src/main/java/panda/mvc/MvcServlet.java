package panda.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.mvc.config.ServletMvcConfig;

/**
 * 挂接到 JSP/Servlet 容器的入口
 * 
 */
public class MvcServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected ActionHandler handler;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		ServletMvcConfig config = new ServletMvcConfig(servletConfig);
		handler = new ActionHandler(config);
	}

	public void destroy() {
		if (handler != null) {
			handler.depose();
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!handler.handle(req, resp)) {
			resp.sendError(404);
		}
	}

	/**
	 * @return the handler
	 */
	public ActionHandler getHandler() {
		return handler;
	}
}
