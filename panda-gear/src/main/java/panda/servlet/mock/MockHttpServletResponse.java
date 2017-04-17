package panda.servlet.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Asserts;
import panda.lang.Chars;
import panda.lang.Strings;
import panda.net.http.HttpHeader;
import panda.servlet.DelegateServletOutputStream;
import panda.servlet.HttpServlets;

/**
 * Mock implementation of the {@link javax.servlet.http.HttpServletResponse} interface.
 *
 * <p>Compatible with Servlet 2.5 as well as Servlet 3.0.
 *
 */
public class MockHttpServletResponse implements HttpServletResponse {

	//---------------------------------------------------------------------
	// ServletResponse properties
	//---------------------------------------------------------------------

	private boolean outputStreamAccessAllowed = true;

	private boolean writerAccessAllowed = true;

	private String characterEncoding = HttpServlets.DEFAULT_CHARACTER_ENCODING;
	
	private boolean charset = false;

	private ByteArrayOutputStream content = new ByteArrayOutputStream();

	private ServletOutputStream outputStream = new ResponseServletOutputStream(this.content);

	private PrintWriter writer;

	private String contentType;

	private int bufferSize = 4096;

	private boolean committed;

	private Locale locale = Locale.getDefault();


	//---------------------------------------------------------------------
	// HttpServletResponse properties
	//---------------------------------------------------------------------

	private final List<Cookie> cookies = new ArrayList<Cookie>();

	private final HttpHeader headers = new HttpHeader();

	private int status = HttpServletResponse.SC_OK;

	private String errorMessage;

	private String forwardedUrl;

	private final List<String> includedUrls = new ArrayList<String>();


	//---------------------------------------------------------------------
	// ServletResponse interface
	//---------------------------------------------------------------------

	/**
	 * Set whether {@link #getOutputStream()} access is allowed.
	 * <p>Default is <code>true</code>.
	 */
	public void setOutputStreamAccessAllowed(boolean outputStreamAccessAllowed) {
		this.outputStreamAccessAllowed = outputStreamAccessAllowed;
	}

	/**
	 * Return whether {@link #getOutputStream()} access is allowed.
	 */
	public boolean isOutputStreamAccessAllowed() {
		return this.outputStreamAccessAllowed;
	}

	/**
	 * Set whether {@link #getWriter()} access is allowed.
	 * <p>Default is <code>true</code>.
	 */
	public void setWriterAccessAllowed(boolean writerAccessAllowed) {
		this.writerAccessAllowed = writerAccessAllowed;
	}

	/**
	 * Return whether {@link #getOutputStream()} access is allowed.
	 */
	public boolean isWriterAccessAllowed() {
		return this.writerAccessAllowed;
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
		this.charset = true;
		updateContentTypeHeader();
	}
	
	private void updateContentTypeHeader() {
		if (this.contentType != null) {
			StringBuilder sb = new StringBuilder(this.contentType);
			if (this.contentType.toLowerCase().indexOf(HttpServlets.CONTENT_TYPE_CHARSET_PREFIX) == -1 && this.charset) {
				sb.append(";").append(HttpServlets.CONTENT_TYPE_CHARSET_PREFIX).append(this.characterEncoding);
			}
			headers.set(HttpHeader.CONTENT_TYPE, sb.toString());
		}
	}
	
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	public ServletOutputStream getOutputStream() {
		if (!this.outputStreamAccessAllowed) {
			throw new IllegalStateException("OutputStream access not allowed");
		}
		return this.outputStream;
	}

	public void setOutputStream(ServletOutputStream outputStream) {
		if (!this.outputStreamAccessAllowed) {
			throw new IllegalStateException("OutputStream access not allowed");
		}
		this.outputStream = outputStream;
	}

	public PrintWriter getWriter() throws UnsupportedEncodingException {
		if (!this.writerAccessAllowed) {
			throw new IllegalStateException("Writer access not allowed");
		}
		if (this.writer == null) {
			Writer targetWriter = (this.characterEncoding != null ?
					new OutputStreamWriter(this.outputStream, this.characterEncoding) : new OutputStreamWriter(this.outputStream));
			this.writer = new ResponsePrintWriter(targetWriter);
		}
		return this.writer;
	}

	public byte[] getContentAsByteArray() {
		flushBuffer();
		return this.content.toByteArray();
	}

	public String getContentAsString() {
		flushBuffer();
		String str;
		if (characterEncoding == null) {
			str = content.toString();
		}
		else {
			str = Strings.newString(content.toByteArray(), characterEncoding);
			if (Strings.isNotEmpty(str) && str.charAt(0) == Chars.BOM) {
				str = str.substring(1);
			}
		}
		return str;
	}

	public void setContentLength(int contentLength) {
		setIntHeader(HttpHeader.CONTENT_LENGTH, contentLength);
	}

	public int getContentLength() {
		int cl = headers.getInt(HttpHeader.CONTENT_LENGTH);
		return cl < 0 ? 0 : cl;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
		if (contentType != null) {
			int charsetIndex = contentType.toLowerCase().indexOf(HttpServlets.CONTENT_TYPE_CHARSET_PREFIX);
			if (charsetIndex != -1) {
				String encoding = contentType.substring(charsetIndex + HttpServlets.CONTENT_TYPE_CHARSET_PREFIX.length());
				this.characterEncoding = encoding;
				this.charset = true;
			}
			updateContentTypeHeader();
		}
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getBufferSize() {
		return this.bufferSize;
	}

	public void flushBuffer() {
		setCommitted(true);
	}

	public void resetBuffer() {
		if (isCommitted()) {
			throw new IllegalStateException("Cannot reset buffer - response is already committed");
		}
		this.content.reset();
	}

	private void setCommittedIfBufferSizeExceeded() {
		int bufSize = getBufferSize();
		if (bufSize > 0 && this.content.size() > bufSize) {
			setCommitted(true);
		}
	}

	public void setCommitted(boolean committed) {
		this.committed = committed;
	}

	public boolean isCommitted() {
		return this.committed;
	}

	public void reset() {
		resetBuffer();
		this.characterEncoding = null;
		this.contentType = null;
		this.locale = null;
		this.cookies.clear();
		this.headers.clear();
		this.status = HttpServletResponse.SC_OK;
		this.errorMessage = null;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return this.locale;
	}


	//---------------------------------------------------------------------
	// HttpServletResponse interface
	//---------------------------------------------------------------------

	public void addCookie(Cookie cookie) {
		Asserts.notNull(cookie, "Cookie must not be null");
		this.cookies.add(cookie);
	}

	public Cookie[] getCookies() {
		return this.cookies.toArray(new Cookie[this.cookies.size()]);
	}

	public Cookie getCookie(String name) {
		Asserts.notNull(name, "Cookie name must not be null");
		for (Cookie cookie : this.cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	public boolean containsHeader(String name) {
		return this.headers.containsKey(name);
	}

	/**
	 * Return the names of all specified headers as a Set of Strings.
	 * <p>As of Servlet 3.0, this method is also defined HttpServletResponse.
	 * @return the <code>Set</code> of header name <code>Strings</code>, or an empty <code>Set</code> if none
	 */
	public Set<String> getHeaderNames() {
		return this.headers.keySet();
	}

	/**
	 * Return the primary value for the given header as a String, if any.
	 * Will return the first value in case of multiple values.
	 * @param name the name of the header
	 * @return the associated header value, or <code>null<code> if none
	 */
	public String getHeader(String name) {
		return headers.getString(name);
	}

	/**
	 * Return all values for the given header as a List of Strings.
	 * <p>As of Servlet 3.0, this method is also defined HttpServletResponse.
	 * @param name the name of the header
	 * @return the associated header values, or an empty List if none
	 */
	public List<String> getHeaders(String name) {
		return headers.getStrings(name);
	}

	/**
	 * The default implementation returns the given URL String as-is.
	 * <p>Can be overridden in subclasses, appending a session id or the like.
	 */
	public String encodeURL(String url) {
		return url;
	}

	/**
	 * The default implementation delegates to {@link #encodeURL},
	 * returning the given URL String as-is.
	 * <p>Can be overridden in subclasses, appending a session id or the like
	 * in a redirect-specific fashion. For general URL encoding rules,
	 * override the common {@link #encodeURL} method instead, appyling
	 * to redirect URLs as well as to general URLs.
	 */
	public String encodeRedirectURL(String url) {
		return encodeURL(url);
	}

	public String encodeUrl(String url) {
		return encodeURL(url);
	}

	public String encodeRedirectUrl(String url) {
		return encodeRedirectURL(url);
	}

	public void sendError(int status, String errorMessage) throws IOException {
		if (isCommitted()) {
			throw new IllegalStateException("Cannot set error status - response is already committed");
		}
		this.status = status;
		this.errorMessage = errorMessage;
		setCommitted(true);
	}

	public void sendError(int status) throws IOException {
		if (isCommitted()) {
			throw new IllegalStateException("Cannot set error status - response is already committed");
		}
		this.status = status;
		setCommitted(true);
	}

	public void sendRedirect(String url) throws IOException {
		if (isCommitted()) {
			throw new IllegalStateException("Cannot send redirect - response is already committed");
		}
		Asserts.notNull(url, "Redirect URL must not be null");
		this.status = HttpServletResponse.SC_MOVED_TEMPORARILY;
		setHeader(HttpHeader.LOCATION, url);
		setCommitted(true);
	}

	public String getRedirectedUrl() {
		return getHeader(HttpHeader.LOCATION);
	}

	public void setDateHeader(String name, long value) {
		headers.setDate(name, (value));
	}

	public void addDateHeader(String name, long value) {
		headers.addDate(name, (value));
	}

	public void setHeader(String name, String value) {
		setHeaderValue(name, value);
	}

	public void addHeader(String name, String value) {
		addHeaderValue(name, value);
	}

	public void setIntHeader(String name, int value) {
		setHeaderValue(name, String.valueOf(value));
	}

	public void addIntHeader(String name, int value) {
		addHeaderValue(name, String.valueOf(value));
	}

	public void setLongHeader(String name, long value) {
		setHeaderValue(name, String.valueOf(value));
	}

	public void addLongHeader(String name, long value) {
		addHeaderValue(name, String.valueOf(value));
	}

	private void setHeaderValue(String name, String value) {
		setSpecialHeader(name, value);
		headers.add(name, value);
	}

	private void addHeaderValue(String name, String value) {
		setSpecialHeader(name, value);
		headers.add(name, value);
	}
	
	private boolean setSpecialHeader(String name, String value) {
		if (HttpHeader.CONTENT_TYPE.equalsIgnoreCase(name)) {
			setContentType((String) value);
			return true;
		}
		else {
			return false;
		}
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setStatus(int status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}

	public int getStatus() {
		return this.status;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}


	//---------------------------------------------------------------------
	// Methods for MockRequestDispatcher
	//---------------------------------------------------------------------

	public void setForwardedUrl(String forwardedUrl) {
		this.forwardedUrl = forwardedUrl;
	}

	public String getForwardedUrl() {
		return this.forwardedUrl;
	}

	public void setIncludedUrl(String includedUrl) {
		this.includedUrls.clear();
		if (includedUrl != null) {
			this.includedUrls.add(includedUrl);
		}
	}

	public String getIncludedUrl() {
		int count = this.includedUrls.size();
		if (count > 1) {
			throw new IllegalStateException(
					"More than 1 URL included - check getIncludedUrls instead: " + this.includedUrls);
		}
		return (count == 1 ? this.includedUrls.get(0) : null);
	}

	public void addIncludedUrl(String includedUrl) {
		Asserts.notNull(includedUrl, "Included URL must not be null");
		this.includedUrls.add(includedUrl);
	}

	public List<String> getIncludedUrls() {
		return this.includedUrls;
	}


	/**
	 * Inner class that adapts the ServletOutputStream to mark the
	 * response as committed once the buffer size is exceeded.
	 */
	private class ResponseServletOutputStream extends DelegateServletOutputStream {

		public ResponseServletOutputStream(OutputStream out) {
			super(out);
		}

		public void write(int b) throws IOException {
			super.write(b);
			super.flush();
			setCommittedIfBufferSizeExceeded();
		}

		public void flush() throws IOException {
			super.flush();
			setCommitted(true);
		}
	}


	/**
	 * Inner class that adapts the PrintWriter to mark the
	 * response as committed once the buffer size is exceeded.
	 */
	private class ResponsePrintWriter extends PrintWriter {

		public ResponsePrintWriter(Writer out) {
			super(out, true);
		}

		public void write(char buf[], int off, int len) {
			super.write(buf, off, len);
			super.flush();
			setCommittedIfBufferSizeExceeded();
		}

		public void write(String s, int off, int len) {
			super.write(s, off, len);
			super.flush();
			setCommittedIfBufferSizeExceeded();
		}

		public void write(int c) {
			super.write(c);
			super.flush();
			setCommittedIfBufferSizeExceeded();
		}

		public void flush() {
			super.flush();
			setCommitted(true);
		}
	}

	//-----------------------------------------------------------
	// Servlet 3.0
	//
	public void setContentLengthLong(long len) {
		setLongHeader(HttpHeader.CONTENT_LENGTH, len);
	}
}
