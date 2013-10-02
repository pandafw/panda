package panda.servlet;

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
 * RequestLoggingFilter
 * 
 * @author yf.frank.wang@gmail.com
 */
public class RequestLoggingFilter implements Filter {
	private static Log log = Logs.getLog(RequestLoggingFilter.class);

	/**
	 * REQUEST_TIME = "panda.servlet.request.time";
	 */
	public static final String REQUEST_TIME = "panda.servlet.request.time";

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;

		request.setAttribute(REQUEST_TIME, System.currentTimeMillis());

		try {
			if (log.isTraceEnabled()) {
				log.trace(HttpServletUtils.dumpRequestTrace(request));
			}
			else if (log.isDebugEnabled()) {
				log.debug(HttpServletUtils.dumpRequestDebug(request));
			}
			else if (log.isInfoEnabled()) {
				log.info(HttpServletUtils.dumpRequestInfo(request));
			}
		}
		catch (Throwable e) {
			//pass
		}
		
		try {
			chain.doFilter(req, res);
		}
		catch (Throwable e) {
			HttpServletUtils.logException(e, request);
			((HttpServletResponse)res).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		finally {
			try {
				if (log.isTraceEnabled()) {
					log.trace(request.getRemoteAddr() + " -> "
							+ request.getRequestURI() + " - END");
				}
			}
			catch (Throwable e) {
				//pass
			}
		}
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}

