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


/**
 * SingleSessionFilter
 * 
 * <pre>
 * Set session.getMaxInactiveInterval() to the MaxAge of JSESSIONID cookie.
 * Because the cookie path of JSESSIONID is "/", all applications under this Web Server will 
 * use the same JSESSIONID.
 * </pre>
 * 
 * @author yf.frank.wang@gmail.com
 */
public class SingleSessionFilter implements Filter {
	/**
	 * log
	 */
	protected static Log log = Logs.getLog(SingleSessionFilter.class);

	/**
	 * JSESSIONID = "JSESSIONID";
	 */
	public final static String JSESSIONID = "JSESSIONID";
	
	/**
	 * Constructor.
	 */
	public SingleSessionFilter() {
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
			log.debug("filter - " + request.getRequestURI() + " [" + (session == null ? "null" : session.getId()) + "]");
		}
		
		if (session != null) {
			Cookie sc = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if (c.getName().equalsIgnoreCase(JSESSIONID)) {
						sc = c;
						break;
					}
				}
			}

			if (sc != null) {
				if (sc.getMaxAge() < session.getMaxInactiveInterval()) {
					Cookie c = new Cookie(JSESSIONID, sc.getValue());
					c.setPath("/");
					c.setMaxAge(session.getMaxInactiveInterval());
					response.addCookie(c);
	
					if (log.isDebugEnabled()) {
						log.debug("Set cookie - [" + JSESSIONID + "=" + c.getValue() + "; path=" + c.getPath() + "; expires=" + c.getMaxAge() + ";]");
					}
				}
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
