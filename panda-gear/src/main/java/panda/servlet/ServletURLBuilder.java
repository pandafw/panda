package panda.servlet;

import javax.servlet.http.HttpServletRequest;

import panda.lang.Strings;
import panda.net.URLBuilder;


/**
 * Servlet URL Builder
 */
public class ServletURLBuilder extends URLBuilder {
	protected HttpServletRequest request;
	protected boolean forceAddSchemeHostAndPort;
	
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the forceAddSchemeHostAndPort
	 */
	public boolean isForceAddSchemeHostAndPort() {
		return forceAddSchemeHostAndPort;
	}

	/**
	 * @param forceAddSchemeHostAndPort the forceAddSchemeHostAndPort to set
	 */
	public void setForceAddSchemeHostAndPort(boolean forceAddSchemeHostAndPort) {
		this.forceAddSchemeHostAndPort = forceAddSchemeHostAndPort;
	}

	/**
	 * build the request URL, append parameters as query string
	 * @return URL
	 */
	@Override
	public String build() {
		// only append scheme if it is different to the current scheme *OR*
		// if we explicitly want it to be appended by having forceAddSchemeHostAndPort = true
		if (forceAddSchemeHostAndPort) {
			host = request.getServerName();
			if (Strings.isEmpty(scheme)) {
				scheme = HttpServlets.getScheme(request);
				port = request.getServerPort();
			}
			if (port <= 0) {
				port = request.getServerPort();
			}
		}
		else if (scheme != null && scheme.equals(HttpServlets.getScheme(request))) {
			scheme = null;
		}

		if (path == null) {
			// Go to "same page"
			// (Applicable to Servlet 2.4 containers)
			// If the request was forwarded, the attribute below will be set with the original URL
			path = HttpServlets.getRequestURI(request);
		}

		return super.build();
	}
	
	//--------------------------------------------------------------
	/**
	 * build the request URL, append request parameters as query string
	 * 
	 * @param request http request
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request) {
		return buildURL(request, request.getParameterMap());
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param params parameters
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, Object params) {
		ServletURLBuilder ub = new ServletURLBuilder();
		ub.setRequest(request);
		ub.setParams(params);
		return ub.build();
	}
}
