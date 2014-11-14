package panda.wing.entity;

import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Objects;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.wing.entity.SUBean;

@Indexes({
	@Index(name="NLC", fields={ "clazz","language","country" }, unique=true)
})
public class Resource extends SUBean {

	private static final long serialVersionUID = -1657205644L;

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

	public static final String[] COLUMNS = new String[] {
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
	@Validates({
		@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER)
	})
	protected Long id;

	@Column(size=100, notNull=true)
	@Comment("class name")
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 100 }", msgId=Validators.MSGID_STRING_LENTH), 
	})
	protected String clazz;

	@Column(notNull=true)
	@Comment("language code")
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 2 }", msgId=Validators.MSGID_STRING_LENTH), 
		@Validate(value=Validators.CONSTANT, params="{ 'list': '${consts.localeLanguageMap}' }", msgId=Validators.MSGID_CONSTANT), 
	})
	protected String language;

	@Column(size=2, notNull=true)
	@Comment("country code")
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 2 }", msgId=Validators.MSGID_STRING_LENTH), 
		@Validate(value=Validators.CONSTANT, params="{ 'list': '${consts.localeCountryMap}' }", msgId=Validators.MSGID_CONSTANT), 
	})
	protected String country;

	@Column(type=DaoTypes.CLOB, size=50000)
	@Comment("resource source")
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 50000 }", msgId=Validators.MSGID_STRING_LENTH)
	})
	protected String source;


	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
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
	 * @return the clazz
	 */
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

