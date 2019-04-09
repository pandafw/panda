package panda.vfs.dao;

import java.io.Serializable;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.PK;
import panda.dao.entity.annotation.Table;
import panda.lang.Objects;

/**
 */
@Table("file_data")
public class DaoFileData implements Serializable {
	private static final long serialVersionUID = 1L;

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String FNM = "fnm";
	public static final String BNO = "bno";
	public static final String SIZE = "size";
	public static final String DATA = "data";

	public static final String[] COLUMNS = new String[] {
			FNM,
			BNO,
			SIZE,
			DATA
		};

	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@PK
	@FK(target=DaoFileItem.class)
	@Column(notNull=true, size=255)
	protected String fnm;
	
	@PK
	@Column(notNull=true)
	protected Integer bno;

	@Column(notNull=true, defaults="0")
	protected Integer size = 0;

	@Column(notNull=true)
	protected byte[] data;

	/**
	 * Constructor
	 */
	public DaoFileData() {
		super();
	}

	/**
	 * @return the file name
	 */
	public String getFnm() {
		return fnm;
	}

	/**
	 * @param fnm the file name to set
	 */
	public void setFnm(String fnm) {
		this.fnm = fnm;
	}

	/**
	 * @return the block no.
	 */
	public Integer getBno() {
		return bno;
	}


	/**
	 * @param bno the block no. to set
	 */
	public void setBno(Integer bno) {
		this.bno = bno;
	}


	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
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
				.append(FNM, fnm)
				.append(BNO, bno)
				.append(SIZE, size)
				.append(DATA, data)
				.toString();
	}

	/**
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(fnm, bno);
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
		
		DaoFileData rhs = (DaoFileData)obj;
		return Objects.equalsBuilder()
				.append(fnm, rhs.fnm)
				.append(bno, rhs.bno)
				.append(size, rhs.size)
				.append(data, rhs.data)
				.isEquals();
	}
}

