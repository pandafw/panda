package panda.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpStatus;

/**
 * Servlet Entry Point
 */
public class MvcServlet extends HttpServlet {
	private static final Log log = Logs.getLog(MvcServlet.class);

	private static final long serialVersionUID = 1L;

	protected MvcHandler handler;

	@Override
	public void init(ServletConfig conf) throws ServletException {
		log.infof("MvcServlet[%s] starting ...", conf.getServletName());

		handler = (MvcHandler)conf.getServletContext().getAttribute(MvcHandler.class.getName());
		if (handler == null) {
			MvcConfig mcfg = new MvcConfig(conf.getServletContext(), conf.getInitParameter("modules"));
			MvcLoader loader = new MvcLoader(mcfg);
			handler = loader.getActionHandler();
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
	public MvcHandler getHandler() {
		return handler;
	}
}
