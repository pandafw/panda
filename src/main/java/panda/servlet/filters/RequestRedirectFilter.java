package panda.servlet.filters;

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
import panda.servlet.ServletURLHelper;


/**
 * @author yf.frank.wang@gmail.com
 */
public class RequestRedirectFilter implements Filter {
	/**
	 * redirect
	 */
	private String redirect;

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		redirect = config.getInitParameter("redirect");
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		String url = ServletURLHelper.buildURL(request);
		
		HttpServlets.sendRedirect(response, redirect + url);
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}

