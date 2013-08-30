package panda.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Files;
import panda.io.Streams;
import panda.lang.Chars;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.net.http.HttpHeader;
import panda.net.http.UserAgent;


/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpServletSupport {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	public static final SimpleDateFormat GMT_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
	static {
		GMT_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private String charset;
	private String fileName;
	private Integer contentLength;
	private String contentType;
	private Boolean attachment;
	private Boolean bom;
	private long expires = -1;
	private String cacheControl;
	private Date lastModified;
	
	private byte[] data;
	private InputStream stream;

	public HttpServletSupport() {
	}

	public HttpServletSupport(HttpServletResponse res) {
		response = res;
	}

	public HttpServletSupport(HttpServletRequest req, HttpServletResponse res) {
		request = req;
		response = res;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the contentLength
	 */
	public Integer getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the attachment
	 */
	public Boolean getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Boolean attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the bom
	 */
	public Boolean getBom() {
		return bom;
	}

	/**
	 * @param bom the bom to set
	 */
	public void setBom(Boolean bom) {
		this.bom = bom;
	}

	/**
	 * @return the expires
	 */
	public long getExpires() {
		return expires;
	}

	/**
	 * @param expires the expires to set
	 */
	public void setExpires(long expires) {
		this.expires = expires;
	}

	/**
	 * @return the cacheControl
	 */
	public String getCacheControl() {
		return cacheControl;
	}

	/**
	 * @param cacheControl the cacheControl to set
	 */
	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}

	/**
	 * @return the lastModified
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return the stream
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * @param stream the stream to set
	 */
	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	
	/**
	 * write response header
	 * @throws IOException if an I/O error occurs
	 */
	public void writeResponseHeader() throws IOException {
		if (Strings.isNotEmpty(charset)) {
			response.setCharacterEncoding(charset);
		}

		if (Strings.isEmpty(contentType)
				&& Strings.isNotEmpty(fileName)) {
			contentType = Files.getContentTypeFor(fileName);
		}

		if (Strings.isNotEmpty(contentType)) {
			if (Strings.isNotEmpty(charset)) {
				contentType += "; charset=" + charset;
			}
			response.setContentType(contentType);
		}

		if (contentLength != null) {
			response.setContentLength(contentLength);
		}

		if (lastModified != null) {
			response.setHeader(HttpHeader.LAST_MODIFIED, DATE_FORMAT.format(lastModified));
		}
		
		boolean noFileCache = true;
		if (Strings.isNotEmpty(fileName)) {
			String fn = HttpServletUtils.EncodeFileName(request, fileName);
			response.setHeader(HttpHeader.CONTENT_DISPOSITION, 
					(Boolean.TRUE.equals(attachment) ? "attachment" : "inline") + "; filename=\"" + fn + "\"");
			
			if (request != null) {
				UserAgent b = new UserAgent(request);
				noFileCache = !b.isMsie();
			}
		}

		if (expires > 0) {
			String cc = "max-age=" + expires;
			if (Strings.isNotEmpty(cacheControl)) {
				cc += ", " + cacheControl;
			}
			response.setHeader(HttpHeader.CACHE_CONTROL, cc);

			Date dexp = lastModified != null ? lastModified : Calendar.getInstance().getTime();
			dexp.setTime(dexp.getTime() + (expires * 1000));
			String sexp = GMT_FORMAT.format(dexp);
			response.setHeader(HttpHeader.EXPIRES, sexp);
		}
		else if (expires == 0 && noFileCache) {
			setResponseNoCache(response);
		}

		if (Boolean.TRUE.equals(bom) && Charsets.isUnicodeCharset(charset)) {
			writeResponseBom();
		}
	}

	/**
	 * Set no cache to response header
	 * @param response HttpServletResponse
	 */
	public static void setResponseNoCache(HttpServletResponse response) {
		response.setHeader(HttpHeader.CACHE_CONTROL, HttpHeader.CACHE_CONTROL_NOCACHE);
		response.setHeader(HttpHeader.PRAGMA, HttpHeader.CACHE_CONTROL_NOCACHE);
		String expires = GMT_FORMAT.format(DateTimes.getDate());
		response.setHeader(HttpHeader.EXPIRES, expires);
	}

	public void writeResponseBom() throws IOException {
		response.getWriter().write(Chars.BOM);
	}

	public void writeResponseText(String text) throws IOException {
		response.getWriter().write(text);
	}

	public void writeResponseData(byte[] data) throws IOException {
		response.getOutputStream().write(data);
	}

	public void writeResponseData(InputStream is) throws IOException {
		writeResponseData(is, 4096);
	}
	
	public void writeResponseData(InputStream is, int bufferSize) throws IOException {
		OutputStream os = null;

		try {
			// Get the outputstream
			os = response.getOutputStream();

			// Copy input to output
			byte[] buf = new byte[bufferSize];
			int sz;
			while (-1 != (sz = is.read(buf))) {
				os.write(buf, 0, sz);
			}

			// Flush
			os.flush();
		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}
}
