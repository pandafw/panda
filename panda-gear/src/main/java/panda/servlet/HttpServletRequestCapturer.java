package panda.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;

public class HttpServletRequestCapturer extends FilteredHttpServletRequestWrapper {
	private ByteArrayOutputStream body;
	private InputStream source;

	public HttpServletRequestCapturer(HttpServletRequest req) {
		super(req);
	}

	@Override
	protected InputStream getSource() throws IOException {
		if (source == null) {
			HttpServletRequest req = (HttpServletRequest)getRequest();
			source = req.getInputStream();
			body = new ByteArrayOutputStream();
			Streams.copy(source, body);
			source = body.toInputStream();
		}
		return source;
	}
	
	public InputStream getBodyStream() throws IOException {
		getSource();
		return body.toInputStream();
	}
}
