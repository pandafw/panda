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

import panda.servlet.HttpServlets;
import panda.servlet.ServletURLBuilder;


/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpsRedirectFilter implements Filter {
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		String schema = request.getScheme();
		if (!"https".equals(schema)) {
			ServletURLBuilder ub = new ServletURLBuilder();
			ub.setScheme("https");
			ub.setParams(request.getParameterMap());
			ub.setForceAddSchemeHostAndPort(true);
			String url = ub.toString();
			
			HttpServletResponse response = (HttpServletResponse)res;
			HttpServlets.sendRedirect(response, url);
		}
		else {
			chain.doFilter(req, res);
		}
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}

