package panda.mock.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

import panda.lang.Asserts;

/**
 * Delegating implementation of {@link javax.servlet.ServletInputStream}.
 *
 * <p>Used by {@link MockHttpServletRequest}; typically not directly
 * used for testing application controllers.
 *
 * @see MockHttpServletRequest
 */
public class DelegatingServletInputStream extends ServletInputStream {

	private final InputStream sourceStream;


	/**
	 * Create a DelegatingServletInputStream for the given source stream.
	 * @param sourceStream the source stream (never <code>null</code>)
	 */
	public DelegatingServletInputStream(InputStream sourceStream) {
		Asserts.notNull(sourceStream, "Source InputStream must not be null");
		this.sourceStream = sourceStream;
	}

	/**
	 * Return the underlying source stream (never <code>null</code>).
	 */
	public final InputStream getSourceStream() {
		return this.sourceStream;
	}


	public int read() throws IOException {
		return this.sourceStream.read();
	}

	public void close() throws IOException {
		super.close();
		this.sourceStream.close();
	}

}
