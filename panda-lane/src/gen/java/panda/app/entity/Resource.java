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
import panda.mvc.Validators;
import panda.mvc.annotation.Validate;
import panda.mvc.annotation.Validates;

@Indexes({
	@Index(name="CLC", fields={ "clazz", "language", "country" }, unique=true)
})
public class Resource extends SUBean implements Serializable {

	private static final long serialVersionUID = -1159960503L;

	/**
	 * Constructor
	 */
	public Resource() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String CLAZZ = "clazz";
	public static final String LANGUAGE = "language";
	public static final String COUNTRY = "country";
	public static final String SOURCE = "source";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			CLAZZ,
			LANGUAGE,
			COUNTRY,
			SOURCE
		};



	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1001)
	@Comment("id")
	protected Long id;

	@Column(size=100, notNull=true)
	@Comment("class name")
	protected String clazz;

	@Column(size=2, notNull=true)
	@Comment("language code")
	protected String language;

	@Column(size=2, notNull=true)
	@Comment("country code")
	protected String country;

	@Column(type=DaoTypes.CLOB, size=50000)
	@Comment("resource source")
	protected String source;


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
	 * @return the clazz
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 100 }", msgId=Validators.MSGID_STRING_LENTH)
	})
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = panda.lang.Strings.stripToNull(clazz);
	}

	/**
	 * @return the language
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 2 }", msgId=Validators.MSGID_STRING_LENTH), 
		@Validate(value=Validators.CONSTANT, params="{ 'list': '%{consts.localeLanguageMap}' }", msgId=Validators.MSGID_CONSTANT), 
		@Validate(value=Validators.EL, params="{ 'el': 'assist.isValidLocale(top.parent.value.language, top.parent.value.country)' }", msgId="validation-locale-invalid")
	})
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = panda.lang.Strings.stripToNull(language);
	}

	/**
	 * @return the country
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 2 }", msgId=Validators.MSGID_STRING_LENTH), 
		@Validate(value=Validators.CONSTANT, params="{ 'list': '%{consts.localeCountryMap}' }", msgId=Validators.MSGID_CONSTANT), 
		@Validate(value=Validators.EL, params="{ 'el': 'assist.isValidLocale(top.parent.value.language, top.parent.value.country)' }", msgId="validation-locale-invalid")
	})
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = panda.lang.Strings.stripToNull(country);
	}

	/**
	 * @return the source
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 50000 }", msgId=Validators.MSGID_STRING_LENTH)
	})
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
	public void copy(Resource src) {
		this.id = src.id;
		this.clazz = src.clazz;
		this.language = src.language;
		this.country = src.country;
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
	public Resource clone() {
		Resource copy = new Resource();
		
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

		Resource rhs = (Resource)obj;
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
				.append(CLAZZ, clazz)
				.append(LANGUAGE, language)
				.append(COUNTRY, country)
				.append(SOURCE, source)
				.appendSuper(super.toString())
				.toString();
	}
}

