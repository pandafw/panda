package panda.app.entity;

import java.io.Serializable;
import panda.app.entity.SUBean;
import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Objects;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.ConstantValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.validator.Validators;

@Indexes({
	@Index(name="NL", fields={ "name", "locale" }, unique=true)
})
public class Template extends SUBean implements Serializable {

	private static final long serialVersionUID = 2051713339L;

	/**
	 * Constructor
	 */
	public Template() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String LOCALE = "locale";
	public static final String SOURCE = "source";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			NAME,
			LOCALE,
			SOURCE
		};



	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1001)
	@Comment("id")
	protected Long id;

	@Column(size=100, notNull=true)
	@Comment("file name")
	protected String name;

	@Column(size=10, notNull=true)
	@Comment("locale")
	protected String locale;

	@Column(type=DaoTypes.CLOB, size=50000)
	@Comment("template source")
	protected String source;


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
	 * @return the name
	 */
	@StringValidate(maxLength=100)
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
	 * @return the locale
	 */
	@StringValidate(maxLength=10)
	@ConstantValidate(list="%{consts.appLocaleMap}")
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = panda.lang.Strings.stripToNull(locale);
	}

	/**
	 * @return the source
	 */
	@StringValidate(maxLength=50000)
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Template src) {
		this.id = src.id;
		this.name = src.name;
		this.locale = src.locale;
		this.source = src.source;
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
	public Template clone() {
		Template copy = new Template();
		
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

		Template rhs = (Template)obj;
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
				.append(NAME, name)
				.append(LOCALE, locale)
				.append(SOURCE, source)
				.appendSuper(super.toString())
				.toString();
	}
}

