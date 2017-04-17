package panda.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

import panda.lang.Asserts;
import panda.lang.Exceptions;
import panda.servlet.mock.MockHttpServletRequest;

/**
 * Delegating implementation of {@link javax.servlet.ServletInputStream}.
 *
 * <p>Used by {@link MockHttpServletRequest}; typically not directly
 * used for testing application controllers.
 *
 * @see MockHttpServletRequest
 */
public class DelegateServletInputStream extends ServletInputStream {

	private final InputStream sourceStream;


	/**
	 * Create a DelegatingServletInputStream for the given source stream.
	 * @param sourceStream the source stream (never <code>null</code>)
	 */
	public DelegateServletInputStream(InputStream sourceStream) {
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
		this.sourceStream.close();
	}

	public boolean isFinished() {
		try {
			return sourceStream.available() > 0;
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public boolean isReady() {
		return true;
	}
}
