package panda.io.filter;

import java.io.File;

/**
 * A file filter that always returns false.
 * 
 * @see FileFilters#falseFileFilter()
 */
public class FalseFileFilter implements IOFileFilter {

	/**
	 * Singleton instance of false filter.
	 */
	public static final IOFileFilter FALSE = new FalseFileFilter();
	/**
	 * Singleton instance of false filter. Please use the identical FalseFileFilter.FALSE constant.
	 * The new name is more JDK 1.5 friendly as it doesn't clash with other values when using static
	 * imports.
	 */
	public static final IOFileFilter INSTANCE = FALSE;

	/**
	 * Restrictive consructor.
	 */
	protected FalseFileFilter() {
	}

	/**
	 * Returns false.
	 * 
	 * @param file the file to check (ignored)
	 * @return false
	 */
	public boolean accept(File file) {
		return false;
	}

	/**
	 * Returns false.
	 * 
	 * @param dir the directory to check (ignored)
	 * @param name the filename (ignored)
	 * @return false
	 */
	public boolean accept(File dir, String name) {
		return false;
	}

}
