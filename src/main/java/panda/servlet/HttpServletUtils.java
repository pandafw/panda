package panda.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.castor.Castors;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.HttpHeader;
import panda.net.http.UserAgent;


/**
 * utility class for http servlet
 * @author yf.frank.wang@gmail.com
 */
public class HttpServletUtils {

	/**
	 * Standard Servlet 2.3+ spec request attributes for include URI and paths.
	 * <p>If included via a RequestDispatcher, the current resource will see the
	 * originating request. Its own URI and paths are exposed as request attributes.
	 */
	public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
	public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
	public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
	public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
	public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

	/**
	 * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
	 * <p>If forwarded to via a RequestDispatcher, the current resource will see its
	 * own URI and paths. The originating URI and paths are exposed as request attributes.
	 */
	public static final String FORWARD_REQUEST_URI_ATTRIBUTE = "javax.servlet.forward.request_uri";
	public static final String FORWARD_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.forward.context_path";
	public static final String FORWARD_SERVLET_PATH_ATTRIBUTE = "javax.servlet.forward.servlet_path";
	public static final String FORWARD_PATH_INFO_ATTRIBUTE = "javax.servlet.forward.path_info";
	public static final String FORWARD_QUERY_STRING_ATTRIBUTE = "javax.servlet.forward.query_string";

	/**
	 * Standard Servlet 2.3+ spec request attributes for error pages.
	 * <p>To be exposed to JSPs that are marked as error pages, when forwarding
	 * to them directly rather than through the servlet container's error page
	 * resolution mechanism.
	 */
	public static final String ERROR_STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";
	public static final String ERROR_EXCEPTION_TYPE_ATTRIBUTE = "javax.servlet.error.exception_type";
	public static final String ERROR_MESSAGE_ATTRIBUTE = "javax.servlet.error.message";
	public static final String ERROR_EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
	public static final String ERROR_REQUEST_URI_ATTRIBUTE = "javax.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME_ATTRIBUTE = "javax.servlet.error.servlet_name";

	public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
	public static final String CONTENT_TYPE_TEXT_XML = "text/xml";
	
	/**
	 * Prefix of the charset clause in a content type String: "charset="
	 */
	public static final String CONTENT_TYPE_CHARSET_PREFIX = "charset=";

	/**
	 * Default character encoding to use when <code>request.getCharacterEncoding</code>
	 * returns <code>null</code>, according to the Servlet spec.
	 */
	public static final String DEFAULT_CHARACTER_ENCODING = Charsets.ISO_8859_1;

	/**
	 * Standard Servlet spec context attribute that specifies a temporary
	 * directory for the current web application, of type <code>java.io.File</code>.
	 */
	public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";

	/**
	 * @param request request
	 * @return requestURI
	 */
	public static String getRequestURL(HttpServletRequest request) {
		String url;
		String uri = (String)request.getAttribute(FORWARD_REQUEST_URI_ATTRIBUTE);
		if (Strings.isEmpty(uri)) {
			url = request.getRequestURL().toString();
		}
		else {
			String qs = (String)request.getAttribute(FORWARD_QUERY_STRING_ATTRIBUTE);
			if (Strings.isEmpty(qs)) {
				url = uri;
			}
			else {
				url = uri + '?' + qs;
			}
		}
		return url;
	}
	
	/**
	 * @param request request
	 * @return requestURI
	 */
	public static String getRequestURI(HttpServletRequest request) {
		String uri = (String)request.getAttribute(FORWARD_REQUEST_URI_ATTRIBUTE);
		if (Strings.isEmpty(uri)) {
			uri = request.getRequestURI();
		}
		return uri;
	}
	
	/**
	 * @param request request
	 * @return relative URI
	 */
	public static String getRelativeURI(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		return getRequestURI(request).substring(request.getContextPath().length());
	}

	public static void sendException(HttpServletRequest request, HttpServletResponse response, Throwable e) throws IOException {
		// send a http error response to use the servlet defined error handler
		// make the exception availible to the web.xml defined error page
		request.setAttribute("javax.servlet.error.exception", e);

		// for compatibility
		request.setAttribute("javax.servlet.jsp.jspException", e);

		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}
	
	public static void logException(HttpServletRequest request, Throwable e) {
		logException(request, e, null);
	}
	
	public static void logException(HttpServletRequest request, Throwable e, String msg) {
		Log log = Logs.getLog(e.getClass());

		StringBuilder sb = new StringBuilder();
		if (Strings.isNotEmpty(msg)) {
			sb.append(msg).append(Streams.LINE_SEPARATOR);
		}
		if (request != null) {
			HttpServletUtils.dumpRequestTrace(request, sb);
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

	public static void dumpRequestTrace(HttpServletRequest request, Appendable writer) {
		try {
			writer.append(request.getRemoteAddr());
			writer.append(" -> ");
			writer.append(request.getRequestURL());

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
			writer.append(request.getRemoteAddr());
			writer.append(" -> ");
			writer.append(request.getRequestURL());

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
		sb.append(request.getRequestURI());
		if (Strings.isNotEmpty(request.getQueryString())) {
			sb.append('?');
			sb.append(request.getQueryString());
		}
		return sb.toString();
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

		ServletRequestMap rm = new ServletRequestMap(request);
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
		
		UserAgent ua = new UserAgent(request);
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
	 * @param res response 
	 * @param url redirect url
	 * @throws IOException if an I/O error occurs
	 */
	public static void sendRedirect(HttpServletResponse res, String url) throws IOException {
		sendRedirect(res, url, false);
	}

	/**
	 * @param res response 
	 * @param url redirect url
	 * @throws IOException if an I/O error occurs
	 */
	public static void sendRedirect(HttpServletResponse res, String url, boolean permanently) throws IOException {
		res.setStatus(permanently ? HttpServletResponse.SC_MOVED_PERMANENTLY : HttpServletResponse.SC_MOVED_TEMPORARILY);
		res.setHeader(HttpHeader.LOCATION, res.encodeRedirectURL(url));
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
		hss.setContentType("text/html");
		hss.setExpires(0);
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
}
