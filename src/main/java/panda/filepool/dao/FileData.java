package panda.filepool.dao;

import java.io.Serializable;

import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.PK;
import panda.filepool.FileItem;
import panda.lang.Objects;

/**
 */
public class FileData implements Serializable {
	private static final long serialVersionUID = 1L;

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String FID = "fid";
	public static final String BNO = "bno";
	public static final String SIZE = "size";
	public static final String DATA = "data";

	public static final String[] COLUMNS = new String[] {
			FID,
			BNO,
			SIZE,
			DATA
		};

	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@PK
	@FK(target=FileItem.class)
	protected Long fid;
	
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
	public FileData() {
		super();
	}

	/**
	 * @return the fileId
	 */
	public Long getFid() {
		return fid;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFid(Long fileId) {
		this.fid = fileId;
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
				.append(FID, fid)
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
		return Objects.hashCodes(fid, bno);
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
		
		FileData rhs = (FileData)obj;
		return Objects.equalsBuilder()
				.append(fid, rhs.fid)
				.append(bno, rhs.bno)
				.append(size, rhs.size)
				.append(data, rhs.data)
				.isEquals();
	}
}

