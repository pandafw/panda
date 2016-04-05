package panda.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Chars;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.net.http.HttpDates;
import panda.net.http.HttpHeader;
import panda.net.http.UserAgent;


/**
 * @author yf.frank.wang@gmail.com
 */
public class HttpServletSupport {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private String charset;
	private String fileName;
	private Integer contentLength;
	private String contentType;
	private Boolean attachment;
	private Boolean bom;
	private int maxAge = -1;
	private String cacheControl = HttpHeader.CACHE_CONTROL_PUBLIC;
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
	 * @return the maxAge (seconds)
	 */
	public int getMaxAge() {
		return maxAge;
	}

	/**
	 * @param maxAge the maxAge (seconds) to set
	 */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
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
			contentType = FileNames.getContentTypeFor(fileName);
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
			response.setHeader(HttpHeader.LAST_MODIFIED, HttpDates.format(lastModified));
		}
		
		boolean noFileCache = true;
		if (Strings.isNotEmpty(fileName)) {
			String fn = HttpServlets.EncodeFileName(request, fileName);
			response.setHeader(HttpHeader.CONTENT_DISPOSITION, 
					(Boolean.TRUE.equals(attachment) ? "attachment" : "inline") + "; filename=\"" + fn + "\"");
			
			if (request != null) {
				UserAgent b = HttpServlets.getUserAgent(request);
				noFileCache = !b.isMsie();
			}
		}

		if (maxAge > 0) {
			HttpServlets.setResponseCache(response, maxAge, cacheControl);
		}
		else if (maxAge == 0 && noFileCache) {
			HttpServlets.setResponseNoCache(response);
		}

		if (Boolean.TRUE.equals(bom) && Charsets.isUnicodeCharset(charset)) {
			writeResponseBom();
		}
	}

	public void writeResponseBom() throws IOException {
		response.getWriter().write(Chars.BOM);
	}

	public void writeResponseText(String text) throws IOException {
		response.getWriter().write(text);
	}

	public void writeResponseData(byte[] data) throws IOException {
		OutputStream os = response.getOutputStream();
		os.write(data);
		os.flush();
	}

	public void writeResponseData(InputStream is) throws IOException {
		writeResponseData(is, 4096);
	}
	
	public void writeResponseData(InputStream is, int bufferSize) throws IOException {
		OutputStream os = response.getOutputStream();
		Streams.copy(is, os, bufferSize);
		os.flush();
	}
	
	public void flush() throws IOException {
		response.flushBuffer();
	}
}
