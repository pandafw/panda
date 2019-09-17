package ${package}.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import panda.app.entity.SUBean;
import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.dao.entity.annotation.Join;
import panda.dao.entity.annotation.JoinColumn;
import panda.dao.entity.annotation.Joins;
import panda.lang.Objects;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.ConstantValidate;
import panda.mvc.annotation.validate.DecimalValidate;
import panda.mvc.annotation.validate.NumberValidate;
import panda.mvc.annotation.validate.RegexValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.annotation.validate.URLValidate;
import panda.mvc.validator.Validators;

@Indexes({
	@Index(name="NG", fields={ "name", "gender" }, unique=true)
})
@Joins({
	@Join(name="UU", target=panda.gems.users.entity.User.class, keys="updatedBy", refs="id")
})
public class Pet extends SUBean implements Serializable {

	private static final long serialVersionUID = -1097920011L;

	/**
	 * Constructor
	 */
	public Pet() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String GENDER = "gender";
	public static final String BIRTHDAY = "birthday";
	public static final String AMOUNT = "amount";
	public static final String PRICE = "price";
	public static final String SHOP_NAME = "shopName";
	public static final String SHOP_TELEPHONE = "shopTelephone";
	public static final String SHOP_LINK = "shopLink";
	public static final String DESCRIPTION = "description";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			NAME,
			GENDER,
			BIRTHDAY,
			AMOUNT,
			PRICE,
			SHOP_NAME,
			SHOP_TELEPHONE,
			SHOP_LINK,
			DESCRIPTION
		};

	public static final String[] _JOINS_ = new String[] {
			UPDATED_BY_NAME
		};

	public static final String _JOIN_UU_ = "UU";

	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1001)
	@Comment("pet id")
	protected Long id;

	@Column(size=100, notNull=true)
	@Comment("pet name")
	protected String name;

	@Column(size=1)
	protected String gender;

	@Column
	protected Date birthday;

	@Column(notNull=true, defaults="0")
	protected Integer amount;

	@Column(size=10, scale=2)
	protected BigDecimal price;

	@Column(size=100)
	protected String shopName;

	@Column(size=20)
	protected String shopTelephone;

	@Column(size=1000)
	protected String shopLink;

	@Column(type=DaoTypes.CLOB, size=5000)
	protected String description;


	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the updatedByName
	 */
	@Override
	@JoinColumn(name="UU", field="name")
	public String getUpdatedByName() {
		return super.getUpdatedByName();
	}

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
	 * @return the gender
	 */
	@ConstantValidate(list="%{consts.petGenderMap}")
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = panda.lang.Strings.stripToNull(gender);
	}

	/**
	 * @return the birthday
	 */
	@CastErrorValidate(msgId=Validators.MSGID_DATE)
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the amount
	 */
	@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
	@NumberValidate(min="0")
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * @return the price
	 */
	@CastErrorValidate(msgId=Validators.MSGID_DECIMAL)
	@NumberValidate(min="0", max="9999999999")
	@DecimalValidate(precision=10, scale=2)
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the shopName
	 */
	@StringValidate(maxLength=100)
	public String getShopName() {
		return shopName;
	}

	/**
	 * @param shopName the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = panda.lang.Strings.stripToNull(shopName);
	}

	/**
	 * @return the shopTelephone
	 */
	@StringValidate(maxLength=20)
	@RegexValidate(regex="#(regex-telno)", msgId="validation-telno")
	public String getShopTelephone() {
		return shopTelephone;
	}

	/**
	 * @param shopTelephone the shopTelephone to set
	 */
	public void setShopTelephone(String shopTelephone) {
		this.shopTelephone = panda.lang.Strings.stripToNull(shopTelephone);
	}

	/**
	 * @return the shopLink
	 */
	@URLValidate
	public String getShopLink() {
		return shopLink;
	}

	/**
	 * @param shopLink the shopLink to set
	 */
	public void setShopLink(String shopLink) {
		this.shopLink = panda.lang.Strings.stripToNull(shopLink);
	}

	/**
	 * @return the description
	 */
	@StringValidate(maxLength=5000)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Pet src) {
		this.id = src.id;
		this.name = src.name;
		this.gender = src.gender;
		this.birthday = src.birthday;
		this.amount = src.amount;
		this.price = src.price;
		this.shopName = src.shopName;
		this.shopTelephone = src.shopTelephone;
		this.shopLink = src.shopLink;
		this.description = src.description;
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
	public Pet clone() {
		Pet copy = new Pet();
		
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

		Pet rhs = (Pet)obj;
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
				.append(GENDER, gender)
				.append(BIRTHDAY, birthday)
				.append(AMOUNT, amount)
				.append(PRICE, price)
				.append(SHOP_NAME, shopName)
				.append(SHOP_TELEPHONE, shopTelephone)
				.append(SHOP_LINK, shopLink)
				.append(DESCRIPTION, description)
				.appendSuper(super.toString())
				.toString();
	}
}

