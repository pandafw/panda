package panda.io.filter;

import java.io.File;

/**
 * Filters files based on size, can filter either smaller files or files equal to or larger than a
 * given threshold.
 * <p>
 * For example, to print all files and directories in the current directory whose size is greater
 * than 1 MB:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(new SizeFileFilter(1024 * 1024));
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @see FileFilters#sizeFileFilter(long)
 * @see FileFilters#sizeFileFilter(long, boolean)
 * @see FileFilters#sizeRangeFileFilter(long, long)
 */
public class SizeFileFilter extends AbstractFileFilter {

	/** The size threshold. */
	private final long size;
	/** Whether the files accepted will be larger or smaller. */
	private final boolean acceptLarger;

	/**
	 * Constructs a new size file filter for files equal to or larger than a certain size.
	 * 
	 * @param size the threshold size of the files
	 * @throws IllegalArgumentException if the size is negative
	 */
	public SizeFileFilter(long size) {
		this(size, true);
	}

	/**
	 * Constructs a new size file filter for files based on a certain size threshold.
	 * 
	 * @param size the threshold size of the files
	 * @param acceptLarger if true, files equal to or larger are accepted, otherwise smaller ones
	 *            (but not equal to)
	 * @throws IllegalArgumentException if the size is negative
	 */
	public SizeFileFilter(long size, boolean acceptLarger) {
		if (size < 0) {
			throw new IllegalArgumentException("The size must be non-negative");
		}
		this.size = size;
		this.acceptLarger = acceptLarger;
	}

	// -----------------------------------------------------------------------
	/**
	 * Checks to see if the size of the file is favorable.
	 * <p>
	 * If size equals threshold and smaller files are required, file <b>IS NOT</b> selected. If size
	 * equals threshold and larger files are required, file <b>IS</b> selected.
	 * 
	 * @param file the File to check
	 * @return true if the filename matches
	 */
	@Override
	public boolean accept(File file) {
		boolean smaller = file.length() < size;
		return acceptLarger ? !smaller : smaller;
	}

	/**
	 * Provide a String representaion of this file filter.
	 * 
	 * @return a String representaion
	 */
	@Override
	public String toString() {
		String condition = acceptLarger ? ">=" : "<";
		return super.toString() + "(" + condition + size + ")";
	}

}
