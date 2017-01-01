package panda.servlet.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.lang.Arrays;
import panda.lang.Strings;

/**
 */
public class WelcomeRedirectFilter implements Filter {
	private String welcomeURL;
	
	private boolean extension = true;
	
	private Set<String> excepts = new HashSet<String>();

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		welcomeURL = config.getInitParameter("welcomeURL");
		extension = !"false".equals(config.getInitParameter("extension"));
		String[] excepts = Strings.split(config.getInitParameter("excepts"));
		if (excepts == null) {
			excepts = new String[] { "/_ah/start" };
		}
		this.excepts.addAll(Arrays.asList(excepts));
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		String redirect = null;
		
		String uri = request.getRequestURI();
		if (!excepts.contains(uri)) {
			if (uri.endsWith("/")) {
				redirect = welcomeURL;
			}
			else if (extension) {
				String page = uri;
				int i = uri.lastIndexOf('/');
				if (i >= 0) {
					page = uri.substring(i + 1);
				}
				if (!Strings.contains(page, '.')) {
					redirect = uri + '/' + welcomeURL;
				}
			}
		}
		
		if (Strings.isNotEmpty(redirect)) {
			response.sendRedirect(redirect);
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
