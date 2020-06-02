package panda.gems.media.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.ForeignKeys;
import panda.dao.entity.annotation.PK;
import panda.lang.Objects;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.validator.Validators;

@ForeignKeys({
	@FK(target=Media.class, fields={ "mid" }, onUpdate=FK.CASCADE, onDelete=FK.CASCADE)
})
public class MediaData {

	/**
	 * Constructor
	 */
	public MediaData() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String MID = "mid";
	public static final String MSZ = "msz";
	public static final String SIZE = "size";
	public static final String DATA = "data";

	public static final String[] _COLUMNS_ = new String[] {
			MID,
			MSZ,
			SIZE,
			DATA
		};



	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@PK
	@Column(notNull=true)
	@Comment("media id")
	protected Long mid;

	@PK
	@Column(notNull=true)
	@Comment("media size")
	protected Integer msz;

	@Column(notNull=true)
	@Comment("file size")
	protected Integer size;

	@Column(type=DaoTypes.BLOB, notNull=true)
	@Comment("file data")
	protected byte[] data;


	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the mid
	 */
	public Long getMid() {
		return mid;
	}

	/**
	 * @param mid the mid to set
	 */
	public void setMid(Long mid) {
		this.mid = mid;
	}

	/**
	 * @return the msz
	 */
	@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
	public Integer getMsz() {
		return msz;
	}

	/**
	 * @param msz the msz to set
	 */
	public void setMsz(Integer msz) {
		this.msz = msz;
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
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(MediaData src) {
		this.mid = src.mid;
		this.msz = src.msz;
		this.size = src.size;
		this.data = src.data;
	}

	public InputStream open() {
		if (data == null) {
			throw new NullPointerException();
		}
		return new ByteArrayInputStream(data);
	}

	/*----------------------------------------------------------------------*
	 * Overrides
	 *----------------------------------------------------------------------*/
	/**
	 * Creates and returns a copy of this object.
	 * @return the copy object
	 */
	@Override
	public MediaData clone() {
		MediaData copy = new MediaData();
		
		copy.copy(this);

		return copy;
	}

	/**
	 * @return  a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(mid,msz);
	}

	/**
	 * @return  <code>true</code> if this object is the same as the obj argument; 
	 * 			<code>false</code> otherwise.
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

		MediaData rhs = (MediaData)obj;
		return Objects.equalsBuilder()
				.append(mid, rhs.mid)
				.append(msz, rhs.msz)
				.isEquals();
	}

	/**
	 * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(MID, mid)
				.append(MSZ, msz)
				.append(SIZE, size)
				.append(DATA, data)
				.toString();
	}
}

