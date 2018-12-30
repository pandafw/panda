package panda.vfs.gae;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import panda.io.MimeTypes;
import panda.lang.Objects;
import panda.vfs.FileItem;

/**
 */
public class GaeFileItem implements FileItem, Serializable {
	private static final long serialVersionUID = 1L;
	
	private GaeFileStore gaeFileStore;

	private String name;
	private int size;
	private Date date;
	private boolean exists;
	private byte[] data;
	
	/**
	 * Constructor
	 * @param gaeFileStore GaeFileStore
	 */
	public GaeFileItem(GaeFileStore gaeFileStore) {
		this.gaeFileStore = gaeFileStore;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the size
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the date
	 */
	@Override
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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
		return exists;
	}

	/**
	 * @param exists the exists to set
	 */
	public void setExists(boolean exists) {
		this.exists = exists;
	}

	/**
	 * @return the data
	 */
	@Override
	public byte[] data() throws IOException {
		if (data == null) {
			data = gaeFileStore.readFile(this);
		}
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return the input stream
	 */
	@Override
	public InputStream open() throws IOException {
		return new ByteArrayInputStream(data());
	}
	
	/**
	 * save data
	 */
	@Override
	public void save(byte[] data) throws IOException {
		gaeFileStore.saveFile(this, data);
	}

	/**
	 * save data
	 */
	@Override
	public void save(InputStream data) throws IOException {
		gaeFileStore.saveFile(this, data);
	}

	@Override
	public void delete() throws IOException {
		gaeFileStore.removeFile(this);
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(NAME, name)
				.append(SIZE, size)
				.append(DATE, date)
				.toString();
	}

	/**
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return ((name == null) ? 0 : name.hashCode());
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
		
		GaeFileItem other = (GaeFileItem)obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}

		return true;
	}

	/**
	 * Creates and returns a copy of this object.
	 * @return the copy object
	 */
	@Override
	public GaeFileItem clone() {
		GaeFileItem copy = new GaeFileItem(gaeFileStore);
		copy.name = name;
		copy.size = size;
		copy.date = date;
		return copy;
	}
}

