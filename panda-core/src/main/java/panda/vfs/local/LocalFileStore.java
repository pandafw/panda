package panda.vfs.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import panda.io.FileIterator;
import panda.io.Files;
import panda.io.Streams;
import panda.io.filter.FileFilters;
import panda.io.filter.IOFileFilter;
import panda.lang.Strings;
import panda.vfs.FileItem;
import panda.vfs.FileStore;

public class LocalFileStore implements FileStore {
	protected String path;

	public LocalFileStore() {
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		if (Strings.isEmpty(path)) {
			path = new File(Files.getTempDirectory(), "files").getPath();
		}
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public Class<? extends FileItem> getItemType() {
		return LocalFileItem.class;
	}
	
	@Override
	public FileItem getFile(String name) {
		File file = new File(getPath(), name);
		return new LocalFileItem(this, file);
	}
	
	@Override
	public List<FileItem> listFiles() throws IOException {
		return listFiles(null, null);
	}
	
	@Override
	public List<FileItem> listFiles(String prefix, Date before) throws IOException {
		List<FileItem> fis = new ArrayList<FileItem>();

		String path = getPath();
		File root = new File(path);
		if (!root.exists()) {
			return fis;
		}
		
		IOFileFilter fil = null;
		if (Strings.isNotEmpty(prefix)) {
			File pref = new File(path, prefix);
			fil = FileFilters.prefixPathFilter(pref.getPath());
		}
		if (before != null) {
			if (fil == null) {
				fil = FileFilters.ageFileFilter(before, false);
			}
			else {
				fil = FileFilters.and(fil, FileFilters.ageFileFilter(before, false));
			}
		}
		if (fil == null) {
			fil = FileFilters.trueFileFilter();
		}
		
		FileIterator fit = Files.iterateFiles(root, fil, FileFilters.trueFileFilter());
		try {
			while (fit.hasNext()) {
				File f = fit.next();
				fis.add(new LocalFileItem(this, f));
			}
		}
		catch (Exception e) {
			throw new IOException("Failed to list files (" + prefix + ", " + before + ")", e);
		}
		finally {
			Streams.safeClose(fit);
		}
		
		return fis;
	}
}
