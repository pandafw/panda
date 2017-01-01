package panda.vfs.local;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import panda.io.Files;
import panda.io.filter.IOFileFilter;
import panda.lang.Arrays;
import panda.lang.Numbers;
import panda.lang.Threads;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.FileItem;
import panda.vfs.FilePool;
import panda.vfs.NullFileItem;

public class LocalFilePool implements FilePool {
	private final static Log log = Logs.getLog(LocalFilePool.class);
	
	protected final static FastDateFormat fdf = FastDateFormat.getInstance("yyyy/MM/dd/HH/mm/ss/SSS");

	private String path = Files.getTempDirectoryPath();

	/**
	 * milliseconds since last modified. (default: 1h) 
	 */
	private long expires = DateTimes.MS_HOUR;

	public LocalFilePool() {
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return (int)(expires / 1000);
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		this.expires = maxAge * 1000;
	}

	protected LocalFileItem randFile(String name, boolean temporary) {
		while (true) {
			long id = DateTimes.getDate().getTime();
			if (temporary && (id & 1L) != 0) {
				id++;
			}
	
			File dir = new File(path, fdf.format(id));
			if (dir.exists()) {
				Threads.safeSleep(1);
				continue;
			}

			File file = new File(dir, name);
			return new LocalFileItem(this, id, file);
		}
	}
	
	@Override
	public FileItem saveFile(String name, final byte[] data, boolean temporary) throws IOException {
		LocalFileItem fi = randFile(name ,temporary);
		fi.getFile().getParentFile().mkdirs();
		Files.write(fi.getFile(), data);
		return fi;
	}
	
	@Override
	public FileItem saveFile(String name, final InputStream data, boolean temporary) throws IOException {
		LocalFileItem fi = randFile(name ,temporary);
		fi.getFile().getParentFile().mkdirs();
		Files.write(fi.getFile(), data);
		return fi;
	}
	
	@Override
	public FileItem findFile(Long id) {
		File dir = new File(path, fdf.format(id));
		if (dir.exists()) {
			File[] fs = dir.listFiles();
			if (Arrays.isNotEmpty(fs)) {
				return new LocalFileItem(this, id, fs[0]);
			}
		}

		return new NullFileItem(id);
	}
	
	protected boolean removeFile(File file) {
		if (file.delete()) {
			File root = new File(path);
			removeDirs(root, file.getParentFile());
			return true;
		}
		return false;
	}

	protected void removeDirs(File root, File dir) {
		while (!root.equals(dir)) {
			if (!dir.delete()) {
				break;
			}
		}
	}
	
	@Override
	public synchronized void clean() throws IOException {
		File root = new File(path);
		
		final long time = System.currentTimeMillis() - expires;
		
		Collection<File> fs = Files.listFiles(root, new IOFileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.lastModified() < time) {
					Integer ms = Numbers.toInt(file.getParentFile().getName());
					if (ms != null && (ms.intValue() & 1) == 0) {
						return true;
					}
				}
				
				return false;
			}

			@Override
			public boolean accept(File dir, String name) {
				return accept(new File(dir, name));
			}
			
		}, true);
		
		for (File f : fs) {
			if (f.delete()) {
				if (log.isDebugEnabled()) {
					log.debug("Remove temporary file: " + f.getPath());
				}
			}
			
			removeDirs(root, f.getParentFile());
		}
	}
}
