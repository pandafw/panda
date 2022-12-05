package panda.net.http;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.Panda;
import panda.codec.binary.Base64;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.io.stream.WriterOutputStream;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Iterators;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.net.Mimes;
import panda.net.URLBuilder;
import panda.vfs.FileItem;

public class HttpRequest {
	// -------------------------------------------------------------
	public static final String DEFAULT_USERAGENT = "Panda/" + Panda.VERSION + " (Java " + Systems.JAVA_VERSION + ")";

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

	public static HttpRequest create(String url, String method) {
		return new HttpRequest().setUrl(url).setMethod(method);
	}

	public static HttpRequest create(String url, String method, Map<String, ?> params) {
		return new HttpRequest().setUrl(url).setMethod(method).setParams(params);
	}

	public static HttpRequest create(String url, String method, Map<String, ?> params, HttpHeader header) {
		return new HttpRequest().setUrl(url).setMethod(method).setParams(params).setHeader(header);
	}

	private String url;
	private String method;
	private HttpHeader header;
	private Map<String, Object> params;
	private InputStream body;
	private String encoding = Charsets.UTF_8;
	private String boundary;

	public HttpRequest() {
	}

	public String getMethod() {
		return method;
	}

	public boolean isGet() {
		return HttpMethod.GET.equalsIgnoreCase(method);
	}

	public boolean isPost() {
		return HttpMethod.POST.equalsIgnoreCase(method);
	}

	public HttpRequest setMethod(String method) {
		this.method = Strings.upperCase(method);
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
	 * @return this
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
		if (url != null && url.indexOf("://") < 0) {
			// default http protocol
			this.url = "http://" + url;
		}
		else {
			this.url = url;
		}
		return this;
	}
	
	/**
	 * @return the url with query string
	 */
	public URL getURL() {
		String url = this.url;
		if (!isPost() && Collections.isNotEmpty(params)) {
			url = URLBuilder.buildURL(url, params, encoding);
		}

		try {
			return new URL(url);
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL: " + url, e);
		}
	}

	public Map<String, Object> getParams() {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		return params;
	}

	public String getURLEncodedParams() {
		return URLBuilder.buildQueryString(params, encoding);
	}

	@SuppressWarnings("unchecked")
	public HttpRequest setParams(Map<String, ?> params) {
		this.params = (Map<String, Object>)params;
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

	public HttpRequest clearParams() {
		if (params != null) {
			params.clear();
		}
		return this;
	}

	public String getContentEncoding() {
		return getHeader().getContentEncoding();
	}

	public HttpRequest setContentEncoding(String encoding) {
		getHeader().setContentEncoding(encoding);
		return this;
	}

	public String getContentType() {
		return getHeader().getContentType();
	}

	public HttpRequest setContentType(String type) {
		getHeader().setContentType(type);
		return this;
	}

	public String getUserAgent() {
		return getHeader().getUserAgent();
	}
	
	public HttpRequest setUserAgent(String agent) {
		getHeader().setUserAgent(agent);
		return this;
	}
	
	public HttpRequest setDefault() {
		setDefaultHeaders();
		setUserAgent(DEFAULT_USERAGENT);
		return this;
	}

	protected HttpRequest setDefaultHeaders() {
		getHeader().setAccept("text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		getHeader().setAcceptEncoding("gzip,deflate");
		getHeader().setAcceptLanguage("en-US,en,ja,zh,zh-CN,zh-TW");
		getHeader().setAcceptCharset("ISO-8859-1,*,utf-8");
		getHeader().setKeepAlive(true);
		return this;
	}

	public HttpRequest asWindowsIE11() {
		setDefaultHeaders();
		setUserAgent(UserAgent.UA_WINDOWS_IE11);
		return this;
	}

	public HttpRequest asWindowsEdge() {
		setDefaultHeaders();
		setUserAgent(UserAgent.UA_WINDOWS_EDGE);
		return this;
	}

	public HttpRequest asWindowsChrome() {
		setDefaultHeaders();
		setUserAgent(UserAgent.UA_WINDOWS_CHROME);
		return this;
	}

	public HttpRequest asWindowsFirefox() {
		setDefaultHeaders();
		setUserAgent(UserAgent.UA_WINDOWS_FIREFOX);
		return this;
	}

	public HttpRequest asAndroidChrome() {
		setDefaultHeaders();
		setUserAgent(UserAgent.UA_ANDROID_CHROME);
		return this;
	}

	public HttpRequest asIPhoneSafari() {
		setDefaultHeaders();
		setUserAgent(UserAgent.UA_IPHONE_SAFARI);
		return this;
	}

	public HttpRequest asIPadSafari() {
		setDefaultHeaders();
		setUserAgent(UserAgent.UA_IPAD_SAFARI);
		return this;
	}
	
	
	public Map<String, Cookie> getCookieMap() {
		Map<String, Cookie> cs = new HashMap<String, Cookie>();
		String hc = getHeader().getString(HttpHeader.COOKIE);
		String[] ss = Strings.split(hc, ';');
		for (String s : ss) {
			Cookie c = new Cookie(s);
			cs.put(c.getName(), c);
		}
		return cs;
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

	public String getAuthentication() {
		return getHeader().getAuthentication();
	}

	public HttpRequest setAuthentication(String value) {
		getHeader().setAuthentication(value);
		return this;
	}

	public HttpRequest setBasicAuthentication(String username, String password, String separator) {
		try {
			byte[] b = (username + separator + password).getBytes(encoding);
			String v = Base64.encodeBase64String(b);
			return setAuthentication("Basic " + v);
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public HttpRequest setBasicAuthentication(String username, String password) {
		return setBasicAuthentication(username, password, ":");
	}

	//------------------------------------------------------------
	private boolean isFile(Object v) {
		return (v instanceof File) || (v instanceof FileItem);
	}

	public String getMultipartBoundary() {
		if (boundary == null) {
			boundary = Randoms.randLetterNumbers(30);
		}
		return boundary;
	}

	public boolean isPostForm() {
		return (HttpMethod.POST.equalsIgnoreCase(method) && body == null && Collections.isNotEmpty(params));
	}

	public boolean isPostFile() {
		if (HttpMethod.POST.equalsIgnoreCase(method) && body == null && Collections.isNotEmpty(params)) {
			for (Entry<String, ?> en : params.entrySet()) {
				if (isFile(en.getValue())) {
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean isGzipEncoding() {
		return HttpHeader.CONTENT_ENCODING_GZIP.equalsIgnoreCase(getContentEncoding());
	}

	public boolean isDeflateEncoding() {
		return HttpHeader.CONTENT_ENCODING_DEFLATE.equalsIgnoreCase(getContentEncoding());
	}

	public void writeBody(OutputStream os) throws IOException {
		try {
			os = Streams.buffer(os);
			if (isGzipEncoding()) {
				os = Streams.gzip(os);
			}
			else if (isDeflateEncoding()) {
				os = Streams.inflater(os);
			}

			if (body == null) {
				writeBodyParams(os, -1);
			}
			else {
				Streams.copy(body, os);
			}
		}
		finally {
			Streams.safeClose(os);
		}
	}
	
	protected boolean writeBodyParams(OutputStream os, int limit) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		if (isPostFile()) {
			Iterator<Entry<String, Object>> it = params.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> en = it.next();
				
				String key = en.getKey();
				Object val = en.getValue();

				if (Iterators.isIterable(val)) {
					for (Iterator itv = Iterators.asIterator(val); itv.hasNext(); ) {
						writeBodyParam(dos, key, itv.next(), limit);
						if (limit > 0 && dos.size() >= limit) {
							return false;
						}
					}
				}
				else {
					writeBodyParam(dos, key, val, limit);
					if (limit > 0 && dos.size() >= limit) {
						return false;
					}
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
		return true;
	}

	protected void writeBodyParam(DataOutputStream dos, String key, Object val, int limit) throws IOException {
		dos.writeBytes("--");
		dos.writeBytes(getMultipartBoundary());
		dos.writeBytes(Strings.CRLF);
		
		dos.writeBytes(HttpHeader.CONTENT_DISPOSITION);
		dos.writeBytes(": form-data; name=\"");
		dos.writeBytes(Mimes.encodeText(key, encoding));
		dos.writeBytes("\"");

		if (isFile(val)) {
			String path = getFilePath(val);
			dos.writeBytes("; filename=\"");
			dos.writeBytes(Mimes.encodeText(path, encoding));
			dos.writeBytes("\"");
			dos.writeBytes(Strings.CRLF);

			dos.writeBytes(HttpHeader.CONTENT_TYPE);
			dos.writeBytes(": ");
			dos.writeBytes(MimeTypes.getMimeType(path));

			dos.writeBytes(Strings.CRLF);
			dos.writeBytes(Strings.CRLF);
			long len = getFileLength(val);
			if (len > 0) {
				InputStream fis = openFile(val);
				try {
					Streams.copyLarge(fis, dos, 0, limit);
					dos.writeBytes(Strings.CRLF);
				}
				finally {
					Streams.safeClose(fis);
				}
			}
		}
		else {
			dos.writeBytes(Strings.CRLF);
			dos.writeBytes(Strings.CRLF);
			if (val != null) {
				dos.write(val.toString().getBytes(encoding));
			}
			dos.writeBytes(Strings.CRLF);
		}
	}
	
	private String getFilePath(Object file) {
		if (file instanceof File) {
			return ((File)file).getPath();
		}
		if (file instanceof FileItem) {
			return ((FileItem)file).getName();
		}
		return "";
	}
	
	private long getFileLength(Object file) {
		if (file instanceof File) {
			return ((File)file).length();
		}
		if (file instanceof FileItem) {
			return ((FileItem)file).getSize();
		}
		return 0L;
	}
	
	private InputStream openFile(Object file) throws IOException {
		if (file instanceof File) {
			return new FileInputStream((File)file);
		}
		if (file instanceof FileItem) {
			return ((FileItem)file).open();
		}
		return null;
	}

	/**
	 * @param writer writer
	 * @throws IOException if an IO error occurs
	 */
	public void toString(Appendable writer) throws IOException {
		toString(writer, TOSTRING_BODY_LIMIT);
	}
	
	/**
	 * @param writer writer
	 * @param limit body size limit
	 * @throws IOException if an IO error occurs
	 */
	public void toString(Appendable writer, int limit) throws IOException {
		writer.append(method).append(' ').append(getURL().toString());
		if (header != null) {
			writer.append(Streams.EOL);
			header.write(writer);
		}
		if (isPost()) {
			writer.append(Streams.EOL);
			WriterOutputStream wos = new WriterOutputStream(writer, encoding);
			if (body != null) {
				if (body.markSupported()) {
					body.mark(Integer.MAX_VALUE);
					Streams.copyLarge(body, wos, 0, limit);
					wos.flush();
					if (body.available() > 0) {
						writer.append(Streams.EOL).append("...");
					}
					body.reset();
					writer.append(Streams.EOL);
				}
				writer.append("<<stream: " + body + " [" + body.available() + "] >>");
			}
			else {
				if (!writeBodyParams(wos, limit)) {
					writer.append(Streams.EOL).append("...");
				}
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
	 * @param limit body size limit
	 * @return request string
	 */
	public String toString(int limit) {
		StringBuilder sb = new StringBuilder();
		try {
			toString(sb, limit);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return sb.toString();
	}
}
