package panda.servlet;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import panda.lang.Asserts;
import panda.lang.Exceptions;
import panda.mock.web.MockHttpServletResponse;

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
		this.targetStream.flush();
	}

	public void close() throws IOException {
		super.close();
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		throw Exceptions.unsupported();
	}

}
