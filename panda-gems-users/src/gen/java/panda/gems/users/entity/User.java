package panda.gems.users.entity;

import java.io.Serializable;
import panda.app.auth.IRole;
import panda.app.auth.IUser;
import panda.app.entity.SCUBean;
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
import panda.mvc.annotation.validate.EmailValidate;
import panda.mvc.annotation.validate.RegexValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.validator.Validators;

@Indexes({
	@Index(name="EMAIL", fields={ "email" }, unique=true)
})
@Joins({
	@Join(name="CU", target=User.class, keys="createdBy", refs="id"),
	@Join(name="UU", target=User.class, keys="updatedBy", refs="id")
})
public class User extends SCUBean implements Serializable, IUser, IRole {

	private static final long serialVersionUID = 602515462L;

	/**
	 * Constructor
	 */
	public User() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String ROLE = "role";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			NAME,
			EMAIL,
			PASSWORD,
			ROLE
		};

	public static final String[] _JOINS_ = new String[] {
			CREATED_BY_NAME,
			UPDATED_BY_NAME
		};

	public static final String _JOIN_CU_ = "CU";
	public static final String _JOIN_UU_ = "UU";

	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1001)
	@Comment("UID")
	protected Long id;

	@Column(size=50, notNull=true)
	@Comment("Name")
	protected String name;

	@Column(size=100, notNull=true)
	@Comment("Email")
	protected String email;

	@Column(size=64, notNull=true)
	@Comment("Password")
	protected String password;

	@Column(type=DaoTypes.VARCHAR, size=5)
	@Comment("Role")
	protected String role;


	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the createdByName
	 */
	@Override
	@JoinColumn(name="CU", field="name")
	public String getCreatedByName() {
		return super.getCreatedByName();
	}

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
	 * @return the email
	 */
	@StringValidate(maxLength=100)
	@EmailValidate
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = panda.lang.Strings.stripToLowerNull(email);
	}

	/**
	 * @return the password
	 */
	@StringValidate(minLength=6, maxLength=16)
	@RegexValidate(regex="#(regex-password)", msgId=Validators.MSGID_PASSWORD)
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = panda.lang.Strings.stripToNull(password);
	}

	/**
	 * @return the role
	 */
	@ConstantValidate(list="%{consts.authRoleMap}")
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = panda.lang.Strings.stripToNull(role);
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(User src) {
		this.id = src.id;
		this.name = src.name;
		this.email = src.email;
		this.password = src.password;
		this.role = src.role;
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
	public User clone() {
		User copy = new User();
		
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

		User rhs = (User)obj;
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
				.append(EMAIL, email)
				.append(PASSWORD, password)
				.append(ROLE, role)
				.appendSuper(super.toString())
				.toString();
	}
}

