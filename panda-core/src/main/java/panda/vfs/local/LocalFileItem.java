package panda.vfs.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import panda.io.Files;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Objects;
import panda.vfs.FileItem;

/**
 */
public class LocalFileItem implements FileItem, Serializable {
	private static final long serialVersionUID = 1L;
	
	private LocalFilePool localFilePool;
	private Long id;
	private File file;
	
	/**
	 * Constructor
	 */
	public LocalFileItem() {
	}

	/**
	 * Constructor
	 * 
	 * @param localFilePool local file pool
	 * @param id file id
	 * @param file the local file
	 */
	public LocalFileItem(LocalFilePool localFilePool, Long id, File file) {
		this.localFilePool = localFilePool;
		this.id = id;
		this.file = file;
	}

	/**
	 * @return the file
	 */
	protected File getFile() {
		return file;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return file.getName();
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
		return Streams.toByteArray(file);
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
		Files.write(file, data);
	}

	/**
	 * save data
	 */
	@Override
	public void save(InputStream data) throws IOException {
		Files.write(file, data);
	}

	@Override
	public void delete() {
		localFilePool.removeFile(file);
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("id", id)
				.append("file", file)
				.toString();
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
		LocalFileItem copy = new LocalFileItem(localFilePool, id, file);
		return copy;
	}
}

