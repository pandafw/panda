package panda.gems.pages.entity;

import java.io.Serializable;
import java.util.Date;
import panda.app.entity.SCUBean;
import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Objects;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.validator.Validators;

@Indexes({
	@Index(name="S", fields={ "slug" }, unique=true),
	@Index(name="TL", fields={ "title" }, unique=true)
})
public class Page extends SCUBean implements Serializable {

	private static final long serialVersionUID = 45910314L;

	/**
	 * Constructor
	 */
	public Page() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String SLUG = "slug";
	public static final String TITLE = "title";
	public static final String TAG = "tag";
	public static final String PUBLISH_DATE = "publishDate";
	public static final String THUMBNAIL = "thumbnail";
	public static final String CONTENT = "content";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			SLUG,
			TITLE,
			TAG,
			PUBLISH_DATE,
			THUMBNAIL,
			CONTENT
		};



	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1)
	@Comment("id")
	protected Long id;

	@Column(size=100)
	protected String slug;

	@Column(size=100, notNull=true)
	@Comment("title")
	protected String title;

	@Column(size=255)
	@Comment("thumbnail")
	protected String tag;

	@Column
	protected Date publishDate;

	@Column(size=32)
	@Comment("thumbnail")
	protected String thumbnail;

	@Column(type=DaoTypes.CLOB, size=100000)
	@Comment("page content")
	protected String content;


	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the id
	 */
	@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
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
	 * @return the slug
	 */
	public String getSlug() {
		return slug;
	}

	/**
	 * @param slug the slug to set
	 */
	public void setSlug(String slug) {
		this.slug = panda.lang.Strings.stripToNull(slug);
	}

	/**
	 * @return the title
	 */
	@StringValidate(maxLength=100)
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = panda.lang.Strings.stripToNull(title);
	}

	/**
	 * @return the tag
	 */
	@StringValidate(maxLength=255)
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
	 * @return the publishDate
	 */
	@CastErrorValidate(msgId=Validators.MSGID_DATE)
	public Date getPublishDate() {
		return publishDate;
	}

	/**
	 * @param publishDate the publishDate to set
	 */
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	/**
	 * @return the thumbnail
	 */
	@StringValidate(maxLength=32)
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = panda.lang.Strings.stripToNull(thumbnail);
	}

	/**
	 * @return the content
	 */
	@StringValidate(maxLength=100000)
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Page src) {
		this.id = src.id;
		this.slug = src.slug;
		this.title = src.title;
		this.tag = src.tag;
		this.publishDate = src.publishDate;
		this.thumbnail = src.thumbnail;
		this.content = src.content;
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
	public Page clone() {
		Page copy = new Page();
		
		copy.copy(this);

		return copy;
	}

	/**
	 * @return  a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
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

		Page rhs = (Page)obj;
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
				.append(SLUG, slug)
				.append(TITLE, title)
				.append(TAG, tag)
				.append(PUBLISH_DATE, publishDate)
				.append(THUMBNAIL, thumbnail)
				.append(CONTENT, content)
				.appendSuper(super.toString())
				.toString();
	}
}

