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

import panda.io.Files;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.net.URLHelper;

/**
 */
public class HttpClient {
	protected static Log log = Logs.getLog(HttpClient.class);

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
		hr.setDefault();
		hr.setBody(body);
		
		HttpClient hc = new HttpClient(hr);
		hc.setReadTimeout(timeout);
		return hc.send();
	}
	
	//---------------------------------------------------------------------
	protected HttpURLConnection httpconn;
	protected HttpRequest request;
	protected HttpResponse response;

	protected Proxy proxy;

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
	protected String[] enabledSslProtocols;
	
	static {
		if (Systems.IS_JAVA_1_6) {
			// fix error: java.net.ProtocolException: Server redirected too many times (JDK1.6)
			// see http://stackoverflow.com/questions/11022934/getting-java-net-protocolexception-server-redirected-too-many-times-error
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		}
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
	 * @return the enabledSslProtocols
	 */
	public String[] getEnabledSslProtocols() {
		return enabledSslProtocols;
	}

	/**
	 * @param enabledSslProtocols the enabledSslProtocols to set
	 */
	public void setEnabledSslProtocols(String ... enabledSslProtocols) {
		this.enabledSslProtocols = enabledSslProtocols;
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

		doSend();
		if (!response.isMoved()) {
			return response;
		}

		String url = request.getURL().toString();
		Set<String> urls = new HashSet<String>();
		urls.add(url);

		request.setMethod(HttpMethod.GET);
		request.setParams(null);
		
		while (true) {
			String location = response.getHeader().getString(HttpHeader.LOCATION);
			url = URLHelper.resolveURL(url, location);
			if (Strings.isEmpty(url)) {
				return response;
			}

			if (urls.contains(url)) {
				log.info("infinite redirect loop detected: " + url);
				return response;
			}

			urls.add(url);
			request.setUrl(url);

			doSend();
			if (!response.isMoved()) {
				return response;
			}
		}
	}
	
	protected HttpResponse doSend() throws IOException {
		if (log.isTraceEnabled()) {
			log.trace(request.toString(102400));
		}
		else if (log.isDebugEnabled()) {
			log.debug(request.toString(1024));
		}
		
		StopWatch sw = new StopWatch();
		if (request.isPost()) {
			openConnection();
			setupRequestHeader();
			setupDoInputOutputFlag();

			OutputStream os = httpconn.getOutputStream();
			request.writeBody(os);
		}
		else {
			openConnection();
			setupRequestHeader();
		}
		createResponse();

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

	protected void createResponse() throws IOException {
		response = new HttpResponse(request.getURL(), httpconn);
	}

	protected void setupDoInputOutputFlag() {
		httpconn.setDoInput(true);
		httpconn.setDoOutput(true);
	}

	protected void openConnection() throws IOException {
		if (proxy != null) {
			httpconn = (HttpURLConnection)request.getURL().openConnection(proxy);
		}
		else {
			httpconn = (HttpURLConnection)request.getURL().openConnection();
		}

		if (!validateSslCert) {
			Https.ignoreValidateCertification(httpconn);
		}
		if (Arrays.isNotEmpty(enabledSslProtocols)) {
			Https.setEnabledProtocals(httpconn, enabledSslProtocols);
		}
		httpconn.setConnectTimeout(connTimeout);
		httpconn.setReadTimeout(readTimeout);
		httpconn.setInstanceFollowRedirects(false);
	}

	protected void setupRequestHeader() {
		Https.setupRequestHeader(httpconn, request.getURL(), request.getHeader());
		
		if (request.isPostFile()) {
			httpconn.setRequestProperty(HttpHeader.CONTENT_TYPE, MimeTypes.MULTIPART_FORM_DATA + "; boundary=" + request.getMultipartBoundary());
		}
		else if (request.isPostForm()) {
			httpconn.setRequestProperty(HttpHeader.CONTENT_TYPE, MimeTypes.X_WWW_FORM_URLECODED + "; charset=" + request.getEncoding());
		}
	}
}
