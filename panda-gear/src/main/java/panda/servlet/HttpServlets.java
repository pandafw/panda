package panda.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.cast.Castors;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.net.Mimes;
import panda.net.Scheme;
import panda.net.URLBuilder;
import panda.net.http.HttpDates;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.net.http.HttpStatus;
import panda.net.http.ParameterParser;
import panda.net.http.UserAgent;

/**
 * utility class for http servlet
 */
public class HttpServlets {
	private static final Log log = Logs.getLog(HttpServlets.class);

	/**
	 * Standard Servlet spec context attribute that specifies a temporary directory for the current web application, of type <code>java.io.File</code>.
	 */
	public static final String SERVLET_TEMP_DIR_CONTEXT = "javax.servlet.context.tempdir";

	/**
	 * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
	 * <p>
	 * If forwarded to via a RequestDispatcher, the current resource will see its own URI and paths. The originating URI and paths are exposed as request attributes.
	 */
	public static final String SERVLET_FORWARD_REQUEST_URI = "javax.servlet.forward.request_uri";
	public static final String SERVLET_FORWARD_QUERY_STRING = "javax.servlet.forward.query_string";

	/**
	 * Standard Servlet 2.3+ spec request attributes for error pages.
	 * <p>
	 * To be exposed to JSPs that are marked as error pages, when forwarding to them directly rather than through the servlet container's error page resolution mechanism.
	 */
	public static final String SERVLET_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
	public static final String SERVLET_ERROR_MESSAGE = "javax.servlet.error.message";
	public static final String SERVLET_ERROR_EXCEPTION = "javax.servlet.error.exception";
	public static final String SERVLET_ERROR_JSP_EXCEPTION = "javax.servlet.error.JspException";

	public static final String CONTENT_TYPE_BOUNDARY = "boundary";

	/**
	 * Prefix of the charset clause in a content type String: "charset="
	 */
	public static final String CONTENT_TYPE_CHARSET_PREFIX = "charset=";

	/**
	 * Default character encoding to use when <code>request.getCharacterEncoding</code> returns <code>null</code>, according to the Servlet spec.
	 */
	public static final String DEFAULT_CHARACTER_ENCODING = Charsets.ISO_8859_1;

	public static boolean isFormUrlEncoded(HttpServletRequest request) {
		String ct = request.getContentType();
		if (HttpMethod.POST.equalsIgnoreCase(request.getMethod())
			&& Strings.startsWithIgnoreCase(ct, MimeTypes.X_WWW_FORM_URLECODED)) {
			return true;
		}
		return false;
	}

	/**
	 * get scheme from X_FORWARD_FOR, request.getScheme()
	 * 
	 * @param request request
	 * @return scheme
	 */
	public static String getScheme(HttpServletRequest request) {
		String scheme = request.getHeader(HttpHeader.X_FORWARDED_PROTO);
		if (Strings.isEmpty(scheme)) {
			scheme = request.getScheme();
		}
		return scheme;
	}

	/**
	 * get server port from X_FORWARD_PORT, request.getServerPort()
	 * 
	 * @param request request
	 * @return port
	 */
	public static int getServerPort(HttpServletRequest request) {
		int port = Numbers.toInt(request.getHeader(HttpHeader.X_FORWARDED_PORT), 0);
		if (port <= 0) {
			// some web server does not set X-FORWARDED-PORT (Azure IIS),
			// so we need to check X-FORWARDED-PROTO
			String proto = request.getHeader(HttpHeader.X_FORWARDED_PROTO);
			if (Scheme.HTTP.equals(proto)) {
				port = 80;
			} else if (Scheme.HTTPS.equals(proto)) {
				port = 443;
			} else {
				port = request.getServerPort();
			}
		}
		return port;
	}

	/**
	 * get remote ip from X_FORWARD_FOR (last), request.getRemoteAddr
	 * 
	 * @param request request
	 * @return remote ip
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader(HttpHeader.X_FORWARDED_FOR);
		if (ip != null && ip.length() > 0) {
			int i = ip.lastIndexOf(',');
			if (i >= 0) {
				ip = ip.substring(i + 1);
			}
		}

		ip = Strings.strip(ip);
		if (Strings.isEmpty(ip)) {
			ip = request.getRemoteAddr();
		} else {
			// fix ipv4:port for IIS httpPlatformHandler
			int i = ip.lastIndexOf(':');
			if (i > 0 && ip.indexOf('.') > 0) {
				// ipv4
				ip = ip.substring(0, i);
			}
		}
		return ip;
	}

	/**
	 * @param request request
	 * @return relative URI
	 */
	public static String getServletPath(HttpServletRequest request) {
		if (request == null) {
			return null;
		}

		String uri = request.getRequestURI();
		uri = uri.substring(request.getContextPath().length());
		uri = Strings.substringBefore(uri, ";jsessionid=");
		return uri;
	}

	/**
	 * @param request servlet request
	 * @return SERVLET_FORWARD_REQUEST_URI + ? + SERVLET_FORWARD_QUERY_STRING
	 */
	public static String getServletForwardRequestURL(HttpServletRequest request) {
		String url = Strings.defaultString(request.getAttribute(SERVLET_FORWARD_REQUEST_URI));
		String query = (String)request.getAttribute(SERVLET_FORWARD_QUERY_STRING);
		if (Strings.isNotEmpty(query)) {
			url += '?' + query;
		}
		return url;
	}

	/**
	 * @param request request
	 * @return requestURL + QueryString
	 */
	public static String getRequestLink(HttpServletRequest request) {
		String uri = (String)request.getAttribute(SERVLET_FORWARD_REQUEST_URI);
		String query = null;
		if (Strings.isEmpty(uri)) {
			uri = request.getRequestURI();
			query = request.getQueryString();
		} else {
			query = (String)request.getAttribute(SERVLET_FORWARD_QUERY_STRING);
		}

		URLBuilder ub = new URLBuilder();
		ub.setScheme(getScheme(request));
		ub.setHost(request.getServerName());
		ub.setPort(getServerPort(request));
		ub.setPath(uri);
		ub.setQuery(query);
		return ub.build();
	}

	/**
	 * @param request request
	 * @return requestURL without QueryString
	 */
	public static String getRequestURL(HttpServletRequest request) {
		ServletURLBuilder ub = new ServletURLBuilder();
		ub.setRequest(request);
		ub.setForceAddSchemeHostAndPort(true);
		return ub.build();
	}

	/**
	 * @param request request
	 * @return requestURI
	 */
	public static String getRequestURI(HttpServletRequest request) {
		String uri = (String)request.getAttribute(SERVLET_FORWARD_REQUEST_URI);
		if (Strings.isEmpty(uri)) {
			uri = request.getRequestURI();
		}
		return uri;
	}

	/**
	 * @param request request
	 * @return request query string
	 */
	public static String getRequestQuery(HttpServletRequest request) {
		String uri = (String)request.getAttribute(SERVLET_FORWARD_REQUEST_URI);
		if (Strings.isEmpty(uri)) {
			return request.getQueryString();
		}
		return (String)request.getAttribute(SERVLET_FORWARD_QUERY_STRING);
	}

	/**
	 * @param request request
	 * @return request query string
	 */
	public static String getRequestQueryString(HttpServletRequest request) {
		String q = getRequestQuery(request);
		String s = Strings.substringBefore(q, '#');
		return s;
	}

	/**
	 * @param request request
	 * @return request query string
	 */
	public static String getRequestQueryAnchor(HttpServletRequest request) {
		String q = getRequestQuery(request);
		String s = Strings.substringAfter(q, '#');
		return s;
	}

	public static void safeReset(ServletResponse response) {
		try {
			response.reset();
		} catch (Exception e) {
		}
	}

	public static Integer getServletErrorCode(ServletRequest request) {
		return (Integer)request.getAttribute(SERVLET_ERROR_STATUS_CODE);
	}

	public static String getServletErrorMessage(ServletRequest request) {
		return (String)request.getAttribute(SERVLET_ERROR_MESSAGE);
	}

	public static Throwable getServletException(ServletRequest request) {
		// support for JSP exception pages, exposing the servlet or JSP exception
		Throwable ex = (Throwable)request.getAttribute(SERVLET_ERROR_EXCEPTION);

		if (ex == null) {
			ex = (Throwable)request.getAttribute(SERVLET_ERROR_JSP_EXCEPTION);
		}

		return ex;
	}

	public static void saveServletException(ServletRequest request, Throwable e) {
		request.setAttribute(SERVLET_ERROR_EXCEPTION, e);

		// for compatibility
		request.setAttribute(SERVLET_ERROR_JSP_EXCEPTION, e);
	}

	public static void clearServletException(ServletRequest request, Throwable e) {
		request.removeAttribute(SERVLET_ERROR_EXCEPTION);

		// for compatibility
		request.removeAttribute(SERVLET_ERROR_JSP_EXCEPTION);
	}

	public static void sendException(HttpServletRequest request, HttpServletResponse response, Throwable e) throws IOException {
		// send a http error response to use the servlet defined error handler
		// make the exception availible to the web.xml defined error page
		saveServletException(request, e);

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	private static Set<String> CLIENT_ABORT_ERRORS = Arrays.toSet("org.apache.catalina.connector.ClientAbortException");

	public static void addClientAbortErrors(String... args) {
		CLIENT_ABORT_ERRORS.addAll(Arrays.asList(args));
	}

	/**
	 * check the exception or the e.getCause() is a ClientAbortException
	 * 
	 * @param e exception
	 * @return true if e or e.getCause() is a ClientAbortException
	 */
	public static boolean isClientAbortError(Throwable e) {
		while (e != null) {
			if (CLIENT_ABORT_ERRORS.contains(e.getClass().getName())) {
				return true;
			}
			e = e.getCause();
		}
		return false;
	}

	/**
	 * @param request http servlet request
	 * @param e error
	 * @param trace dump request trace or not
	 * @return request exception dump string
	 */
	public static String dumpException(HttpServletRequest request, Throwable e, boolean trace) {
		StringBuilder sb = new StringBuilder();

		sb.append(e.getClass().getName()).append(": ").append(e.getMessage());
		if (request != null) {
			sb.append(Streams.EOL);
			if (trace) {
				dumpRequestFull(request, sb);
			} else {
				dumpRequestInfo(request, sb);
			}
		}
		return sb.toString();
	}

	public static void dumpRequestInfo(HttpServletRequest request, Appendable writer) {
		try {
			writer.append(request.getRemoteAddr());
			String ip = request.getHeader(HttpHeader.X_FORWARDED_FOR);
			if (Strings.isNotEmpty(ip)) {
				writer.append('(').append(ip).append(')');
			}
			writer.append(" -> ").append(request.getMethod()).append(' ');
			String fw = getServletForwardRequestURL(request);
			if (Strings.isNotEmpty(fw)) {
				writer.append(fw).append(" -> ");
			}
			writer.append(request.getRequestURI());
			if (Strings.isNotEmpty(request.getQueryString())) {
				writer.append('?').append(request.getQueryString());
			}
		} catch (Throwable e) {
		}
	}

	/**
	 * dump request head and body
	 * 
	 * @param request request
	 * @return string
	 */
	public static String dumpRequestFull(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestFull(request, sb);
		return sb.toString();
	}

	public static void dumpRequestFull(HttpServletRequest request, Appendable writer) {
		try {
			dumpRequestInfo(request, writer);

			writer.append(Streams.EOL);
			dumpRequestHeaders(request, writer);

			if (HttpMethod.POST.equalsIgnoreCase(request.getMethod())
				&& !request.getParameterMap().isEmpty()) {
				writer.append(Streams.EOL);
				dumpRequestParameters(request, writer);
			}
		} catch (Throwable e) {
		}
	}

	public static String dumpContextAttributes(ServletContext context) {
		StringBuilder sb = new StringBuilder();
		dumpContextAttributes(context, sb);
		return sb.toString();
	}

	public static void dumpContextAttributes(ServletContext context, Appendable writer) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Enumeration e = context.getAttributeNames(); e.hasMoreElements();) {
			String key = (String)e.nextElement();
			Object value = context.getAttribute(key);
			map.put(key, value);
		}
		Jsons.toJson(map, writer, 2);
	}

	public static String dumpSessionAttributes(HttpSession session) {
		StringBuilder sb = new StringBuilder();
		dumpSessionAttributes(session, sb);
		return sb.toString();
	}

	public static void dumpSessionAttributes(HttpSession session, Appendable writer) {
		HttpSessionMap sm = new HttpSessionMap(session);
		Jsons.toJson(sm, writer, 2);
	}

	/**
	 * dump request info
	 * 
	 * @param request request
	 * @return string
	 */
	public static String dumpRequestInfo(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestInfo(request, sb);
		return sb.toString();
	}

	/**
	 * dump request properties
	 * 
	 * @param request request
	 * @return properties dump string
	 */
	public static String dumpRequestProperties(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestProperties(request, sb);
		return sb.toString();
	}

	public static void dumpRequestProperties(HttpServletRequest request, Appendable writer) {
		if (request == null) return;

		Map<String, Object> m = new LinkedHashMap<String, Object>();
		m.put("characterEncoding", request.getCharacterEncoding());
		m.put("contentLength", String.valueOf(request.getContentLength()));
		m.put("contentType", request.getContentType());
		m.put("local", '[' + request.getLocalAddr() + "]:" + request.getLocalPort());
		m.put("protocol", request.getProtocol());
		m.put("remote", '[' + request.getRemoteAddr() + "]:" + request.getRemotePort());
		m.put("requestURI", request.getRequestURI());
		m.put("requestURL", request.getRequestURL().toString());
		m.put("scheme", request.getScheme());
		m.put("server", request.getServerName() + ':' + request.getServerPort());
		m.put("servlet", request.getServletPath() + ':' + request.getMethod());
		m.put("pathInfo", request.getPathInfo());

		Enumeration<String> em = request.getAttributeNames();
		while (em.hasMoreElements()) {
			String n = em.nextElement();
			if (n.startsWith("javax.")) {
				m.put(n, String.valueOf(request.getAttribute(n)));
			}
		}
		Jsons.toJson(m, writer, 2);
	}

	/**
	 * dump request attributes
	 * 
	 * @param request request
	 * @return attributes dump string
	 */
	public static String dumpRequestAttributes(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestAttributes(request, sb);
		return sb.toString();
	}

	public static void dumpRequestAttributes(ServletRequest request, Appendable writer) {
		if (request == null) return;

		ServletRequestAttrMap rm = new ServletRequestAttrMap(request);
		Jsons.toJson(rm, writer, 2);
	}

	/**
	 * dump request headers
	 * 
	 * @param request request
	 * @return header dump string
	 */
	public static String dumpRequestHeaders(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestHeaders(request, sb);
		return sb.toString();
	}

	public static void dumpRequestHeaders(HttpServletRequest request, Appendable writer) {
		JsonObject m = new JsonObject();
		for (Enumeration ek = request.getHeaderNames(); ek.hasMoreElements();) {
			String key = (String)ek.nextElement();
			for (Enumeration ev = request.getHeaders(key); ev.hasMoreElements();) {
				m.accumulate(key, String.valueOf(ev.nextElement()));
			}
		}
		Jsons.toJson(m, writer, 2);
	}

	/**
	 * dump request parameters
	 * 
	 * @param request request
	 * @return parameters dump string
	 */
	public static String dumpRequestParameters(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestParameters(request, sb);
		return sb.toString();
	}

	public static void dumpRequestParameters(ServletRequest request, Appendable writer) {
		if (request == null) {
			return;
		}

		Map<?, ?> pm = request.getParameterMap();
		Jsons.toJson(pm, writer, 2);
	}

	/**
	 * dump request cookies
	 * 
	 * @param request request
	 * @return cookies dump string
	 */
	public static String dumpRequestCookies(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestCookies(request, sb);
		return sb.toString();
	}

	public static void dumpRequestCookies(HttpServletRequest request, Appendable writer) {
		if (request == null) {
			return;
		}

		Cookie[] cs = request.getCookies();
		Jsons.toJson(cs, writer, 2);
	}

	public static String dumpRequestStream(HttpServletRequest request, String encoding) {
		StringBuilder sb = new StringBuilder();
		InputStreamReader in = null;
		try {
			if (Strings.isEmpty(encoding)) {
				in = new InputStreamReader(request.getInputStream());
			} else {
				in = new InputStreamReader(request.getInputStream(), encoding);
			}
			Streams.copy(in, sb);
		} catch (IOException e) {
			sb.append(Exceptions.getStackTrace(e));
		} finally {
			Streams.safeClose(in);
		}
		return sb.toString();
	}

	/**
	 * get cookies
	 * 
	 * @param request request
	 * @return Cookie map
	 */
	public static Map<String, String> getCookies(HttpServletRequest request) {
		Map<String, String> cm = new HashMap<String, String>();
		Cookie[] cs = request.getCookies();
		if (cs != null) {
			for (Cookie c : cs) {
				if (c != null) {
					cm.put(c.getName(), c.getValue());
				}
			}
		}
		return cm;
	}

	/**
	 * get cookie map
	 * 
	 * @param request request
	 * @return Cookie map
	 */
	public static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cm = new HashMap<String, Cookie>();
		Cookie[] cs = request.getCookies();
		if (cs != null) {
			for (Cookie c : cs) {
				if (c != null) {
					cm.put(c.getName(), c);
				}
			}
		}
		return cm;
	}

	public static String getBoundary(HttpServletRequest request) {
		return getBoundary(request.getContentType());
	}

	public static byte[] getBoundaryBytes(HttpServletRequest request) {
		return getBoundaryBytes(request.getContentType());
	}

	public static String getBoundary(String contentType) {
		if (Strings.isEmpty(contentType)) {
			return null;
		}

		ParameterParser parser = new ParameterParser(true);
		// Parameter parser can handle null input
		return parser.get(contentType, CONTENT_TYPE_BOUNDARY, ";,");
	}

	public static byte[] getBoundaryBytes(String contentType) {
		String boundaryStr = getBoundary(contentType);

		if (boundaryStr == null) {
			return null;
		}

		byte[] boundary;
		try {
			boundary = boundaryStr.getBytes(Charsets.ISO_8859_1);
		} catch (UnsupportedEncodingException e) {
			boundary = boundaryStr.getBytes(); // Intentionally falls back to default charset
		}
		return boundary;
	}

	/**
	 * Retrieve the content length of the request.
	 * 
	 * @param request http servlet request
	 * @return The content length of the request.
	 */
	public static long getContentLength(HttpServletRequest request) {
		long size;
		try {
			size = Long.parseLong(request.getHeader(HttpHeader.CONTENT_LENGTH));
		} catch (NumberFormatException e) {
			size = request.getContentLength();
		}
		return size;
	}

	/**
	 * @param request http servlet request
	 * @return userAgent
	 */
	public static UserAgent getUserAgent(HttpServletRequest request) {
		return new UserAgent(request.getHeader(HttpHeader.USER_AGENT));
	}

	/**
	 * @param request request
	 * @return character encoding
	 */
	public static String getEncoding(HttpServletRequest request) {
		return getEncoding(request, Charsets.UTF_8);
	}

	/**
	 * @param request request
	 * @param defaultEncoding default encoding
	 * @return character encoding
	 */
	public static String getEncoding(HttpServletRequest request, String defaultEncoding) {
		String cs = request.getCharacterEncoding();
		if (!Charsets.isSupportedCharset(cs)) {
			cs = defaultEncoding;
		}
		return cs;
	}

	/**
	 * getCookie will return the cookie object that has the name in the Cookies of the request
	 * 
	 * @param request request
	 * @param name the cookie's name
	 * @return Cookie if no such cookie, return null
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * getCookie will return the cookie object that has the name in the Cookies of the request
	 * 
	 * @param request request
	 * @param name the cookie's name
	 * @return Cookie value if no such cookie, return null
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * remove cookie to response
	 * 
	 * @param response response
	 * @param name the cookie's name
	 * @param path the cookie's path
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path) {
		Cookie c = new Cookie(name, Strings.EMPTY);
		c.setMaxAge(0);
		c.setPath(path);
		response.addCookie(c);
	}

	private static String quoteFileName(String filename) {
		return "filename=\"" + filename + '"';
	}

	private static String rfc2231FileName(String filename, String charset) throws UnsupportedEncodingException {
		return "filename*=" + charset + "''" + URLEncoder.encode(filename, charset);
	}

	/**
	 * encode file name by User-Agent
	 * 
	 * @param request request
	 * @param charset the charset
	 * @param filename file name
	 * @return encoded file name
	 * @throws UnsupportedEncodingException if an error occurs
	 */
	public static String EncodeFileName(HttpServletRequest request, String charset, String filename) throws UnsupportedEncodingException {
		if (Strings.isEmpty(charset)) {
			charset = Charsets.UTF_8;
		}

		if (request == null) {
			return quoteFileName(URLEncoder.encode(filename, charset));
		}

		UserAgent ua = getUserAgent(request);
		if (ua.isMsie() || ua.isEdge()) {
			return quoteFileName(URLEncoder.encode(filename, charset));
		}

		if (ua.isSafari()) {
			if (ua.getBrowser().getMajor() < 6) {
				return quoteFileName(filename);
			}
			return rfc2231FileName(filename, charset);
		}

		return quoteFileName(Mimes.encodeText(filename, charset));
	}

	/**
	 * Set response header
	 * 
	 * @param response HttpServletResponse
	 * @param params params map
	 * @throws IOException if an I/O error occurs
	 */
	public static void setResponseHeader(
		HttpServletResponse response,
		Map<?, ?> params) throws IOException {
		setResponseHeader(null, response, params);
	}

	/**
	 * Set response header
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param params params map
	 * @throws IOException if an I/O error occurs
	 */
	public static void setResponseHeader(HttpServletRequest request,
		HttpServletResponse response,
		Map<?, ?> params) throws IOException {

		HttpServletResponser hss = Castors.scast(params, HttpServletResponser.class);
		hss.setRequest(request);
		hss.setResponse(response);

		hss.writeHeader();
	}

	/**
	 * Set cache control to response header
	 * 
	 * @param response HttpServletResponse
	 * @param maxAge max age for the response
	 */
	public static void setResponseCache(HttpServletResponse response, int maxAge) {
		setResponseCache(response, maxAge, HttpHeader.CACHE_CONTROL_PUBLIC);
	}

	public static boolean checkAndSetNotModified(HttpServletRequest request, HttpServletResponse response, Date lastModified, int maxage) {
		// check for if-modified-since, prior to any other headers
		long ifModifiedSince = 0;
		if (request != null) {
			try {
				ifModifiedSince = request.getDateHeader(HttpHeader.IF_MODIFIED_SINCE);
			} catch (Exception e) {
				log.warn("Ignore invalid If-Modified-Since header value: " + request.getHeader(HttpHeader.IF_MODIFIED_SINCE));
			}
		}

		long mod = lastModified == null ? 0 : lastModified.getTime();
		long exp = mod + maxage * 1000;

		if (ifModifiedSince > 0 && ifModifiedSince <= mod) {
			// not modified, content is not sent - only basic
			// headers and status SC_NOT_MODIFIED
			response.setHeader(HttpHeader.EXPIRES, HttpDates.format(exp));
			response.setStatus(HttpStatus.SC_NOT_MODIFIED);
			return true;
		}
		return false;
	}

	/**
	 * Set cache control to response header
	 * 
	 * @param response HttpServletResponse
	 * @param maxAge max age
	 * @param cacheControl cache control
	 */
	public static void setResponseCache(HttpServletResponse response, int maxAge, String cacheControl) {
		String cc = "max-age=" + maxAge;
		if (Strings.isNotEmpty(cacheControl)) {
			cc += ", " + cacheControl;
		}
		response.setHeader(HttpHeader.CACHE_CONTROL, cc);

		long tm = System.currentTimeMillis();
		String now = HttpDates.format(tm);
		response.setHeader(HttpHeader.DATE, now);

		tm += (maxAge * 1000L);
		String expires = HttpDates.format(tm);
		response.setHeader(HttpHeader.EXPIRES, expires);
		response.setHeader(HttpHeader.RETRY_AFTER, expires);
	}

	/**
	 * Set no cache to response header
	 * 
	 * @param response HttpServletResponse
	 */
	public static void setResponseNoCache(HttpServletResponse response) {
		response.setHeader(HttpHeader.CACHE_CONTROL, HttpHeader.CACHE_CONTROL_NOCACHE);
		response.setHeader(HttpHeader.PRAGMA, HttpHeader.CACHE_CONTROL_NOCACHE);
		response.setHeader(HttpHeader.EXPIRES, "-1");
	}

	public static boolean isModifiedSince(HttpServletRequest request, long lastModifiedMillis) {
		// check for if-modified-since, prior to any other headers
		long ifModifiedSince = 0;
		try {
			ifModifiedSince = request.getDateHeader(HttpHeader.IF_MODIFIED_SINCE);
		} catch (Exception e) {
		}

		return (ifModifiedSince > 0 && ifModifiedSince <= lastModifiedMillis);
	}

	/**
	 * @param res response
	 * @param url redirect url
	 */
	public static void sendRedirect(HttpServletResponse res, String url) {
		sendRedirect(res, url, false);
	}

	/**
	 * @param res response
	 * @param url redirect url
	 * @param permanently 301 or 302
	 */
	public static void sendRedirect(HttpServletResponse res, String url, boolean permanently) {
		res.setStatus(permanently ? HttpServletResponse.SC_MOVED_PERMANENTLY : HttpServletResponse.SC_MOVED_TEMPORARILY);
		res.setHeader(HttpHeader.LOCATION, url);
	}

	/**
	 * @param res response
	 * @param url redirect url
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeRedirect(HttpServletResponse res, String url) throws IOException {
		writeRedirect(res, url, true);
	}

	/**
	 * @param res response
	 * @param url redirect url
	 * @param encode encode redirect url
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeRedirect(HttpServletResponse res, String url, boolean encode) throws IOException {
		HttpServletResponser hss = new HttpServletResponser(res);
		hss.setCharset(Charsets.UTF_8);
		hss.setContentType(MimeTypes.TEXT_HTML);
		hss.setMaxAge(0);
		hss.writeHeader();

		PrintWriter pw = res.getWriter();
		pw.write("<html><head><meta http-equiv=\"refresh\" content=\"0; url=");
		if (encode) {
			url = res.encodeRedirectURL(url);
		}
		pw.write(url);
		pw.write("\"></head><body></body></html>");
		pw.flush();
	}

	/**
	 * @param res response
	 * @param sc status code
	 */
	public static void safeSendError(HttpServletResponse res, int sc) {
		try {
			res.sendError(sc);
		} catch (Throwable e) {
		}
	}

	/**
	 * @param res response
	 * @param sc status code
	 */
	public static void sendError(HttpServletResponse res, int sc) {
		try {
			res.sendError(sc);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * safe drain request stream
	 * 
	 * @param req http servlet request
	 */
	public static void safeDrain(HttpServletRequest req) {
		try {
			Streams.drain(req.getInputStream());
		} catch (IOException e) {
			//
		}
	}

	/**
	 * safe drain request stream
	 * 
	 * @param req http servlet request
	 * @param timeout the timeout to stop drain (milliseconds)
	 */
	public static void safeDrain(HttpServletRequest req, long timeout) {
		try {
			Streams.drain(req.getInputStream(), timeout);
		} catch (IOException e) {
			//
		}
	}
}
