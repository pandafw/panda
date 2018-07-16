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

import panda.lang.Exceptions;
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

	public static class ChainFilter implements ServletFilter {
		private FilterChain chain;

		public ChainFilter(FilterChain chain) {
			this.chain = chain;
		}

		@Override
		public boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc) {
			try {
				chain.doFilter(req, res);
				return true;
			}
			catch (Throwable e) {
				throw Exceptions.wrapThrow(e);
			}
		}
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		handler.handle((HttpServletRequest)req, (HttpServletResponse)res, new ChainFilter(chain));
	}
}
