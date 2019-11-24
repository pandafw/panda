package panda.vfs.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import panda.io.FileNames;
import panda.io.Files;
import panda.io.MimeTypes;
import panda.lang.Strings;
import panda.vfs.FileItem;

/**
 */
public class LocalFileItem implements FileItem, Serializable {
	private static final long serialVersionUID = 2L;
	
	private LocalFileStore localFileStore;
	private File file;
	
	/**
	 * Constructor
	 * 
	 * @param localFileStore local file store
	 * @param file the local file
	 */
	public LocalFileItem(LocalFileStore localFileStore, File file) {
		this.localFileStore = localFileStore;
		this.file = file;
	}

	/**
	 * @return the file
	 */
	protected File getFile() {
		return file;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		String name = Strings.removeStart(file.getPath(), localFileStore.getLocation());
		name = FileNames.separatorsToUnix(name);
		name = Strings.stripStart(name, '/');
		return name;
	}

	/**
	 * @return the size
	 */
	@Override
	public int getSize() {
		return (int)file.length();
	}

	/**
	 * @return the date
	 */
	@Override
	public Date getDate() {
		return new Date(file.lastModified());
	}

	/**
	 * @return the content type
	 */
	@Override
	public String getType() {
		return MimeTypes.getMimeType(getName());
	}

	/**
	 * @return true
	 */
	@Override
	public boolean isExists() {
		return file.exists();
	}

	/**
	 * @return the data
	 */
	@Override
	public byte[] data() throws IOException {
		return Files.readToBytes(file);
	}

	/**
	 * @return the input stream
	 */
	@Override
	public InputStream open() throws IOException {
		return new FileInputStream(file);
	}
	
	/**
	 * save data
	 */
	@Override
	public void save(byte[] data) throws IOException {
		Files.makeParents(file);
		Files.write(file, data);
	}

	/**
	 * save data
	 */
	@Override
	public void save(InputStream data) throws IOException {
		Files.makeParents(file);
		Files.write(file, data);
	}

	@Override
	public void delete() {
		File root = new File(localFileStore.getLocation());

		File f = file;
		while (f.delete()) {
			f = f.getParentFile();
			if (root.equals(f)) {
				break;
			}
		}
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return file == null ? "" : file.getPath();
	}

	/**
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return ((file == null) ? 0 : file.hashCode());
	}

	/**
	 * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		LocalFileItem other = (LocalFileItem)obj;
		if (file == null) {
			if (other.file != null) {
				return false;
			}
		}
		else if (!file.equals(other.file)) {
			return false;
		}

		return true;
	}

	/**
	 * Creates and returns a copy of this object.
	 * @return the copy object
	 */
	@Override
	public LocalFileItem clone() {
		LocalFileItem copy = new LocalFileItem(localFileStore, file);
		return copy;
	}
}

