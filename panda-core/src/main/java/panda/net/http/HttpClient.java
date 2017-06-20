package panda.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import panda.Panda;
import panda.io.Files;
import panda.io.MimeType;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Strings;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.net.URLHelper;

/**
 */
public class HttpClient {
	protected static Log log = Logs.getLog(HttpClient.class);

	public static final String DEFAULT_USERAGENT = HttpClient.class.getName() + '/' + Panda.VERSION;

	public static final String DEFAULT_USERAGENT_PC = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36";

	public static HttpResponse get(String url) throws IOException {
		return send(url, HttpMethod.GET, null);
	}
	
	public static HttpResponse get(String url, int timeout) throws IOException {
		return send(url, HttpMethod.GET, null, null, timeout);
	}

	public static HttpResponse get(String url, Map<String, Object> params) throws IOException {
		return send(url, HttpMethod.GET, params);
	}
	
	public static HttpResponse post(String url) throws IOException {
		return send(url, HttpMethod.POST, null);
	}
	
	public static HttpResponse post(String url, Map<String, Object> params) throws IOException {
		return send(url, HttpMethod.POST, params);
	}

	public static HttpResponse post(String url, byte[] body) throws IOException {
		return send(url, HttpMethod.POST, null, body, 0);
	}
	
	public static HttpResponse send(String url, String method, Map<String, Object> params) throws IOException {
		return send(url, method, params, null, 0);
	}
	
	public static HttpResponse send(String url, String method, Map<String, Object> params, byte[] body, int timeout) throws IOException {
		HttpRequest hr = HttpRequest.create(url, method, params);
		hr.getHeader().setDefaultAgentPC();
		hr.setBody(body);
		
		HttpClient hc = new HttpClient(hr);
		hc.setReadTimeout(timeout);
		return hc.send();
	}
	
	//---------------------------------------------------------------------
	protected HttpRequest request;
	protected HttpResponse response;

	protected Proxy proxy;
	protected HttpURLConnection conn;

	/**
	 * connection timeout (default: 30s)
	 */
	protected int connTimeout = 30000;
	
	/**
	 * read timeout (default: 300s)
	 */
	protected int readTimeout = 300000;

	protected boolean autoRedirect = false;
	protected boolean validateSslCert = true;
	
	static {
		// fix error: java.net.ProtocolException: Server redirected too many times
		// see http://stackoverflow.com/questions/11022934/getting-java-net-protocolexception-server-redirected-too-many-times-error
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	}

	public HttpClient() {
		this(new HttpRequest());
	}

	public HttpClient(boolean autoRedirect) {
		this(new HttpRequest(), autoRedirect);
	}

	public HttpClient(HttpRequest request) {
		this(request, true);
	}

	public HttpClient(HttpRequest request, boolean autoRedirect) {
		this.autoRedirect = autoRedirect;
		setRequest(request);
	}

	/**
	 * @return the request
	 */
	public HttpRequest getReq() {
		return request;
	}

	/**
	 * @return the request
	 */
	public HttpRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpRequest request) {
		Asserts.notNull(request, "the request object is null");
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public HttpResponse getRes() {
		return response;
	}

	/**
	 * @return the response
	 */
	public HttpResponse getResponse() {
		return response;
	}

	/**
	 * @return the autoRedirect
	 */
	public boolean isAutoRedirect() {
		return autoRedirect;
	}

	/**
	 * @param autoRedirect the autoRedirect to set
	 */
	public void setAutoRedirect(boolean autoRedirect) {
		this.autoRedirect = autoRedirect;
	}

	/**
	 * @return the validateSslCert
	 */
	public boolean isValidateSslCert() {
		return validateSslCert;
	}

	/**
	 * @param validateSslCert the validateSslCert to set
	 */
	public void setValidateSslCert(boolean validateSslCert) {
		this.validateSslCert = validateSslCert;
	}

	/**
	 * @return the connTimeout
	 */
	public int getConnTimeout() {
		return connTimeout;
	}

	/**
	 * @param connTimeout the connTimeout to set
	 */
	public void setConnTimeout(int connTimeout) {
		if (connTimeout > 0) {
			this.connTimeout = connTimeout;
		}
	}

	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * @param readTimeout the readTimeout to set
	 */
	public void setReadTimeout(int readTimeout) {
		if (readTimeout > 0) {
			this.readTimeout = readTimeout;
		}
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public HttpResponse doGet() throws IOException {
		request.setMethod(HttpMethod.GET);
		return send();
	}

	public HttpResponse doPost() throws IOException {
		request.setMethod(HttpMethod.POST);
		return send();
	}

	public HttpResponse send() throws IOException {
		if (!autoRedirect) {
			return doSend();
		}

		HttpResponse res = doSend();
		if (!res.isMoved()) {
			return res;
		}

		String url = request.getURL().toString();
		Set<String> urls = new HashSet<String>();
		urls.add(url);

		request.setMethod(HttpMethod.GET);
		request.setParams(null);
		
		while (true) {
			String location = res.getHeader().getString(HttpHeader.LOCATION);
			url = URLHelper.resolveURL(url, location);
			if (Strings.isEmpty(url)) {
				return res;
			}

			if (urls.contains(url)) {
				log.info("infinite redirect loop detected: " + url);
				return res;
			}

			urls.add(url);
			request.setUrl(url);

			res = doSend();
			if (!res.isMoved()) {
				return res;
			}
		}
	}
	
	protected HttpResponse doSend() throws IOException {
		if (log.isTraceEnabled()) {
			log.trace(request.toString(10240));
		}
		else if (log.isDebugEnabled()) {
			log.debug(request.toString(1024));
		}
		
		HttpResponse response;

		StopWatch sw = new StopWatch();
		if (request.isPost()) {
			openConnection();
			setupRequestHeader();
			setupDoInputOutputFlag();

			OutputStream os = null;
			try {
				os = conn.getOutputStream();
				request.writeBody(os);
				os.flush();
			}
			finally {
				Streams.safeClose(os);
			}
			response = createResponse();
		}
		else {
			openConnection();
			setupRequestHeader();
			response = createResponse();
		}

		if (log.isDebugEnabled()) {
			StringBuilder msg = new StringBuilder();
			msg.append(request.getMethod()).append(' ').append(request.getURL())
				.append(" - ").append(response.getStatusLine());
			msg.append(" (");
			if (response.getContentLength() != null) {
				msg.append(Files.toDisplaySize(response.getContentLength())).append(" / ");
			}
			msg.append(sw).append(')');
			if (response.getHeader() != null) {
				msg.append(Streams.LINE_SEPARATOR); 
				response.getHeader().write(msg);
			}

			String text = response.getContentText(log.isTraceEnabled() ? 10240 : 1024);
			if (Strings.isNotEmpty(text)) {
				msg.append(Streams.LINE_SEPARATOR).append(text);
			}

			if (log.isTraceEnabled()) {
				log.trace(msg.toString());
			}
			else {
				log.debug(msg.toString());
			}
		}
		return response;
	}

	protected HttpResponse createResponse() throws IOException {
		response = new HttpResponse(request.getURL(), conn);
		return response;
	}

	protected void setupDoInputOutputFlag() {
		conn.setDoInput(true);
		conn.setDoOutput(true);
	}

	protected void openConnection() throws IOException {
		if (proxy != null) {
			conn = (HttpURLConnection)request.getURL().openConnection(proxy);
		}
		else {
			conn = (HttpURLConnection)request.getURL().openConnection();
		}

		if (!validateSslCert) {
			Https.ignoreValidateCertification(conn);
		}
		conn.setConnectTimeout(connTimeout);
		conn.setReadTimeout(readTimeout);
		conn.setInstanceFollowRedirects(false);
	}

	protected void setupRequestHeader() {
		Https.setupRequestHeader(conn, request.getURL(), request.getHeader());
		
		if (request.isPostFile()) {
			conn.setRequestProperty(HttpHeader.CONTENT_TYPE, MimeType.MULTIPART_FORM_DATA + "; boundary=" + request.getMultipartBoundary());
		}
		else if (request.isPostForm()) {
			conn.setRequestProperty(HttpHeader.CONTENT_TYPE, MimeType.X_WWW_FORM_URLECODED + "; charset=" + request.getEncoding());
		}
	}
}
