package panda.net.http;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpRequest {
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

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
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

	public HttpRequest setCookies(Cookie ... cookies) {
		return setCookies(Arrays.asList(cookies));
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

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
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

	private boolean isFile(Object v) {
		return (v instanceof File);
	}

	public String getMultipartBoundary() {
		return "---------------------------[Panda]7d91571440efc";
	}

	public boolean isPostForm() {
		return (HttpMethod.POST.equals(method) && Collections.isNotEmpty(params));
	}

	public boolean isPostFile() {
		if (HttpMethod.POST.equals(method) && Collections.isNotEmpty(params)) {
			for (Entry<String, ?> en : params.entrySet()) {
				if (isFile(en.getValue())) {
					return true;
				}
			}
		}
		
		return false;
	}

	public void writeBody(OutputStream os) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(os);
		DataOutputStream outs = new DataOutputStream(bos);
		if (isPostFile()) {
			for (Entry<String, ?> en : params.entrySet()) {
				outs.writeBytes("--");
				outs.writeBytes(getMultipartBoundary());
				outs.writeBytes(Strings.CRLF);
				
				String key = en.getKey();
				Object val = en.getValue();
				File f = null;
				if (isFile(val)) {
					f = (File)val;
				}
				if (f != null && f.exists()) {
					outs.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\";    filename=\"" + f.getPath() + "\"");
					outs.writeBytes(Strings.CRLF);
					outs.writeBytes("Content-Type:   application/octet-stream");
					outs.writeBytes(Strings.CRLF);
					outs.writeBytes(Strings.CRLF);
					if (f.length() > 0) {
						Streams.copy(f, outs);
						outs.writeBytes(Strings.CRLF);
					}
				}
				else {
					outs.writeBytes("Content-Disposition:    form-data;    name=\"" + key + "\"");
					outs.writeBytes(Strings.CRLF);
					outs.writeBytes(Strings.CRLF);
					outs.writeBytes(val.toString());
					outs.writeBytes(Strings.CRLF);
				}
			}

			outs.writeBytes("--");
			outs.writeBytes(getMultipartBoundary());
			outs.writeBytes("--");
			outs.writeBytes(Strings.CRLF);
		}
		else {
			outs.writeBytes(getURLEncodedParams());
		}

		outs.flush();;
	}

	public void toString(Appendable writer) throws IOException {
		writer.append(String.valueOf(method)).append(' ').append(getURL().toString());
		if (header != null) {
			writer.append(Streams.LINE_SEPARATOR);
			header.toString(writer);
		}
		if (!isGet()) {
			writer.append(Streams.LINE_SEPARATOR);
			WriterOutputStream wos = new WriterOutputStream(writer, encoding);
			writeBody(wos);
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
