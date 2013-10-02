package panda.net.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpRequest {

	public static HttpRequest get(String url) {
		return create(url, HttpMethod.GET, new HashMap<String, Object>());
	}

	public static HttpRequest get(String url, HttpHeader header) {
		return HttpRequest.create(url, HttpMethod.GET, new HashMap<String, Object>(), header);
	}

	public static HttpRequest create(String url, HttpMethod method) {
		return new HttpRequest().setUrl(url).setMethod(method);
	}

	public static HttpRequest create(String url, HttpMethod method, Map<String, Object> params) {
		return new HttpRequest().setUrl(url).setMethod(method).setParams(params);
	}

	public static HttpRequest create(String url, HttpMethod method, Map<String, Object> params, HttpHeader header) {
		return new HttpRequest().setUrl(url).setMethod(method).setParams(params).setHeader(header);
	}

	private String url;
	private HttpMethod method;
	private HttpHeader header;
	private Map<String, Object> params;
	private byte[] data;
	private String encoding = Charsets.UTF_8;

	public HttpRequest() {
	}

	public HttpMethod getMethod() {
		return method;
	}

	public boolean isGet() {
		return HttpMethod.GET == method;
	}

	public boolean isPost() {
		return HttpMethod.POST == method;
	}

	public HttpRequest setMethod(HttpMethod method) {
		this.method = method;
		return this;
	}

	public HttpHeader getHeader() {
		if (header == null) {
			header = HttpHeader.create();
		}
		return header;
	}

	public HttpRequest setHeader(HttpHeader header) {
		this.header = header;
		return this;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public void setData(String data) {
		this.data = Strings.getBytes(data, encoding);
	}

	public HttpRequest setParams(Map<String, Object> params) {
		this.params = params;
		return this;
	}

	public HttpRequest setUrl(String url) {
		if (url != null && url.indexOf("://") < 0)
			// default http protocol
			this.url = "http://" + url;
		else
			this.url = url;
		return this;
	}

	public HttpRequest setCookies(Cookie cookies) {
		getHeader().set(HttpHeader.COOKIE, cookies.toString());
		return this;
	}

	public Cookie getCookies() {
		String s = getHeader().getString(HttpHeader.COOKIE);
		return new Cookie(s);
	}

	public URL getUrl() {
		StringBuilder sb = new StringBuilder(url);
		try {
			if (isGet() && Collections.isNotEmpty(params)) {
				sb.append(url.indexOf('?') > 0 ? '&' : '?');
				sb.append(getURLEncodedParams());
			}
			return new URL(sb.toString());
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException(sb.toString(), e);
		}
	}

	public Map<String, Object> getParams() {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		return params;
	}

	public String getURLEncodedParams() {
		return URLHelper.buildParametersString(params, encoding);
	}

	public InputStream getInputStream() {
		if (data == null) {
			return new ByteArrayInputStream(Strings.getBytesUtf8(getURLEncodedParams()));
		}
		return new ByteArrayInputStream(data);
	}

	public void toString(Appendable writer) throws IOException {
		writer.append(String.valueOf(method)).append(' ').append(getUrl().toString());
		if (header != null) {
			writer.append(Strings.CRLF);
			header.toString(writer);
		}
		if (!isGet()) {
			writer.append(Strings.CRLF);
			Streams.copy(getInputStream(), writer, encoding);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			toString(sb);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return sb.toString();
	}
}
