package panda.mock.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

import panda.lang.Asserts;

/**
 * Delegating implementation of {@link javax.servlet.ServletOutputStream}.
 *
 * <p>Used by {@link MockHttpServletResponse}; typically not directly
 * used for testing application controllers.
 *
 * @see MockHttpServletResponse
 */
public class DelegatingServletOutputStream extends ServletOutputStream {

	private final OutputStream targetStream;


	/**
	 * Create a DelegatingServletOutputStream for the given target stream.
	 * @param targetStream the target stream (never <code>null</code>)
	 */
	public DelegatingServletOutputStream(OutputStream targetStream) {
		Asserts.notNull(targetStream, "Target OutputStream must not be null");
		this.targetStream = targetStream;
	}

	/**
	 * Return the underlying target stream (never <code>null</code>).
	 */
	public final OutputStream getTargetStream() {
		return this.targetStream;
	}


	public void write(int b) throws IOException {
		this.targetStream.write(b);
	}

	public void flush() throws IOException {
		super.flush();
		this.targetStream.flush();
	}

	public void close() throws IOException {
		super.close();
		this.targetStream.close();
	}

}
