package panda.app.entity;

import java.io.Serializable;
import panda.app.entity.SUBean;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Objects;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;

@Indexes({
	@Index(name="CLCN", fields={ "clazz", "language", "country", "name" }, unique=true)
})
public class Property extends SUBean implements Serializable {

	private static final long serialVersionUID = 140558409L;

	/**
	 * Constructor
	 */
	public Property() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String CLAZZ = "clazz";
	public static final String LANGUAGE = "language";
	public static final String COUNTRY = "country";
	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String MEMO = "memo";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			CLAZZ,
			LANGUAGE,
			COUNTRY,
			NAME,
			VALUE,
			MEMO
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

	@Column(size=50, notNull=true)
	@Comment("property name")
	protected String name;

	@Column(size=5000)
	@Comment("property value")
	protected String value;

	@Column(size=1000)
	@Comment("memo")
	protected String memo;


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
	 * @return the name
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 50 }", msgId=Validators.MSGID_STRING_LENTH)
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
	 * @return the value
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 5000 }", msgId=Validators.MSGID_STRING_LENTH)
	})
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the memo
	 */
	@Validates({
		@Validate(value=Validators.STRING, params="{ 'maxLength': 1000 }", msgId=Validators.MSGID_STRING_LENTH)
	})
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = panda.lang.Strings.stripToNull(memo);
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Property src) {
		this.id = src.id;
		this.clazz = src.clazz;
		this.language = src.language;
		this.country = src.country;
		this.name = src.name;
		this.value = src.value;
		this.memo = src.memo;
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
	public Property clone() {
		Property copy = new Property();
		
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

		Property rhs = (Property)obj;
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
				.append(NAME, name)
				.append(VALUE, value)
				.append(MEMO, memo)
				.appendSuper(super.toString())
				.toString();
	}
}

