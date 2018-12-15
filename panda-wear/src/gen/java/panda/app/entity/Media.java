package panda.app.entity;

import java.io.Serializable;
import panda.app.entity.CUBean;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Id;
import panda.lang.Objects;
import panda.mvc.annotation.Validate;
import panda.mvc.annotation.Validates;
import panda.mvc.validator.Validators;
import panda.vfs.FileItem;

public class Media extends CUBean implements Serializable {

	private static final long serialVersionUID = -1112649986L;

	/**
	 * Constructor
	 */
	public Media() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String MID = "mid";
	public static final String TAG = "tag";
	public static final String NAME = "name";
	public static final String SIZE = "size";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String FILE = "file";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			MID,
			TAG,
			NAME,
			SIZE,
			WIDTH,
			HEIGHT
		};



	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1001)
	protected Long id;

	@Column
	protected Long mid;

	@Column(size=10)
	protected String tag;

	@Column(size=200)
	protected String name;

	@Column(notNull=true)
	protected Integer size;

	@Column(notNull=true)
	protected Integer width;

	@Column(notNull=true)
	protected Integer height;

	protected FileItem file;


	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the id
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
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
	 * @return the mid
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
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
	 * @return the tag
	 */
	@Validates({
		@Validate(value=Validators.CONSTANT, params="{ 'list': '%{consts.mediaTagMap}' }", msgId=Validators.MSGID_CONSTANT)
	})
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = panda.lang.Strings.stripToNull(tag);
	}

	/**
	 * @return the name
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 200 }", msgId=Validators.MSGID_STRING_LENTH)
	})
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = panda.lang.Strings.stripToNull(name);
	}

	/**
	 * @return the size
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
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
	 * @return the width
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
	public Integer getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * @return the file
	 */
	@Validates({
		@Validate(value=Validators.FILE, msgId=Validators.MSGID_FILE), 
		@Validate(value=Validators.IMAGE, msgId=Validators.MSGID_IMAGE), 
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_FILE)
	})
	public FileItem getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(FileItem file) {
		this.file = file;
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Media src) {
		this.id = src.id;
		this.mid = src.mid;
		this.tag = src.tag;
		this.name = src.name;
		this.size = src.size;
		this.width = src.width;
		this.height = src.height;
		this.file = src.file;
		super.copy(src);
	}

	/*----------------------------------------------------------------------*
	 * Overrides
	 *----------------------------------------------------------------------*/
	/**
	 * Creates and returns a copy of this object.
	 * @return the copy object
	 */
	@Override
	public Media clone() {
		Media copy = new Media();
		
		copy.copy(this);

		return copy;
	}

	/**
	 * @return  a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(id);
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

		Media rhs = (Media)obj;
		return Objects.equalsBuilder()
				.append(id, rhs.id)
				.isEquals();
	}

	/**
	 * @return  a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(ID, id)
				.append(MID, mid)
				.append(TAG, tag)
				.append(NAME, name)
				.append(SIZE, size)
				.append(WIDTH, width)
				.append(HEIGHT, height)
				.append(FILE, file)
				.appendSuper(super.toString())
				.toString();
	}
}

