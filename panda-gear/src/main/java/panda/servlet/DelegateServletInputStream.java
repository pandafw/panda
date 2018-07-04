package panda.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

import panda.lang.Asserts;
import panda.lang.Exceptions;

/**
 * Delegating implementation of {@link javax.servlet.ServletInputStream}.
 */
public class DelegateServletInputStream extends ServletInputStream {

	private final InputStream source;

	/**
	 * Create a DelegatingServletInputStream for the given source stream.
	 * @param source the source stream (never <code>null</code>)
	 */
	public DelegateServletInputStream(InputStream source) {
		Asserts.notNull(source, "Source InputStream must not be null");
		this.source = source;
	}

	/**
	 * @return the underlying source stream (never <code>null</code>).
	 */
	public final InputStream getSourceStream() {
		return this.source;
	}


	public int read() throws IOException {
		return this.source.read();
	}

	public void close() throws IOException {
		this.source.close();
	}

	public boolean isFinished() {
		try {
			return source.available() > 0;
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public boolean isReady() {
		return true;
	}
}
