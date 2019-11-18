package panda.io.stream;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import panda.lang.Iterators;

public abstract class ListWriter {
	/**
	 * Writes the list to the file.
	 *
	 * @param list a collection with each comma-separated element as a separate entry.
	 * @throws IOException if an IO error occurred
	 */
	public void writeList(Collection<?> list) throws IOException {
		writeIterator(list.iterator());
	}

	/**
	 * Writes the next line to the file.
	 *
	 * @param array a array with each comma-separated element as a separate entry.
	 * @throws IOException if an IO error occurred
	 */
	public void writeArray(Object[] array) throws IOException {
		writeIterator(Iterators.asIterator(array));
	}

	/**
	 * Writes the next line to the file.
	 *
	 * @param array a array with each comma-separated element as a separate entry.
	 * @throws IOException if an IO error occurred
	 */
	public void writeArray(Object array) throws IOException {
		writeIterator(Iterators.asIterator(array));
	}

	/**
	 * Writes the list to the file.
	 *
	 * @param it a iterator with each comma-separated element as a separate entry.
	 * @throws IOException if an IO error occurred
	 */
	public abstract void writeIterator(Iterator it) throws IOException;

}
