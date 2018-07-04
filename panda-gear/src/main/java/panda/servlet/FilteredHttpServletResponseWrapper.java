package panda.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import panda.io.stream.WriterOutputStream;
import panda.lang.Charsets;
import panda.net.http.HttpHeader;
import panda.net.http.HttpStatus;

public class FilteredHttpServletResponseWrapper extends HttpServletResponseWrapper {
	protected HttpHeader head;
	protected HttpStatus status;
	protected ServletOutputStream stream;
	protected PrintWriter writer;
	
	public FilteredHttpServletResponseWrapper(HttpServletResponse res) {
		super(res);
		head = new HttpHeader();
		status = new HttpStatus(SC_OK);
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
		return status.getStatus();
	}

	/**
	 * @return the statusMsg
	 */
	public String getStatusMsg() {
		return status.getReason();
	}

	/**
	 * flush writer and stream
	 * @throws IOException throw if an error occurs
	 */
	public void flush() throws IOException {
		if (writer != null) {
			writer.flush();
		}
		if (stream != null) {
			stream.flush();
		}
	}

	//------------------------------------------------------------------
	@Override
	public void sendError(int sc, String msg) throws IOException {
		status.setStatus(sc, msg);
		super.sendError(sc, msg);
	}

	@Override
	public void sendError(int sc) throws IOException {
		status.setStatus(sc);
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
		status.setStatus(sc);
		super.setStatus(sc);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setStatus(int sc, String sm) {
		status.setStatus(sc, sm);
		super.setStatus(sc, sm);
	}

	/**
	 * do not throw exception even if stream is already returned.
	 * @return writer
	 */
	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			if (stream == null) {
				writer = super.getWriter();
			}
			else {
				String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
				writer = new PrintWriter(new OutputStreamWriter(stream, cs));
			}
		}
		return writer;
	}
	
	/**
	 * do not throw exception even if writer is already returned.
	 * @return writer
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (stream == null) {
			if (writer == null) {
				stream = super.getOutputStream();
			}
			else {
				String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
				stream = new DelegateServletOutputStream(new WriterOutputStream(writer, cs));
			}
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
		flush();
		super.flushBuffer();
	}
	
	@Override
	public void reset() {
		super.reset();
		
		status.setStatus(SC_OK);
		head.clear();
	}
}
