package panda.io.filter;

import java.io.File;

/**
 * This filter accepts <code>File</code>s that are files (not directories).
 * <p>
 * For example, here is how to print out a list of the real files within the current directory:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(FileFileFilter.FILE);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @see FileFilters#fileFileFilter()
 */
public class FileFileFilter extends AbstractFileFilter {

	/** Singleton instance of file filter */
	public static final IOFileFilter FILE = new FileFileFilter();

	/**
	 * Restrictive consructor.
	 */
	protected FileFileFilter() {
	}

	/**
	 * Checks to see if the file is a file.
	 * 
	 * @param file the File to check
	 * @return true if the file is a file
	 */
	@Override
	public boolean accept(File file) {
		return file.isFile();
	}

}
