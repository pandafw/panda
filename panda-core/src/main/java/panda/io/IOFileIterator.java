package panda.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import panda.io.filter.DirectoryFileFilter;
import panda.io.filter.FalseFileFilter;
import panda.io.filter.FileFilters;
import panda.io.filter.IOFileFilter;
import panda.io.filter.SuffixFileFilter;
import panda.io.filter.TrueFileFilter;
import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.collection.KeyValue;

/**
 * An Iterator for glob files.
 */
public class IOFileIterator implements FileIterator {

	/**
	 * stack of directory stream and path iterator
	 */
	protected Stack<KeyValue<?, ?>> stack;

	/**
	 * file filter
	 */
	private IOFileFilter filter;
	
	/** 
	 * include directory or not
	 */
	private boolean incdir;
	
	/**
	 * current file
	 */
	private File file;

	/**
	 * Allows iteration over the files in a given directory (no recursive)
	 * which match an array of extensions. 
	 *
	 * @param directory the directory to search in
	 * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is
	 *            {@code null}, all files are returned.
	 */
	public IOFileIterator(File directory, String... extensions) {
		this(directory, false, extensions);
	}

	/**
	 * Allows iteration over the files in a given directory (and optionally its sub directories)
	 * which match an array of extensions. 
	 * 
	 * @param directory the directory to search in
	 * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is
	 *            {@code null}, all files are returned.
	 * @param recursive if true all sub directories are searched as well
	 */
	public IOFileIterator(File directory, boolean recursive, String... extensions) {
		IOFileFilter filter;
		if (Arrays.isEmpty(extensions)) {
			filter = TrueFileFilter.INSTANCE;
		}
		else {
			final String[] suffixes = toSuffixes(extensions);
			filter = new SuffixFileFilter(suffixes);
		}
		open(directory, filter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE, false);
	}

	/**
	 * Allows iteration over the files in given directory (and optionally its sub directories).
	 * <p>
	 * All files found are filtered by an IOFileFilter. 
	 * <p>
	 * 
	 * @param directory the directory to search in
	 * @param fileFilter filter to apply when finding files.
	 * @param recursive if true all sub directories are searched as well
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public IOFileIterator(File directory, boolean recursive, IOFileFilter fileFilter) {
		open(directory, fileFilter, recursive ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE, false);
	}

	/**
	 * Allows iteration over the files in given directory (and optionally its sub directories).
	 * <p>
	 * All files found are filtered by an IOFileFilter.
	 * <p>
	 * 
	 * @param directory the directory to search in
	 * @param fileFilter filter to apply when finding files.
	 * @param dirFilter optional filter to apply when finding sub directories. If this parameter is
	 *            {@code null}, sub directories will not be included in the search. Use
	 *            TrueFileFilter.INSTANCE to match all directories.
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public IOFileIterator(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		open(directory, fileFilter, dirFilter, false);
	}

	/**
	 * Allows iteration over the files in given directory (and optionally its sub directories).
	 * <p>
	 * All files found are filtered by an IOFileFilter. 
	 * <p>
	 * 
	 * @param directory the directory to search in
	 * @param fileFilter filter to apply when finding files.
	 * @param dirFilter optional filter to apply when finding sub directories. If this parameter is
	 *            {@code null}, sub directories will not be included in the search. Use
	 *            TrueFileFilter.INSTANCE to match all directories.
	 * @param incdir if true all sub directories are included as well
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public IOFileIterator(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter, boolean incdir) {
		open(directory, fileFilter, dirFilter, incdir);
	}
	
	// -----------------------------------------------------------------------
	protected void open(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter, boolean incdir) {
		validateListFilesParameters(directory, fileFilter);

		IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
		IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);
		filter = FileFilters.or(effFileFilter, effDirFilter);

		this.incdir = incdir;

		// include directory self
		if (incdir && directory.isDirectory()) {
			file = directory;
		}

		stack = new Stack<KeyValue<?, ?>>();
		openDir(directory);
	}
	
	protected void openDir(File directory) {
		String[] fs = directory.list();
		Iterator<String> it = Arrays.asList(fs).iterator();
		stack.push(new KeyValue<File, Iterator<String>>(directory, it));
	}
	
	@SuppressWarnings("unchecked")
	protected File nextFile() {
		KeyValue<?, ?> kv = stack.peek();
		File dir = (File)kv.getKey(); 
		Iterator<String> it = (Iterator<String>)kv.getValue();
		if (it.hasNext()) {
			String n = it.next();
			File f = new File(dir, n);
			return f;
		}
		return null;
	}
	
	protected void closeDir() {
		stack.pop();
	}

	// -----------------------------------------------------------------------
	/**
	 * Indicates whether the <code>Iterator</code> has more files.
	 * 
	 * @return <code>true</code> if has more files
	 * @throws IllegalStateException if an IO exception occurs
	 */
	public boolean hasNext() {
		if (file != null) {
			return true;
		}
		
		if (stack.empty()) {
			return false;
		}
		
		while (!stack.empty()) {
			File f;
			while ((f = nextFile()) != null) {
				if (filter != null && !filter.accept(f)) {
					continue;
				}
				
				if (f.isDirectory()) {
					openDir(f);
					if (incdir) {
						file = f;
						return true;
					}
				}
				else {
					file = f;
					return true;
				}
			}

			closeDir();
		}

		return false;
	}

	/**
	 * Returns the next file.
	 * 
	 * @return the next file
	 * @throws NoSuchElementException if there is no file to return
	 */
	public File next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more files");
		}
		
		File f = file;
		file = null;
		return f;
	}

	/**
	 * Unsupported.
	 * 
	 * @throws UnsupportedOperationException always
	 */
	public void remove() {
		throw Exceptions.unsupported("Remove unsupported on FileIterator");
	}

	@Override
	public void close() throws Exception {
		stack.empty();
	}

	// -----------------------------------------------------------------------
	/**
	 * Converts an array of file extensions to suffixes for use with IOFileFilters.
	 * 
	 * @param extensions an array of extensions. Format: {"java", "xml"}
	 * @return an array of suffixes. Format: {".java", ".xml"}
	 */
	private String[] toSuffixes(final String[] extensions) {
		final List<String> suffixes = new ArrayList<String>(extensions.length);
		for (String e : extensions) {
			if (Strings.isEmpty(e)) {
				continue;
			}
			if (e.charAt(0) == '.') {
				suffixes.add(e);
			}
			else {
				suffixes.add('.' + e);
			}
		}
		return suffixes.toArray(new String[suffixes.size()]);
	}
	
	/**
	 * Validates the given arguments.
	 * <ul>
	 * <li>Throws {@link IllegalArgumentException} if {@code directory} is not a directory</li>
	 * <li>Throws {@link NullPointerException} if {@code fileFilter} is null</li>
	 * </ul>
	 * 
	 * @param directory The File to test
	 * @param fileFilter The IOFileFilter to test
	 */
	private void validateListFilesParameters(final File directory, final IOFileFilter fileFilter) {
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Parameter 'directory' is not a directory: " + directory);
		}
		if (fileFilter == null) {
			throw new NullPointerException("Parameter 'fileFilter' is null");
		}
	}
	
	/**
	 * Returns a filter that accepts files in addition to the {@link File} objects accepted by the
	 * given filter.
	 * 
	 * @param fileFilter a base filter to add to
	 * @return a filter that accepts files
	 */
	private IOFileFilter setUpEffectiveFileFilter(final IOFileFilter fileFilter) {
		return FileFilters.and(fileFilter, FileFilters.notFileFilter(DirectoryFileFilter.INSTANCE));
	}

	/**
	 * Returns a filter that accepts directories in addition to the {@link File} objects accepted by
	 * the given filter.
	 * 
	 * @param dirFilter a base filter to add to
	 * @return a filter that accepts directories
	 */
	private IOFileFilter setUpEffectiveDirFilter(final IOFileFilter dirFilter) {
		return dirFilter == null ? FalseFileFilter.INSTANCE : FileFilters.and(dirFilter,
			DirectoryFileFilter.INSTANCE);
	}

}
