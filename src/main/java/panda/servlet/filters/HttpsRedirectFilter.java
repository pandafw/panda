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

import panda.servlet.HttpServletUtils;
import panda.servlet.ServletURLHelper;


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
			String url = ServletURLHelper.buildURL(request, "https", 0, request.getParameterMap(), true, false);
			
			HttpServletResponse response = (HttpServletResponse)res;
			HttpServletUtils.sendRedirect(response, url);
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

