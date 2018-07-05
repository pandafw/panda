package panda.net.nntp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import panda.net.io.DotTerminatedMessageReader;
import panda.net.io.Util;

/**
 * Wraps a {@link BufferedReader} and returns an {@code Iterable<String>} which returns the
 * individual lines from the reader.
 */
class ReplyIterator implements Iterator<String>, Iterable<String> {

	private final BufferedReader reader;

	private String line;

	private Exception savedException;

	/**
	 * @param _reader the reader to wrap
	 * @param addDotReader whether to additionally wrap the reader in a DotTerminatedMessageReader
	 * @throws IOException
	 */
	ReplyIterator(BufferedReader _reader, boolean addDotReader) throws IOException {
		reader = addDotReader ? new DotTerminatedMessageReader(_reader) : _reader;
		line = reader.readLine(); // prime the iterator
		if (line == null) {
			Util.closeQuietly(reader);
		}
	}

	ReplyIterator(BufferedReader _reader) throws IOException {
		this(_reader, true);
	}

	// @Override
	public boolean hasNext() {
		if (savedException != null) {
			throw new NoSuchElementException(savedException.toString());
		}
		return line != null;
	}

	// @Override
	public String next() throws NoSuchElementException {
		if (savedException != null) {
			throw new NoSuchElementException(savedException.toString());
		}
		String prev = line;
		if (prev == null) {
			throw new NoSuchElementException();
		}
		try {
			line = reader.readLine(); // save next line
			if (line == null) {
				Util.closeQuietly(reader);
			}
		}
		catch (IOException ex) {
			savedException = ex; // if it fails, save the exception, as it does not apply to this
									// call
			Util.closeQuietly(reader);
		}
		return prev;
	}

	// @Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	// @Override
	public Iterator<String> iterator() {
		return this;
	}
}
