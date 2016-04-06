package panda.net.http;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.io.Streams;
import panda.io.stream.WriterOutputStream;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;
import panda.net.URLHelper;

/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpRequest {
	private static final int TOSTRING_BODY_LIMIT = 1024;
	
	public static HttpRequest get(String url) {
		return new HttpRequest().setUrl(url).setMethod(HttpMethod.GET);
	}

	public static HttpRequest get(String url, HttpHeader header) {
		return new HttpRequest().setUrl(url).setMethod(HttpMethod.GET).setHeader(header);
	}

	public static HttpRequest post(String url) {
		return new HttpRequest().setUrl(url).setMethod(HttpMethod.POST);
	}

	public static HttpRequest post(String url, HttpHeader header) {
		return new HttpRequest().setUrl(url).setMethod(HttpMethod.POST).setHeader(header);
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
	private InputStream body;
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

	public HttpRequest addHeader(String name, Object value) {
		getHeader().add(name, value);
		return this;
	}

	public HttpRequest setHeader(String name, Object value) {
		getHeader().set(name, value);
		return this;
	}
	
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public HttpRequest setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public InputStream getBody() {
		return body;
	}

	public HttpRequest setBody(InputStream body) {
		this.body = body;
		return this;
	}

	public HttpRequest setBody(byte[] body) {
		this.body = (body == null ? null : new ByteArrayInputStream(body));
		return this;
	}

	public HttpRequest setBody(String body) {
		this.body = (body == null ? null : new ByteArrayInputStream(Strings.getBytes(body, encoding)));
		return this;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	public HttpRequest setUrl(String url) {
		if (url != null && url.indexOf("://") < 0)
			// default http protocol
			this.url = "http://" + url;
		else
			this.url = url;
		return this;
	}
	
	/**
	 * @return the url with query string
	 */
	public URL getURL() {
		StringBuilder sb = new StringBuilder(url);
		try {
			if (isGet() && Collections.isNotEmpty(params)) {
				URLHelper.appendQuerySeparator(sb, false);
				URLHelper.appendQueryString(sb, params, false, encoding);
			}
			return new URL(sb.toString());
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL: " + sb, e);
		}
	}

	public Map<String, Object> getParams() {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		return params;
	}

	public String getURLEncodedParams() {
		return URLHelper.buildQueryString(params, encoding);
	}

	public HttpRequest setParams(Map<String, Object> params) {
		this.params = params;
		return this;
	}

	public HttpRequest setParam(String name, Object value) {
		getParams().put(name, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public HttpRequest addParam(String name, Object value) {
		Object o = getParams().get(name);
		if (o == null) {
			getParams().put(name, value);
		}
		else if (o instanceof Collection) {
			((Collection)o).add(o);
		}
		else {
			List<Object> os = new ArrayList<Object>();
			os.add(o);
			os.add(value);
		}
		return this;
	}

	public List<Cookie> getCookies() {
		List<Cookie> cs = new ArrayList<Cookie>();
		String hc = getHeader().getString(HttpHeader.COOKIE);
		String[] ss = Strings.split(hc, ';');
		for (String s : ss) {
			cs.add(new Cookie(s));
		}
		return cs;
	}

	public HttpRequest setCookies(Cookie ... cookies) {
		return setCookies(Arrays.asList(cookies));
	}

	public HttpRequest setCookies(Collection<Cookie> cookies) {
		if (Collections.isEmpty(cookies)) {
			getHeader().remove(HttpHeader.COOKIE);
			return this;
		}
		
		StringBuilder sb = new StringBuilder();
		for (Cookie c : cookies) {
			if (sb.length() > 0) {
				sb.append("; ");
			}
			if (c.isValid()) {
				sb.append(c.getName()).append('=').append(c.getValue());
			}
		}
		
		if (sb.length() > 0) {
			getHeader().set(HttpHeader.COOKIE, sb.toString());
		}
		else {
			getHeader().remove(HttpHeader.COOKIE);
		}
		return this;
	}

	public HttpRequest setBasicAuthentication(String username, String password) {
		try {
			byte[] b = (username + ':' + password).getBytes(encoding);
			String v = "Basic " + Base64.encodeBase64String(b);
			getHeader().set(HttpHeader.AUTHORIZATION, v);
			return this;
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	//------------------------------------------------------------
	private boolean isFile(Object v) {
		return (v instanceof File);
	}

	public String getMultipartBoundary() {
		return "---------------------------[Panda]7d91571440efc";
	}

	public boolean isPostForm() {
		return (HttpMethod.POST.equals(method) && body == null && Collections.isNotEmpty(params));
	}

	public boolean isPostFile() {
		if (HttpMethod.POST.equals(method) && body == null && Collections.isNotEmpty(params)) {
			for (Entry<String, ?> en : params.entrySet()) {
				if (isFile(en.getValue())) {
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean isGzipEncoding() {
		return "gzip".equalsIgnoreCase(getHeader().getString(HttpHeader.CONTENT_ENCODING));
	}

	public void writeBody(OutputStream os) throws IOException {
		os = Streams.buffer(os);
		if (isGzipEncoding()) {
			os = Streams.gzip(os);
		}

		if (body != null) {
			Streams.copy(body, os);
			os.flush();
			return;
		}

		DataOutputStream dos = new DataOutputStream(os);
		if (isPostFile()) {
			for (Entry<String, ?> en : params.entrySet()) {
				dos.writeBytes("--");
				dos.writeBytes(getMultipartBoundary());
				dos.writeBytes(Strings.CRLF);
				
				String key = en.getKey();
				Object val = en.getValue();
				File f = null;
				if (isFile(val)) {
					f = (File)val;
				}
				if (f != null && f.exists()) {
					dos.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\";    filename=\"" + f.getPath() + "\"");
					dos.writeBytes(Strings.CRLF);
					dos.writeBytes("Content-Type:   application/octet-stream");
					dos.writeBytes(Strings.CRLF);
					dos.writeBytes(Strings.CRLF);
					if (f.length() > 0) {
						Streams.copy(f, dos);
						dos.writeBytes(Strings.CRLF);
					}
				}
				else {
					dos.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\"");
					dos.writeBytes(Strings.CRLF);
					dos.writeBytes(Strings.CRLF);
					dos.writeBytes(val.toString());
					dos.writeBytes(Strings.CRLF);
				}
			}

			dos.writeBytes("--");
			dos.writeBytes(getMultipartBoundary());
			dos.writeBytes("--");
			dos.writeBytes(Strings.CRLF);
		}
		else if (Collections.isNotEmpty(params)) {
			dos.writeBytes(getURLEncodedParams());
		}
		dos.flush();
	}

	/**
	 * @param writer writer
	 * @throws IOException
	 */
	public void toString(Appendable writer) throws IOException {
		toString(writer, TOSTRING_BODY_LIMIT);
	}
	
	/**
	 * @param writer writer
	 * @param bodyLimit body size limit
	 * @throws IOException
	 */
	public void toString(Appendable writer, int bodyLimit) throws IOException {
		writer.append(String.valueOf(method)).append(' ').append(getURL().toString());
		if (header != null) {
			writer.append(Streams.LINE_SEPARATOR);
			header.toString(writer);
		}
		if (!isGet()) {
			writer.append(Streams.LINE_SEPARATOR);
			WriterOutputStream wos = new WriterOutputStream(writer, encoding);
			if (body != null) {
				if (body.available() > bodyLimit || !body.markSupported()) {
					writer.append("<<stream: " + body + " - " + body.available() + ">>");
				}
				else {
					body.mark(Integer.MAX_VALUE);
					writeBody(wos);
					body.reset();
				}
			}
			else {
				writeBody(wos);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toString(TOSTRING_BODY_LIMIT);
	}

	/**
	 * @param bodyLimit body size limit
	 * @return request string
	 */
	public String toString(int bodyLimit) {
		StringBuilder sb = new StringBuilder();
		try {
			toString(sb, bodyLimit);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return sb.toString();
	}
}
