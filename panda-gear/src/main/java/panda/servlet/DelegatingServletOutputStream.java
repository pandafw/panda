package panda.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

import panda.lang.Asserts;
import panda.servlet.mock.MockHttpServletResponse;

/**
 * Delegating implementation of {@link javax.servlet.ServletOutputStream}.
 *
 * <p>Used by {@link MockHttpServletResponse}; typically not directly
 * used for testing application controllers.
 *
 * @see MockHttpServletResponse
 */
public class DelegatingServletOutputStream extends ServletOutputStream {

	private final OutputStream[] ostreams;

	/**
	 * Create a DelegatingServletOutputStream for the given target stream.
	 * @param targetStreams the target streams (never <code>null</code>)
	 */
	public DelegatingServletOutputStream(OutputStream ... targetStreams) {
		Asserts.notNull(targetStreams, "Target OutputStreams must not be null");
		Asserts.noNullElements(targetStreams);
		this.ostreams = targetStreams;
	}

	public void write(int b) throws IOException {
		for (OutputStream os : ostreams) {
			os.write(b);
		}
	}

	public void flush() throws IOException {
		for (OutputStream os : ostreams) {
			os.flush();
		}
	}

	public void close() throws IOException {
		for (OutputStream os : ostreams) {
			os.close();
		}
	}

	public boolean isReady() {
		return true;
	}
}
