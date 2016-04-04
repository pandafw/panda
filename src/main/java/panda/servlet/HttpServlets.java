package panda.servlet;

import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.cast.Castors;
import panda.io.MimeType;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.net.URLHelper;
import panda.net.http.HttpDates;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.net.http.ParameterParser;
import panda.net.http.UserAgent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * utility class for http servlet
 * @author yf.frank.wang@gmail.com
 */
public class HttpServlets {

	/**
	 * Standard Servlet spec context attribute that specifies a temporary
	 * directory for the current web application, of type <code>java.io.File</code>.
	 */
	public static final String SERVLET_TEMP_DIR_CONTEXT = "javax.servlet.context.tempdir";

	/**
	 * Standard Servlet 2.3+ spec request attributes for include URI and paths.
	 * <p>If included via a RequestDispatcher, the current resource will see the
	 * originating request. Its own URI and paths are exposed as request attributes.
	 */
	public static final String SERVLET_INCLUDE_REQUEST_URI = "javax.servlet.include.request_uri";
	public static final String SERVLET_INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";
	public static final String SERVLET_INCLUDE_SERVLET_PATH = "javax.servlet.include.servlet_path";
	public static final String SERVLET_INCLUDE_PATH_INFO = "javax.servlet.include.path_info";
	public static final String SERVLET_INCLUDE_QUERY_STRING = "javax.servlet.include.query_string";

	/**
	 * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
	 * <p>If forwarded to via a RequestDispatcher, the current resource will see its
	 * own URI and paths. The originating URI and paths are exposed as request attributes.
	 */
	public static final String SERVLET_FORWARD_REQUEST_URI = "javax.servlet.forward.request_uri";
	public static final String SERVLET_FORWARD_CONTEXT_PATH = "javax.servlet.forward.context_path";
	public static final String SERVLET_FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";
	public static final String SERVLET_FORWARD_PATH_INFO = "javax.servlet.forward.path_info";
	public static final String SERVLET_FORWARD_QUERY_STRING = "javax.servlet.forward.query_string";

	/**
	 * Standard Servlet 2.3+ spec request attributes for error pages.
	 * <p>To be exposed to JSPs that are marked as error pages, when forwarding
	 * to them directly rather than through the servlet container's error page
	 * resolution mechanism.
	 */
	public static final String SERVLET_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
	public static final String SERVLET_ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
	public static final String SERVLET_ERROR_MESSAGE = "javax.servlet.error.message";
	public static final String SERVLET_ERROR_EXCEPTION = "javax.servlet.error.exception";
	public static final String SERVLET_ERROR_JSP_EXCEPTION = "javax.servlet.error.JspException";
	public static final String SERVLET_ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
	public static final String SERVLET_ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";

	public static final String CONTENT_TYPE_BOUNDARY = "boundary";
	
	/**
	 * Prefix of the charset clause in a content type String: "charset="
	 */
	public static final String CONTENT_TYPE_CHARSET_PREFIX = "charset=";

	/**
	 * Default character encoding to use when <code>request.getCharacterEncoding</code>
	 * returns <code>null</code>, according to the Servlet spec.
	 */
	public static final String DEFAULT_CHARACTER_ENCODING = Charsets.ISO_8859_1;

	public static boolean isFormUrlEncoded(HttpServletRequest request) {
		String ct = request.getContentType();
		if (HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod()) 
				&& ct != null
				&& ct.startsWith(MimeType.X_WWW_FORM_URLECODED)) {
			return true;
		}
		return false;
	}

	/**
	 * get remote ip from X_REAL_IP, X_FORWARD_FOR, request.getRemoteAddr
	 * @param request request
	 * @return remote ip
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader(HttpHeader.X_REAL_IP);
		if (Strings.isEmpty(ip)) {
			ip = request.getHeader(HttpHeader.X_FORWARD_FOR);
			int i = Strings.indexOf(ip, ',');
			if (i >= 0) {
				ip = ip.substring(i + 1);
			}
			ip = Strings.strip(ip);
			if (Strings.isEmpty(ip)) {
				ip = request.getRemoteAddr();
			}
		}
		return ip;
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
		}
		else {
			uri = URLHelper.decodeURL(uri);
			query = (String)request.getAttribute(SERVLET_FORWARD_QUERY_STRING);
		}

		return URLHelper.buildURL(request.getScheme(), request.getServerName(), request.getServerPort(), uri, query);
	}

	/**
	 * @param request request
	 * @return requestURL without QueryString
	 */
	public static String getRequestURL(HttpServletRequest request) {
		return ServletURLHelper.buildURL(request, null, null, true, false);
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
		uri = URLHelper.decodeURL(uri);
		return uri;
	}
	
	/**
	 * @param request request
	 * @return relative URI
	 */
	public static String getServletPath(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String uri = (String)request.getAttribute(SERVLET_INCLUDE_REQUEST_URI);
		if (Strings.isEmpty(uri)) {
			uri = request.getRequestURI();
		}
		uri = uri.substring(request.getContextPath().length());
		uri = Strings.substringBefore(uri, ";jsessionid=");
		uri = URLHelper.decodeURL(uri);
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
		}
		catch (Exception e) {
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
	
	public static void logException(HttpServletRequest request, Throwable e) {
		logException(request, e, null);
	}
	
	private static String[] EXCLUDE_ERRORS = { "org.apache.catalina.connector.ClientAbortException" };
	
	public static void logException(HttpServletRequest request, Throwable e, String msg) {
		if (Arrays.contains(EXCLUDE_ERRORS, e.getClass().getName())) {
			return;
		}
		
		Log log = Logs.getLog(e.getClass());

		StringBuilder sb = new StringBuilder();
		if (Strings.isNotEmpty(msg)) {
			sb.append(msg).append(Streams.LINE_SEPARATOR);
		}
		if (request != null) {
			HttpServlets.dumpRequestTrace(request, sb);
			sb.append(Streams.LINE_SEPARATOR);
		}
		sb.append(Exceptions.getStackTrace(e));

		log.error(sb.toString());
	}
	
	/**
	 * dump request trace
	 * @param request request 
	 * @return string
	 */
	public static String dumpRequestTrace(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestTrace(request, sb);
		return sb.toString();
	}

	public static void dumpRequestPath(HttpServletRequest request, Appendable writer) {
		try {
			writer.append(request.getRemoteAddr());
			String ip = request.getHeader(HttpHeader.X_REAL_IP);
			if (Strings.isNotEmpty(ip)) {
				writer.append('(').append(ip).append(')');
			}
			writer.append(" -> ");
			writer.append(request.getRequestURL());
		}
		catch (IOException e) {
			Exceptions.wrapThrow(e);
		}
	}

	public static void dumpRequestTrace(HttpServletRequest request, Appendable writer) {
		try {
			dumpRequestPath(request, writer);

			writer.append(Streams.LINE_SEPARATOR);
			dumpRequestHeaders(request, writer);

			if (!request.getParameterMap().isEmpty()) {
				writer.append(Streams.LINE_SEPARATOR);
				dumpRequestParameters(request, writer);
			}
			
			if (!Arrays.isEmpty(request.getCookies())) {
				writer.append(Streams.LINE_SEPARATOR);
				dumpRequestCookies(request, writer);
			}
		}
		catch (IOException e) {
			Exceptions.wrapThrow(e);
		}
	}

	/**
	 * dump request debug
	 * @param request request 
	 * @return string
	 */
	public static String dumpRequestDebug(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestDebug(request, sb);
		return sb.toString();
	}

	public static void dumpRequestDebug(HttpServletRequest request, Appendable writer) {
		try {
			dumpRequestPath(request, writer);

			writer.append(Streams.LINE_SEPARATOR);
			dumpRequestHeaders(request, writer);

			if (!request.getParameterMap().isEmpty()) {
				writer.append(Streams.LINE_SEPARATOR);
				dumpRequestParameters(request, writer);
			}
		}
		catch (IOException e) {
			Exceptions.wrapThrow(e);
		}
	}

	public static String dumpContextAttributes(ServletContext context) {
		StringBuilder sb = new StringBuilder();
		dumpContextAttributes(context, sb);
		return sb.toString();
	}

	public static void dumpContextAttributes(ServletContext context, Appendable writer) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Enumeration e = context.getAttributeNames(); e.hasMoreElements(); ) {
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
	 * @param request request 
	 * @return string
	 */
	public static String dumpRequestInfo(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getRemoteAddr());
		sb.append(" -> ");
		sb.append(request.getRequestURL());
		return sb.toString();
	}

	/**
	 * dump request properties
	 * @param request request
	 * @return properties dump string
	 */
	public static String dumpRequestProperties(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestProperties(request, sb);
		return sb.toString();
	}

	public static void dumpRequestProperties(HttpServletRequest request, Appendable writer) {
		if (request == null)
			return;

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
	 * @param request request
	 * @return attributes dump string
	 */
	public static String dumpRequestAttributes(ServletRequest request) {
		StringBuilder sb = new StringBuilder();
		dumpRequestAttributes(request, sb);
		return sb.toString();
	}

	public static void dumpRequestAttributes(ServletRequest request, Appendable writer) {
		if (request == null)
			return;

		ServletRequestAttrMap rm = new ServletRequestAttrMap(request);
		Jsons.toJson(rm, writer, 2);
	}

	/**
	 * dump request headers
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
		for (Enumeration ek = request.getHeaderNames(); ek.hasMoreElements(); ) {
			String key = (String)ek.nextElement();
			for (Enumeration ev = request.getHeaders(key); ev.hasMoreElements(); ) {
				m.accumulate(key, String.valueOf(ev.nextElement()));
			}
		}
		Jsons.toJson(m, writer, 2);
	}

	/**
	 * dump request parameters
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
		
		Map<?,?> pm = request.getParameterMap();
		Jsons.toJson(pm, writer, 2);
	}

	/**
	 * dump request cookies
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
			}
			else {
				in = new InputStreamReader(request.getInputStream(), encoding);
			}
			Streams.copy(in, sb);
		}
		catch (IOException e) {
			sb.append(Exceptions.getStackTrace(e));
		}
		finally {
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
		}
		catch (UnsupportedEncodingException e) {
			boundary = boundaryStr.getBytes(); // Intentionally falls back to default charset
		}
		return boundary;
	}

	/**
	 * Retrieve the content length of the request.
	 * 
	 * @return The content length of the request.
	 */
	public static long getContentLength(HttpServletRequest request) {
		long size;
		try {
			size = Long.parseLong(request.getHeader(HttpHeader.CONTENT_LENGTH));
		}
		catch (NumberFormatException e) {
			size = request.getContentLength();
		}
		return size;
	}

	/**
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
	 * getCookie will return the cookie object that has the name in the Cookies
	 * of the request
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
	 * getCookie will return the cookie object that has the name in the Cookies
	 * of the request
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
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path) {
		Cookie c = new Cookie(name, Strings.EMPTY);
		c.setMaxAge(0);
		c.setPath(path);
		response.addCookie(c);
	}

	/**
	 * encode file name by User-Agent
	 * @param request request
	 * @param filename file name
	 * @return encoded file name
	 * @throws UnsupportedEncodingException  if an error occurs
	 */
	public static String EncodeFileName(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
		final String enc = Charsets.UTF_8;
		if (request == null) {
			return URLEncoder.encode(filename, enc);
		}
		
		UserAgent ua = getUserAgent(request);
		if (ua.isChrome() || ua.isFirefox()) {
			return MimeUtility.encodeWord(filename, enc, "B");
		}
		else if (ua.isMsie()) {
			return URLEncoder.encode(filename, enc);
		}
		else if (ua.isSafari()) {
			return filename;
		}
		else {
			return MimeUtility.encodeWord(filename, enc, "B");
		}
	}

	/**
	 * Set response header
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
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param params params map
	 * @throws IOException if an I/O error occurs
	 */
	public static void setResponseHeader(HttpServletRequest request, 
			HttpServletResponse response, 
			Map<?, ?> params) throws IOException {

		HttpServletSupport hss = Castors.scast(params, HttpServletSupport.class);
		hss.setRequest(request);
		hss.setResponse(response);
		
		hss.writeResponseHeader();
	}

	/**
	 * Set no cache to response header
	 * @param response HttpServletResponse
	 */
	public static void setResponseNoCache(HttpServletResponse response) {
		response.setHeader(HttpHeader.CACHE_CONTROL, HttpHeader.CACHE_CONTROL_NOCACHE);
		response.setHeader(HttpHeader.PRAGMA, HttpHeader.CACHE_CONTROL_NOCACHE);
		String expires = HttpDates.format(DateTimes.getDate());
		response.setHeader(HttpHeader.EXPIRES, expires);
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
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeRedirect(HttpServletResponse res, String url, boolean encode) throws IOException {
		HttpServletSupport hss = new HttpServletSupport(res);
		hss.setCharset(Charsets.UTF_8);
		hss.setContentType(MimeType.TEXT_HTML);
		hss.setMaxAge(0);
		hss.writeResponseHeader();
		
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
	 */
	public static void sendError(HttpServletResponse res, int sc) {
		try {
			res.sendError(sc);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * safe drain request stream
	 * @param req http servlet request
	 */
	public static void safeDrain(HttpServletRequest req) {
		try {
			Streams.drain(req.getInputStream());
		}
		catch (IOException e) {
			//
		}
	}

	/**
	 * safe drain request stream
	 * @param req http servlet request
	 * @param timeout the timeout to stop drain (milliseconds)
	 */
	public static void safeDrain(HttpServletRequest req, long timeout) {
		try {
			Streams.drain(req.getInputStream(), timeout);
		}
		catch (IOException e) {
			//
		}
	}
}
