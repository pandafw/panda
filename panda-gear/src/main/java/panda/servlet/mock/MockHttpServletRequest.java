package panda.servlet.mock;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.net.URLBuilder;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.servlet.DelegateServletInputStream;

/**
 * Mock implementation of the {@link javax.servlet.http.HttpServletRequest} interface.
 *
 * <p>Compatible with Servlet 2.5 and partially with Servlet 3.0 (notable exceptions:
 * the <code>getPart(s)</code> and <code>startAsync</code> families of methods).
 *
 */
public class MockHttpServletRequest implements HttpServletRequest {

	/**
	 * The default protocol: 'http'.
	 */
	public static final String DEFAULT_PROTOCOL = "http";

	/**
	 * The default server address: '127.0.0.1'.
	 */
	public static final String DEFAULT_SERVER_ADDR = "127.0.0.1";

	/**
	 * The default server name: 'localhost'.
	 */
	public static final String DEFAULT_SERVER_NAME = "localhost";

	/**
	 * The default server port: '80'.
	 */
	public static final int DEFAULT_SERVER_PORT = 80;

	/**
	 * The default remote address: '127.0.0.1'.
	 */
	public static final String DEFAULT_REMOTE_ADDR = "127.0.0.1";

	/**
	 * The default remote host: 'localhost'.
	 */
	public static final String DEFAULT_REMOTE_HOST = "localhost";

	private static final String CONTENT_TYPE_HEADER = HttpHeader.CONTENT_TYPE;
	
	private static final String CHARSET_PREFIX = "charset=";


	private boolean active = true;


	// ---------------------------------------------------------------------
	// ServletRequest properties
	// ---------------------------------------------------------------------

	private final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	private byte[] content;

	private String characterEncoding;

	private String contentType = MimeTypes.X_WWW_FORM_URLECODED;

	private final Map<String, String[]> parameters = new LinkedHashMap<String, String[]>(16);

	private String protocol = DEFAULT_PROTOCOL;

	private String scheme = DEFAULT_PROTOCOL;

	private String serverName = DEFAULT_SERVER_NAME;

	private int serverPort = DEFAULT_SERVER_PORT;

	private String remoteAddr = DEFAULT_REMOTE_ADDR;

	private String remoteHost = DEFAULT_REMOTE_HOST;

	/** List of locales in descending order */
	private final List<Locale> locales = new LinkedList<Locale>();

	private boolean secure = false;

	private final MockServletContext servletContext;

	private int remotePort = DEFAULT_SERVER_PORT;

	private String localName = DEFAULT_SERVER_NAME;

	private String localAddr = DEFAULT_SERVER_ADDR;

	private int localPort = DEFAULT_SERVER_PORT;


	// ---------------------------------------------------------------------
	// HttpServletRequest properties
	// ---------------------------------------------------------------------

	private String authType;

	private Cookie[] cookies;

	private final HttpHeader headers = new HttpHeader();

	private String method;

	private String pathInfo;

	private String contextPath = "";

	private String queryString;

	private String remoteUser;

	private final Set<String> userRoles = new HashSet<String>();

	private Principal userPrincipal;

	private String requestedSessionId;

	private String requestURI;

	private String servletPath = "";

	private HttpSession session;

	private boolean requestedSessionIdValid = true;

	private boolean requestedSessionIdFromCookie = true;

	private boolean requestedSessionIdFromURL = false;

	private ServletInputStream stream;

	private BufferedReader reader;
	
	// ---------------------------------------------------------------------
	// Constructors
	// ---------------------------------------------------------------------
	/**
	 * Create a new MockHttpServletRequest with a default
	 * {@link MockServletContext}.
	 * @param method the request method (may be <code>null</code>)
	 * @param requestURI the request URI (may be <code>null</code>)
	 * @see #setMethod
	 * @see #setRequestURI
	 * @see MockServletContext
	 */
	public MockHttpServletRequest(String method, String requestURI) {
		this(null, method, requestURI);
	}

	/**
	 * Create a new MockHttpServletRequest.
	 * @param servletContext the ServletContext that the request runs in (may be
	 * <code>null</code> to use a default MockServletContext)
	 * @see MockServletContext
	 */
	public MockHttpServletRequest(MockServletContext servletContext) {
		this(servletContext, HttpMethod.GET, "");
	}

	/**
	 * Create a new MockHttpServletRequest.
	 * @param servletContext the ServletContext that the request runs in (may be
	 * <code>null</code> to use a default MockServletContext)
	 * @param method the request method (may be <code>null</code>)
	 * @param requestURI the request URI (may be <code>null</code>)
	 * @see #setMethod
	 * @see #setRequestURI
	 * @see MockServletContext
	 */
	public MockHttpServletRequest(MockServletContext servletContext, String method, String requestURI) {
		this.servletContext = (servletContext != null ? servletContext : new MockServletContext());
		this.method = method;
		this.requestURI = requestURI;
		this.locales.add(Locale.ENGLISH);
		setCharacterEncoding(Charsets.UTF_8);
	}


	/**
	 * Return the ServletContext that this request is associated with. (Not
	 * available in the standard HttpServletRequest interface for some reason.)
	 * @return MockServletContext
	 */
	public MockServletContext getMockServletContext() {
		return this.servletContext;
	}

	// ---------------------------------------------------------------------
	// Lifecycle methods
	// ---------------------------------------------------------------------

	/**
	 * Return the ServletContext that this request is associated with. (Not
	 * available in the standard HttpServletRequest interface for some reason.)
	 */
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	/**
	 * @return whether this request is still active (that is, not completed yet).
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * Mark this request as completed, keeping its state.
	 */
	public void close() {
		this.active = false;
	}

	/**
	 * Invalidate this request, clearing its state.
	 */
	public void invalidate() {
		close();
		clearAttributes();
	}

	/**
	 * Check whether this request is still active (that is, not completed yet),
	 * throwing an IllegalStateException if not active anymore.
	 * @throws IllegalStateException if request is not active
	 */
	protected void checkActive() throws IllegalStateException {
		if (!this.active) {
			throw new IllegalStateException("Request is not active anymore");
		}
	}


	// ---------------------------------------------------------------------
	// ServletRequest interface
	// ---------------------------------------------------------------------

	public Object getAttribute(String name) {
		checkActive();
		return this.attributes.get(name);
	}

	public Enumeration<String> getAttributeNames() {
		checkActive();
		return new Vector<String>(this.attributes.keySet()).elements();
	}

	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
		updateContentTypeHeader();
	}
	
	private void updateContentTypeHeader() {
		if (this.contentType != null) {
			StringBuilder sb = new StringBuilder(this.contentType);
			if (!this.contentType.toLowerCase().contains(CHARSET_PREFIX) && this.characterEncoding != null) {
				sb.append(";").append(CHARSET_PREFIX).append(this.characterEncoding);
			}
			doAddHeaderValue(CONTENT_TYPE_HEADER, sb.toString(), true);
		}
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getContentLength() {
		return (this.content != null ? this.content.length : -1);
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
		if (contentType != null) {
			int charsetIndex = contentType.toLowerCase().indexOf(CHARSET_PREFIX);
			if (charsetIndex != -1) {
				String encoding = contentType.substring(charsetIndex + CHARSET_PREFIX.length());
				this.characterEncoding = encoding;
			}
			updateContentTypeHeader();
		}
	}

	public String getContentType() {
		return this.contentType;
	}

	public BufferedReader getReader() throws UnsupportedEncodingException {
		if (reader == null) {
			reader = new BufferedReader(new InputStreamReader(getInputStream(), characterEncoding));
		}
		return reader;
	}

	public ServletInputStream getInputStream() {
		if (stream == null) {
			if (content != null) {
				stream = new DelegateServletInputStream(new ByteArrayInputStream(content));
			}
			else if (Collections.isNotEmpty(parameters)) {
				String ps = URLBuilder.buildQueryString(parameters, characterEncoding);
				content = Strings.getBytes(ps, characterEncoding);
				stream = new DelegateServletInputStream(new ByteArrayInputStream(content));
			}
			else {
				stream = new DelegateServletInputStream(Streams.closedInputStream());
			}
		}
		return stream;
	}

	/**
	 * Set a single value for the specified HTTP parameter.
	 * <p>
	 * If there are already one or more values registered for the given
	 * parameter name, they will be replaced.
	 * @param name name
	 * @param value value
	 */
	public void setParameter(String name, String value) {
		setParameter(name, new String[] { value });
	}

	/**
	 * Set an array of values for the specified HTTP parameter.
	 * <p>
	 * If there are already one or more values registered for the given
	 * parameter name, they will be replaced.
	 * @param name name
	 * @param values values
	 */
	public void setParameter(String name, String[] values) {
		Asserts.notNull(name, "Parameter name must not be null");
		this.parameters.put(name, values);
	}

	/**
	 * Sets all provided parameters <emphasis>replacing</emphasis> any existing
	 * values for the provided parameter names. To add without replacing
	 * existing values, use {@link #addParameters(java.util.Map)}.
	 * @param params parameters
	 */
	@SuppressWarnings("rawtypes")
	public void setParameters(Map params) {
		Asserts.notNull(params, "Parameter map must not be null");
		for (Object key : params.keySet()) {
			Asserts.isInstanceOf(String.class, key, "Parameter map key must be of type [" + String.class.getName() + "]");
			Object value = params.get(key);
			if (value instanceof String) {
				this.setParameter((String) key, (String) value);
			}
			else if (value instanceof String[]) {
				this.setParameter((String) key, (String[]) value);
			}
			else {
				throw new IllegalArgumentException("Parameter map value must be single value " + " or array of type ["
						+ String.class.getName() + "]");
			}
		}
	}

	/**
	 * Add a single value for the specified HTTP parameter.
	 * <p>
	 * If there are already one or more values registered for the given
	 * parameter name, the given value will be added to the end of the list.
	 * @param name name
	 * @param value value
	 */
	public void addParameter(String name, String value) {
		addParameter(name, new String[] { value });
	}

	/**
	 * Add an array of values for the specified HTTP parameter.
	 * <p>
	 * If there are already one or more values registered for the given
	 * parameter name, the given values will be added to the end of the list.
	 * @param name name
	 * @param values values
	 */
	public void addParameter(String name, String[] values) {
		Asserts.notNull(name, "Parameter name must not be null");
		String[] oldArr = this.parameters.get(name);
		if (oldArr != null) {
			String[] newArr = new String[oldArr.length + values.length];
			System.arraycopy(oldArr, 0, newArr, 0, oldArr.length);
			System.arraycopy(values, 0, newArr, oldArr.length, values.length);
			this.parameters.put(name, newArr);
		}
		else {
			this.parameters.put(name, values);
		}
	}

	/**
	 * Adds all provided parameters <emphasis>without</emphasis> replacing any
	 * existing values. To replace existing values, use
	 * {@link #setParameters(java.util.Map)}.
	 * @param params parameters
	 */
	@SuppressWarnings("unchecked")
	public void addParameters(Map<String, Object> params) {
		Asserts.notNull(params, "Parameter map must not be null");
		for (Entry<String, Object> en : params.entrySet()) {
			String key = en.getKey();
			Asserts.isInstanceOf(String.class, en.getKey(), "Parameter map key must be of type [" + String.class.getName() + "]");

			Object value = en.getValue();
			if (value instanceof String) {
				this.addParameter(key, (String) value);
			}
			else if (value instanceof String[]) {
				this.addParameter(key, (String[]) value);
			}
			else if (value instanceof Collection) {
				this.addParameter(key, ((Collection<String>)value).toArray(new String[0]));
			}
			else {
				throw new IllegalArgumentException("Parameter map value must be single value " + " or array of type ["
						+ String.class.getName() + "]");
			}
		}
	}

	/**
	 * Remove already registered values for the specified HTTP parameter, if
	 * any.
	 * @param name name
	 */
	public void removeParameter(String name) {
		Asserts.notNull(name, "Parameter name must not be null");
		this.parameters.remove(name);
	}

	/**
	 * Removes all existing parameters.
	 */
	public void removeAllParameters() {
		this.parameters.clear();
	}

	public String getParameter(String name) {
		Asserts.notNull(name, "Parameter name must not be null");
		String[] arr = this.parameters.get(name);
		return (arr != null && arr.length > 0 ? arr[0] : null);
	}

	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.parameters.keySet());
	}

	public String[] getParameterValues(String name) {
		Asserts.notNull(name, "Parameter name must not be null");
		return this.parameters.get(name);
	}

	public Map<String, String[]> getParameterMap() {
		return Collections.unmodifiableMap(this.parameters);
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProtocol() {
		return this.protocol;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getScheme() {
		return this.scheme;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getRemoteAddr() {
		return this.remoteAddr;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public String getRemoteHost() {
		return this.remoteHost;
	}

	public void setAttribute(String name, Object value) {
		checkActive();
		Asserts.notNull(name, "Attribute name must not be null");
		if (value != null) {
			this.attributes.put(name, value);
		}
		else {
			this.attributes.remove(name);
		}
	}

	public void removeAttribute(String name) {
		checkActive();
		Asserts.notNull(name, "Attribute name must not be null");
		this.attributes.remove(name);
	}

	/**
	 * Clear all of this request's attributes.
	 */
	public void clearAttributes() {
		this.attributes.clear();
	}

	/**
	 * Add a new preferred locale, before any existing locales.
	 * @param locale locale
	 */
	public void addPreferredLocale(Locale locale) {
		Asserts.notNull(locale, "Locale must not be null");
		this.locales.add(0, locale);
	}

	public Locale getLocale() {
		return this.locales.get(0);
	}

	public Enumeration<Locale> getLocales() {
		return Collections.enumeration(this.locales);
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public boolean isSecure() {
		return this.secure;
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return new MockRequestDispatcher(path);
	}

	public String getRealPath(String path) {
		return this.servletContext.getRealPath(path);
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public int getRemotePort() {
		return this.remotePort;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getLocalName() {
		return this.localName;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public String getLocalAddr() {
		return this.localAddr;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public int getLocalPort() {
		return this.localPort;
	}


	// ---------------------------------------------------------------------
	// HttpServletRequest interface
	// ---------------------------------------------------------------------

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getAuthType() {
		return this.authType;
	}

	public void setCookies(Cookie... cookies) {
		this.cookies = cookies;
	}

	public Cookie[] getCookies() {
		return this.cookies;
	}

	/**
	 * Add a header entry for the given name.
	 * <p>If there was no entry for that header name before, the value will be used
	 * as-is. In case of an existing entry, a String array will be created,
	 * adding the given value (more specifically, its toString representation)
	 * as further element.
	 * <p>Multiple values can only be stored as list of Strings, following the
	 * Servlet spec (see <code>getHeaders</code> accessor). As alternative to
	 * repeated <code>addHeader</code> calls for individual elements, you can
	 * use a single call with an entire array or Collection of values as
	 * parameter.
	 * @param name name
	 * @param value value
	 * @see #getHeaderNames
	 * @see #getHeader
	 * @see #getHeaders
	 * @see #getDateHeader
	 * @see #getIntHeader
	 */
	public void addHeader(String name, String value) {
		if (CONTENT_TYPE_HEADER.equalsIgnoreCase(name)) {
			setContentType((String) value);
			return;
		}
		doAddHeaderValue(name, value, false);
	}
	
	private void doAddHeaderValue(String name, String value, boolean replace) {
		if (replace) {
			headers.set(name, value);
		}
		else {
			headers.add(name, value);
		}
	}

	public long getDateHeader(String name) {
		Date d = headers.getDate(name);
		return d == null ? -1 : d.getTime();
	}

	public int getIntHeader(String name) {
		return headers.getInt(name);
	}

	public String getHeader(String name) {
		return headers.getString(name);
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getHeaders(String name) {
		List<String> ss = headers.getStrings(name);
		return Collections.enumeration(ss != null ? ss : Collections.EMPTY_LIST);
	}

	public Enumeration<String> getHeaderNames() {
		return Collections.enumeration(this.headers.keySet());
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return this.method;
	}

	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	public String getPathInfo() {
		return this.pathInfo;
	}

	public String getPathTranslated() {
		return (this.pathInfo != null ? getRealPath(this.pathInfo) : null);
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getContextPath() {
		return this.contextPath;
	}

	public String getQueryString() {
		if (queryString == null) {
			queryString = "";
			if (HttpMethod.GET.equals(method) && Collections.isNotEmpty(parameters)) {
				queryString = URLBuilder.buildQueryString(parameters, characterEncoding);
			}
		}
		return queryString;
	}

	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}

	public String getRemoteUser() {
		return this.remoteUser;
	}

	public void addUserRole(String role) {
		this.userRoles.add(role);
	}

	public boolean isUserInRole(String role) {
		return (this.userRoles.contains(role) || (this.servletContext instanceof MockServletContext &&
				((MockServletContext) this.servletContext).getDeclaredRoles().contains(role)));
	}

	public void setUserPrincipal(Principal userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	public Principal getUserPrincipal() {
		return this.userPrincipal;
	}

	public void setRequestedSessionId(String requestedSessionId) {
		this.requestedSessionId = requestedSessionId;
	}

	public String getRequestedSessionId() {
		return this.requestedSessionId;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getRequestURI() {
		return this.requestURI;
	}

	public StringBuffer getRequestURL() {
		URLBuilder ub = new URLBuilder();
		ub.setScheme(scheme);
		ub.setHost(serverName);
		ub.setPort(serverPort);
		ub.setPath(requestURI);
		ub.setQuery(getQueryString());
		return new StringBuffer(ub.build());
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public String getServletPath() {
		return this.servletPath;
	}

	public void setSession(HttpSession session) {
		this.session = session;
		if (session instanceof MockHttpSession) {
			MockHttpSession mockSession = ((MockHttpSession) session);
			mockSession.access();
		}
	}

	public HttpSession getSession(boolean create) {
		checkActive();
		// Reset session if invalidated.
		if (this.session instanceof MockHttpSession && ((MockHttpSession) this.session).isInvalid()) {
			this.session = null;
		}
		// Create new session if necessary.
		if (this.session == null && create) {
			this.session = new MockHttpSession(this.servletContext);
		}
		return this.session;
	}

	public HttpSession getSession() {
		return getSession(true);
	}

	public void setRequestedSessionIdValid(boolean requestedSessionIdValid) {
		this.requestedSessionIdValid = requestedSessionIdValid;
	}

	public boolean isRequestedSessionIdValid() {
		return this.requestedSessionIdValid;
	}

	public void setRequestedSessionIdFromCookie(boolean requestedSessionIdFromCookie) {
		this.requestedSessionIdFromCookie = requestedSessionIdFromCookie;
	}

	public boolean isRequestedSessionIdFromCookie() {
		return this.requestedSessionIdFromCookie;
	}

	public void setRequestedSessionIdFromURL(boolean requestedSessionIdFromURL) {
		this.requestedSessionIdFromURL = requestedSessionIdFromURL;
	}

	public boolean isRequestedSessionIdFromURL() {
		return this.requestedSessionIdFromURL;
	}

	public boolean isRequestedSessionIdFromUrl() {
		return isRequestedSessionIdFromURL();
	}

	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return (this.userPrincipal != null && this.remoteUser != null && this.authType != null);
	}

	public void login(String username, String password) throws ServletException {
		throw new ServletException("Username-password authentication not supported - override the login method");
	}

	public void logout() throws ServletException {
		this.userPrincipal = null;
		this.remoteUser = null;
		this.authType = null;
	}

	//-----------------------------------------------------------
	// Servlet 3.0
	//
	@Override
	public AsyncContext getAsyncContext() {
		return null;
	}

	public long getContentLengthLong() {
		return getContentLength();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException {
		return null;
	}

	public String changeSessionId() {
		return null;
	}

	@Override
	public Part getPart(String arg0) throws IOException, ServletException {
		return null;
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return null;
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
		return null;
	}

}
