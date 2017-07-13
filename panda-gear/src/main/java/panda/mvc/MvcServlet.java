package panda.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.config.ServletMvcConfig;
import panda.net.http.HttpStatus;

/**
 * Servlet Entry Point
 */
public class MvcServlet extends HttpServlet {
	private static final Log log = Logs.getLog(MvcServlet.class);

	private static final long serialVersionUID = 1L;

	protected ActionHandler handler;

	@Override
	public void init(ServletConfig conf) throws ServletException {
		log.infof("MvcServlet[%s] starting ...", conf.getServletName());

		handler = (ActionHandler)conf.getServletContext().getAttribute(ActionHandler.class.getName());
		if (handler == null) {
			ServletMvcConfig config = new ServletMvcConfig(conf);
			handler = new ActionHandler(config);
			Mvcs.setActionHandler(conf.getServletContext(), handler);
		}
	}

	public void destroy() {
		if (handler != null) {
			handler.depose();
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (!handler.handle(req, resp)) {
			resp.sendError(HttpStatus.SC_NOT_FOUND);
		}
	}

	/**
	 * @return the handler
	 */
	public ActionHandler getHandler() {
		return handler;
	}
}
