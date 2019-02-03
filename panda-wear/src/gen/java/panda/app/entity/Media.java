package panda.app.entity;

import java.io.Serializable;
import panda.app.entity.CUBean;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Objects;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.ConstantValidate;
import panda.mvc.annotation.validate.FileValidate;
import panda.mvc.annotation.validate.ImageValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.validator.Validators;
import panda.vfs.FileItem;

@Indexes({
	@Index(name="CA", fields={ "createdAt" }),
	@Index(name="NAME", fields={ "name" }),
	@Index(name="TAG", fields={ "tag" })
})
public class Media extends CUBean implements Serializable {

	private static final long serialVersionUID = 1692718689L;

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
	public static final String TAG = "tag";
	public static final String NAME = "name";
	public static final String SIZE = "size";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String FILE = "file";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
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
	protected String id;

	@Column(size=10)
	protected String tag;

	@Column(size=200)
	protected String name;

	@Column(notNull=true)
	protected Integer size;

	@Column
	protected Integer width;

	@Column
	protected Integer height;

	protected FileItem file;


	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = panda.lang.Strings.stripToNull(id);
	}

	/**
	 * @return the tag
	 */
	@ConstantValidate(list="%{consts.mediaTagMap}")
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
	@StringValidate(maxLength=200)
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
	@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
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
	@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
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
	@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
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
	@FileValidate
	@ImageValidate
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

