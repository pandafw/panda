package panda.gems.tager.entity;

import java.io.Serializable;
import panda.app.entity.Bean;
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
	@Index(name="NKC", fields={ "name", "kind", "code" }, unique=true)
})
public class Tag extends Bean implements Serializable {

	private static final long serialVersionUID = -1418775623L;

	/**
	 * Constructor
	 */
	public Tag() {
		super();
	}

	/*----------------------------------------------------------------------*
	 * Constants
	 *----------------------------------------------------------------------*/
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String KIND = "kind";
	public static final String CODE = "code";

	public static final String[] _COLUMNS_ = new String[] {
			ID,
			NAME,
			KIND,
			CODE
		};



	/*----------------------------------------------------------------------*
	 * Properties
	 *----------------------------------------------------------------------*/
	@Id(start=1)
	@Comment("id")
	protected Long id;

	@Column(size=100, notNull=true)
	protected String name;

	@Column(size=10)
	protected String kind;

	@Column(size=255, notNull=true)
	protected String code;


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
	 * @return the kind
	 */
	@ConstantValidate(list="%{consts.tagKindMap}")
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
	 * @return the code
	 */
	@StringValidate(maxLength=255)
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = panda.lang.Strings.stripToNull(code);
	}


	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Tag src) {
		this.id = src.id;
		this.name = src.name;
		this.kind = src.kind;
		this.code = src.code;
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
	public Tag clone() {
		Tag copy = new Tag();
		
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

		Tag rhs = (Tag)obj;
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
				.append(KIND, kind)
				.append(CODE, code)
				.appendSuper(super.toString())
				.toString();
	}
}

