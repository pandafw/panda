package panda.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import panda.io.Files;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import panda.net.http.ssl.ValidCertTrustManage;
import panda.net.http.ssl.ValidHostnameVerifier;

/**
 * @author yf.frank.wang@gmail.com
 */
//TODO: local cache
public class HttpClient {
	protected static Log log = Logs.getLog(HttpClient.class);
	private static TrustManager[] validSslCertTrusts = { new ValidCertTrustManage() }; 
	private static ValidHostnameVerifier validHostnameVerifier = new ValidHostnameVerifier();
	

	/**
	 * DEFAULT_CONN_TIMEOUT = 30 seconds
	 */
	public static int DEFAULT_CONN_TIMEOUT = 30 * 1000;

	/**
	 * DEFAULT_READ_TIMEOUT = 10 minutes
	 */
	public static int DEFAULT_READ_TIMEOUT = 10 * 60 * 1000;

	public static HttpResponse get(String url) throws IOException {
		return send(url, HttpMethod.GET, null);
	}
	
	public static HttpResponse get(String url, int timeout) throws IOException {
		return send(url, HttpMethod.GET, null, timeout);
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
	
	public static HttpResponse send(String url, HttpMethod method, Map<String, Object> params) throws IOException {
		return send(url, method, params, 0);
	}
	
	public static HttpResponse send(String url, HttpMethod method, Map<String, Object> params, int timeout) throws IOException {
		HttpRequest hr = HttpRequest.create(url, method, params);
		hr.getHeader().setDefault();
		
		HttpClient hc = new HttpClient(hr);
		hc.setReadTimeout(timeout);
		return hc.send();
	}
	
	//---------------------------------------------------------------------
	protected HttpRequest request;
	protected HttpResponse response;

	protected Proxy proxy;
	protected HttpURLConnection conn;

	protected int connTimeout;
	protected int readTimeout;

	protected boolean autoRedirect = false;
	protected boolean validateSslCert = true;
	
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
	 * @return the connTimeout
	 */
	public int getConnTimeout() {
		return connTimeout;
	}

	/**
	 * @param connTimeout the connTimeout to set
	 */
	public void setConnTimeout(int connTimeout) {
		this.connTimeout = connTimeout;
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
		this.readTimeout = readTimeout;
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
			url = URLHelper.concatURL(url, location);
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
		final String CRLF = Streams.LINE_SEPARATOR;

		if (log.isDebugEnabled()) {
			log.debug(request.toString());
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

		if (log.isInfoEnabled()) {
			StringBuilder msg = new StringBuilder();
			msg.append(request.getMethod()).append(' ').append(request.getURL())
				.append(" - ").append(response.getStatusLine());
			if (response.getContentLength() != null) {
				msg.append(" (").append(Files.toDisplaySize(response.getContentLength())).append(')');
			}
			msg.append(" [").append(sw).append(']');

			if (log.isDebugEnabled()) {
				if (response.getHeader() != null) {
					msg.append(CRLF); 
					response.getHeader().toString(msg);
				}

				String text = response.getContentText();
				if (Strings.isNotEmpty(text)) {
					msg.append(CRLF).append(text);
				}
				log.debug(msg);
			}
			else {
				log.info(msg);
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

		if (!validateSslCert && conn instanceof HttpsURLConnection) {
			ignoreValidateCertification((HttpsURLConnection)conn);
		}
		conn.setConnectTimeout(connTimeout > 0 ? connTimeout : DEFAULT_CONN_TIMEOUT);
		conn.setReadTimeout(readTimeout > 0 ? readTimeout : DEFAULT_READ_TIMEOUT);
	}

	protected void ignoreValidateCertification(HttpsURLConnection sconn) {
		try {
			SSLContext sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, validSslCertTrusts, new SecureRandom());
			sconn.setSSLSocketFactory(sslcontext.getSocketFactory());
			sconn.setHostnameVerifier(validHostnameVerifier);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	protected void setupRequestHeader() {
		URL url = request.getURL();
		String host = url.getHost();
		if (url.getPort() > 0 && url.getPort() != 80) {
			host += ":" + url.getPort();
		}
		conn.setRequestProperty(HttpHeader.HOST, host);
		
		HttpHeader header = request.getHeader();
		if (null != header) {
			for (Entry<String, Object> en : header.entrySet()) {
				String key = en.getKey();
				Object val = en.getValue();
				if (val instanceof List) {
					for (Object v : (List)val) {
						conn.addRequestProperty(key, v.toString());
					}
				}
				else {
					conn.addRequestProperty(key, val.toString());
				}
			}
		}
		
		if (request.isPostFile()) {
			conn.setRequestProperty(HttpHeader.CONTENT_TYPE, HttpHeader.MULTIPART_FORM_DATA + "; boundary=" + request.getMultipartBoundary());
		}
		else if (request.isPostForm()) {
			conn.setRequestProperty(HttpHeader.CONTENT_TYPE, HttpHeader.X_WWW_FORM_URLECODED + "; charset=" + request.getEncoding());
		}
	}
}
