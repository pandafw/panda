package panda.io.filter;

import java.io.File;
import java.util.Date;

import panda.io.Files;

/**
 * Filters files based on a cutoff time, can filter either newer files or files equal to or older.
 * <p>
 * For example, to print all files and directories in the current directory older than one day:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * // We are interested in files older than one day
 * long cutoff = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
 * String[] files = dir.list(new AgeFileFilter(cutoff));
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 */
public class AgeFileFilter extends AbstractFileFilter {

	/** The cutoff time threshold. */
	private final long cutoff;
	/** Whether the files accepted will be older or newer. */
	private final boolean acceptOlder;

	/**
	 * Constructs a new age file filter for files equal to or older than a certain cutoff
	 * 
	 * @param cutoff the threshold age of the files
	 */
	public AgeFileFilter(long cutoff) {
		this(cutoff, true);
	}

	/**
	 * Constructs a new age file filter for files on any one side of a certain cutoff.
	 * 
	 * @param cutoff the threshold age of the files
	 * @param acceptOlder if true, older files (at or before the cutoff) are accepted, else newer
	 *            ones (after the cutoff).
	 */
	public AgeFileFilter(long cutoff, boolean acceptOlder) {
		this.acceptOlder = acceptOlder;
		this.cutoff = cutoff;
	}

	/**
	 * Constructs a new age file filter for files older than (at or before) a certain cutoff date.
	 * 
	 * @param cutoffDate the threshold age of the files
	 */
	public AgeFileFilter(Date cutoffDate) {
		this(cutoffDate, true);
	}

	/**
	 * Constructs a new age file filter for files on any one side of a certain cutoff date.
	 * 
	 * @param cutoffDate the threshold age of the files
	 * @param acceptOlder if true, older files (at or before the cutoff) are accepted, else newer
	 *            ones (after the cutoff).
	 */
	public AgeFileFilter(Date cutoffDate, boolean acceptOlder) {
		this(cutoffDate.getTime(), acceptOlder);
	}

	/**
	 * Constructs a new age file filter for files older than (at or before) a certain File (whose
	 * last modification time will be used as reference).
	 * 
	 * @param cutoffReference the file whose last modification time is usesd as the threshold age of
	 *            the files
	 */
	public AgeFileFilter(File cutoffReference) {
		this(cutoffReference, true);
	}

	/**
	 * Constructs a new age file filter for files on any one side of a certain File (whose last
	 * modification time will be used as reference).
	 * 
	 * @param cutoffReference the file whose last modification time is usesd as the threshold age of
	 *            the files
	 * @param acceptOlder if true, older files (at or before the cutoff) are accepted, else newer
	 *            ones (after the cutoff).
	 */
	public AgeFileFilter(File cutoffReference, boolean acceptOlder) {
		this(cutoffReference.lastModified(), acceptOlder);
	}

	// -----------------------------------------------------------------------
	/**
	 * Checks to see if the last modification of the file matches cutoff favorably.
	 * <p>
	 * If last modification time equals cutoff and newer files are required, file <b>IS NOT</b>
	 * selected. If last modification time equals cutoff and older files are required, file
	 * <b>IS</b> selected.
	 * 
	 * @param file the File to check
	 * @return true if the filename matches
	 */
	@Override
	public boolean accept(File file) {
		boolean newer = Files.isFileNewer(file, cutoff);
		return acceptOlder ? !newer : newer;
	}

	/**
	 * Provide a String representaion of this file filter.
	 * 
	 * @return a String representaion
	 */
	@Override
	public String toString() {
		String condition = acceptOlder ? "<=" : ">";
		return super.toString() + "(" + condition + cutoff + ")";
	}
}
