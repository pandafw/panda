package panda.vfs.gcs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import com.google.appengine.tools.cloudstorage.ListItem;

import panda.io.FileNames;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Objects;
import panda.vfs.FileItem;

/**
 */
public class GcsFileItem implements FileItem, Serializable {
	private static final long serialVersionUID = 1L;
	
	private GcsFilePool gcsFilePool;
	private String id;
	private String name;
	private int size;
	private Date date;
	private boolean exists;
	
	/**
	 * Constructor
	 * @param gcsFilePool GcsFilePool
	 */
	public GcsFileItem(GcsFilePool gcsFilePool) {
		this.gcsFilePool = gcsFilePool;
	}

	/**
	 * Constructor
	 * 
	 * @param gcsFilePool local file pool
	 * @param id file id
	 * @param item the gcs file item
	 */
	public GcsFileItem(GcsFilePool gcsFilePool, String id, ListItem item) {
		this.gcsFilePool = gcsFilePool;
		this.id = id;
		this.name = FileNames.getName(item.getName());
		this.size = (int)item.getLength();
		this.date = item.getLastModified();
		this.exists = true;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
		return Streams.toByteArray(open());
	}

	/**
	 * @return the input stream
	 */
	@Override
	public InputStream open() throws IOException {
		return gcsFilePool.openFile(this);
	}
	
	/**
	 * save data
	 */
	@Override
	public void save(byte[] data) throws IOException {
		gcsFilePool.saveFile(this, data);
	}

	/**
	 * save data
	 */
	@Override
	public void save(InputStream data) throws IOException {
		gcsFilePool.saveFile(this, data);
	}

	@Override
	public void delete() throws IOException {
		gcsFilePool.removeFile(this);
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(ID, id)
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
		return ((id == null) ? 0 : id.hashCode());
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
		
		GcsFileItem other = (GcsFileItem)obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		}
		else if (!id.equals(other.id)) {
			return false;
		}

		return true;
	}

	/**
	 * Creates and returns a copy of this object.
	 * @return the copy object
	 */
	@Override
	public GcsFileItem clone() {
		GcsFileItem copy = new GcsFileItem(gcsFilePool);
		copy.id = id;
		copy.name = name;
		copy.size = size;
		copy.date = date;
		return copy;
	}
}

