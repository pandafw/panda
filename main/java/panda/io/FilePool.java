package panda.io;

import java.io.File;
import java.io.IOException;

import panda.lang.Systems;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;


/**
 * a simple file pool
 * @author yf.frank.wang@gmail.com
 */
public class FilePool implements Runnable {
	private final static Log log = Logs.getLog(FilePool.class);

	private static FilePool instance;

	/**
	 * work directory
	 */
	private File workdir = Systems.getJavaIoTmpDir();

	/**
	 * The prefix string to be used in generating the file's name; must be at least three characters long
	 */
	private String prefix = "fcs-";

	/**
	 * milliseconds since file last modified. (default: 1h)
	 */
	private long expiry = 60 * 60 * 1000;

	/**
	 * @return the instance
	 */
	public synchronized static FilePool getInstance() {
		if (instance == null) {
			instance = new FilePool();
		}
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public synchronized static void setInstance(FilePool instance) {
		FilePool.instance = instance;
	}

	/**
	 * @return the workdir
	 */
	public File getWorkdir() {
		return workdir;
	}

	/**
	 * @param workdir the workdir to set
	 */
	public void setWorkdir(File workdir) {
		if (!workdir.exists()) {
			workdir.mkdirs();
		}
		if (!workdir.isDirectory()) {
			throw new IllegalArgumentException("FileCacheTask work directory is not a directory: " + workdir.getPath());
		}
		this.workdir = workdir;
	}

	/**
	 * @param workdir the workdir to set
	 */
	public void setWorkdir(String workdir) {
		setWorkdir(new File(workdir));
	}

	/**
	 * @return the expiry
	 */
	public long getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	/**
	 * new file for delete when expired
	 * @return cache file
	 * @throws IOException if an I/O error occurs
	 */
	public File newFile() throws IOException {
		return newFile(".tmp");
	}

	/**
	 * new file for delete when expired
	 * @param suffix suffix
	 * @return cache file
	 * @throws IOException if an I/O error occurs
	 */
	public File newFile(String suffix) throws IOException {
		File nf = File.createTempFile(prefix, suffix, workdir);
		if (log.isDebugEnabled()) {
			log.debug("New file [" + nf.getPath() + "]");
		}
		return nf;
	}

	/**
	 * add file for delete when expired
	 * @param file file
	 * @param suffix suffix
	 * @return cache file
	 * @throws IOException if an I/O error occurs
	 */
	public File addFile(File file, String suffix) throws IOException {
		if (file.exists() && file.isFile()) {
			File nf = File.createTempFile(prefix, "." + suffix, workdir);
			nf.delete();
			if (!file.renameTo(nf)) {
				Streams.copy(file, nf);
			}
			if (log.isDebugEnabled()) {
				log.debug("Add file [" + file.getPath() + "] -> [" + nf.getPath() + "]");
			}
			return nf;
		}
		return file;
	}

	/**
	 * add file for delete when expired
	 * @param file file
	 * @return cache file
	 * @throws IOException if an I/O error occurs
	 */
	public File addFile(File file) throws IOException {
		return addFile(file, Files.getFileNameExtension(file));
	}

	/**
	 * get file
	 * @param fileName fileName
	 * @return file name[
	 */
	public File getFile(String fileName) {
		File file = new File(workdir, fileName);
		if (file.exists() && file.isFile()) {
			return file;
		}
		return null;
	}

	public void deleteFile(File file) {
		if (file.delete()) {
			if (log.isInfoEnabled()) {
				log.info("Delete file: " + file.getPath() + " ... [OK]");
			}
		}
		else {
			if (log.isWarnEnabled()) {
				log.warn("Failed to delete file: " + file.getPath());
			}
		}
	}
	
	/**
	 * delete all files in the work directory
	 */
	public synchronized void cleanup() {
		for (File file : workdir.listFiles()) {
			deleteFile(file);
		}
	}

	public synchronized void deleteExpiredFiles() {
		long time = DateTimes.getTime();

		for (File file : workdir.listFiles()) {
			if (time - file.lastModified() > expiry) {
				deleteFile(file);
			}
		}
	}
	
	public void run() {
		deleteExpiredFiles();
	}
}
