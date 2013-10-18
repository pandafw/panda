package panda.io.stream;

import java.io.Writer;

/**
 * This {@link Writer} writes all data to the famous <b>/dev/null</b>.
 * <p>
 * This <code>Writer</code> has no destination (file/socket etc.) and all characters written to it
 * are ignored and lost.
 * 
 */
public class NullWriter extends Writer {

	/**
	 * A singleton.
	 */
	public static final NullWriter INSTANCE = new NullWriter();

	/**
	 * Constructs a new NullWriter.
	 */
	public NullWriter() {
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param c The character to write
	 * @return this writer
	 */
	@Override
	public Writer append(final char c) {
		// to /dev/null
		return this;
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param csq The character sequence to write
	 * @param start The index of the first character to write
	 * @param end The index of the first character to write (exclusive)
	 * @return this writer
	 */
	@Override
	public Writer append(final CharSequence csq, final int start, final int end) {
		// to /dev/null
		return this;
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param csq The character sequence to write
	 * @return this writer
	 */
	@Override
	public Writer append(final CharSequence csq) {
		// to /dev/null
		return this;
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param idx The character to write
	 */
	@Override
	public void write(final int idx) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param chr The characters to write
	 */
	@Override
	public void write(final char[] chr) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param chr The characters to write
	 * @param st The start offset
	 * @param end The number of characters to write
	 */
	@Override
	public void write(final char[] chr, final int st, final int end) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param str The string to write
	 */
	@Override
	public void write(final String str) {
		// to /dev/null
	}

	/**
	 * Does nothing - output to <code>/dev/null</code>.
	 * 
	 * @param str The string to write
	 * @param st The start offset
	 * @param end The number of characters to write
	 */
	@Override
	public void write(final String str, final int st, final int end) {
		// to /dev/null
	}

	/** @see java.io.Writer#flush() */
	@Override
	public void flush() {
		// to /dev/null
	}

	/** @see java.io.Writer#close() */
	@Override
	public void close() {
		// to /dev/null
	}

}
