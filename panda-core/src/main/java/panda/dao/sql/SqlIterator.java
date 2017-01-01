package panda.dao.sql;

import java.io.Closeable;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Strings;

/**
 * An SQL Iterator
 */
public class SqlIterator implements Iterator<String>, Closeable {

	/** The reader that is being read. */
	private final PushbackReader reader;

	/** The current sql. */
	private StringBuilder sql = new StringBuilder();
	
	/** A flag indicating if the iterator has been fully read. */
	private boolean eof = false;

	/**
	 * Constructs an iterator of the lines for a <code>Reader</code>.
	 * 
	 * @param text the sql text, not null
	 * @throws IllegalArgumentException if the reader is null
	 */
	public SqlIterator(final String text) throws IllegalArgumentException {
		if (text == null) {
			throw new IllegalArgumentException("sql text must not be null");
		}
		this.reader = new PushbackReader(new StringReader(text));
	}

	/**
	 * Constructs an iterator of the lines for a <code>Reader</code>.
	 * 
	 * @param reader the <code>Reader</code> to read from, not null
	 * @throws IllegalArgumentException if the reader is null
	 */
	public SqlIterator(final Reader reader) throws IllegalArgumentException {
		if (reader == null) {
			throw new IllegalArgumentException("Reader must not be null");
		}
		this.reader = new PushbackReader(reader);
	}

	// -----------------------------------------------------------------------
	/**
	 * Indicates whether the <code>Reader</code> has more lines. If there is an
	 * <code>IOException</code> then {@link #close()} will be called on this instance.
	 * 
	 * @return <code>true</code> if the Reader has more lines
	 * @throws IllegalStateException if an IO exception occurs
	 */
	public boolean hasNext() {
		if (sql.length() > 0) {
			return true;
		}
		
		if (eof) {
			return false;
		}

		try {
			while (true) {
				int c = reader.read();
				if (c == Streams.EOF) {
					eof = true;
					break;
				}
				if (c == '/') {
					c = reader.read();
					if (c == Streams.EOF) {
						sql.append('/');
						break;
					}
					if (c == '*') {
						while ((c = reader.read()) != Streams.EOF) {
							if (c == '*') {
								c = reader.read();
								if (c == Streams.EOF || c == '/') {
									break;
								}
							}
						}
						if (c == Streams.EOF) {
							eof = true;
							break;
						}
					}
					else {
						sql.append('/');
					}
				}
				else if (c == '-') {
					c = reader.read();
					if (c == Streams.EOF) {
						sql.append('-');
						break;
					}
					if (c == '-') {
						while ((c = reader.read()) != Streams.EOF) {
							if (c == '\n') {
								break;
							}
						}
						if (c == Streams.EOF) {
							eof = true;
							break;
						}
					}
					else {
						sql.append('-');
					}
				}
				else if (c == '\'') {
					sql.append((char)c);
					while ((c = reader.read()) != Streams.EOF) {
						sql.append((char)c);
						if (c == '\'') {
							c = reader.read();
							if (c == Streams.EOF) {
								break;
							}
							if (c != '\'') {
								reader.unread(c);
								break;
							}
							sql.append((char)c);
						}
					}
					if (c == Streams.EOF) {
						eof = true;
						break;
					}
				}
				else if (Character.isWhitespace(c)) {
					// do not append leading space
					if (sql.length() > 0) {
						sql.append((char)c);
					}
				}
				else if (c == ';') {
					Strings.removeEnd(sql);
					if (sql.length() > 0) {
						return true;
					}
				}
				else {
					sql.append((char)c);
				}
			}
		}
		catch (IOException ioe) {
			close();
			throw new IllegalStateException(ioe);
		}

		Strings.removeEnd(sql);
		return sql.length() > 0;
	}

	/**
	 * Returns the next line in the wrapped <code>Reader</code>.
	 * 
	 * @return the next line from the input
	 * @throws NoSuchElementException if there is no line to return
	 */
	public String next() {
		return nextLine();
	}

	/**
	 * Returns the next line in the wrapped <code>Reader</code>.
	 * 
	 * @return the next line from the input
	 * @throws NoSuchElementException if there is no line to return
	 */
	public String nextLine() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more lines");
		}

		try {
			return sql.toString();
		}
		finally {
			sql.setLength(0);
		}
	}

	/**
	 * Closes the underlying <code>Reader</code> quietly. This method is useful if you only want to
	 * process the first few lines of a larger file. If you do not close the iterator then the
	 * <code>Reader</code> remains open. This method can safely be called multiple times.
	 */
	public void close() {
		eof = true;
		sql.setLength(0);
		Streams.safeClose(reader);
	}

	/**
	 * Unsupported.
	 * 
	 * @throws UnsupportedOperationException always
	 */
	public void remove() {
		throw Exceptions.unsupported("Remove unsupported on LineIterator");
	}
}
