package panda.io.filter;

import java.io.File;

/**
 * A file filter that always returns true.
 * 
 * @see FileFilters#trueFileFilter()
 */
public class TrueFileFilter implements IOFileFilter {

	/**
	 * Singleton instance of true filter.
	 */
	public static final IOFileFilter TRUE = new TrueFileFilter();
	/**
	 * Singleton instance of true filter. Please use the identical TrueFileFilter.TRUE constant. The
	 * new name is more JDK 1.5 friendly as it doesn't clash with other values when using static
	 * imports.
	 */
	public static final IOFileFilter INSTANCE = TRUE;

	/**
	 * Restrictive consructor.
	 */
	protected TrueFileFilter() {
	}

	/**
	 * Returns true.
	 * 
	 * @param file the file to check (ignored)
	 * @return true
	 */
	public boolean accept(File file) {
		return true;
	}

	/**
	 * Returns true.
	 * 
	 * @param dir the directory to check (ignored)
	 * @param name the filename (ignored)
	 * @return true
	 */
	public boolean accept(File dir, String name) {
		return true;
	}

}
