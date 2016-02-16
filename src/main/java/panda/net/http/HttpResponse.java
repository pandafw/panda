package panda.net.http;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.InflaterInputStream;

import panda.io.Files;
import panda.io.Streams;
import panda.lang.Chars;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.chardet.LangHint;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.net.Inets;

/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpResponse implements Closeable {
	private static final Log log = HttpClient.log;
	
	private URL url;
	private HttpHeader header;
	private List<Cookie> cookies;
	private String protocol;
	private String statusLine;
	private int statusCode;
	private String statusReason;
	private InputStream rawStream;
	private InputStream stream;
	private byte[] content;

	public HttpResponse(URL url, HttpURLConnection conn) throws IOException {
		this.url = url;
		statusCode = conn.getResponseCode();
		if (statusCode < 0) {
			throw new IOException("Invalid HTTP response");
		}

		statusReason = conn.getResponseMessage();
		statusLine = conn.getHeaderField(0);
		if (!statusLine.startsWith("HTTP/1.")) {
			statusLine = statusCode + " " + statusReason;
		}
		
		header = new HttpHeader();
		header.putAll(conn.getHeaderFields());

		try {
			rawStream = conn.getInputStream();
		}
		catch (IOException e) {
			rawStream = conn.getErrorStream();
		}
	}

	/**
	 * @return the statusLine
	 */
	public String getStatusLine() {
		return statusLine;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public String getProtocol() {
		if (protocol == null) {
			protocol = statusLine;
			if (Strings.isNotEmpty(statusLine)) {
				if (statusLine.startsWith("HTTP/1.")) {
					int codePos = statusLine.indexOf(' ');
					if (codePos > 0) {
						protocol = statusLine.substring(0, codePos);
					}
				}
			}
		}
		return protocol;
	}

	public HttpHeader getHeader() {
		return header;
	}

	public boolean isOK() {
		return statusCode == HttpStatus.SC_OK;
	}

	public boolean isMoved() {
		return statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY;
	}

	public boolean isServerError() {
		return statusCode >= 500 && statusCode < 600;
	}

	public boolean isClientError() {
		return statusCode >= 400 && statusCode < 500;
	}

	/**
	 * get Last-Modified date from HTTP headers
	 * @return date
	 */
	public Date getLastModified() {
		return header.getDate(HttpHeader.LAST_MODIFIED);
	}
	
	/**
	 * get encoding from HTTP headers
	 * @return encoding
	 */
	public String getContentEncoding() {
		return header.getString(HttpHeader.CONTENT_ENCODING);
	}

	/**
	 * get content length from HTTP headers
	 * @return content length
	 */
	public Long getContentLength() {
		return Numbers.toLong(header.getString(HttpHeader.CONTENT_LENGTH));
	}
	
	/**
	 * get content type from HTTP headers
	 * @return content type
	 */
	public String getContentType() {
		String contentType = header.getString(HttpHeader.CONTENT_TYPE);
		return Strings.substringBefore(contentType, ';');
	}
	
	/**
	 * get charset from HTTP headers
	 * @return charset name
	 */
	public String getContentCharset() {
		String contentType = header.getString(HttpHeader.CONTENT_TYPE);
		return Streams.getCharsetFromContentTypeString(contentType);
	}

	/**
	 * detect charset from HTTP headers & HTTP body
	 * @return charset detected
	 * @throws IOException  if an IO error occurs
	 */
	public String detectContentCharset() throws IOException {
		String cs = getContentCharset();
		if (!Charsets.isSupportedCharset(cs)) {
			cs = Charsets.detectCharset(getContent(), LangHint.ALL);
			if (!Charsets.isSupportedCharset(cs)) {
				cs = Charsets.UTF_8;
			}
		}
		return cs;
	}

	public List<Cookie> getCookies() {
		if (cookies == null) {
			cookies = new ArrayList<Cookie>();
			List<String> cs = header.getStrings(HttpHeader.SET_COOKIE);
			if (cs != null) {
				for (String s : cs) {
					Cookie c = new Cookie(s);
					if (c.isValid()) {
						cookies.add(c);
					}
				}
			}
		}
		return cookies;
	}

	public InputStream getRawStream() {
		return rawStream;
	}

	public HttpResponse safeDrain() {
		Streams.safeDrain(rawStream);
		return this;
	}

	public HttpResponse drain() throws IOException {
		Streams.drain(rawStream);
		return this;
	}
	
	public InputStream getStream() throws IOException {
		if (content != null) {
			stream = new ByteArrayInputStream(content);
		}
		else if (stream == null) {
			if (rawStream != null) {
				String encoding = getContentEncoding();
				if (encoding != null && encoding.contains("gzip")) {
					stream = Streams.gzip(rawStream);
				}
				else if (encoding != null && encoding.contains("deflate")) {
					stream = new InflaterInputStream(rawStream);
				}
				else {
					stream = rawStream;
				}
			}
			else {
				stream = Streams.closedInputStream();
			}
		}
		return stream;
	}

	public Reader getReader() throws IOException {
		return getReader(null);
	}

	public Reader getReader(String charset) throws IOException {
		if (Strings.isEmpty(charset)) {
			charset = detectContentCharset();
		}
		return new InputStreamReader(getStream(), charset);
	}

	public byte[] getContent() throws IOException {
		if (content == null) {
			try {
				StopWatch sw = new StopWatch();
				InputStream is = getStream();
				content = Streams.toByteArray(is);
				sw.stop();

				if (log.isDebugEnabled()) {
					log.debug("DOWN " + url + " - (" 
							+ Files.toDisplaySize(content.length) + " / " + sw 
							+ ") [" + Inets.toSpeedString(content.length, sw.getTime()) + "]");
				}
			}
			finally {
				close();
			}
		}
		return content;
	}

	public String getContentText(String charsetName) throws IOException {
		// call this for save content byte array 
		getContent();
		
		StringBuilder sb = new StringBuilder();
		Reader rd = getReader(charsetName);
		int c = rd.read();
		if (c != Streams.EOF) {
			if (c != Chars.BOM) {
				sb.append((char)c);
			}
			Streams.copy(rd, sb);
		}
		return sb.toString();
	}

	public String getContentText() throws IOException {
		return getContentText(null);
	}

	@Override
	public void close() {
		Streams.safeClose(stream);
		Streams.safeClose(rawStream);
	}

	public void toString(Appendable writer) throws IOException {
		writer.append(statusLine);
		
		if (header != null) {
			writer.append(Streams.LINE_SEPARATOR);
			header.toString(writer);
		}

		String text = getContentText();
		if (Strings.isNotEmpty(text)) {
			writer.append(Streams.LINE_SEPARATOR);
			writer.append(text);
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
