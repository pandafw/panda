package panda.servlet;

import javax.servlet.http.HttpServletRequest;

import panda.lang.Charsets;
import panda.lang.Strings;
import panda.net.URLHelper;


/**
 * Servlet URLHelper
 * @author yf.frank.wang@gmail.com
 */
public class ServletURLHelper extends URLHelper {
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
		return buildURL(request, params, false);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param query query string
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, String query) {
		return buildURL(request, query, false);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, Object params, boolean escapeAmp) {
		return buildURL(request, params, false, escapeAmp);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param query query string
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, String query, boolean escapeAmp) {
		return buildURL(request, query, false, escapeAmp);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param params parameters
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, Object params, boolean forceAddSchemeHostAndPort, boolean escapeAmp) {
		return buildURL(request, null, 0, null, null, params, forceAddSchemeHostAndPort, escapeAmp, Charsets.UTF_8);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param query query string
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, String query, boolean forceAddSchemeHostAndPort, boolean escapeAmp) {
		return buildURL(request, null, 0, null, query, null, forceAddSchemeHostAndPort, escapeAmp, Charsets.UTF_8);
	}


	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param query query string
	 * @param params parameters
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, String query, Object params, boolean forceAddSchemeHostAndPort, boolean escapeAmp) {
		return buildURL(request, null, 0, null, query, params, forceAddSchemeHostAndPort, escapeAmp, Charsets.UTF_8);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param scheme scheme
	 * @param port port
	 * @param params parameters
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, String scheme, int port, Object params,
			boolean forceAddSchemeHostAndPort, boolean escapeAmp) {
		return buildURL(request, scheme, port, null, null, params, forceAddSchemeHostAndPort, escapeAmp, Charsets.UTF_8);
	}


	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param uri request uri
	 * @param params parameters
	 * @param scheme scheme
	 * @param port port
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, 
			String scheme, int port, String uri, String query, Object params,
			boolean forceAddSchemeHostAndPort, boolean escapeAmp) {
		return buildURL(request, scheme, port, uri, query, params, forceAddSchemeHostAndPort, escapeAmp, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param uri request uri
	 * @param params parameters
	 * @param scheme scheme
	 * @param port port
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @param encoding url encoding
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, 
			String scheme, int port, String uri, String query, Object params,
			boolean forceAddSchemeHostAndPort, boolean escapeAmp, String encoding) {
		return buildURL(request, scheme, port, uri, query, params, forceAddSchemeHostAndPort, escapeAmp, SUPPRESS_NULL, Charsets.UTF_8);
	}
	
	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param uri request uri
	 * @param params parameters
	 * @param scheme scheme
	 * @param port port
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @param encoding url encoding
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, 
			String scheme, int port, String uri, String query, Object params,
			boolean forceAddSchemeHostAndPort, boolean escapeAmp, int suppress, String encoding) {

		String host = null;
		
		// only append scheme if it is different to the current scheme *OR*
		// if we explicitly want it to be appended by having forceAddSchemeHostAndPort = true
		if (forceAddSchemeHostAndPort) {
			host = request.getServerName();
			if (Strings.isEmpty(scheme)) {
				scheme = request.getScheme();
				port = request.getServerPort();
			}
			if (port <= 0) {
				port = request.getServerPort();
			}
		}
		else if (scheme != null && scheme.equals(request.getScheme())) {
			scheme = null;
		}

		if (uri == null) {
			// Go to "same page"
			// (Applicable to Servlet 2.4 containers)
			// If the request was forwarded, the attribute below will be set with the original URL
			uri = HttpServlets.getRequestURI(request);
		}

		return buildURL(scheme, host, port, uri, query, params, escapeAmp, suppress, encoding);
	}
}
