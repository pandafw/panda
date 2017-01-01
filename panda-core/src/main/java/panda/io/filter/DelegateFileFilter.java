package panda.io.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * This class turns a Java FileFilter or FilenameFilter into an IO FileFilter.
 */
public class DelegateFileFilter extends AbstractFileFilter {

	/** The Filename filter */
	private final FilenameFilter filenameFilter;
	/** The File filter */
	private final FileFilter fileFilter;

	/**
	 * Constructs a delegate file filter around an existing FilenameFilter.
	 * 
	 * @param filter the filter to decorate
	 */
	public DelegateFileFilter(FilenameFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("The FilenameFilter must not be null");
		}
		this.filenameFilter = filter;
		this.fileFilter = null;
	}

	/**
	 * Constructs a delegate file filter around an existing FileFilter.
	 * 
	 * @param filter the filter to decorate
	 */
	public DelegateFileFilter(FileFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("The FileFilter must not be null");
		}
		this.fileFilter = filter;
		this.filenameFilter = null;
	}

	/**
	 * Checks the filter.
	 * 
	 * @param file the file to check
	 * @return true if the filter matches
	 */
	@Override
	public boolean accept(File file) {
		if (fileFilter != null) {
			return fileFilter.accept(file);
		}
		else {
			return super.accept(file);
		}
	}

	/**
	 * Checks the filter.
	 * 
	 * @param dir the directory
	 * @param name the filename in the directory
	 * @return true if the filter matches
	 */
	@Override
	public boolean accept(File dir, String name) {
		if (filenameFilter != null) {
			return filenameFilter.accept(dir, name);
		}
		else {
			return super.accept(dir, name);
		}
	}

	/**
	 * Provide a String representaion of this file filter.
	 * 
	 * @return a String representaion
	 */
	@Override
	public String toString() {
		String delegate = fileFilter != null ? fileFilter.toString() : filenameFilter.toString();
		return super.toString() + "(" + delegate + ")";
	}

}
