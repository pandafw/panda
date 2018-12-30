package panda.vfs.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.PK;
import panda.dao.entity.annotation.Table;
import panda.io.MimeTypes;
import panda.lang.Objects;
import panda.vfs.FileItem;

/**
 */
@Table("file_item")
public class DaoFileItem implements FileItem, Serializable {
	private static final long serialVersionUID = 2L;
	
	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String[] COLUMNS = new String[] {
			NAME,
			SIZE,
			DATE
		};

	private DaoFileStore daoFileStore;
	private byte[] data;
	
	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@PK
	@Column(notNull=true, size=255)
	protected String name;

	@Column(notNull=true, defaults="0")
	protected int size = 0;

	@Column(notNull=true)
	protected Date date;

	private boolean exists = true;
	
	/**
	 * Constructor
	 */
	public DaoFileItem() {
		super();
	}

	/**
	 * @return the daoFileStore
	 */
	protected DaoFileStore getDaoFileStore() {
		return daoFileStore;
	}

	/**
	 * @param daoFileStore the daoFileStore to set
	 */
	protected void setDaoFileStore(DaoFileStore daoFileStore) {
		this.daoFileStore = daoFileStore;
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
		return MimeTypes.getMimeType(name);
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
			data = daoFileStore.readFile(this);
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
		daoFileStore.saveFile(this, data);
	}
	
	/**
	 * save data
	 */
	@Override
	public void save(InputStream data) throws IOException {
		daoFileStore.saveFile(this, data);
	}

	@Override
	public void delete() throws IOException {
		daoFileStore.deleteFile(this);
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
		
		DaoFileItem other = (DaoFileItem)obj;
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
	public DaoFileItem clone() {
		DaoFileItem copy = new DaoFileItem();
		
		copy.name = this.name;
		copy.size = this.size;
		copy.date = this.date;
		copy.data = this.data;

		return copy;
	}
}

