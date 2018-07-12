package panda.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.Files;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Chars;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.net.http.HttpDates;
import panda.net.http.HttpHeader;
import panda.net.http.UserAgent;
import panda.vfs.FileItem;


/**
 * Http Servlet Response Support class
 */
public class HttpServletResponser {
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
	private int bufferSize = 4096;

	private Object body;

	public HttpServletResponser() {
	}

	public HttpServletResponser(HttpServletResponse res) {
		response = res;
	}

	public HttpServletResponser(HttpServletRequest req, HttpServletResponse res) {
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
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return the body
	 */
	public Object getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Object body) {
		this.body = body;
	}

	public void setFile(File file) {
		setFileName(file.getName());
		setContentLength((int)file.length());
		setBody(file);
	}

	public void setFile(FileItem file) {
		setFileName(file.getName());
		setContentLength(file.getSize());
		setContentType(file.getContentType());
		setBody(file);
	}
	
	/**
	 * write response header
	 * @throws IOException if an I/O error occurs
	 */
	public void writeHeader() throws IOException {
		if (Strings.isNotEmpty(charset)) {
			response.setCharacterEncoding(charset);
		}

		if (Strings.isEmpty(contentType)
				&& Strings.isNotEmpty(fileName)) {
			contentType = MimeTypes.getMimeType(fileName);
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
			if (!response.containsHeader(HttpHeader.CONTENT_DISPOSITION)) {
				String fn = HttpServlets.EncodeFileName(request, charset, fileName);
				String cd = Boolean.TRUE.equals(attachment) ? HttpHeader.CONTENT_DISPOSITION_ATTACHMENT : HttpHeader.CONTENT_DISPOSITION_INLINE;
				response.setHeader(HttpHeader.CONTENT_DISPOSITION, cd + "; " + fn);
			}
			
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
			writeBom();
		}
	}

	public void writeBody() throws IOException {
		if (body == null) {
			return;
		}
		
		if (body instanceof CharSequence) {
			writeText(((CharSequence)body).toString());
		}
		else if (body instanceof char[]) {
			writeText((char[])body);
		}
		else if (body instanceof byte[]) {
			writeBytes((byte[])body);
		}
		else if (body instanceof File) {
			writeFile((File)body);
		}
		else if (body instanceof FileItem) {
			writeFile((FileItem)body);
		}
		else if (body instanceof Reader) {
			writeReader((Reader)body);
		}
		else if (body instanceof InputStream) {
			writeStream((InputStream)body);
		}
	}

	public void writeBom() throws IOException {
		response.getWriter().write(Chars.BOM);
	}

	public void writeText(String text) throws IOException {
		response.getWriter().write(text);
	}

	public void writeText(char[] text) throws IOException {
		response.getWriter().write(text);
	}

	public void writeBytes(byte[] data) throws IOException {
		OutputStream os = response.getOutputStream();
		os.write(data);
		os.flush();
	}

	public void writeFile(File file) throws IOException {
		OutputStream os = response.getOutputStream();
		Files.copyFile(file, os);
		os.flush();
	}

	public void writeFile(FileItem file) throws IOException {
		writeStream(file.getInputStream());
	}

	public void writeReader(Reader r) throws IOException {
		writeReader(r, bufferSize);
	}

	public void writeReader(Reader r, int bufferSize) throws IOException {
		try {
			Writer w = response.getWriter();
			Streams.copy(r, w, bufferSize);
			w.flush();
		}
		finally {
			Streams.safeClose(r);
		}
	}

	public void writeStream(InputStream is) throws IOException {
		writeStream(is, bufferSize);
	}
	
	public void writeStream(InputStream is, int bufferSize) throws IOException {
		try {
			OutputStream os = response.getOutputStream();
			Streams.copy(is, os, bufferSize);
			os.flush();
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	public void flush() throws IOException {
		response.flushBuffer();
	}
}
