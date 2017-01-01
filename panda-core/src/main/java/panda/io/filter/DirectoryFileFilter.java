package panda.io.filter;

import java.io.File;

/**
 * This filter accepts <code>File</code>s that are directories.
 * <p>
 * For example, here is how to print out a list of the current directory's subdirectories:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(DirectoryFileFilter.INSTANCE);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @see FileFilters#directoryFileFilter()
 */
public class DirectoryFileFilter extends AbstractFileFilter {

	/**
	 * Singleton instance of directory filter.
	 */
	public static final IOFileFilter DIRECTORY = new DirectoryFileFilter();
	/**
	 * Singleton instance of directory filter. Please use the identical
	 * DirectoryFileFilter.DIRECTORY constant. The new name is more JDK 1.5 friendly as it doesn't
	 * clash with other values when using static imports.
	 */
	public static final IOFileFilter INSTANCE = DIRECTORY;

	/**
	 * Restrictive consructor.
	 */
	protected DirectoryFileFilter() {
	}

	/**
	 * Checks to see if the file is a directory.
	 * 
	 * @param file the File to check
	 * @return true if the file is a directory
	 */
	@Override
	public boolean accept(File file) {
		return file.isDirectory();
	}

}
