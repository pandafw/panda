package panda.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Charsets;

public class HttpServletResponseCapturer extends FilteredHttpServletResponseWrapper {
	private ByteArrayOutputStream body;

	public HttpServletResponseCapturer(HttpServletResponse res) throws IOException {
		super(res);
		body = new ByteArrayOutputStream();
	}

	/**
	 * @return the body
	 * @throws IOException if an IO error occurred
	 */
	public InputStream getBodyStream() throws IOException {
		flush();
		return body.toInputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (stream != null) {
			throw new IllegalStateException("the getOutputStream() method has already been called on this response");
		}
		if (writer == null) {
			String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
			writer = new PrintWriter(new OutputStreamWriter(delegateServletOutputStream(), cs));
		}
		return writer;
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("the getWriter() method has already been called on this response");
		}
		if (stream == null) {
			stream = delegateServletOutputStream();
		}
		return stream;
	}

	private ServletOutputStream delegateServletOutputStream() throws IOException {
		return new DelegateServletOutputStream(body, super.getOutputStream());
	}

	@Override
	public void reset() {
		super.reset();
		body.reset();
	}

	@Override
	public void resetBuffer() {
		super.resetBuffer();
		body.reset();
	}

}
