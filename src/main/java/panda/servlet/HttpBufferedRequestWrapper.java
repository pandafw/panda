package panda.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.net.http.URLHelper;

public class HttpBufferedRequestWrapper extends HttpServletRequestWrapper {
	private byte[] body;

	public HttpBufferedRequestWrapper(HttpServletRequest req) throws IOException {
		super(req);

		if (!HttpServletUtils.isFormUrlEncoded(req)) {
			InputStream is = req.getInputStream();
			body = Streams.toByteArray(is);
		}
	}

	@Override
	public BufferedReader getReader() throws IOException {
		if (body == null) {
			return super.getReader();
		}
		InputStream is = new ByteArrayInputStream(body);
		Reader r = new InputStreamReader(is, 
			Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8));
		return new BufferedReader(r);
	}

	public ServletInputStream getInputStream() throws IOException {
		if (body == null) {
			return super.getInputStream();
		}

		InputStream is = new ByteArrayInputStream(body);
		return new DelegatingServletInputStream(is);
	}

	public byte[] getBody() {
		if (body != null) {
			return body;
		}

		String ps = URLHelper.buildParametersString(getParameterMap());
		String cs = Charsets.defaultEncoding(getCharacterEncoding(), Charsets.UTF_8);
		return Strings.getBytes(ps, cs);
	}
}
