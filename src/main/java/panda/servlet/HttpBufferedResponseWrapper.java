package panda.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Charsets;
import panda.net.http.HttpHeader;
import panda.net.http.HttpResponse;

public class HttpBufferedResponseWrapper extends HttpServletResponseWrapper {
	private HttpHeader head;
	private int statusCode = SC_OK;
	private String statusMsg = HttpResponse.getStatusReason(SC_OK);
	private ServletOutputStream stream;
	private PrintWriter writer;
	
	private ByteArrayOutputStream body;

	public HttpBufferedResponseWrapper(HttpServletResponse res) throws IOException {
		super(res);
		head = new HttpHeader();
		body = new ByteArrayOutputStream();
	}

	/**
	 * @return the head
	 */
	public HttpHeader getHead() {
		return head;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the statusMsg
	 */
	public String getStatusMsg() {
		return statusMsg;
	}

	/**
	 * @return the body
	 */
	public InputStream getBodyStream() {
		return body.toInputStream();
	}

	//------------------------------------------------------------------
	@Override
	public void sendError(int sc, String msg) throws IOException {
		statusCode = sc;
		statusMsg = msg;
		super.sendError(sc, msg);
	}

	@Override
	public void sendError(int sc) throws IOException {
		statusCode = sc;
		statusMsg = HttpResponse.getStatusReason(sc);
		super.sendError(sc);
	}

	@Override
	public void setDateHeader(String name, long date) {
		head.setDate(name, date);
		super.setDateHeader(name, date);
	}

	@Override
	public void addDateHeader(String name, long date) {
		head.addDate(name, date);
		super.addDateHeader(name, date);
	}

	@Override
	public void setHeader(String name, String value) {
		head.set(name, value);
		super.setHeader(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		head.add(name, value);
		super.addHeader(name, value);
	}

	@Override
	public void setIntHeader(String name, int value) {
		head.setInt(name, value);
		super.setIntHeader(name, value);
	}

	@Override
	public void addIntHeader(String name, int value) {
		head.addInt(name, value);
		super.addIntHeader(name, value);
	}

	@Override
	public void setStatus(int sc) {
		statusCode = sc;
		statusMsg = HttpResponse.getStatusReason(sc);
		super.setStatus(sc);
	}

	@Override
	public void setStatus(int sc, String sm) {
		statusCode = sc;
		statusMsg = sm;
		super.setStatus(sc, sm);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (stream != null) {
			throw new IllegalStateException("the getOutputStream() method has already been called on this response");
		}
		if (writer == null) {
			String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
			writer = new PrintWriter(new OutputStreamWriter(body, cs));
		}
		return writer;
	}

	@Override
	public ServletOutputStream getOutputStream() {
		if (writer != null) {
			throw new IllegalStateException("the getWriter() method has already been called on this response");
		}
		if (stream == null) {
			stream = new DelegatingServletOutputStream(body);
		}
		return stream;
	}

	@Override
	public void setContentLength(int len) {
		head.setInt(HttpHeader.CONTENT_LENGTH, len);
		super.setContentLength(len);
	}

	@Override
	public void setContentType(String type) {
		head.set(HttpHeader.CONTENT_TYPE, type);
		super.setContentType(type);
	}

	@Override
	public void flushBuffer() throws IOException {
		if (writer != null) {
			writer.flush();
		}
		if (stream != null) {
			stream.flush();
		}
	}

	@Override
	public void reset() {
		super.reset();
		
		statusCode = 0;
		statusMsg = null;
		head.clear();
		body.reset();
	}

	@Override
	public void resetBuffer() {
		super.resetBuffer();
		body.reset();
	}

}
