package panda.mock.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

/**
 * Mock implementation of the {@link javax.servlet.jsp.JspWriter} class.
 *
 * <p>Used for testing the web framework; only necessary for testing
 * applications when testing custom JSP tags.
 *
 */
public class MockJspWriter extends JspWriter {

	private final HttpServletResponse response;

	private PrintWriter targetWriter;


	/**
	 * Create a MockJspWriter for the given response,
	 * using the response's default Writer.
	 * @param response the servlet response to wrap
	 */
	public MockJspWriter(HttpServletResponse response) {
		this(response, null);
	}

	/**
	 * Create a MockJspWriter for the given plain Writer.
	 * @param targetWriter the target Writer to wrap
	 */
	public MockJspWriter(Writer targetWriter) {
		this(null, targetWriter);
	}

	/**
	 * Create a MockJspWriter for the given response.
	 * @param response the servlet response to wrap
	 * @param targetWriter the target Writer to wrap
	 */
	public MockJspWriter(HttpServletResponse response, Writer targetWriter) {
		super(DEFAULT_BUFFER, true);
		this.response = (response != null ? response : new MockHttpServletResponse());
		if (targetWriter instanceof PrintWriter) {
			this.targetWriter = (PrintWriter) targetWriter;
		}
		else if (targetWriter != null) {
			this.targetWriter = new PrintWriter(targetWriter);
		}
	}

	/**
	 * Lazily initialize the target Writer.
	 */
	protected PrintWriter getTargetWriter() throws IOException {
		if (this.targetWriter == null) {
			this.targetWriter = this.response.getWriter();
		}
		return this.targetWriter;
	}


	public void clear() throws IOException {
		if (this.response.isCommitted()) {
			throw new IOException("Response already committed");
		}
		this.response.resetBuffer();
	}

	public void clearBuffer() throws IOException {
	}

	public void flush() throws IOException {
		this.response.flushBuffer();
	}

	public void close() throws IOException {
		flush();
	}

	public int getRemaining() {
		return Integer.MAX_VALUE;
	}

	public void newLine() throws IOException {
		getTargetWriter().println();
	}

	public void write(char value[], int offset, int length) throws IOException {
		getTargetWriter().write(value, offset, length);
	}

	public void print(boolean value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(char value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(char[] value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(double value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(float value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(int value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(long value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(Object value) throws IOException {
		getTargetWriter().print(value);
	}

	public void print(String value) throws IOException {
		getTargetWriter().print(value);
	}

	public void println() throws IOException {
		getTargetWriter().println();
	}

	public void println(boolean value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(char value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(char[] value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(double value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(float value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(int value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(long value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(Object value) throws IOException {
		getTargetWriter().println(value);
	}

	public void println(String value) throws IOException {
		getTargetWriter().println(value);
	}

}
