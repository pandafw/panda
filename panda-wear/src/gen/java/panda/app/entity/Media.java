package panda.app.entity;

import java.io.Serializable;
import panda.app.entity.UBean;
import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Id;
import panda.lang.Objects;
import panda.mvc.annotation.Validate;
import panda.mvc.annotation.Validates;
import panda.mvc.validator.Validators;
import panda.vfs.FileItem;

public class Media extends UBean implements Serializable {

	private static final long serialVersionUID = -1419169521L;

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
	public static final String KIND = "kind";
	public static final String MEDIA_NAME = "mediaName";
	public static final String MEDIA_DATA = "mediaData";
	public static final String MEDIA_SIZE = "mediaSize";
	public static final String MEDIA_WIDTH = "mediaWidth";
	public static final String MEDIA_HEIGHT = "mediaHeight";
	public static final String MEDIA_FILE = "mediaFile";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			KIND,
			MEDIA_NAME,
			MEDIA_DATA,
			MEDIA_SIZE,
			MEDIA_WIDTH,
			MEDIA_HEIGHT
		};



	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1001)
	protected Long id;

	@Column(size=2)
	protected String kind;

	@Column(size=255)
	protected String mediaName;

	@Column(type=DaoTypes.BLOB, notNull=true)
	protected byte[] mediaData;

	@Column(notNull=true)
	protected Integer mediaSize;

	@Column(notNull=true)
	protected Integer mediaWidth;

	@Column(notNull=true)
	protected Integer mediaHeight;

	protected FileItem mediaFile;


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
	 * @return the kind
	 */
	@Validates({
		@Validate(value=Validators.CONSTANT, params="{ 'list': '%{consts.mediaKindMap}' }", msgId=Validators.MSGID_CONSTANT)
	})
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind(String kind) {
		this.kind = panda.lang.Strings.stripToNull(kind);
	}

	/**
	 * @return the mediaName
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 255 }", msgId=Validators.MSGID_STRING_LENTH)
	})
	public String getMediaName() {
		return mediaName;
	}

	/**
	 * @param mediaName the mediaName to set
	 */
	public void setMediaName(String mediaName) {
		this.mediaName = panda.lang.Strings.stripToNull(mediaName);
	}

	/**
	 * @return the mediaData
	 */
	public byte[] getMediaData() {
		return mediaData;
	}

	/**
	 * @param mediaData the mediaData to set
	 */
	public void setMediaData(byte[] mediaData) {
		this.mediaData = mediaData;
	}

	/**
	 * @return the mediaSize
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
	public Integer getMediaSize() {
		return mediaSize;
	}

	/**
	 * @param mediaSize the mediaSize to set
	 */
	public void setMediaSize(Integer mediaSize) {
		this.mediaSize = mediaSize;
	}

	/**
	 * @return the mediaWidth
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
	public Integer getMediaWidth() {
		return mediaWidth;
	}

	/**
	 * @param mediaWidth the mediaWidth to set
	 */
	public void setMediaWidth(Integer mediaWidth) {
		this.mediaWidth = mediaWidth;
	}

	/**
	 * @return the mediaHeight
	 */
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
	public Integer getMediaHeight() {
		return mediaHeight;
	}

	/**
	 * @param mediaHeight the mediaHeight to set
	 */
	public void setMediaHeight(Integer mediaHeight) {
		this.mediaHeight = mediaHeight;
	}

	/**
	 * @return the mediaFile
	 */
	@Validates({
		@Validate(value=Validators.FILE, msgId=Validators.MSGID_FILE), 
		@Validate(value=Validators.IMAGE, msgId=Validators.MSGID_IMAGE), 
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_FILE)
	})
	public FileItem getMediaFile() {
		return mediaFile;
	}

	/**
	 * @param mediaFile the mediaFile to set
	 */
	public void setMediaFile(FileItem mediaFile) {
		this.mediaFile = mediaFile;
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Media src) {
		this.id = src.id;
		this.kind = src.kind;
		this.mediaName = src.mediaName;
		this.mediaData = src.mediaData;
		this.mediaSize = src.mediaSize;
		this.mediaWidth = src.mediaWidth;
		this.mediaHeight = src.mediaHeight;
		this.mediaFile = src.mediaFile;
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
				.append(KIND, kind)
				.append(MEDIA_NAME, mediaName)
				.append(MEDIA_DATA, mediaData)
				.append(MEDIA_SIZE, mediaSize)
				.append(MEDIA_WIDTH, mediaWidth)
				.append(MEDIA_HEIGHT, mediaHeight)
				.append(MEDIA_FILE, mediaFile)
				.appendSuper(super.toString())
				.toString();
	}
}

