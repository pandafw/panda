package panda.app.entity;

import java.io.Serializable;
import panda.app.entity.SUBean;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.lang.Objects;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.ConstantValidate;
import panda.mvc.annotation.validate.ELValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.validator.Validators;

@Indexes({
	@Index(name="CLCN", fields={ "clazz", "language", "country", "name" }, unique=true)
})
public class Property extends SUBean implements Serializable {

	private static final long serialVersionUID = -150761943L;

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
	 * @return the clazz
	 */
	@StringValidate(maxLength=100)
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
	@StringValidate(maxLength=2)
	@ConstantValidate(list="%{consts.localeLanguageMap}")
	@ELValidate(el="assist.isValidLocale(top.parent.value.language, top.parent.value.country)", msgId=Validators.MSGID_LOCALE)
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
	@StringValidate(maxLength=2)
	@ConstantValidate(list="%{consts.localeCountryMap}")
	@ELValidate(el="assist.isValidLocale(top.parent.value.language, top.parent.value.country)", msgId=Validators.MSGID_LOCALE)
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
	@StringValidate(maxLength=50)
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
	@StringValidate(maxLength=5000)
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
	@StringValidate(maxLength=1000)
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

