package panda.vfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import panda.io.MimeTypes;

/**
 */
public class NullFileItem implements FileItem {
	
	protected Long id;

	protected String name;

	/**
	 * Constructor
	 * 
	 * @param id the file id
	 */
	public NullFileItem(Long id) {
		this.id = id;
	}

	/**
	 * Constructor
	 * 
	 * @param id the file id
	 * @param name the file name
	 */
	public NullFileItem(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the size
	 */
	@Override
	public int getSize() {
		return 0;
	}

	/**
	 * @return the date
	 */
	@Override
	public Date getDate() {
		return new Date(0);
	}

	/**
	 * @return the content type
	 */
	@Override
	public String getType() {
		return MimeTypes.getMimeType(name);
	}

	/**
	 * @return false
	 */
	@Override
	public boolean isExists() {
		return false;
	}

	/**
	 * @return the data
	 */
	@Override
	public byte[] data() throws IOException {
		throw error();
	}

	/**
	 * @return the input stream
	 */
	@Override
	public InputStream open() throws IOException {
		throw error();
	}

	/**
	 * save data
	 */
	@Override
	public void save(byte[] data) throws IOException {
		error();
	}
	
	/**
	 * save data
	 */
	@Override
	public void save(InputStream data) throws IOException {
		error();
	}

	@Override
	public void delete() throws IOException {
		throw error();
	}

	private IOException error() {
		return new FileNotFoundException(toString());
	}
	
	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return String.format("NullFileItem(id: %s, name: %s)", String.valueOf(id), String.valueOf(name));
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
		
		NullFileItem other = (NullFileItem)obj;
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
	public NullFileItem clone() {
		NullFileItem copy = new NullFileItem(id, name);
		return copy;
	}
}

