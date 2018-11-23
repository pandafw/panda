package panda.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import panda.io.filter.IOFileFilter;
import panda.lang.collection.KeyValue;

/**
 * An Iterator for glob files.
 */
public class NioFileIterator extends IOFileIterator {

	/**
	 * Allows iteration over the files in a given directory (no recursive)
	 * which match an array of extensions. 
	 *
	 * @param directory the directory to search in
	 * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is
	 *            {@code null}, all files are returned.
	 */
	public NioFileIterator(File directory, String... extensions) {
		super(directory, extensions);
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
	public NioFileIterator(File directory, boolean recursive, String... extensions) {
		super(directory, recursive, extensions);
	}

	/**
	 * Allows iteration over the files in given directory (and optionally its sub directories).
	 * <p>
	 * All files found are filtered by an IOFileFilter. 
	 * <p>
	 * 
	 * @param directory the directory to search in
	 * @param recursive if true all sub directories are searched as well
	 * @param fileFilter filter to apply when finding files.
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public NioFileIterator(File directory, boolean recursive, IOFileFilter fileFilter) {
		super(directory, recursive, fileFilter);
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
	public NioFileIterator(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		super(directory, fileFilter, dirFilter);
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
	public NioFileIterator(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter, boolean incdir) {
		super(directory, fileFilter, dirFilter, incdir);
	}

	// -----------------------------------------------------------------------
	@Override
	protected void openDir(File directory) {
		try {
			DirectoryStream<Path> ds = java.nio.file.Files.newDirectoryStream(Paths.get(directory.getPath()));
			Iterator<Path> it = ds.iterator();
			stack.push(new KeyValue<DirectoryStream<Path>, Iterator<Path>>(ds, it));
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected File nextFile() {
		Iterator<Path> it = (Iterator<Path>)stack.peek().getValue();
		
		if (it.hasNext()) {
			Path p = it.next();
			File f = p.toFile();
			return f;
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void closeDir() {
		try {
			DirectoryStream<Path> ds = (DirectoryStream<Path>)stack.pop().getKey();
			ds.close();
		}
		catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void close() throws Exception {
		while (!stack.empty()) {
			DirectoryStream<Path> ds = (DirectoryStream<Path>)stack.pop().getKey();
			ds.close();
		}
	}

}
