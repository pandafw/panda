package panda.io.filter;

import java.io.File;

/**
 * An abstract class which implements the Java FileFilter and FilenameFilter interfaces via the
 * IOFileFilter interface.
 * <p>
 * Note that a subclass <b>must</b> override one of the accept methods, otherwise your class will
 * infinitely loop.
 */
public abstract class AbstractFileFilter implements IOFileFilter {

	/**
	 * Checks to see if the File should be accepted by this filter.
	 * 
	 * @param file the File to check
	 * @return true if this file matches the test
	 */
	public boolean accept(File file) {
		return accept(file.getParentFile(), file.getName());
	}

	/**
	 * Checks to see if the File should be accepted by this filter.
	 * 
	 * @param dir the directory File to check
	 * @param name the filename within the directory to check
	 * @return true if this file matches the test
	 */
	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}

	/**
	 * Provide a String representaion of this file filter.
	 * 
	 * @return a String representaion
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
