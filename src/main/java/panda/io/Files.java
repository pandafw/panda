package panda.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.log.Log;
import panda.log.Logs;


/**
 * File Utilities class.
 * @author yf.frank.wang@gmail.com
 */
public class Files {
	private static Log log = Logs.getLog(Files.class);

	protected static final String TOP_PATH = "..";

	protected static final String CURRENT_PATH = ".";

	protected static final char EXTENSION_SEPARATOR = '.';

	protected static final Pattern ABS_PATH = Pattern.compile("^[/\\\\]|[a-zA-Z]:[/\\\\]");

	/**
	 * The Unix directory separator character.
	 */
	public static final char DIR_SEPARATOR_UNIX = '/';
	/**
	 * The Windows directory separator character.
	 */
	public static final char DIR_SEPARATOR_WINDOWS = '\\';
	/**
	 * The system directory separator character.
	 */
	public static final char DIR_SEPARATOR = File.separatorChar;

	/**
	 * Apply the given relative path to the given path, assuming standard Java folder separation
	 * (i.e. "/" separators);
	 * 
	 * @param path the path to start from (usually a full file path)
	 * @param relativePath the relative path to apply (relative to the full file path above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		if (Strings.isEmpty(relativePath)) {
			return path;
		}

		int separatorIndex = path.lastIndexOf(DIR_SEPARATOR_UNIX);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (relativePath.charAt(0) == DIR_SEPARATOR_UNIX) {
				newPath += DIR_SEPARATOR_UNIX;
			}
			return newPath + relativePath;
		}
		else {
			return relativePath;
		}
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that Windows separators
	 * ("\") are replaced by simple slashes.
	 * 
	 * @param path the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		String pathToUse = Strings.replaceChars(path, DIR_SEPARATOR_WINDOWS, DIR_SEPARATOR_UNIX);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}

		List<String> pathList = Texts.parseCsv(pathToUse, DIR_SEPARATOR_UNIX);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathList.size() - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathList.get(i))) {
				// Points to current directory - drop it.
			}
			else if (TOP_PATH.equals(pathList.get(i))) {
				// Registering top path found.
				tops++;
			}
			else {
				if (tops > 0) {
					// Merging path element with corresponding to top path.
					tops--;
				}
				else {
					// Normal path element found.
					pathElements.add(0, pathList.get(i));
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + Strings.join(pathElements, DIR_SEPARATOR_UNIX);
	}

	/**
	 * Compare two paths after normalization of them.
	 * 
	 * @param path1 first path for comparison
	 * @param path2 second path for comparison
	 * @return whether the two paths are equivalent after normalization
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * 
	 * @param path the file path
	 * @return the extracted filename
	 */
	public static String getFileName(String path) {
		if (path == null) {
			return null;
		}

		int sepIndex = path.lastIndexOf(DIR_SEPARATOR_UNIX);
		if (sepIndex < 0) {
			sepIndex = path.lastIndexOf(DIR_SEPARATOR_WINDOWS);
		}
		return (sepIndex >= 0 ? path.substring(sepIndex + 1) : path);
	}

	/**
	 * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * 
	 * @param file the file
	 * @return the extracted filename
	 */
	public static String getFileName(File file) {
		if (file == null) {
			return null;
		}
		return file.getName();
	}

	/**
	 * Extract the base filename from the given path, e.g. "mypath/myfile.txt" -> "myfile".
	 * 
	 * @param path the file path
	 * @return the extracted base filename
	 */
	public static String getFileNameBase(String path) {
		if (path == null) {
			return null;
		}
		String fn = getFileName(path);
		return stripFileNameExtension(fn);
	}

	/**
	 * Extract the base filename from the given path, e.g. "mypath/myfile.txt" -> "myfile".
	 * 
	 * @param file the file object
	 * @return the extracted base filename
	 */
	public static String getFileNameBase(File file) {
		if (file == null) {
			return null;
		}
		return getFileNameBase(file.getName());
	}

	/**
	 * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
	 * 
	 * @param path the file path
	 * @return the extracted filename extension
	 */
	public static String getFileNameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : "");
	}

	/**
	 * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
	 * 
	 * @param file the file object
	 * @return the extracted filename extension
	 */
	public static String getFileNameExtension(File file) {
		if (file == null) {
			return null;
		}
		return getFileNameExtension(file.getName());
	}

	/**
	 * Strip the filename extension from the given path, e.g. "mypath/myfile.txt" ->
	 * "mypath/myfile".
	 * 
	 * @param path the file path
	 * @return the path with stripped filename extension, or <code>null</code> if none
	 */
	public static String stripFileNameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	/**
	 * Strip the filename extension from the given path, e.g. "mypath/myfile.txt" ->
	 * "mypath/myfile".
	 * 
	 * @param file the file object
	 * @return the path with stripped filename extension, or <code>null</code> if none
	 */
	public static String stripFileNameExtension(File file) {
		if (file == null) {
			return null;
		}
		return stripFileNameExtension(file.getPath());
	}

	/**
	 * Removes a leading path from a second path.
	 * 
	 * @param lead The leading path, must not be null, must be absolute.
	 * @param path The path to remove from, must not be null, must be absolute.
	 * @return path's normalized absolute if it doesn't start with leading; path's path with
	 *         leading's path removed otherwise.
	 */
	public static String removeLeadingPath(String lead, String path) {
		if (lead.equals(path)) {
			return "";
		}
		if (path.startsWith(lead)) {
			path = path.substring(lead.length());
			if (path.length() > 0) {
				if (path.charAt(0) == DIR_SEPARATOR_UNIX || path.charAt(0) == DIR_SEPARATOR_WINDOWS) {
					// remove first '//'
					path = path.substring(1);
				}
			}
		}
		return path;
	}

	/**
	 * Removes a leading path from a second path.
	 * 
	 * @param lead The leading path, must not be null, must be absolute.
	 * @param path The path to remove from, must not be null, must be absolute.
	 * @return path's normalized absolute if it doesn't start with leading; path's path with
	 *         leading's path removed otherwise.
	 */
	public static String removeLeadingPath(File lead, File path) {
		return removeLeadingPath(lead.getAbsolutePath(), path.getAbsolutePath());
	}

	/**
	 * Tests whether or not a given path matches a given pattern.
	 * 
	 * @param path The path to match, as a String.
	 * @param pattern The pattern to match against.
	 * @return <code>true</code> if the pattern matches against the string, or <code>false</code>
	 *         otherwise.
	 */
	public static boolean pathMatch(String path, String pattern) {
		return pathMatch(path, pattern, true);
	}

	/**
	 * Tests whether or not a given path matches a given pattern.
	 * 
	 * @param path The path to match, as a String.
	 * @param pattern The pattern to match against.
	 * @param isCaseSensitive Whether or not matching should be performed case sensitively.
	 * @return <code>true</code> if the pattern matches against the string, or <code>false</code>
	 *         otherwise.
	 */
	public static boolean pathMatch(String path, String pattern, boolean isCaseSensitive) {
		if (path == null || path == null) {
			return false;
		}

		String[] strDirs = Strings.split(path.replace('\\', '/'), '/');
		String[] patDirs = Strings.split(pattern.replace('\\', '/'), '/');

		int patIdxStart = 0;
		int patIdxEnd = patDirs.length - 1;
		int strIdxStart = 0;
		int strIdxEnd = strDirs.length - 1;

		// up to first '**'
		while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
			String patDir = patDirs[patIdxStart];
			if (patDir.equals("**")) {
				break;
			}
			if (!Texts.wildcardMatch(strDirs[strIdxStart], patDir, isCaseSensitive)) {
				return false;
			}
			patIdxStart++;
			strIdxStart++;
		}
		if (strIdxStart > strIdxEnd) {
			// String is exhausted
			for (int i = patIdxStart; i <= patIdxEnd; i++) {
				if (!patDirs[i].equals("**")) {
					return false;
				}
			}
			return true;
		}
		else {
			if (patIdxStart > patIdxEnd) {
				// String not exhausted, but pattern is. Failure.
				return false;
			}
		}

		// up to last '**'
		while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
			String patDir = patDirs[patIdxEnd];
			if (patDir.equals("**")) {
				break;
			}
			if (!Texts.wildcardMatch(strDirs[strIdxEnd], patDir, isCaseSensitive)) {
				return false;
			}
			patIdxEnd--;
			strIdxEnd--;
		}
		if (strIdxStart > strIdxEnd) {
			// String is exhausted
			for (int i = patIdxStart; i <= patIdxEnd; i++) {
				if (!patDirs[i].equals("**")) {
					return false;
				}
			}
			return true;
		}

		while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
			int patIdxTmp = -1;
			for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
				if (patDirs[i].equals("**")) {
					patIdxTmp = i;
					break;
				}
			}
			if (patIdxTmp == patIdxStart + 1) {
				// '**/**' situation, so skip one
				patIdxStart++;
				continue;
			}
			// Find the pattern between padIdxStart & padIdxTmp in str between
			// strIdxStart & strIdxEnd
			int patLength = (patIdxTmp - patIdxStart - 1);
			int strLength = (strIdxEnd - strIdxStart + 1);
			int foundIdx = -1;
			strLoop: for (int i = 0; i <= strLength - patLength; i++) {
				for (int j = 0; j < patLength; j++) {
					String subPat = patDirs[patIdxStart + j + 1];
					String subStr = strDirs[strIdxStart + i + j];
					if (!Texts.wildcardMatch(subStr, subPat, isCaseSensitive)) {
						continue strLoop;
					}
				}

				foundIdx = strIdxStart + i;
				break;
			}

			if (foundIdx == -1) {
				return false;
			}

			patIdxStart = patIdxTmp;
			strIdxStart = foundIdx + patLength;
		}

		for (int i = patIdxStart; i <= patIdxEnd; i++) {
			if (!patDirs[i].equals("**")) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the MIME type for the specified file name.
	 * 
	 * @param filename the specified file name
	 * @return a String indicating the MIME type for the specified file name.
	 */
	public static String getContentTypeFor(String filename) {
		return URLConnection.getFileNameMap().getContentTypeFor(filename);
	}

	public static boolean isAbsolutePath(String path) {
		Matcher m = ABS_PATH.matcher(path);
		return m.find();
	}

	/**
	 * directory copy
	 * 
	 * @param src source directory
	 * @param target target directory
	 * @return true if copy successfully
	 * @throws IOException if an IO error occurs
	 */
	public static boolean copyDir(File src, File target) throws IOException {
		if (src == null || target == null || !src.exists())
			return false;
		if (!src.isDirectory())
			throw new IOException(src.getAbsolutePath() + " should be a directory!");
		if (!target.exists())
			if (!makedirs(target))
				return false;
		boolean re = true;
		File[] files = src.listFiles();
		if (null != files) {
			for (File f : files) {
				if (f.isFile())
					re &= copyFile(f,
						new File(target.getAbsolutePath() + DIR_SEPARATOR + f.getName()));
				else
					re &= copyDir(f,
						new File(target.getAbsolutePath() + DIR_SEPARATOR + f.getName()));
			}
		}
		return re;
	}

	/**
	 * file copy
	 * 
	 * @param src source file
	 * @param target target file
	 * @return true if copy successfully
	 * @throws IOException if an IO error occurs
	 */
	public static boolean copyFile(File src, File target) throws IOException {
		if (src == null || target == null || !src.exists())
			return false;

		if (!target.exists())
			if (!createFile(target))
				return false;

		Streams.copy(src, target);
		return target.setLastModified(src.lastModified());
	}

	/**
	 * 自动决定是 copy 文件还是目录
	 * 
	 * @param src 源
	 * @param target 目标
	 * @return 是否 copy 成功
	 */
	public static boolean copy(File src, File target) throws IOException {
		if (src.isDirectory())
			return copyDir(src, target);
		return copyFile(src, target);
	}


	/**
	 * directory check
	 * @return false if file is null or not directory
	 */
	public static boolean isDirectory(File f) {
		return null != f && f.exists() && f.isDirectory();
	}

	/**
	 * file check
	 * @return false if file is null or not file
	 */
	public static boolean isFile(File f) {
		return null != f && f.exists() && f.isFile();
	}

	/**
	 * 创建新文件，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param f 文件对象
	 * @return false，如果文件已存在。 true 创建成功
	 * @throws IOException if an IO error occurs
	 */
	public static boolean createFile(File f) throws IOException {
		if (null == f || f.exists())
			return false;
		makedirs(f.getParentFile());
		return f.createNewFile();
	}

	/**
	 * 创建新目录，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param dir 目录对象
	 * @return false，如果目录已存在。 true 创建成功
	 */
	public static boolean makedirs(File dir) {
		if (null == dir || dir.exists())
			return false;
		return dir.mkdirs();
	}
	
	/**
	 * delete the file and sub files
	 * @param file file
	 * @return deleted file and folder count
	 * @throws IOException if an IO error occurs
	 */
	public static int deltree(File file) throws IOException {
		int cnt = 0;
		
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] cfs = file.listFiles();
				for (File cf : cfs) {
					cnt += deltree(cf);
				}
			}
			if (log.isDebugEnabled()) {
				log.debug("delete " + file.getCanonicalPath());
			}
			if (!file.delete()) {
				throw new IOException("Can not delete file: " + file.getCanonicalPath());
			}
			cnt++;
		}
		return cnt;
	}

	/**
	 * 一个 Vistor 模式的目录深层遍历
	 * 
	 * @param f 要遍历的目录或者文件，如果是目录，深层遍历，否则，只访问一次文件
	 * @param fv 对文件要进行的操作
	 * @param filter 遍历目录时，哪些文件应该被忽略
	 * @return 遍历的文件个数
	 */
	public static int visitFile(File f, FileVisitor fv, FileFilter filter) {
		int re = 0;
		if (f.isFile()) {
			fv.visit(f);
			re++;
		}
		else if (f.isDirectory()) {
			File[] fs = null == filter ? f.listFiles() : f.listFiles(filter);
			if (fs != null)
				for (File theFile : fs)
					re += visitFile(theFile, fv, filter);
		}
		return re;
	}

	/**
	 * 将两个文件对象比较，得出相对路径
	 * 
	 * @param base 基础文件对象
	 * @param file 相对文件对象
	 * @return 相对于基础文件对象的相对路径
	 */
	public static String getRelativePath(File base, File file) {
		String pathBase = base.getAbsolutePath();
		if (base.isDirectory())
			pathBase += "/";

		String pathFile = file.getAbsolutePath();
		if (file.isDirectory())
			pathFile += "/";

		return getRelativePath(pathBase, pathFile);
	}

	/**
	 * 将两个路径比较，得出相对路径
	 * 
	 * @param base 基础路径，以 '/' 结束，表示目录
	 * @param path 相对文件路径，以 '/' 结束，表示目录
	 * @return 相对于基础路径对象的相对路径
	 */
	public static String getRelativePath(String base, String path) {
		String[] bb = Strings.split(getCanonicalPath(base), "\\/");
		String[] ff = Strings.split(getCanonicalPath(path), "\\/");
		int len = Math.min(bb.length, ff.length);
		int pos = 0;
		for (; pos < len; pos++)
			if (!bb[pos].equals(ff[pos]))
				break;

		if (len == pos && bb.length == ff.length)
			return "./";

		int dir = 1;
		if (base.endsWith("/"))
			dir = 0;

		StringBuilder sb = new StringBuilder(Strings.repeat("../", bb.length - pos - dir));
		return sb.append(Strings.join(ff, "/", pos, ff.length)).toString();
	}

	/**
	 * 整理路径。 将会合并路径中的 ".."
	 * 
	 * @param path 路径
	 * @return 整理后的路径
	 */
	public static String getCanonicalPath(String path) {
		if (Strings.isBlank(path))
			return path;
		String[] pa = Strings.split(path, "\\/");
		LinkedList<String> paths = new LinkedList<String>();
		for (String s : pa) {
			if ("..".equals(s)) {
				if (paths.size() > 0)
					paths.removeLast();
				continue;
			}
			else {
				paths.add(s);
			}
		}
		if (path.charAt(0) == '/') {
			return "/" + Strings.join(paths, "/");
		}
		return Strings.join(paths, "/");
	}

	/**
	 * @return 当前账户的主目录全路径
	 */
	public static String home() {
		return System.getProperty("user.home");
	}

	/**
	 * @param path 相对用户主目录的路径
	 * @return 相对用户主目录的全路径
	 */
	public static String home(String path) {
		return home() + path;
	}

	/**
	 * 获取一个路径的绝对路径。如果该路径不存在，则返回null
	 * 
	 * @param path 路径
	 * @return 绝对路径
	 */
	public static String absolute(String path) {
		return absolute(path, Classes.getClassLoader(), Charsets.defaultEncoding());
	}

	/**
	 * 获取一个路径的绝对路径。如果该路径不存在，则返回null
	 * 
	 * @param path 路径
	 * @param klassLoader 参考 ClassLoader
	 * @param enc 路径编码方式
	 * @return 绝对路径
	 */
	public static String absolute(String path, ClassLoader klassLoader, String enc) {
		path = normalize(path, enc);
		if (Strings.isEmpty(path))
			return null;

		File f = new File(path);
		if (!f.exists()) {
			URL url = null;
			try {
				url = klassLoader.getResource(path);
				if (null == url)
					url = Thread.currentThread().getContextClassLoader().getResource(path);
				if (null == url)
					url = ClassLoader.getSystemResource(path);
			}
			catch (Throwable e) {
			}
			if (null != url)
				return normalize(url.getPath(), Charsets.UTF_8);// 通过URL获取String,一律使用UTF-8编码进行解码
			return null;
		}
		return path;
	}

	/**
	 * 让路径变成正常路径，将 ~ 替换成用户主目录
	 * 
	 * @param path 路径
	 * @return 正常化后的路径
	 */
	public static String normalize(String path) {
		return normalize(path, Charsets.defaultEncoding());
	}

	/**
	 * 让路径变成正常路径，将 ~ 替换成用户主目录
	 * 
	 * @param path 路径
	 * @param enc 路径编码方式
	 * @return 正常化后的路径
	 */
	public static String normalize(String path, String enc) {
		if (Strings.isEmpty(path))
			return null;
		if (path.charAt(0) == '~')
			path = home() + path.substring(1);
		try {
			return URLDecoder.decode(path, enc);
		}
		catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 遍历文件夹下以特定后缀结尾的文件(不包括文件夹,不包括.开头的文件)
	 * 
	 * @param path 根路径
	 * @param suffix 后缀
	 * @param deep 是否深层遍历
	 * @param fv 你所提供的访问器,当然就是你自己的逻辑咯
	 */
	public static final void visitFile(String path, final String suffix, final boolean deep, final FileVisitor fv) {
		visitFile(new File(path), new FileVisitor() {
			public void visit(File f) {
				if (f.isDirectory())
					return;
				fv.visit(f);
			}
		}, new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory())
					return deep;
				return !f.getName().startsWith(".") && f.getName().endsWith(suffix);
			}
		});
	}
}
