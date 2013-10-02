package panda.servlet;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import panda.lang.Charsets;
import panda.lang.Collections;
import panda.net.http.URLHelper;


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
	public static String buildURL(HttpServletRequest request, Map params) {
		return buildURL(request, params, false);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param params parameters
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, Map params, boolean escapeAmp) {
		return buildURL(request, params, false, escapeAmp);
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
	public static String buildURL(HttpServletRequest request, Map params, boolean forceAddSchemeHostAndPort, boolean escapeAmp) {
		return buildURL(request, null, params, null, 0, forceAddSchemeHostAndPort, escapeAmp, Charsets.UTF_8);
	}

	/**
	 * build the request URL, append parameters as query string
	 * 
	 * @param request http request
	 * @param params parameters
	 * @param scheme scheme
	 * @param port port
	 * @param forceAddSchemeHostAndPort add schema and port
	 * @param escapeAmp escape &
	 * @return URL
	 */
	public static String buildURL(HttpServletRequest request, Map params, String scheme, int port,
			boolean forceAddSchemeHostAndPort, boolean escapeAmp) {
		return buildURL(request, null, params, scheme, port, forceAddSchemeHostAndPort, escapeAmp, Charsets.UTF_8);
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
	public static String buildURL(HttpServletRequest request, String uri, Map params, String scheme, int port,
			boolean forceAddSchemeHostAndPort, boolean escapeAmp, String encoding) {
		StringBuilder link = new StringBuilder();

		// only append scheme if it is different to the current scheme *OR*
		// if we explicity want it to be appended by having forceAddSchemeHostAndPort = true
		if (forceAddSchemeHostAndPort) {
			String reqScheme = request.getScheme();
			link.append(scheme != null ? scheme : reqScheme);
			link.append("://").append(request.getServerName());

			if (scheme != null) {
				// If switching schemes, use the configured port for the particular scheme.
				if (!scheme.equals(reqScheme)) {
					if (port > 0) {
						link.append(":").append(port);
					}
					// Else use the port from the current request.
				}
				else {
					int reqPort = request.getServerPort();

					if ((scheme.equals("http") && (reqPort != 80))
							|| (scheme.equals("https") && reqPort != 443)) {
						link.append(":").append(reqPort);
					}
				}
			}
		}
		else if ((scheme != null) && !scheme.equals(request.getScheme())) {
			link.append(scheme).append("://").append(request.getServerName());

			if (port > 0) {
				link.append(":").append(port);
			}
		}

		if (uri == null) {
			// Go to "same page"
			// (Applicable to Servlet 2.4 containers)
			// If the request was forwarded, the attribute below will be set with the original URL
			uri = HttpServletUtils.getRequestURI(request);
		}

		link.append(uri);

		if (Collections.isNotEmpty(params)) {
			appendParamSeparator(link, escapeAmp);
			buildParametersString(link, params, escapeAmp, encoding);
		}

		return link.toString();
	}
}
