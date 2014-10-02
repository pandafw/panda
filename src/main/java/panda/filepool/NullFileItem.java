package panda.filepool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 */
public class NullFileItem implements FileItem {
	
	protected Long id;

	protected String name;

	/**
	 * Constructor
	 */
	public NullFileItem(Long id) {
		this.id = id;
	}

	/**
	 * Constructor
	 */
	public NullFileItem(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return 0;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return new Date(0);
	}

	/**
	 * @return the flag
	 */
	public int getFlag() {
		return 0;
	}

	/**
	 * @return true if this is a temporary file
	 */
	public boolean isTemporary() {
		return true;
	}

	/**
	 * @return false
	 */
	public boolean isExists() {
		return false;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() throws IOException {
		throw error();
	}

	/**
	 * @return the input stream
	 */
	public InputStream getInputStream() throws IOException {
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

