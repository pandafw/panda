package panda.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Charsets;

public class HttpServletResponseBlocker extends FilteredHttpServletResponseWrapper {
	private ByteArrayOutputStream body;

	public HttpServletResponseBlocker(HttpServletResponse res) throws IOException {
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

	/**
	 * @return the body
	 * @throws IOException if an IO error occurred
	 */
	public String getBodyContent() throws IOException {
		InputStream is = getBodyStream();
		String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
		return Streams.toString(is, cs);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
			writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), cs));
		}
		return writer;
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (stream == null) {
			stream = new DelegateServletOutputStream(body);
		}
		return stream;
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
