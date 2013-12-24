package panda.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import panda.io.stream.ByteArrayOutputStream;
import panda.net.http.HttpHeader;
import panda.net.http.HttpResponse;

public class HttpBufferedResponseWrapper extends HttpServletResponseWrapper {
	private HttpHeader head;
	private int statusCode = SC_OK;
	private String statusMsg = HttpResponse.getStatusReason(SC_OK);
	
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
	public byte[] getBody() {
		return body.toByteArray();
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
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		super.sendRedirect(location);
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
		String cs = this.getCharacterEncoding();
		return new PrintWriter(new OutputStreamWriter(body, cs));
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return new DelegatingServletOutputStream(body);
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
	}

	@Override
	public boolean isCommitted() {
		return false;
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

	@Override
	public void setLocale(Locale loc) {
		super.setLocale(loc);
	}

}
