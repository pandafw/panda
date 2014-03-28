package panda.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Charsets;
import panda.net.http.URLHelper;

public class HttpBufferedRequestWrapper extends HttpServletRequestWrapper {
	private ByteArrayOutputStream body;
	private BufferedReader reader;
	private ServletInputStream stream;

	public HttpBufferedRequestWrapper(HttpServletRequest req) throws IOException {
		super(req);
	}

	public InputStream getBodyStream() throws IOException {
		if (body != null) {
			return body.toInputStream();
		}

		if (HttpServlets.isFormUrlEncoded(this)) {
			String ps = URLHelper.buildQueryString(getParameterMap());
			String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
			return Streams.toInputStream(ps, cs);
		}
		else {
			getInputStream();
			return body.toInputStream();
		}
	}

	//------------------------------------------------------
	@Override
	public BufferedReader getReader() throws IOException {
		String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
		if (body == null) {
			Reader r = super.getReader();
			body = new ByteArrayOutputStream();
			Streams.copy(r, body, cs);
		}

		if (stream != null) {
			throw new IllegalStateException("the getInputStream() method has been called on this request");
		}
		if (reader != null) {
			return reader;
		}
		
		InputStream is = body.toInputStream();
		Reader r = new InputStreamReader(is, cs);
		reader = new BufferedReader(r);
		return reader;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (body == null) {
			InputStream is = super.getInputStream();
			body = new ByteArrayOutputStream();
			Streams.copy(is, body);
		}

		if (reader != null) {
			throw new IllegalStateException("the getReader() method has been called on this request");
		}
		if (stream != null) {
			return stream;
		}
		
		InputStream is = body.toInputStream();
		stream = new DelegatingServletInputStream(is);
		return stream;
	}
}
