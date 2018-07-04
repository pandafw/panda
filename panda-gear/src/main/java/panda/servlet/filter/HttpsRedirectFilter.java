package panda.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.net.Scheme;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLBuilder;


public class HttpsRedirectFilter implements Filter {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		String schema = HttpServlets.getScheme(request);
		if (Scheme.HTTP.equals(schema)) {
			ServletURLBuilder sub = new ServletURLBuilder();
			sub.setRequest(request);
			sub.setScheme(Scheme.HTTPS);
			sub.setHost(request.getServerName());
			sub.setParams(request.getParameterMap());
			String url = sub.build();
			
			HttpServletResponse response = (HttpServletResponse)res;
			HttpServlets.sendRedirect(response, url);
		}
		else {
			chain.doFilter(req, res);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
	}
}

