package panda.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import panda.log.Log;
import panda.log.Logs;
import panda.servlet.HttpServlets;
import panda.servlet.ServletURLHelper;


/**
 * SessionRecoverFilter
 * 
 * <pre>
 * Set cookie [CSESSIONID=JSESSIONID; path=request.getContextPath(); max-age=session.getMaxInactiveInterval()].
 * if request.getSession(false) is null and cookie.JSESSION is null and cookie.CSESSIONID exists,
 * set response cookie [JSESSIONID=CSESSION], 
 * and redirect to the request URL with query string build by request.parameters. 
 * </pre>
 * 
 * @author yf.frank.wang@gmail.com
 */
public class SessionRecoveryFilter implements Filter {
	/**
	 * log
	 */
	protected static Log log = Logs.getLog(SessionRecoveryFilter.class);

	/**
	 * JSESSIONID = "JSESSIONID";
	 */
	public final static String JSESSIONID = "JSESSIONID";
	
	/**
	 * CSESSIONID = "CSESSIONID";
	 */
	public final static String CSESSIONID = "CSESSIONID";
	
	/**
	 * Constructor.
	 */
	public SessionRecoveryFilter() {
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req; 
		HttpServletResponse response = (HttpServletResponse)res; 

		HttpSession session = request.getSession(false);

		if (log.isDebugEnabled()) {
			log.debug("[" + (session == null ? "null" : session.getId()) + "] " + request.getRequestURI());
		}
		
		String csessionId = null;
		String jsessionId = null;
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equalsIgnoreCase(CSESSIONID)) {
					csessionId = c.getValue();
				}
				else if (c.getName().equalsIgnoreCase(JSESSIONID)) {
					jsessionId = c.getValue();
				}
			}
		}

		if (session == null) {
			if (csessionId != null && !csessionId.equals(jsessionId)) {
				Cookie c = new Cookie(JSESSIONID, csessionId);
				c.setPath("/");
				response.addCookie(c);

				String url = ServletURLHelper.buildURL(request);
				if (log.isDebugEnabled()) {
					log.debug("[" + JSESSIONID + "=" + c.getValue() + "] redirect: " + url);
				}
				HttpServlets.sendRedirect(response, url);

				return;
			}
		}
		else {
			if (jsessionId != null && !jsessionId.equals(csessionId)) {
				Cookie c = new Cookie(CSESSIONID, jsessionId);
				c.setPath(request.getContextPath());
				c.setMaxAge(session.getMaxInactiveInterval());

				if (log.isDebugEnabled()) {
					log.debug("Set cookie - [" + CSESSIONID + "=" + c.getValue() + "; path=" + c.getPath() + "; max-age=" + c.getMaxAge() + ";]");
				}
				response.addCookie(c);
			}
		}
		
		chain.doFilter(req, res);
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

}
