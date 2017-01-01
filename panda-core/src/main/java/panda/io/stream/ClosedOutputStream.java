package panda.io.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Closed output stream. This stream throws an exception on all attempts to write something to the
 * stream.
 * <p>
 * Typically uses of this class include testing for corner cases in methods that accept an output
 * stream and acting as a sentinel value instead of a {@code null} output stream.
 */
public class ClosedOutputStream extends OutputStream {

	/**
	 * A singleton.
	 */
	public static final ClosedOutputStream INSTANCE = new ClosedOutputStream();

	/**
	 * Throws an {@link IOException} to indicate that the stream is closed.
	 * 
	 * @param b ignored
	 * @throws IOException always thrown
	 */
	@Override
	public void write(final int b) throws IOException {
		throw new IOException("write(" + b + ") failed: stream is closed");
	}

}
