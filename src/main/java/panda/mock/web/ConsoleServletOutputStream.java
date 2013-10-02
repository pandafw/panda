package panda.mock.web;

import java.io.IOException;
import java.io.PrintStream;

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
public class ConsoleServletOutputStream extends ServletOutputStream {

	private PrintStream targetStream;


	/**
	 * Create a DelegatingServletOutputStream for the given target stream.
	 */
	public ConsoleServletOutputStream() {
		targetStream = System.out;
	}

	/**
	 * Create a DelegatingServletOutputStream for the given target stream.
	 * @param targetStream the target stream (never <code>null</code>)
	 */
	public ConsoleServletOutputStream(PrintStream targetStream) {
		Asserts.notNull(targetStream, "Target OutputStream must not be null");
		this.targetStream = targetStream;
	}

	/**
	 * Return the underlying target stream (never <code>null</code>).
	 */
	public final PrintStream getTargetStream() {
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
	}

}
