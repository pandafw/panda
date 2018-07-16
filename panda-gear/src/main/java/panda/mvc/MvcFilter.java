package panda.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.log.Log;
import panda.log.Logs;

/**
 * Filter Entry Point
 */
public class MvcFilter implements Filter {

	private static final Log log = Logs.getLog(MvcFilter.class);

	protected MvcHandler handler;

	public void init(FilterConfig conf) throws ServletException {
		log.infof("MvcFilter[%s] starting ...", conf.getFilterName());

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

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		if (handler.handle(request, response)) {
			return;
		}

		chain.doFilter(req, res);
	}
}
