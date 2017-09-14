package panda.io;

import java.io.File;
import java.util.Iterator;

/**
 * An Iterator for glob files.
 */
public interface FileIterator extends Iterator<File>, AutoCloseable {
}
