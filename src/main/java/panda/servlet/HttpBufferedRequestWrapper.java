package panda.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import panda.io.Streams;

public class HttpBufferedRequestWrapper extends HttpServletRequestWrapper {
	private byte[] body;

	public HttpBufferedRequestWrapper(HttpServletRequest req) throws IOException {
		super(req);

		InputStream is = req.getInputStream();
		body = Streams.toByteArray(is);
	}

	public ServletInputStream getInputStream() {
		InputStream is = new ByteArrayInputStream(body);
		return new DelegatingServletInputStream(is);
	}

	public byte[] getBody() {
		return body;
	}
}
