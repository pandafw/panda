package panda.io;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import panda.io.filter.IOFileFilter;
import panda.io.stream.BOMInputStream;
import panda.lang.Charsets;
import panda.lang.Iterators;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.Systems;

/**
 * File Utilities class.
 * 
 */
public class Files {
	/**
	 * An empty array of type <code>File</code>.
	 */
	public static final File[] EMPTY_FILE_ARRAY = new File[0];

	/**
	 * The file copy buffer size (30 MB)
	 */
	private static final long FILE_COPY_BUFFER_SIZE = Numbers.MB * 30;

	public static class FileLocker implements Closeable {
		RandomAccessFile file;
		FileLock lock;

		public FileLocker(RandomAccessFile file, FileLock lock) {
			this.file = file;
			this.lock = lock;
		}

		@Override
		public void close() throws IOException {
			if (lock != null) {
				lock.release();
			}
			file.close();
		}
	}

	/**
	 * lock file
	 * @param file the File object
	 * @return the FileLocker
	 * @throws IOException if an IO error occurs
	 */
	public static FileLocker lock(File file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		
		// Get a file channel for the file
		FileChannel channel = raf.getChannel();

		// Use the file channel to create a lock on the file.
		// This method blocks until it can retrieve the lock.
		FileLock lock = channel.lock();

		return new FileLocker(raf, lock);
	}
	
	/**
	 * try to lock file
	 * @param file the File object
	 * @return the FileLocker
	 * @throws IOException if an IO error occurs
	 */
	public static FileLocker tryLock(File file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		// Get a file channel for the file
		FileChannel channel = raf.getChannel();

		// Try acquiring the lock without blocking. This method returns
		// null or throws an exception if the file is already locked.
		try {
			FileLock lock = channel.tryLock();

			return new FileLocker(raf, lock);
		}
		catch (OverlappingFileLockException e) {
			// File is already locked in this thread or virtual machine
			raf.close();
			return null;
		}
	}
	
	/**
	 * copy file or directory
	 * 
	 * @param src source file or directory
	 * @param des target file or directory
	 * @throws IOException if an IO error occurs
	 */
	public static void copy(File src, File des) throws IOException {
		if (src.isDirectory()) {
			copyDir(src, des);
		}
		else {
			copyFile(src, des);
		}
	}

	/**
	 * existence check
	 * 
	 * @param file the file name
	 * @return false if file is null or not exists
	 */
	public static boolean exists(String file) {
		return null != file && new File(file).exists();
	}

	/**
	 * existence check
	 * 
	 * @param file the File object
	 * @return false if file is null or not exists
	 */
	public static boolean exists(File file) {
		return null != file && file.exists();
	}

	/**
	 * directory check
	 * 
	 * @param dir the directory name
	 * @return false if file is null or not directory
	 */
	public static boolean isDirectory(String dir) {
		return null != dir && isDirectory(new File(dir));
	}

	/**
	 * directory check
	 * 
	 * @param dir the File object
	 * @return false if file is null or not directory
	 */
	public static boolean isDirectory(File dir) {
		return null != dir && dir.exists() && dir.isDirectory();
	}

	/**
	 * file check
	 * 
	 * @param f the file name
	 * @return false if file is null or not file
	 */
	public static boolean isFile(String f) {
		return null != f && isFile(new File(f));
	}

	/**
	 * file check
	 * 
	 * @param f the File object
	 * @return false if file is null or not file
	 */
	public static boolean isFile(File f) {
		return null != f && f.exists() && f.isFile();
	}

	/**
	 * create a file. If the parent folder does not exist, the parent folder will be created.
	 * 
	 * @param file file
	 * @return false if file already exists or failed to create, true if created successfully
	 * @throws IOException if an IO error occurs
	 */
	public static boolean createFile(File file) throws IOException {
		if (null == file || file.exists()) {
			return false;
		}
		makeDirs(file.getParentFile());
		return file.createNewFile();
	}

	// -----------------------------------------------------------------------
	/**
	 * Construct a file from the set of name elements.
	 * 
	 * @param directory the parent directory
	 * @param names the name elements
	 * @return the file
	 */
	public static File getFile(final File directory, final String... names) {
		if (directory == null) {
			throw new NullPointerException("directorydirectory must not be null");
		}
		if (names == null) {
			throw new NullPointerException("names must not be null");
		}
		File file = directory;
		for (final String name : names) {
			file = new File(file, name);
		}
		return file;
	}

	/**
	 * Construct a file from the set of name elements.
	 * 
	 * @param names the name elements
	 * @return the file
	 */
	public static File getFile(final String... names) {
		if (names == null) {
			throw new NullPointerException("names must not be null");
		}
		File file = null;
		for (final String name : names) {
			if (file == null) {
				file = new File(name);
			}
			else {
				file = new File(file, name);
			}
		}
		return file;
	}

	/**
	 * Returns the path to the system temporary directory.
	 * 
	 * @return the path to the system temporary directory.
	 */
	public static String getTempDirectoryPath() {
		return Systems.JAVA_IO_TMPDIR;
	}

	/**
	 * Returns a {@link File} representing the system temporary directory.
	 * 
	 * @return the system temporary directory.
	 */
	public static File getTempDirectory() {
		return Systems.getJavaIoTmpDir();
	}

	/**
	 * Returns the path to the user's home directory.
	 * 
	 * @return the path to the user's home directory.
	 */
	public static String getUserDirectoryPath() {
		return System.getProperty("user.home");
	}

	/**
	 * Returns a {@link File} representing the user's home directory.
	 * 
	 * @return the user's home directory.
	 */
	public static File getUserDirectory() {
		return new File(getUserDirectoryPath());
	}

	// -----------------------------------------------------------------------
	private static String formatSize(final double size) {
		return Numbers.cutFormat(size, 2);
	}
	
	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String toDisplaySize(final BigInteger size) {
		if (size == null) {
			return Strings.EMPTY;
		}

		return toDisplaySize(new BigDecimal(size));
	}
	
	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String toDisplaySize(final BigDecimal size) {
		if (size == null) {
			return Strings.EMPTY;
		}

		String sz;
		if (size.compareTo(Numbers.BD_YB) > 0) {
			sz = formatSize(size.divide(Numbers.BD_YB).doubleValue()) + " YB";
		}
		else if (size.compareTo(Numbers.BD_ZB) > 0) {
			sz = formatSize(size.divide(Numbers.BD_ZB).doubleValue()) + " ZB";
		}
		else {
			sz = toDisplaySize(size.longValue());
		}
		return sz;
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String toDisplaySize(final Long size) {
		if (size == null) {
			return Strings.EMPTY;
		}

		return toDisplaySize(size.longValue());
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String toDisplaySize(final long size) {
		return toDisplaySize((double)size);
	}
	

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String toDisplaySize(final double size) {
		String sz;
		if (size >= Numbers.EB) {
			sz = formatSize(size / Numbers.EB) + " EB";
		}
		else if (size >= Numbers.PB) {
			sz = formatSize(size / Numbers.PB) + " PB";
		}
		else if (size >= Numbers.TB) {
			sz = formatSize(size / Numbers.TB) + " TB";
		}
		else if (size >= Numbers.GB) {
			sz = formatSize(size / Numbers.GB) + " GB";
		}
		else if (size >= Numbers.MB) {
			sz = formatSize(size / Numbers.MB) + " MB";
		}
		else if (size >= Numbers.KB) {
			sz = formatSize(size / Numbers.KB) + " KB";
		}
		else {
			sz = formatSize(size) + " bytes";
		}
		return sz;
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String toDisplaySize(final Integer size) {
		if (size == null) {
			return Strings.EMPTY;
		}

		return toDisplaySize(size.longValue());
	}

	/**
	 * parse display size to number.
	 * return null if the input string is not a valid display size string.
	 * 
	 * @param str display size string
	 * @return number
	 */
	public static BigDecimal parseDisplaySize(final String str) {
		if (Strings.isEmpty(str)) {
			return null;
		}

		int i = 0;
		while (i < str.length() && !Character.isLetter(str.charAt(i))) {
			i++;
		}

		BigDecimal n = Numbers.toBigDecimal(Strings.strip(str.substring(0, i)));
		if (n == null) {
			return null;
		}

		if (i == str.length()) {
			return n;
		}

		char unit = str.charAt(i);
		switch (unit) {
		case 'Y':
		case 'y':
			n = n.multiply(Numbers.BD_YB);
			break;
		case 'Z':
		case 'z':
			n = n.multiply(Numbers.BD_ZB);
			break;
		case 'E':
		case 'e':
			n = n.multiply(Numbers.BD_EB);
			break;
		case 'P':
		case 'p':
			n = n.multiply(Numbers.BD_PB);
			break;
		case 'T':
		case 't':
			n = n.multiply(Numbers.BD_TB);
			break;
		case 'G':
		case 'g':
			n = n.multiply(Numbers.BD_GB);
			break;
		case 'M':
		case 'm':
			n = n.multiply(Numbers.BD_MB);
			break;
		case 'K':
		case 'k':
			n = n.multiply(Numbers.BD_KB);
			break;
		}
		return n;
	}

	// -----------------------------------------------------------------------
	/**
	 * Implements the same behaviour as the "touch" utility on Unix. It creates a new file with size
	 * 0 or, if the file exists already, it is opened and closed without modifying it, but updating
	 * the file date and time.
	 * <p>
	 * NOTE: this method throws an IOException if the last modified date of the file
	 * cannot be set. Also, this method creates parent directories if they do not
	 * exist.
	 * 
	 * @param file the File to touch
	 * @throws IOException If an I/O problem occurs
	 */
	public static void touch(final File file) throws IOException {
		if (!file.exists()) {
			final OutputStream out = Streams.openOutputStream(file);
			Streams.safeClose(out);
		}
		final boolean success = file.setLastModified(System.currentTimeMillis());
		if (!success) {
			throw new IOException("Unable to set the last modification time for " + file);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Converts a Collection containing java.io.File instanced into array representation. This is to
	 * account for the difference between File.listFiles() and FileUtils.listFiles().
	 * 
	 * @param files a Collection containing java.io.File instances
	 * @return an array of java.io.File
	 */
	public static File[] convertFileCollectionToFileArray(final Collection<File> files) {
		return files.toArray(new File[files.size()]);
	}

	// -----------------------------------------------------------------------
	private static Collection<File> fileIteratorToList(FileIterator it) {
		try {
			return Iterators.toList(it);
		}
		finally {
			Streams.safeClose(it);
		}
	}
	
	/**
	 * Finds files within a given directory (no sub directories) which match an array
	 * of extensions.
	 * 
	 * @param directory the directory to search in
	 * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is
	 *            {@code null}, all files are returned.
	 * @return an collection of java.io.File with the matching files
	 */
	public static Collection<File> listFiles(File directory, String... extensions) {
		return fileIteratorToList(iterateFiles(directory, extensions));
	}

	/**
	 * Finds files within a given directory (and optionally its sub directories) which match an array
	 * of extensions.
	 * 
	 * @param directory the directory to search in
	 * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is
	 *            {@code null}, all files are returned.
	 * @param recursive if true all sub directories are searched as well
	 * @return an collection of java.io.File with the matching files
	 */
	public static Collection<File> listFiles(File directory, boolean recursive, String... extensions) {
		return fileIteratorToList(iterateFiles(directory, recursive, extensions));
	}

	/**
	 * Finds files within a given directory (and optionally its sub directories). All files found are
	 * filtered by an IOFileFilter.
	 * <p>
	 * If your search should recurse into sub directories you can pass in an IOFileFilter for
	 * directories. You don't need to bind a DirectoryFileFilter (via logical AND) to this filter.
	 * This method does that for you.
	 * <p>
	 * An example: If you want to search through all directories called "temp" you pass in
	 * <code>FileFilters.NameFileFilter("temp")</code>
	 * <p>
	 * Another common usage of this method is find files in a directory tree but ignoring the
	 * directories generated CVS. You can simply pass in
	 * <code>FileFilters.makeCVSAware(null)</code>.
	 * 
	 * @param directory the directory to search in
	 * @param fileFilter filter to apply when finding files.
	 * @param recursive if true all sub directories are searched as well
	 * @return an collection of java.io.File with the matching files
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public static Collection<File> listFiles(File directory, IOFileFilter fileFilter, boolean recursive) {
		return fileIteratorToList(iterateFiles(directory, fileFilter, recursive));
	}
	
	/**
	 * Finds files within a given directory (and optionally its sub directories). All files found are
	 * filtered by an IOFileFilter.
	 * <p>
	 * If your search should recurse into sub directories you can pass in an IOFileFilter for
	 * directories. You don't need to bind a DirectoryFileFilter (via logical AND) to this filter.
	 * This method does that for you.
	 * <p>
	 * An example: If you want to search through all directories called "temp" you pass in
	 * <code>FileFilters.NameFileFilter("temp")</code>
	 * <p>
	 * Another common usage of this method is find files in a directory tree but ignoring the
	 * directories generated CVS. You can simply pass in
	 * <code>FileFilters.makeCVSAware(null)</code>.
	 * 
	 * @param directory the directory to search in
	 * @param fileFilter filter to apply when finding files.
	 * @param dirFilter optional filter to apply when finding sub directories. If this parameter is
	 *            {@code null}, sub directories will not be included in the search. Use
	 *            TrueFileFilter.INSTANCE to match all directories.
	 * @return an collection of java.io.File with the matching files
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public static Collection<File> listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		return fileIteratorToList(iterateFiles(directory, fileFilter, dirFilter));
	}

	/**
	 * Finds files within a given directory (and optionally its sub directories). All files found are
	 * filtered by an IOFileFilter.
	 * <p>
	 * The resulting collection includes the sub directories themselves.
	 * <p>
	 * 
	 * @see Files#listFiles
	 * @param directory the directory to search in
	 * @param fileFilter filter to apply when finding files.
	 * @param dirFilter optional filter to apply when finding sub directories. If this parameter is
	 *            {@code null}, sub directories will not be included in the search. Use
	 *            TrueFileFilter.INSTANCE to match all directories.
	 * @return an collection of java.io.File with the matching files
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public static Collection<File> listFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		return fileIteratorToList(iterateFilesAndDirs(directory, fileFilter, dirFilter));
	}

	// -----------------------------------------------------------------------
	/**
	 * Allows iteration over the files in a given directory (no recursive)
	 * which match an array of extensions. 
	 * 
	 * @param directory the directory to search in
	 * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is
	 *            {@code null}, all files are returned.
	 * @return an iterator of java.io.File with the matching files
	 */
	public static FileIterator iterateFiles(File directory, String... extensions) {
		if (Systems.IS_OS_APPENGINE) {
			return new IOFileIterator(directory, extensions);
		}
		return new NioFileIterator(directory, extensions);
	}

	/**
	 * Allows iteration over the files in a given directory (and optionally its sub directories)
	 * which match an array of extensions. 
	 * 
	 * @param directory the directory to search in
	 * @param extensions an array of extensions, ex. {"java","xml"}. If this parameter is
	 *            {@code null}, all files are returned.
	 * @param recursive if true all sub directories are searched as well
	 * @return an iterator of java.io.File with the matching files
	 */
	public static FileIterator iterateFiles(File directory, boolean recursive, String... extensions) {
		if (Systems.IS_OS_APPENGINE) {
			return new IOFileIterator(directory, recursive, extensions);
		}
		return new NioFileIterator(directory, recursive, extensions);
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
	 * @return an iterator of java.io.File for the matching files
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public static FileIterator iterateFiles(File directory, IOFileFilter fileFilter, boolean recursive) {
		if (Systems.IS_OS_APPENGINE) {
			return new IOFileIterator(directory, fileFilter, recursive);
		}
		return new NioFileIterator(directory, fileFilter, recursive);
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
	 * @return an iterator of java.io.File for the matching files
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public static FileIterator iterateFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		if (Systems.IS_OS_APPENGINE) {
			return new IOFileIterator(directory, fileFilter, dirFilter);
		}
		return new NioFileIterator(directory, fileFilter, dirFilter);
	}

	/**
	 * Allows iteration over the files in given directory (and optionally its sub directories).
	 * <p>
	 * All files found are filtered by an IOFileFilter. 
	 * <p>
	 * The resulting iterator includes the sub directories themselves.
	 * 
	 * @param directory the directory to search in
	 * @param fileFilter filter to apply when finding files.
	 * @param dirFilter optional filter to apply when finding sub directories. If this parameter is
	 *            {@code null}, sub directories will not be included in the search. Use
	 *            TrueFileFilter.INSTANCE to match all directories.
	 * @return an iterator of java.io.File for the matching files
	 * @see panda.io.filter.FileFilters
	 * @see panda.io.filter.NameFileFilter
	 */
	public static FileIterator iterateFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
		if (Systems.IS_OS_APPENGINE) {
			return new IOFileIterator(directory, fileFilter, dirFilter, true);
		}
		return new NioFileIterator(directory, fileFilter, dirFilter, true);
	}

	// -----------------------------------------------------------------------
	/**
	 * Compares the contents of two files to determine if they are equal or not.
	 * <p>
	 * This method checks to see if the two files are different lengths or if they point to the same
	 * file, before resorting to byte-by-byte comparison of the contents.
	 * <p>
	 * Code origin: Avalon
	 * 
	 * @param file1 the first file
	 * @param file2 the second file
	 * @return true if the content of the files are equal or they both don't exist, false otherwise
	 * @throws IOException in case of an I/O error
	 */
	public static boolean contentEquals(final File file1, final File file2) throws IOException {
		final boolean file1Exists = file1.exists();
		if (file1Exists != file2.exists()) {
			return false;
		}

		if (!file1Exists) {
			// two not existing files are equal
			return true;
		}

		if (file1.isDirectory() || file2.isDirectory()) {
			// don't want to compare directory contents
			throw new IOException("Can't compare directories, only files");
		}

		if (file1.length() != file2.length()) {
			// lengths differ, cannot be equal
			return false;
		}

		if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
			// same file
			return true;
		}

		InputStream input1 = null;
		InputStream input2 = null;
		try {
			input1 = new FileInputStream(file1);
			input2 = new FileInputStream(file2);
			return Streams.contentEquals(input1, input2);

		}
		finally {
			Streams.safeClose(input1);
			Streams.safeClose(input2);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Compares the contents of two files to determine if they are equal or not.
	 * <p>
	 * This method checks to see if the two files point to the same file, before resorting to
	 * line-by-line comparison of the contents.
	 * <p>
	 * 
	 * @param file1 the first file
	 * @param file2 the second file
	 * @param charsetName the character encoding to be used. May be null, in which case the platform
	 *            default is used
	 * @return true if the content of the files are equal or neither exists, false otherwise
	 * @throws IOException in case of an I/O error
	 * @see Streams#contentEqualsIgnoreEOL(Reader, Reader)
	 */
	public static boolean contentEqualsIgnoreEOL(final File file1, final File file2, final String charsetName)
			throws IOException {
		final boolean file1Exists = file1.exists();
		if (file1Exists != file2.exists()) {
			return false;
		}

		if (!file1Exists) {
			// two not existing files are equal
			return true;
		}

		if (file1.isDirectory() || file2.isDirectory()) {
			// don't want to compare directory contents
			throw new IOException("Can't compare directories, only files");
		}

		if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
			// same file
			return true;
		}

		Reader input1 = null;
		Reader input2 = null;
		try {
			if (charsetName == null) {
				input1 = new InputStreamReader(new FileInputStream(file1));
				input2 = new InputStreamReader(new FileInputStream(file2));
			}
			else {
				input1 = new InputStreamReader(new FileInputStream(file1), charsetName);
				input2 = new InputStreamReader(new FileInputStream(file2), charsetName);
			}
			return Streams.contentEqualsIgnoreEOL(input1, input2);

		}
		finally {
			Streams.safeClose(input1);
			Streams.safeClose(input2);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Convert from a <code>URL</code> to a <code>File</code>.
	 * <p>
	 * From version 1.1 this method will decode the URL. Syntax such as
	 * <code>file:///my%20docs/file.txt</code> will be correctly decoded to
	 * <code>/my docs/file.txt</code>. Starting with version 1.5, this method uses UTF-8 to decode
	 * percent-encoded octets to characters. Additionally, malformed percent-encoded octets are
	 * handled leniently by passing them through literally.
	 * 
	 * @param url the file URL to convert, {@code null} returns {@code null}
	 * @return the equivalent <code>File</code> object, or {@code null} if the URL's protocol is not
	 *         <code>file</code>
	 */
	public static File toFile(final URL url) {
		if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
			return null;
		}
		else {
			String filename = url.getFile().replace('/', File.separatorChar);
			filename = decodeUrl(filename);
			return new File(filename);
		}
	}

	/**
	 * Decodes the specified URL as per RFC 3986, i.e. transforms percent-encoded octets to
	 * characters by decoding with the UTF-8 character set. This function is primarily intended for
	 * usage with {@link java.net.URL} which unfortunately does not enforce proper URLs. As such,
	 * this method will leniently accept invalid characters or malformed percent-encoded octets and
	 * simply pass them literally through to the result string. Except for rare edge cases, this
	 * will make unencoded URLs pass through unaltered.
	 * 
	 * @param url The URL to decode, may be {@code null}.
	 * @return The decoded URL or {@code null} if the input was {@code null}.
	 */
	static String decodeUrl(final String url) {
		String decoded = url;
		if (url != null && url.indexOf('%') >= 0) {
			final int n = url.length();
			final StringBuilder buffer = new StringBuilder();
			final ByteBuffer bytes = ByteBuffer.allocate(n);
			for (int i = 0; i < n;) {
				if (url.charAt(i) == '%') {
					try {
						do {
							final byte octet = (byte)Integer.parseInt(url.substring(i + 1, i + 3), 16);
							bytes.put(octet);
							i += 3;
						}
						while (i < n && url.charAt(i) == '%');
						continue;
					}
					catch (final RuntimeException e) {
						// malformed percent-encoded octet, fall through and
						// append characters literally
					}
					finally {
						if (bytes.position() > 0) {
							bytes.flip();
							buffer.append(Charsets.CS_UTF_8.decode(bytes).toString());
							bytes.clear();
						}
					}
				}
				buffer.append(url.charAt(i++));
			}
			decoded = buffer.toString();
		}
		return decoded;
	}

	/**
	 * Converts each of an array of <code>URL</code> to a <code>File</code>.
	 * <p>
	 * Returns an array of the same size as the input. If the input is {@code null}, an empty array
	 * is returned. If the input contains {@code null}, the output array contains {@code null} at
	 * the same index.
	 * <p>
	 * This method will decode the URL. Syntax such as <code>file:///my%20docs/file.txt</code> will
	 * be correctly decoded to <code>/my docs/file.txt</code>.
	 * 
	 * @param urls the file URLs to convert, {@code null} returns empty array
	 * @return a non-{@code null} array of Files matching the input, with a {@code null} item if
	 *         there was a {@code null} at that index in the input array
	 * @throws IllegalArgumentException if any file is not a URL file
	 * @throws IllegalArgumentException if any file is incorrectly encoded
	 */
	public static File[] toFiles(final URL[] urls) {
		if (urls == null || urls.length == 0) {
			return EMPTY_FILE_ARRAY;
		}
		final File[] files = new File[urls.length];
		for (int i = 0; i < urls.length; i++) {
			final URL url = urls[i];
			if (url != null) {
				if (url.getProtocol().equals("file") == false) {
					throw new IllegalArgumentException("URL could not be converted to a File: " + url);
				}
				files[i] = toFile(url);
			}
		}
		return files;
	}

	/**
	 * Converts each of an array of <code>File</code> to a <code>URL</code>.
	 * <p>
	 * Returns an array of the same size as the input.
	 * 
	 * @param files the files to convert, must not be {@code null}
	 * @return an array of URLs matching the input
	 * @throws IOException if a file cannot be converted
	 * @throws NullPointerException if the parameter is null
	 */
	public static URL[] toURLs(final File[] files) throws IOException {
		final URL[] urls = new URL[files.length];

		for (int i = 0; i < urls.length; i++) {
			urls[i] = files[i].toURI().toURL();
		}

		return urls;
	}

	// -----------------------------------------------------------------------
	/**
	 * Copies a file to a directory preserving the file date.
	 * <p>
	 * This method copies the contents of the specified source file to a file of the same name in
	 * the specified destination directory. The destination directory is created if it does not
	 * exist. If the destination file exists, then this method will overwrite it.
	 * <p>
	 * <strong>Note:</strong> This method tries to preserve the file's last modified date/times
	 * using {@link File#setLastModified(long)}, however it is not guaranteed that the operation
	 * will succeed. If the modification operation fails, no indication is provided.
	 * 
	 * @param srcFile an existing file to copy, must not be {@code null}
	 * @param destDir the directory to place the copy in, must not be {@code null}
	 * @throws NullPointerException if source or destination is null
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 * @see #copyFile(File, File, boolean)
	 */
	public static void copyFileToDir(final File srcFile, final File destDir) throws IOException {
		copyFileToDir(srcFile, destDir, true);
	}

	/**
	 * Copies a file to a directory optionally preserving the file date.
	 * <p>
	 * This method copies the contents of the specified source file to a file of the same name in
	 * the specified destination directory. The destination directory is created if it does not
	 * exist. If the destination file exists, then this method will overwrite it.
	 * <p>
	 * <strong>Note:</strong> Setting <code>preserveFileDate</code> to {@code true} tries to
	 * preserve the file's last modified date/times using {@link File#setLastModified(long)},
	 * however it is not guaranteed that the operation will succeed. If the modification operation
	 * fails, no indication is provided.
	 * 
	 * @param srcFile an existing file to copy, must not be {@code null}
	 * @param destDir the directory to place the copy in, must not be {@code null}
	 * @param preserveFileDate true if the file date of the copy should be the same as the original
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 * @see #copyFile(File, File, boolean)
	 */
	public static void copyFileToDir(final File srcFile, final File destDir, final boolean preserveFileDate)
			throws IOException {
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
		}
		final File destFile = new File(destDir, srcFile.getName());
		copyFile(srcFile, destFile, preserveFileDate);
	}

	/**
	 * Copies a file to a new location preserving the file date.
	 * <p>
	 * This method copies the contents of the specified source file to the specified destination
	 * file. The directory holding the destination file is created if it does not exist. If the
	 * destination file exists, then this method will overwrite it.
	 * <p>
	 * <strong>Note:</strong> This method tries to preserve the file's last modified date/times
	 * using {@link File#setLastModified(long)}, however it is not guaranteed that the operation
	 * will succeed. If the modification operation fails, no indication is provided.
	 * 
	 * @param srcFile an existing file to copy, must not be {@code null}
	 * @param destFile the new file, must not be {@code null}
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 * @see #copyFileToDir(File, File)
	 */
	public static void copyFile(final File srcFile, final File destFile) throws IOException {
		copyFile(srcFile, destFile, true);
	}

	/**
	 * Copies a file to a new location.
	 * <p>
	 * This method copies the contents of the specified source file to the specified destination
	 * file. The directory holding the destination file is created if it does not exist. If the
	 * destination file exists, then this method will overwrite it.
	 * <p>
	 * <strong>Note:</strong> Setting <code>preserveFileDate</code> to {@code true} tries to
	 * preserve the file's last modified date/times using {@link File#setLastModified(long)},
	 * however it is not guaranteed that the operation will succeed. If the modification operation
	 * fails, no indication is provided.
	 * 
	 * @param srcFile an existing file to copy, must not be {@code null}
	 * @param destFile the new file, must not be {@code null}
	 * @param preserveFileDate true if the file date of the copy should be the same as the original
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 * @see #copyFileToDir(File, File, boolean)
	 */
	public static void copyFile(final File srcFile, final File destFile, final boolean preserveFileDate)
			throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		final File parentFile = destFile.getParentFile();
		if (parentFile != null) {
			if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
				throw new IOException("Destination '" + parentFile + "' directory cannot be created");
			}
		}
		if (destFile.exists() && destFile.canWrite() == false) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}
		doCopyFile(srcFile, destFile, preserveFileDate);
	}

	/**
	 * Copy bytes from a <code>File</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * </p>
	 * 
	 * @param input the <code>File</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyFile(final File input, final OutputStream output) throws IOException {
		final FileInputStream fis = new FileInputStream(input);
		try {
			return Streams.copyLarge(fis, output);
		}
		finally {
			fis.close();
		}
	}

	/**
	 * Internal copy file method.
	 * 
	 * @param srcFile the validated source file, must not be {@code null}
	 * @param destFile the validated destination file, must not be {@code null}
	 * @param preserveFileDate whether to preserve the file date
	 * @throws IOException if an error occurs
	 */
	private static void doCopyFile(final File srcFile, final File destFile, final boolean preserveFileDate)
			throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' exists but is a directory");
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			final long size = input.size();
			long pos = 0;
			long count = 0;
			while (pos < size) {
				final long remain = size - pos;
				count = remain > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : remain;
				final long bytesCopied = output.transferFrom(input, pos, count);
				if (bytesCopied == 0) { // IO-385 - can happen if file is truncated after caching
										// the size
					break; // ensure we don't loop forever
				}
				pos += bytesCopied;
			}
		}
		finally {
			Streams.safeClose(output);
			Streams.safeClose(fos);
			Streams.safeClose(input);
			Streams.safeClose(fis);
		}

		final long srcLen = srcFile.length();
		final long dstLen = destFile.length();
		if (srcLen != dstLen) {
			throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile
					+ "' Expected length: " + srcLen + " Actual: " + dstLen);
		}
		if (preserveFileDate) {
			destFile.setLastModified(srcFile.lastModified());
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Copies a directory to within another directory preserving the file dates.
	 * <p>
	 * This method copies the source directory and all its contents to a directory of the same name
	 * in the specified destination directory.
	 * <p>
	 * The destination directory is created if it does not exist. If the destination directory did
	 * exist, then this method merges the source with the destination, with the source taking
	 * precedence.
	 * <p>
	 * <strong>Note:</strong> This method tries to preserve the files' last modified date/times
	 * using {@link File#setLastModified(long)}, however it is not guaranteed that those operations
	 * will succeed. If the modification operation fails, no indication is provided.
	 * 
	 * @param srcDir an existing directory to copy, must not be {@code null}
	 * @param destDir the directory to place the copy in, must not be {@code null}
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyDirToDir(final File srcDir, final File destDir) throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (srcDir.exists() && srcDir.isDirectory() == false) {
			throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (destDir.exists() && destDir.isDirectory() == false) {
			throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
		}
		copyDir(srcDir, new File(destDir, srcDir.getName()), true);
	}

	/**
	 * Copies a whole directory to a new location preserving the file dates.
	 * <p>
	 * This method copies the specified directory and all its child directories and files to the
	 * specified destination. The destination is the new location and name of the directory.
	 * <p>
	 * The destination directory is created if it does not exist. If the destination directory did
	 * exist, then this method merges the source with the destination, with the source taking
	 * precedence.
	 * <p>
	 * <strong>Note:</strong> This method tries to preserve the files' last modified date/times
	 * using {@link File#setLastModified(long)}, however it is not guaranteed that those operations
	 * will succeed. If the modification operation fails, no indication is provided.
	 * 
	 * @param srcDir an existing directory to copy, must not be {@code null}
	 * @param destDir the new directory, must not be {@code null}
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyDir(final File srcDir, final File destDir) throws IOException {
		copyDir(srcDir, destDir, true);
	}

	/**
	 * Copies a whole directory to a new location.
	 * <p>
	 * This method copies the contents of the specified source directory to within the specified
	 * destination directory.
	 * <p>
	 * The destination directory is created if it does not exist. If the destination directory did
	 * exist, then this method merges the source with the destination, with the source taking
	 * precedence.
	 * <p>
	 * <strong>Note:</strong> Setting <code>preserveFileDate</code> to {@code true} tries to
	 * preserve the files' last modified date/times using {@link File#setLastModified(long)},
	 * however it is not guaranteed that those operations will succeed. If the modification
	 * operation fails, no indication is provided.
	 * 
	 * @param srcDir an existing directory to copy, must not be {@code null}
	 * @param destDir the new directory, must not be {@code null}
	 * @param preserveFileDate true if the file date of the copy should be the same as the original
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyDir(final File srcDir, final File destDir, final boolean preserveFileDate)
			throws IOException {
		copyDir(srcDir, destDir, null, preserveFileDate);
	}

	/**
	 * Copies a filtered directory to a new location preserving the file dates.
	 * <p>
	 * This method copies the contents of the specified source directory to within the specified
	 * destination directory.
	 * <p>
	 * The destination directory is created if it does not exist. If the destination directory did
	 * exist, then this method merges the source with the destination, with the source taking
	 * precedence.
	 * <p>
	 * <strong>Note:</strong> This method tries to preserve the files' last modified date/times
	 * using {@link File#setLastModified(long)}, however it is not guaranteed that those operations
	 * will succeed. If the modification operation fails, no indication is provided.
	 * <h4>Example: Copy directories only</h4>
	 * 
	 * <pre>
	 * // only copy the directory structure
	 * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY);
	 * </pre>
	 * 
	 * <h4>Example: Copy directories and txt files</h4>
	 * 
	 * <pre>
	 * // Create a filter for &quot;.txt&quot; files
	 * IOFileFilter txtSuffixFilter = FileFilters.suffixFileFilter(&quot;.txt&quot;);
	 * IOFileFilter txtFiles = FileFilters.andFileFilter(FileFileFilter.FILE, txtSuffixFilter);
	 * 
	 * // Create a filter for either directories or &quot;.txt&quot; files
	 * FileFilter filter = FileFilters.orFileFilter(DirectoryFileFilter.DIRECTORY, txtFiles);
	 * 
	 * // Copy using the filter
	 * FileUtils.copyDirectory(srcDir, destDir, filter);
	 * </pre>
	 * 
	 * @param srcDir an existing directory to copy, must not be {@code null}
	 * @param destDir the new directory, must not be {@code null}
	 * @param filter the filter to apply, null means copy all directories and files should be the
	 *            same as the original
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyDir(final File srcDir, final File destDir, final FileFilter filter) throws IOException {
		copyDir(srcDir, destDir, filter, true);
	}

	/**
	 * Copies a filtered directory to a new location.
	 * <p>
	 * This method copies the contents of the specified source directory to within the specified
	 * destination directory.
	 * <p>
	 * The destination directory is created if it does not exist. If the destination directory did
	 * exist, then this method merges the source with the destination, with the source taking
	 * precedence.
	 * <p>
	 * <strong>Note:</strong> Setting <code>preserveFileDate</code> to {@code true} tries to
	 * preserve the files' last modified date/times using {@link File#setLastModified(long)},
	 * however it is not guaranteed that those operations will succeed. If the modification
	 * operation fails, no indication is provided.
	 * <h4>Example: Copy directories only</h4>
	 * 
	 * <pre>
	 * // only copy the directory structure
	 * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY, false);
	 * </pre>
	 * 
	 * <h4>Example: Copy directories and txt files</h4>
	 * 
	 * <pre>
	 * // Create a filter for &quot;.txt&quot; files
	 * IOFileFilter txtSuffixFilter = FileFilters.suffixFileFilter(&quot;.txt&quot;);
	 * IOFileFilter txtFiles = FileFilters.andFileFilter(FileFileFilter.FILE, txtSuffixFilter);
	 * 
	 * // Create a filter for either directories or &quot;.txt&quot; files
	 * FileFilter filter = FileFilters.orFileFilter(DirectoryFileFilter.DIRECTORY, txtFiles);
	 * 
	 * // Copy using the filter
	 * FileUtils.copyDirectory(srcDir, destDir, filter, false);
	 * </pre>
	 * 
	 * @param srcDir an existing directory to copy, must not be {@code null}
	 * @param destDir the new directory, must not be {@code null}
	 * @param filter the filter to apply, null means copy all directories and files
	 * @param preserveFileDate true if the file date of the copy should be the same as the original
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyDir(final File srcDir, final File destDir, final FileFilter filter,
			final boolean preserveFileDate) throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (srcDir.exists() == false) {
			throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
		}
		if (srcDir.isDirectory() == false) {
			throw new IOException("Source '" + srcDir + "' exists but is not a directory");
		}
		if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
			throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
		}

		// Cater for destination being directory within the source directory (see IO-141)
		List<String> exclusionList = null;
		if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
			final File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
			if (srcFiles != null && srcFiles.length > 0) {
				exclusionList = new ArrayList<String>(srcFiles.length);
				for (final File srcFile : srcFiles) {
					final File copiedFile = new File(destDir, srcFile.getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}
		doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
	}

	/**
	 * Internal copy directory method.
	 * 
	 * @param srcDir the validated source directory, must not be {@code null}
	 * @param destDir the validated destination directory, must not be {@code null}
	 * @param filter the filter to apply, null means copy all directories and files
	 * @param preserveFileDate whether to preserve the file date
	 * @param exclusionList List of files and directories to exclude from the copy, may be null
	 * @throws IOException if an error occurs
	 */
	private static void doCopyDirectory(final File srcDir, final File destDir, final FileFilter filter,
			final boolean preserveFileDate, final List<String> exclusionList) throws IOException {
		// recurse
		final File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
		if (srcFiles == null) { // null if abstract pathname does not denote a directory, or if an
								// I/O error occurs
			throw new IOException("Failed to list contents of " + srcDir);
		}
		if (destDir.exists()) {
			if (destDir.isDirectory() == false) {
				throw new IOException("Destination '" + destDir + "' exists but is not a directory");
			}
		}
		else {
			if (!destDir.mkdirs() && !destDir.isDirectory()) {
				throw new IOException("Destination '" + destDir + "' directory cannot be created");
			}
		}
		if (destDir.canWrite() == false) {
			throw new IOException("Destination '" + destDir + "' cannot be written to");
		}
		for (final File srcFile : srcFiles) {
			final File dstFile = new File(destDir, srcFile.getName());
			if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
				if (srcFile.isDirectory()) {
					doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
				}
				else {
					doCopyFile(srcFile, dstFile, preserveFileDate);
				}
			}
		}

		// Do this last, as the above has probably affected directory metadata
		if (preserveFileDate) {
			destDir.setLastModified(srcDir.lastModified());
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Copies bytes from the URL <code>source</code> to a file <code>destination</code>. The
	 * directories up to <code>destination</code> will be created if they don't already exist.
	 * <code>destination</code> will be overwritten if it already exists.
	 * <p>
	 * Warning: this method does not set a connection or read timeout and thus might block forever.
	 * Use {@link #copyURLToFile(URL, File, int, int)} with reasonable timeouts to prevent this.
	 * 
	 * @param source the <code>URL</code> to copy bytes from, must not be {@code null}
	 * @param destination the non-directory <code>File</code> to write bytes to (possibly
	 *            overwriting), must not be {@code null}
	 * @throws IOException if <code>source</code> URL cannot be opened
	 * @throws IOException if <code>destination</code> is a directory
	 * @throws IOException if <code>destination</code> cannot be written
	 * @throws IOException if <code>destination</code> needs creating but can't be
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyURLToFile(final URL source, final File destination) throws IOException {
		Streams.copy(source.openStream(), destination);
	}

	/**
	 * Copies bytes from the URL <code>source</code> to a file <code>destination</code>. The
	 * directories up to <code>destination</code> will be created if they don't already exist.
	 * <code>destination</code> will be overwritten if it already exists.
	 * 
	 * @param source the <code>URL</code> to copy bytes from, must not be {@code null}
	 * @param destination the non-directory <code>File</code> to write bytes to (possibly
	 *            overwriting), must not be {@code null}
	 * @param connectionTimeout the number of milliseconds until this method will timeout if no
	 *            connection could be established to the <code>source</code>
	 * @param readTimeout the number of milliseconds until this method will timeout if no data could
	 *            be read from the <code>source</code>
	 * @throws IOException if <code>source</code> URL cannot be opened
	 * @throws IOException if <code>destination</code> is a directory
	 * @throws IOException if <code>destination</code> cannot be written
	 * @throws IOException if <code>destination</code> needs creating but can't be
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyURLToFile(final URL source, final File destination, final int connectionTimeout,
			final int readTimeout) throws IOException {
		final URLConnection connection = source.openConnection();
		connection.setConnectTimeout(connectionTimeout);
		connection.setReadTimeout(readTimeout);
		Streams.copy(connection.getInputStream(), destination);
	}

	// -----------------------------------------------------------------------
	/**
	 * Deletes a directory recursively.
	 * 
	 * @param directory directory to delete
	 * @throws IOException in case deletion is unsuccessful
	 */
	public static void deleteDir(final File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		if (!isSymlink(directory)) {
			cleanDir(directory);
		}

		if (!directory.delete()) {
			final String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}

	/**
	 * Deletes a file, never throwing an exception. If file is a directory, delete it and all
	 * sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
	 * </ul>
	 * 
	 * @param file file or directory to delete, can be {@code null}
	 * @return {@code true} if the file or directory was deleted, otherwise {@code false}
	 */
	public static boolean safeDelete(final File file) {
		if (file == null || !file.exists()) {
			return false;
		}
		
		try {
			if (file.isDirectory()) {
				cleanDir(file);
			}
		}
		catch (final Exception ignored) {
		}

		try {
			return file.delete();
		}
		catch (final Exception ignored) {
			return false;
		}
	}

	/**
	 * Determines whether the {@code parent} directory contains the {@code child} element (a file or
	 * directory).
	 * <p>
	 * Files are normalized before comparison.
	 * </p>
	 * Edge cases:
	 * <ul>
	 * <li>A {@code directory} must not be null: if null, throw IllegalArgumentException</li>
	 * <li>A {@code directory} must be a directory: if not a directory, throw
	 * IllegalArgumentException</li>
	 * <li>A directory does not contain itself: return false</li>
	 * <li>A null child file is not contained in any parent: return false</li>
	 * </ul>
	 * 
	 * @param directory the file to consider as the parent.
	 * @param child the file to consider as the child.
	 * @return true is the candidate leaf is under by the specified composite. False otherwise.
	 * @throws IOException if an IO error occurs while checking the files.
	 * @see FileNames#directoryContains(String, String)
	 */
	public static boolean directoryContains(final File directory, final File child) throws IOException {

		// Fail fast against NullPointerException
		if (directory == null) {
			throw new IllegalArgumentException("Directory must not be null");
		}

		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Not a directory: " + directory);
		}

		if (child == null) {
			return false;
		}

		if (!directory.exists() || !child.exists()) {
			return false;
		}

		// Canonicalize paths (normalizes relative paths)
		final String canonicalParent = directory.getCanonicalPath();
		final String canonicalChild = child.getCanonicalPath();

		return FileNames.directoryContains(canonicalParent, canonicalChild);
	}

	/**
	 * Cleans a directory without deleting it.
	 * 
	 * @param directory directory to clean
	 * @throws IOException in case cleaning is unsuccessful
	 */
	public static void cleanDir(final File directory) throws IOException {
		if (!directory.exists()) {
			final String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			final String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		final File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (final File file : files) {
			try {
				forceDelete(file);
			}
			catch (final IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Waits for NFS to propagate a file creation, imposing a timeout.
	 * <p>
	 * This method repeatedly tests {@link File#exists()} until it returns true up to the maximum
	 * time specified in seconds.
	 * 
	 * @param file the file to check, must not be {@code null}
	 * @param seconds the maximum time in seconds to wait
	 * @return true if file exists
	 * @throws NullPointerException if the file is {@code null}
	 */
	public static boolean waitFor(final File file, final int seconds) {
		int timeout = 0;
		int tick = 0;
		while (!file.exists()) {
			if (tick++ >= 10) {
				tick = 0;
				if (timeout++ > seconds) {
					return false;
				}
			}
			try {
				Thread.sleep(100);
			}
			catch (final InterruptedException ignore) {
				// ignore exception
			}
			catch (final Exception ex) {
				break;
			}
		}
		return true;
	}

	// -----------------------------------------------------------------------
	/**
	 * Reads the contents of a file line by line to a List of Strings. The file is always closed.
	 * 
	 * @param file the file to read, must not be {@code null}
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @return the list of Strings representing each line in the file, never {@code null}
	 * @throws IOException in case of an I/O error
	 */
	public static List<String> readLines(final File file, final Charset encoding) throws IOException {
		InputStream in = null;
		try {
			in = Streams.openInputStream(file);
			return Streams.readLines(in, encoding);
		}
		finally {
			Streams.safeClose(in);
		}
	}

	/**
	 * Reads the contents of a file line by line to a List of Strings. The file is always closed.
	 * 
	 * @param file the file to read, must not be {@code null}
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @return the list of Strings representing each line in the file, never {@code null}
	 * @throws IOException in case of an I/O error
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} 
	 */
	public static List<String> readLines(final File file, final String encoding) throws IOException {
		return readLines(file, Charsets.toCharset(encoding));
	}

	/**
	 * Reads the contents of a file line by line to a List of Strings using the default encoding for
	 * the VM. The file is always closed.
	 * 
	 * @param file the file to read, must not be {@code null}
	 * @return the list of Strings representing each line in the file, never {@code null}
	 * @throws IOException in case of an I/O error
	 */
	public static List<String> readLines(final File file) throws IOException {
		BOMInputStream in = null;
		try {
			in = new BOMInputStream(Streams.openInputStream(file));
			Charset cs = in.hasBOM() ? in.getBOMCharset() : Charset.defaultCharset();
			return Streams.readLines(in, cs);
		}
		finally {
			Streams.safeClose(in);
		}
	}

	/**
	 * Returns an Iterator for the lines in a <code>File</code>.
	 * <p>
	 * This method opens an <code>InputStream</code> for the file. When you have finished with the
	 * iterator you should close the stream to free internal resources. This can be done by calling
	 * the {@link LineIterator#close()} method.
	 * <p>
	 * The recommended usage pattern is:
	 * 
	 * <pre>
	 * LineIterator it = FileUtils.lineIterator(file, &quot;UTF-8&quot;);
	 * try {
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// / do something with line
	 * 	}
	 * }
	 * finally {
	 * 	LineIterator.safeClose(iterator);
	 * }
	 * </pre>
	 * <p>
	 * If an exception occurs during the creation of the iterator, the underlying stream is closed.
	 * 
	 * @param file the file to open for input, must not be {@code null}
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @return an Iterator of the lines in the file, never {@code null}
	 * @throws IOException in case of an I/O error (file closed)
	 */
	public static LineIterator lineIterator(final File file, final String encoding) throws IOException {
		InputStream in = null;
		try {
			in = Streams.openInputStream(file);
			return Streams.lineIterator(in, encoding);
		}
		catch (IOException ex) {
			Streams.safeClose(in);
			throw ex;
		}
		catch (RuntimeException ex) {
			Streams.safeClose(in);
			throw ex;
		}
	}

	/**
	 * Returns an Iterator for the lines in a <code>File</code> using the default encoding for the
	 * VM.
	 * 
	 * @param file the file to open for input, must not be {@code null}
	 * @return an Iterator of the lines in the file, never {@code null}
	 * @throws IOException in case of an I/O error (file closed)
	 * @see #lineIterator(File, String)
	 */
	public static LineIterator lineIterator(final File file) throws IOException {
		return lineIterator(file, null);
	}

	// -----------------------------------------------------------------------
	/**
	 * Writes a CharSequence to a file creating the file if it does not exist using the default
	 * encoding for the VM.
	 * 
	 * @param file the file to write
	 * @param data the content to write to the file
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final CharSequence data) throws IOException {
		write(file, data, Charset.defaultCharset(), false);
	}

	/**
	 * Writes a CharSequence to a file creating the file if it does not exist using the default
	 * encoding for the VM.
	 * 
	 * @param file the file to write
	 * @param data the content to write to the file
	 * @param append if {@code true}, then the data will be added to the end of the file rather than
	 *            overwriting
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final CharSequence data, final boolean append) throws IOException {
		write(file, data, Charset.defaultCharset(), append);
	}

	/**
	 * Writes a CharSequence to a file creating the file if it does not exist.
	 * 
	 * @param file the file to write
	 * @param data the content to write to the file
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final CharSequence data, final Charset encoding) throws IOException {
		write(file, data, encoding, false);
	}

	/**
	 * Writes a CharSequence to a file creating the file if it does not exist.
	 * 
	 * @param file the file to write
	 * @param data the content to write to the file
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported by the VM
	 */
	public static void write(final File file, final CharSequence data, final String encoding) throws IOException {
		write(file, data, encoding, false);
	}

	/**
	 * Writes a CharSequence to a file creating the file if it does not exist.
	 * 
	 * @param file the file to write
	 * @param data the content to write to the file
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @param append if {@code true}, then the data will be added to the end of the file rather than
	 *            overwriting
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final CharSequence data, final Charset encoding, final boolean append)
			throws IOException {
		OutputStream out = null;
		try {
			out = Streams.openOutputStream(file, append);
			Streams.write(data, out, encoding);
			out.close(); // don't swallow close Exception if copy completes normally
		}
		finally {
			Streams.safeClose(out);
		}
	}

	/**
	 * Writes a CharSequence to a file creating the file if it does not exist.
	 * 
	 * @param file the file to write
	 * @param data the content to write to the file
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @param append if {@code true}, then the data will be added to the end of the file rather than
	 *            overwriting
	 * @throws IOException in case of an I/O error
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} 
	 */
	public static void write(final File file, final CharSequence data, final String encoding, final boolean append)
			throws IOException {
		write(file, data, Charsets.toCharset(encoding), append);
	}

	/**
	 * Writes a byte array to a file creating the file if it does not exist.
	 * <p>
	 * NOTE: As from v1.3, the parent directories of the file will be created if they do not exist.
	 * 
	 * @param file the file to write to
	 * @param data the content to write to the file
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data) throws IOException {
		write(file, data, false);
	}

	/**
	 * Writes a byte array to a file creating the file if it does not exist.
	 * 
	 * @param file the file to write to
	 * @param data the content to write to the file
	 * @param append if {@code true}, then bytes will be added to the end of the file rather than
	 *            overwriting
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data, final boolean append)
			throws IOException {
		write(file, data, 0, data.length, append);
	}

	/**
	 * Writes {@code len} bytes from the specified byte array starting at offset {@code off} to a
	 * file, creating the file if it does not exist.
	 * 
	 * @param file the file to write to
	 * @param data the content to write to the file
	 * @param off the start offset in the data
	 * @param len the number of bytes to write
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data, final int off, final int len)
			throws IOException {
		write(file, data, off, len, false);
	}

	/**
	 * Writes {@code len} bytes from the specified byte array starting at offset {@code off} to a
	 * file, creating the file if it does not exist.
	 * 
	 * @param file the file to write to
	 * @param data the content to write to the file
	 * @param off the start offset in the data
	 * @param len the number of bytes to write
	 * @param append if {@code true}, then bytes will be added to the end of the file rather than
	 *            overwriting
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data, final int off, final int len,
			final boolean append) throws IOException {
		OutputStream out = null;
		try {
			out = Streams.openOutputStream(file, append);
			out.write(data, off, len);
			out.close(); // don't swallow close Exception if copy completes normally
		}
		finally {
			Streams.safeClose(out);
		}
	}

	/**
	 * Writes stream to a file, creating the file if it does not exist.
	 * 
	 * @param file the file to write to
	 * @param stream the content to write to the file
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final InputStream stream) throws IOException {
		write(file, stream, false);
	}
	
	/**
	 * Writes stream to a file, creating the file if it does not exist.
	 * 
	 * @param file the file to write to
	 * @param stream the content to write to the file
	 * @param append if {@code true}, then bytes will be added to the end of the file rather than
	 *            overwriting
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final InputStream stream, final boolean append) throws IOException {
		OutputStream out = null;
		try {
			out = Streams.openOutputStream(file, append);
			Streams.copy(stream, out);
			out.close(); // don't swallow close Exception if copy completes normally
		}
		finally {
			Streams.safeClose(out);
		}
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line. The specified character encoding and the default line ending
	 * will be used.
	 * <p>
	 * NOTE: As from v1.3, the parent directories of the file will be created if they do not exist.
	 * 
	 * @param file the file to write to
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported by the VM
	 */
	public static void writeLines(final File file, final String encoding, final Collection<?> lines) throws IOException {
		writeLines(file, encoding, lines, null, false);
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line, optionally appending. The specified character encoding and
	 * the default line ending will be used.
	 * 
	 * @param file the file to write to
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @param append if {@code true}, then the lines will be added to the end of the file rather
	 *            than overwriting
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported by the VM
	 */
	public static void writeLines(final File file, final String encoding, final Collection<?> lines,
			final boolean append) throws IOException {
		writeLines(file, encoding, lines, null, append);
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line. The default VM encoding and the default line ending will be
	 * used.
	 * 
	 * @param file the file to write to
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines) throws IOException {
		writeLines(file, null, lines, null, false);
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line. The default VM encoding and the default line ending will be
	 * used.
	 * 
	 * @param file the file to write to
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @param append if {@code true}, then the lines will be added to the end of the file rather
	 *            than overwriting
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines, final boolean append) throws IOException {
		writeLines(file, null, lines, null, append);
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line. The specified character encoding and the line ending will be
	 * used.
	 * <p>
	 * NOTE: As from v1.3, the parent directories of the file will be created if they do not exist.
	 * 
	 * @param file the file to write to
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @param lineEnding the line separator to use, {@code null} is system default
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported by the VM
	 */
	public static void writeLines(final File file, final String encoding, final Collection<?> lines,
			final String lineEnding) throws IOException {
		writeLines(file, encoding, lines, lineEnding, false);
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line. The specified character encoding and the line ending will be
	 * used.
	 * 
	 * @param file the file to write to
	 * @param encoding the encoding to use, {@code null} means platform default
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @param lineEnding the line separator to use, {@code null} is system default
	 * @param append if {@code true}, then the lines will be added to the end of the file rather
	 *            than overwriting
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported by the VM
	 */
	public static void writeLines(final File file, final String encoding, final Collection<?> lines,
			final String lineEnding, final boolean append) throws IOException {
		FileOutputStream out = null;
		try {
			out = Streams.openOutputStream(file, append);
			final BufferedOutputStream buffer = new BufferedOutputStream(out);
			Streams.writeLines(lines, lineEnding, buffer, encoding);
			buffer.flush();
			out.close(); // don't swallow close Exception if copy completes normally
		}
		finally {
			Streams.safeClose(out);
		}
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line. The default VM encoding and the specified line ending will be
	 * used.
	 * 
	 * @param file the file to write to
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @param lineEnding the line separator to use, {@code null} is system default
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines, final String lineEnding)
			throws IOException {
		writeLines(file, null, lines, lineEnding, false);
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to the specified
	 * <code>File</code> line by line. The default VM encoding and the specified line ending will be
	 * used.
	 * 
	 * @param file the file to write to
	 * @param lines the lines to write, {@code null} entries produce blank lines
	 * @param lineEnding the line separator to use, {@code null} is system default
	 * @param append if {@code true}, then the lines will be added to the end of the file rather
	 *            than overwriting
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines, final String lineEnding,
			final boolean append) throws IOException {
		writeLines(file, null, lines, lineEnding, append);
	}

	// -----------------------------------------------------------------------
	/**
	 * Deletes a file. If file is a directory, delete it and all sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>You get exceptions when a file or directory cannot be deleted. (java.io.File methods
	 * returns a boolean)</li>
	 * </ul>
	 * 
	 * @param file file or directory to delete, must not be {@code null}
	 * @throws NullPointerException if the directory is {@code null}
	 * @throws FileNotFoundException if the file was not found
	 * @throws IOException in case deletion is unsuccessful
	 */
	public static void forceDelete(final File file) throws IOException {
		if (file.isDirectory()) {
			deleteDir(file);
		}
		else {
			final boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: " + file);
				}
				final String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	/**
	 * Schedules a file to be deleted when JVM exits. If file is directory delete it and all
	 * sub-directories.
	 * 
	 * @param file file or directory to delete, must not be {@code null}
	 * @throws NullPointerException if the file is {@code null}
	 * @throws IOException in case deletion is unsuccessful
	 */
	public static void forceDeleteOnExit(final File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirOnExit(file);
		}
		else {
			file.deleteOnExit();
		}
	}

	/**
	 * Schedules a directory recursively for deletion on JVM exit.
	 * 
	 * @param directory directory to delete, must not be {@code null}
	 * @throws NullPointerException if the directory is {@code null}
	 * @throws IOException in case deletion is unsuccessful
	 */
	private static void deleteDirOnExit(final File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		directory.deleteOnExit();
		if (!isSymlink(directory)) {
			cleanDirOnExit(directory);
		}
	}

	/**
	 * Cleans a directory without deleting it.
	 * 
	 * @param directory directory to clean, must not be {@code null}
	 * @throws NullPointerException if the directory is {@code null}
	 * @throws IOException in case cleaning is unsuccessful
	 */
	private static void cleanDirOnExit(final File directory) throws IOException {
		if (!directory.exists()) {
			final String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			final String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		final File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (final File file : files) {
			try {
				forceDeleteOnExit(file);
			}
			catch (final IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}

	public static void makeDirs(final String directory) throws IOException {
		makeDirs(new File(directory));
	}
	
	/**
	 * Makes a directory, including any necessary but nonexistent parent directories. If a file
	 * already exists with specified name but it is not a directory then an IOException is thrown.
	 * If the directory cannot be created (or does not already exist) then an IOException is thrown.
	 * 
	 * @param directory directory to create, must not be {@code null}
	 * @throws NullPointerException if the directory is {@code null}
	 * @throws IOException if the directory cannot be created or the file already exists but is not
	 *             a directory
	 */
	public static void makeDirs(final File directory) throws IOException {
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				final String message = "File " + directory + " exists and is "
						+ "not a directory. Unable to create directory.";
				throw new IOException(message);
			}
		}
		else {
			if (!directory.mkdirs()) {
				// Double-check that some other thread or process hasn't made
				// the directory in the background
				if (!directory.isDirectory()) {
					final String message = "Unable to create directory " + directory;
					throw new IOException(message);
				}
			}
		}
	}

	/**
	 * Makes any necessary but nonexistent parent directories for a given File. If the parent
	 * directory cannot be created then an IOException is thrown.
	 * 
	 * @param file file with parent to create, must not be {@code null}
	 * @throws NullPointerException if the file is {@code null}
	 * @throws IOException if the parent directory cannot be created
	 */
	public static void makeParents(final File file) throws IOException {
		final File parent = file.getParentFile();
		if (parent == null) {
			return;
		}
		makeDirs(parent);
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the size of the specified file or directory. If the provided {@link File} is a
	 * regular file, then the file's length is returned. If the argument is a directory, then the
	 * size of the directory is calculated recursively. If a directory or subdirectory is security
	 * restricted, its size will not be included.
	 * 
	 * @param file the regular file or directory to return the size of (must not be {@code null}).
	 * @return the length of the file, or recursive size of the directory, provided (in bytes).
	 * @throws NullPointerException if the file is {@code null}
	 * @throws IllegalArgumentException if the file does not exist.
	 */
	public static long sizeOf(final File file) {

		if (!file.exists()) {
			final String message = file + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (file.isDirectory()) {
			return sizeOfDir0(file); // private method; expects directory
		}
		else {
			return file.length();
		}

	}

	/**
	 * Returns the size of the specified file or directory. If the provided {@link File} is a
	 * regular file, then the file's length is returned. If the argument is a directory, then the
	 * size of the directory is calculated recursively. If a directory or subdirectory is security
	 * restricted, its size will not be included.
	 * 
	 * @param file the regular file or directory to return the size of (must not be {@code null}).
	 * @return the length of the file, or recursive size of the directory, provided (in bytes).
	 * @throws NullPointerException if the file is {@code null}
	 * @throws IllegalArgumentException if the file does not exist.
	 */
	public static BigInteger sizeOfAsBigInteger(final File file) {

		if (!file.exists()) {
			final String message = file + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (file.isDirectory()) {
			return sizeOfDirBig0(file); // internal method
		}
		else {
			return BigInteger.valueOf(file.length());
		}

	}

	/**
	 * Counts the size of a directory recursively (sum of the length of all files).
	 * 
	 * @param directory directory to inspect, must not be {@code null}
	 * @return size of directory in bytes, 0 if directory is security restricted, a negative number
	 *         when the real total is greater than {@link Long#MAX_VALUE}.
	 * @throws NullPointerException if the directory is {@code null}
	 */
	public static long sizeOfDir(final File directory) {
		checkDir(directory);
		return sizeOfDir0(directory);
	}

	private static long sizeOfDir0(final File directory) {
		final File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			return 0L;
		}
		long size = 0;

		for (final File file : files) {
			try {
				if (!isSymlink(file)) {
					size += sizeOf0(file); // internal method
					if (size < 0) {
						break;
					}
				}
			}
			catch (final IOException ioe) {
				// Ignore exceptions caught when asking if a File is a symlink.
			}
		}

		return size;
	}

	private static long sizeOf0(File file) {
		if (file.isDirectory()) {
			return sizeOfDir0(file);
		}
		else {
			return file.length(); // will be 0 if file does not exist
		}
	}

	/**
	 * Counts the size of a directory recursively (sum of the length of all files).
	 * 
	 * @param directory directory to inspect, must not be {@code null}
	 * @return size of directory in bytes, 0 if directory is security restricted.
	 * @throws NullPointerException if the directory is {@code null}
	 */
	public static BigInteger sizeOfDirAsBigInteger(final File directory) {
		checkDir(directory);
		return sizeOfDirBig0(directory);
	}

	private static BigInteger sizeOfDirBig0(final File directory) {
		final File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			return BigInteger.ZERO;
		}
		BigInteger size = BigInteger.ZERO;

		for (final File file : files) {
			try {
				if (!isSymlink(file)) {
					size = size.add(sizeOfBig0(file));
				}
			}
			catch (final IOException ioe) {
				// Ignore exceptions caught when asking if a File is a symlink.
			}
		}

		return size;
	}

	private static BigInteger sizeOfBig0(final File fileOrDir) {
		if (fileOrDir.isDirectory()) {
			return sizeOfDirBig0(fileOrDir);
		}
		else {
			return BigInteger.valueOf(fileOrDir.length());
		}
	}

	/**
	 * Checks that the given {@code File} exists and is a directory.
	 * 
	 * @param directory The {@code File} to check.
	 * @throws IllegalArgumentException if the given {@code File} does not exist or is not a
	 *             directory.
	 */
	private static void checkDir(final File directory) {
		if (!directory.exists()) {
			throw new IllegalArgumentException(directory + " does not exist");
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(directory + " is not a directory");
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Tests if the specified <code>File</code> is newer than the reference <code>File</code>.
	 * 
	 * @param file the <code>File</code> of which the modification date must be compared, must not
	 *            be {@code null}
	 * @param reference the <code>File</code> of which the modification date is used, must not be
	 *            {@code null}
	 * @return true if the <code>File</code> exists and has been modified more recently than the
	 *         reference <code>File</code>
	 * @throws IllegalArgumentException if the file is {@code null}
	 * @throws IllegalArgumentException if the reference file is {@code null} or doesn't exist
	 */
	public static boolean isFileNewer(final File file, final File reference) {
		if (reference == null) {
			throw new IllegalArgumentException("No specified reference file");
		}
		if (!reference.exists()) {
			throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
		}
		return isFileNewer(file, reference.lastModified());
	}

	/**
	 * Tests if the specified <code>File</code> is newer than the specified <code>Date</code>.
	 * 
	 * @param file the <code>File</code> of which the modification date must be compared, must not
	 *            be {@code null}
	 * @param date the date reference, must not be {@code null}
	 * @return true if the <code>File</code> exists and has been modified after the given
	 *         <code>Date</code>.
	 * @throws IllegalArgumentException if the file is {@code null}
	 * @throws IllegalArgumentException if the date is {@code null}
	 */
	public static boolean isFileNewer(final File file, final Date date) {
		if (date == null) {
			throw new IllegalArgumentException("No specified date");
		}
		return isFileNewer(file, date.getTime());
	}

	/**
	 * Tests if the specified <code>File</code> is newer than the specified time reference.
	 * 
	 * @param file the <code>File</code> of which the modification date must be compared, must not
	 *            be {@code null}
	 * @param timeMillis the time reference measured in milliseconds since the epoch (00:00:00 GMT,
	 *            January 1, 1970)
	 * @return true if the <code>File</code> exists and has been modified after the given time
	 *         reference.
	 * @throws IllegalArgumentException if the file is {@code null}
	 */
	public static boolean isFileNewer(final File file, final long timeMillis) {
		if (file == null) {
			throw new IllegalArgumentException("No specified file");
		}
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() > timeMillis;
	}

	// -----------------------------------------------------------------------
	/**
	 * Tests if the specified <code>File</code> is older than the reference <code>File</code>.
	 * 
	 * @param file the <code>File</code> of which the modification date must be compared, must not
	 *            be {@code null}
	 * @param reference the <code>File</code> of which the modification date is used, must not be
	 *            {@code null}
	 * @return true if the <code>File</code> exists and has been modified before the reference
	 *         <code>File</code>
	 * @throws IllegalArgumentException if the file is {@code null}
	 * @throws IllegalArgumentException if the reference file is {@code null} or doesn't exist
	 */
	public static boolean isFileOlder(final File file, final File reference) {
		if (reference == null) {
			throw new IllegalArgumentException("No specified reference file");
		}
		if (!reference.exists()) {
			throw new IllegalArgumentException("The reference file '" + reference + "' doesn't exist");
		}
		return isFileOlder(file, reference.lastModified());
	}

	/**
	 * Tests if the specified <code>File</code> is older than the specified <code>Date</code>.
	 * 
	 * @param file the <code>File</code> of which the modification date must be compared, must not
	 *            be {@code null}
	 * @param date the date reference, must not be {@code null}
	 * @return true if the <code>File</code> exists and has been modified before the given
	 *         <code>Date</code>.
	 * @throws IllegalArgumentException if the file is {@code null}
	 * @throws IllegalArgumentException if the date is {@code null}
	 */
	public static boolean isFileOlder(final File file, final Date date) {
		if (date == null) {
			throw new IllegalArgumentException("No specified date");
		}
		return isFileOlder(file, date.getTime());
	}

	/**
	 * Tests if the specified <code>File</code> is older than the specified time reference.
	 * 
	 * @param file the <code>File</code> of which the modification date must be compared, must not
	 *            be {@code null}
	 * @param timeMillis the time reference measured in milliseconds since the epoch (00:00:00 GMT,
	 *            January 1, 1970)
	 * @return true if the <code>File</code> exists and has been modified before the given time
	 *         reference.
	 * @throws IllegalArgumentException if the file is {@code null}
	 */
	public static boolean isFileOlder(final File file, final long timeMillis) {
		if (file == null) {
			throw new IllegalArgumentException("No specified file");
		}
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() < timeMillis;
	}

	// -----------------------------------------------------------------------
	/**
	 * Computes the checksum of a file using the CRC32 checksum routine. The value of the checksum
	 * is returned.
	 * 
	 * @param file the file to checksum, must not be {@code null}
	 * @return the checksum value
	 * @throws NullPointerException if the file or checksum is {@code null}
	 * @throws IllegalArgumentException if the file is a directory
	 * @throws IOException if an IO error occurs reading the file
	 */
	public static long checksumCRC32(final File file) throws IOException {
		final CRC32 crc = new CRC32();
		checksum(file, crc);
		return crc.getValue();
	}

	/**
	 * Computes the checksum of a file using the specified checksum object. Multiple files may be
	 * checked using one <code>Checksum</code> instance if desired simply by reusing the same
	 * checksum object. For example:
	 * 
	 * <pre>
	 * long csum = FileUtils.checksum(file, new CRC32()).getValue();
	 * </pre>
	 * 
	 * @param file the file to checksum, must not be {@code null}
	 * @param checksum the checksum object to be used, must not be {@code null}
	 * @return the checksum specified, updated with the content of the file
	 * @throws NullPointerException if the file or checksum is {@code null}
	 * @throws IllegalArgumentException if the file is a directory
	 * @throws IOException if an IO error occurs reading the file
	 */
	public static Checksum checksum(final File file, final Checksum checksum) throws IOException {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Checksums can't be computed on directories");
		}
		InputStream in = null;
		try {
			in = new CheckedInputStream(new FileInputStream(file), checksum);
			Streams.copy(in, Streams.nullOutputStream());
		}
		finally {
			Streams.safeClose(in);
		}
		return checksum;
	}

	/**
	 * Moves a directory.
	 * <p>
	 * When the destination directory is on another file system, do a "copy and delete".
	 * 
	 * @param srcDir the directory to be moved
	 * @param destDir the destination directory
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws FileExistsException if the destination directory exists
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs moving the file
	 */
	public static void moveDir(final File srcDir, final File destDir) throws IOException {
		if (srcDir == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcDir.exists()) {
			throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
		}
		if (!srcDir.isDirectory()) {
			throw new IOException("Source '" + srcDir + "' is not a directory");
		}
		if (destDir.exists()) {
			throw new FileExistsException("Destination '" + destDir + "' already exists");
		}
		final boolean rename = srcDir.renameTo(destDir);
		if (!rename) {
			if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath() + File.separator)) {
				throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
			}
			copyDir(srcDir, destDir);
			deleteDir(srcDir);
			if (srcDir.exists()) {
				throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir
						+ "'");
			}
		}
	}

	/**
	 * Moves a directory to another directory.
	 * 
	 * @param src the file to be moved
	 * @param destDir the destination file
	 * @param createDestDir If {@code true} create the destination directory, otherwise if
	 *            {@code false} throw an IOException
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws FileExistsException if the directory exists in the destination directory
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs moving the file
	 */
	public static void moveDirToDir(final File src, final File destDir, final boolean createDestDir)
			throws IOException {
		if (src == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination directory must not be null");
		}
		if (!destDir.exists() && createDestDir) {
			destDir.mkdirs();
		}
		if (!destDir.exists()) {
			throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir="
					+ createDestDir + "]");
		}
		if (!destDir.isDirectory()) {
			throw new IOException("Destination '" + destDir + "' is not a directory");
		}
		moveDir(src, new File(destDir, src.getName()));

	}

	/**
	 * Moves a file.
	 * <p>
	 * When the destination file is on another file system, do a "copy and delete".
	 * 
	 * @param srcFile the file to be moved
	 * @param destFile the destination file
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws FileExistsException if the destination file exists
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs moving the file
	 */
	public static void moveFile(final File srcFile, final File destFile) throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destFile == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!srcFile.exists()) {
			throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
		}
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' is a directory");
		}
		if (destFile.exists()) {
			throw new FileExistsException("Destination '" + destFile + "' already exists");
		}
		if (destFile.isDirectory()) {
			throw new IOException("Destination '" + destFile + "' is a directory");
		}
		final boolean rename = srcFile.renameTo(destFile);
		if (!rename) {
			copyFile(srcFile, destFile);
			if (!srcFile.delete()) {
				Files.safeDelete(destFile);
				throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile
						+ "'");
			}
		}
	}

	/**
	 * Moves a file to a directory.
	 * 
	 * @param srcFile the file to be moved
	 * @param destDir the destination file
	 * @param createDestDir If {@code true} create the destination directory, otherwise if
	 *            {@code false} throw an IOException
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws FileExistsException if the destination file exists
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs moving the file
	 */
	public static void moveFileToDiry(final File srcFile, final File destDir, final boolean createDestDir)
			throws IOException {
		if (srcFile == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination directory must not be null");
		}
		if (!destDir.exists() && createDestDir) {
			destDir.mkdirs();
		}
		if (!destDir.exists()) {
			throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir="
					+ createDestDir + "]");
		}
		if (!destDir.isDirectory()) {
			throw new IOException("Destination '" + destDir + "' is not a directory");
		}
		moveFile(srcFile, new File(destDir, srcFile.getName()));
	}

	/**
	 * Moves a file or directory to the destination directory.
	 * <p>
	 * When the destination is on another file system, do a "copy and delete".
	 * 
	 * @param src the file or directory to be moved
	 * @param destDir the destination directory
	 * @param createDestDir If {@code true} create the destination directory, otherwise if
	 *            {@code false} throw an IOException
	 * @throws NullPointerException if source or destination is {@code null}
	 * @throws FileExistsException if the directory or file exists in the destination directory
	 * @throws IOException if source or destination is invalid
	 * @throws IOException if an IO error occurs moving the file
	 */
	public static void moveToDir(final File src, final File destDir, final boolean createDestDir)
			throws IOException {
		if (src == null) {
			throw new NullPointerException("Source must not be null");
		}
		if (destDir == null) {
			throw new NullPointerException("Destination must not be null");
		}
		if (!src.exists()) {
			throw new FileNotFoundException("Source '" + src + "' does not exist");
		}
		if (src.isDirectory()) {
			moveDirToDir(src, destDir, createDestDir);
		}
		else {
			moveFileToDiry(src, destDir, createDestDir);
		}
	}

	/**
	 * Determines whether the specified file is a Symbolic Link rather than an actual file.
	 * <p>
	 * Will not return true if there is a Symbolic Link anywhere in the path, only if the specific
	 * file is.
	 * <p>
	 * <b>Note:</b> the current implementation always returns {@code false} if the system is
	 * detected as Windows using {@link FileNames#isSystemWindows()}
	 * 
	 * @param file the file to check
	 * @return true if the file is a Symbolic Link
	 * @throws IOException if an IO error occurs while checking the file
	 */
	public static boolean isSymlink(final String file) throws IOException {
		return null != file && isSymlink(new File(file));
	}
	
	/**
	 * Determines whether the specified file is a Symbolic Link rather than an actual file.
	 * <p>
	 * Will not return true if there is a Symbolic Link anywhere in the path, only if the specific
	 * file is.
	 * <p>
	 * <b>Note:</b> the current implementation always returns {@code false} if the system is
	 * detected as Windows using {@link FileNames#isSystemWindows()}
	 * 
	 * @param file the file to check
	 * @return true if the file is a Symbolic Link
	 * @throws IOException if an IO error occurs while checking the file
	 */
	public static boolean isSymlink(final File file) throws IOException {
		if (file == null) {
			throw new NullPointerException("File must not be null");
		}
		if (FileNames.isSystemWindows()) {
			return false;
		}
		File fileInCanonicalDir = null;
		if (file.getParent() == null) {
			fileInCanonicalDir = file;
		}
		else {
			final File canonicalDir = file.getParentFile().getCanonicalFile();
			fileInCanonicalDir = new File(canonicalDir, file.getName());
		}

		if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
			return false;
		}
		else {
			return true;
		}
	}
}
