package panda.io.filter;

import java.io.File;

/**
 * This filter accepts files or directories that are empty.
 * <p>
 * If the <code>File</code> is a directory it checks that it contains no files.
 * <p>
 * Example, showing how to print out a list of the current directory's empty files/directories:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(EmptyFileFilter.EMPTY);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * <p>
 * Example, showing how to print out a list of the current directory's non-empty files/directories:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(EmptyFileFilter.NOT_EMPTY);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 */
public class EmptyFileFilter extends AbstractFileFilter {

	/** Singleton instance of <i>empty</i> filter */
	public static final IOFileFilter EMPTY = new EmptyFileFilter();

	/** Singleton instance of <i>not-empty</i> filter */
	public static final IOFileFilter NOT_EMPTY = new NotFileFilter(EMPTY);

	/**
	 * Restrictive consructor.
	 */
	protected EmptyFileFilter() {
	}

	/**
	 * Checks to see if the file is empty.
	 * 
	 * @param file the file or directory to check
	 * @return {@code true} if the file or directory is <i>empty</i>, otherwise {@code false}.
	 */
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			return files == null || files.length == 0;
		}
		else {
			return file.length() == 0;
		}
	}

}
