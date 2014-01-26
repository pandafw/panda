package panda.net.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.io.Files;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Strings;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;

/**
 * @author yf.frank.wang@gmail.com
 */
//TODO: local cache
public class HttpClient {
	protected static Log log = Logs.getLog(HttpClient.class);

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
		return new HttpClient(HttpRequest.get(url)).setTimeout(timeout).send();
	}

	public static HttpResponse get(String url, Map<String, Object> params) throws IOException {
		return send(url, HttpMethod.GET, params);
	}
	
	public static HttpResponse post(String url) throws IOException {
		return send(url, HttpMethod.POST, null);
	}
	
	public static HttpResponse post(String url, Map<String, Object> forms) throws IOException {
		return send(url, HttpMethod.POST, forms);
	}
	
	public static HttpResponse send(String url, HttpMethod method, Map<String, Object> forms) throws IOException {
		return new HttpClient(HttpRequest.create(url, method, forms)).send();
	}
	
	//---------------------------------------------------------------------
	protected HttpRequest request;
	protected HttpResponse response;

	protected Proxy proxy;
	protected HttpURLConnection conn;
	protected int timeout;

	protected boolean autoRedirect;
	
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
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public HttpClient setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
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

	public HttpResponse doPostFiles() throws IOException {
		try {
			String boundary = "---------------------------[Panda]7d91571440efc";
			openConnection();
			setupRequestHeader();
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			setupDoInputOutputFlag();
			Map<String, Object> params = request.getParams();
			if (null != params && params.size() > 0) {
				DataOutputStream outs = new DataOutputStream(conn.getOutputStream());
				for (Entry<String, ?> entry : params.entrySet()) {
					outs.writeBytes("--" + boundary + Strings.CRLF);
					String key = entry.getKey();
					File f = null;
					if (entry.getValue() instanceof File)
						f = (File)entry.getValue();
					else if (entry.getValue() instanceof String)
						f = new File(entry.getValue().toString());
					if (f != null && f.exists()) {
						outs.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\";    filename=\""
								+ entry.getValue() + "\"\r\n");
						outs.writeBytes("Content-Type:   application/octet-stream\r\n\r\n");
						if (f.length() == 0)
							continue;

						Streams.copy(f, outs);
						outs.writeBytes("\r\n");
					}
					else {
						outs.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\"\r\n\r\n");
						outs.writeBytes(entry.getValue() + "\r\n");
					}
				}
				outs.writeBytes("--" + boundary + "--" + Strings.CRLF);
				Streams.safeFlush(outs);
				Streams.safeClose(outs);
			}

			return createResponse();

		}
		catch (IOException e) {
			throw new IOException(request.getURL().toString(), e);
		}
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
			InputStream ins = request.getInputStream();
			if (null != ins) {
				OutputStream ops = null;
				try {
					ops = conn.getOutputStream();
					Streams.copy(ins, ops);
				}
				finally {
					Streams.safeClose(ins);
					Streams.safeFlush(ops);
					Streams.safeClose(ops);
				}
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

		conn.setConnectTimeout(DEFAULT_CONN_TIMEOUT);
		conn.setReadTimeout(timeout > 0 ? timeout : DEFAULT_READ_TIMEOUT);
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
	}
}
