package panda.dao;

import java.util.Iterator;

/**
 * Iterator for data
 * @param <T> data type
 */
public interface DaoIterator<T> extends Iterator<T>, AutoCloseable {
	void close();
}
