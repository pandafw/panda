package panda.filepool.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Table;
import panda.filepool.FileItem;
import panda.lang.Objects;

/**
 */
@Table("FileItem")
public class DaoFileItem implements FileItem, Serializable {
	public static final int TEMPORARY = 0;
	public static final int ARCHIVE = 1;

	private static final long serialVersionUID = 1L;
	
	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String SIZE = "size";
	public static final String DATE = "date";
	public static final String FLAG = "flag";

	public static final String[] COLUMNS = new String[] {
			ID,
			NAME,
			SIZE,
			DATE,
			FLAG,
		};

	private DaoFilePool daoFilePool;
	private byte[] data;
	
	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id
	protected Long id;

	@Column(notNull=true, size=250)
	protected String name;

	@Column(notNull=true, defaults="0")
	protected int size = 0;

	@Column(notNull=true)
	protected Date date;

	@Column(notNull=true, defaults="0")
	protected int flag = 0;

	/**
	 * Constructor
	 */
	public DaoFileItem() {
		super();
	}

	/**
	 * @return the daoFilePool
	 */
	protected DaoFilePool getDaoFilePool() {
		return daoFilePool;
	}

	/**
	 * @param daoFilePool the daoFilePool to set
	 */
	protected void setDaoFilePool(DaoFilePool daoFilePool) {
		this.daoFilePool = daoFilePool;
	}

	/**
	 * @return the id
	 */
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
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	/**
	 * @return true if this is a temporary file
	 */
	public boolean isTemporary() {
		return flag == TEMPORARY;
	}

	/**
	 * @return true
	 */
	public boolean isExists() {
		return true;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() throws IOException {
		if (data == null) {
			data = daoFilePool.readFile(this);
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
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(getData());
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
				.append(FLAG, flag)
				.append("data", data)
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
		
		DaoFileItem other = (DaoFileItem)obj;
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
	public DaoFileItem clone() {
		DaoFileItem copy = new DaoFileItem();
		
		copy.id = this.id;
		copy.name = this.name;
		copy.size = this.size;
		copy.date = this.date;
		copy.flag = this.flag;
		copy.data = this.data;

		return copy;
	}
}

