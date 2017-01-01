package panda.io.filter;

import java.io.File;

/**
 * This filter accepts <code>File</code>s that can be written to.
 * <p>
 * Example, showing how to print out a list of the current directory's <i>writable</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(CanWriteFileFilter.CAN_WRITE);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * <p>
 * Example, showing how to print out a list of the current directory's <i>un-writable</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(CanWriteFileFilter.CANNOT_WRITE);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * <p>
 * <b>N.B.</b> For read-only files, use <code>CanReadFileFilter.READ_ONLY</code>.
 */
public class CanWriteFileFilter extends AbstractFileFilter {

	/** Singleton instance of <i>writable</i> filter */
	public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();

	/** Singleton instance of not <i>writable</i> filter */
	public static final IOFileFilter CANNOT_WRITE = new NotFileFilter(CAN_WRITE);

	/**
	 * Restrictive consructor.
	 */
	protected CanWriteFileFilter() {
	}

	/**
	 * Checks to see if the file can be written to.
	 * 
	 * @param file the File to check
	 * @return {@code true} if the file can be written to, otherwise {@code false}.
	 */
	@Override
	public boolean accept(File file) {
		return file.canWrite();
	}

}
