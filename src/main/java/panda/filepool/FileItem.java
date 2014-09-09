package panda.filepool;

import java.io.Serializable;
import java.util.Date;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Id;
import panda.lang.Objects;

/**
 */
public class FileItem implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int TEMPORARY = 0;
	public static final int ARCHIVE = 1;
	
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

	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id
	protected Long id;

	@Column(notNull=true, size=250)
	protected String name;

	@Column(notNull=true, defaults="0")
	protected Integer size = 0;

	@Column(notNull=true)
	protected Date date;

	@Column(notNull=true)
	protected Integer flag = 0;

	protected byte[] data;

	/**
	 * Constructor
	 */
	public FileItem() {
		super();
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
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
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
	public Integer getFlag() {
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
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
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
		
		FileItem other = (FileItem)obj;
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
	public FileItem clone() {
		FileItem copy = new FileItem();
		
		copy.id = this.id;
		copy.name = this.name;
		copy.size = this.size;
		copy.date = this.date;
		copy.flag = this.flag;
		copy.data = this.data;

		return copy;
	}
}

