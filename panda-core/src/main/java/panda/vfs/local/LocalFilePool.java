package panda.vfs.local;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import panda.io.FileIterator;
import panda.io.FileNames;
import panda.io.Files;
import panda.io.Streams;
import panda.io.filter.FileFilters;
import panda.lang.Arrays;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.FileItem;
import panda.vfs.FilePool;

public class LocalFilePool implements FilePool {
	private final static Log log = Logs.getLog(LocalFilePool.class);
	
	protected final static FastDateFormat fdf = FastDateFormat.getInstance("yyyy/MM/dd/HH/mm/ss/SSS");

	protected String path;

	protected long serial;
	
	/**
	 * milliseconds since last modified. (default: 1 day) 
	 */
	protected long expires = DateTimes.MS_DAY;

	public LocalFilePool() {
	}

	public void initialize() {
		if (Strings.isEmpty(path)) {
			path = new File(Files.getTempDirectory(), "files").getPath();
		}
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
		this.expires = maxAge * 1000L;
	}

	protected synchronized LocalFileItem getLocalFile(String name) {
		name = FileNames.trimFileName(name);
		if (Strings.isEmpty(name)) {
			name = "noname";
		}

		long time = DateTimes.getDate().getTime();
		if (serial < time) {
			serial = time;
		}
		else {
			serial++;
		}

		while (true) {
			File dir = new File(path, fdf.format(serial));
			if (dir.exists()) {
				serial++;
				continue;
			}
			File file = new File(dir, name);
			return new LocalFileItem(this, String.valueOf(serial), file);
		}
	}
	
	@Override
	public Class<? extends FileItem> getItemType() {
		return LocalFileItem.class;
	}
	
	@Override
	public FileItem saveFile(String name, final byte[] data) throws IOException {
		LocalFileItem fi = getLocalFile(name);
		fi.getFile().getParentFile().mkdirs();
		Files.write(fi.getFile(), data);
		return fi;
	}
	
	@Override
	public FileItem saveFile(String name, final InputStream data) throws IOException {
		LocalFileItem fi = getLocalFile(name);
		fi.getFile().getParentFile().mkdirs();
		Files.write(fi.getFile(), data);
		return fi;
	}
	
	@Override
	public FileItem findFile(String id) {
		Long t = Numbers.toLong(id, 0L);
		File dir = new File(path, fdf.format(t));
		if (dir.exists()) {
			File[] fs = dir.listFiles();
			if (Arrays.isNotEmpty(fs)) {
				return new LocalFileItem(this, String.valueOf(t), fs[0]);
			}
		}

		return new LocalFileItem(this, String.valueOf(t), new File(dir, "noname"));
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
			if (!dir.exists()) {
				break;
			}
			if (!dir.delete()) {
				break;
			}
			dir = dir.getParentFile();
		}
	}
	
	@Override
	public synchronized int clean() throws IOException {
		File root = new File(path);
		
		final long time = System.currentTimeMillis() - expires;
		Collection<File> fs = Files.listFiles(root, true, FileFilters.ageFileFilter(time));

		int cnt = 0;
		for (File f : fs) {
			if (f.delete()) {
				cnt++;
				if (log.isDebugEnabled()) {
					log.debug("Remove temporary file: " + f.getPath());
				}
			}
			
			removeDirs(root, f.getParentFile());
		}
		
		return cnt;
	}
	
	@Override
	public List<FileItem> listFiles() throws IOException {
		File root = new File(path);
		
		if (!root.exists()) {
			return null;
		}
		
		List<FileItem> fis = new ArrayList<FileItem>();
		FileIterator fit = Files.iterateFiles(root, true);
		try {
			while (fit.hasNext()) {
				String id;
				File f = fit.next();
				String rp = FileNames.getRelativePath(root, f.getParentFile());
				try {
					Date d = fdf.parse(rp);
					id = String.valueOf(d.getTime());
				}
				catch (ParseException e) {
					continue;
				}
				fis.add(new LocalFileItem(this, id, f));
			}
		}
		finally {
			Streams.safeClose(fit);
		}
		
		return fis;
	}
}
