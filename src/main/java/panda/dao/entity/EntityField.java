package panda.dao.entity;

import java.lang.reflect.Type;

import panda.lang.Objects;
import panda.lang.Strings;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class EntityField {
	private static final int PRIMARY_KEY = 0x0001;
	private static final int NOT_NULL = 0x0002;
	private static final int UNSIGNED = 0x0004;
	private static final int READONLY = 0x0008;
	private static final int NUMBER_IDENTITY = 0x0010;
	private static final int STRING_IDENTITY = 0x0020;
	private static final int IDENTITY = NUMBER_IDENTITY | STRING_IDENTITY;
	private static final int AUTO_GENERATE = 0x0040;
	private static final int AUTO_INCREMENT = NUMBER_IDENTITY | AUTO_GENERATE;

	private Entity<?> entity;
	private String name;
	private Type type;
	private String column;
	private String comment;
	private String jdbcType;
	private String nativeType;
	private int flag;
	private int size;
	private int scale;
	private int idStartWith;
	private String defaultValue;
	private String joinName;
	private String joinField;
	
	/**
	 * constructor
	 */
	protected EntityField() {
		super();
	}

	/**
	 * @return the entity
	 */
	public Entity<?> getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	protected void setEntity(Entity<?> entity) {
		this.entity = entity;
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
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	protected void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	protected void setColumn(String column) {
		this.column = column;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	protected void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the jdbcType
	 */
	public String getJdbcType() {
		return jdbcType;
	}

	/**
	 * @param jdbcType the jdbcType to set
	 */
	protected void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	/**
	 * @return the nativeType
	 */
	public String getNativeType() {
		return nativeType;
	}

	/**
	 * @param nativeType the nativeType to set
	 */
	public void setNativeType(String nativeType) {
		this.nativeType = nativeType;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	protected void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	protected void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @return the identity
	 */
	public boolean isIdentity() {
		return (flag & IDENTITY) != 0;
	}

	/**
	 * @return the identity
	 */
	public boolean isNumberIdentity() {
		return (flag & NUMBER_IDENTITY) != 0;
	}

	/**
	 * @param identity the identity to set
	 */
	protected void setNumberIdentity(boolean identity) {
		if (identity) {
			flag |= NUMBER_IDENTITY;
		}
		else {
			flag &= ~NUMBER_IDENTITY;
		}
	}

	/**
	 * @return the identity
	 */
	public boolean isStringIdentity() {
		return (flag & STRING_IDENTITY) != 0;
	}

	/**
	 * @param identity the identity to set
	 */
	protected void setStringIdentity(boolean identity) {
		if (identity) {
			flag |= STRING_IDENTITY;
		}
		else {
			flag &= ~STRING_IDENTITY;
		}
	}

	/**
	 * @return the autoIncrement
	 */
	public boolean isAutoIncrement() {
		return (flag & AUTO_INCREMENT) != 0;
	}

	/**
	 * @return the autoGenerate
	 */
	public boolean isAutoGenerate() {
		return (flag & AUTO_GENERATE) != 0;
	}

	/**
	 * @param autoGenerate the autoGenerate to set
	 */
	protected void setAutoGenerate(boolean autoGenerate) {
		if (autoGenerate) {
			flag |= AUTO_GENERATE;
		}
		else {
			flag &= ~AUTO_GENERATE;
		}
	}

	/**
	 * @return the idStartWith
	 */
	public int getIdStartWith() {
		return idStartWith;
	}

	/**
	 * @param idStartWith the idStartWith to set
	 */
	protected void setIdStartWith(int idStartWith) {
		this.idStartWith = idStartWith;
	}

	/**
	 * @return the primaryKey
	 */
	public boolean isPrimaryKey() {
		return (flag & PRIMARY_KEY) != 0;
	}

	/**
	 * @param primaryKey the primaryKey to set
	 */
	protected void setPrimaryKey(boolean primaryKey) {
		if (primaryKey) {
			flag |= PRIMARY_KEY;
		}
		else {
			flag &= ~PRIMARY_KEY;
		}
	}

	/**
	 * @return the notNull
	 */
	public boolean isNotNull() {
		return (flag & NOT_NULL) != 0;
	}

	/**
	 * @param notNull the notNull to set
	 */
	protected void setNotNull(boolean notNull) {
		if (notNull) {
			flag |= NOT_NULL;
		}
		else {
			flag &= ~NOT_NULL;
		}
	}

	/**
	 * @return the unsigned
	 */
	public boolean isUnsigned() {
		return (flag & UNSIGNED) != 0;
	}

	/**
	 * @param unsigned the unsigned to set
	 */
	protected void setUnsigned(boolean unsigned) {
		if (unsigned) {
			flag |= UNSIGNED;
		}
		else {
			flag &= ~UNSIGNED;
		}
	}
	
	/**
	 * @return the readonly
	 */
	public boolean isReadonly() {
		return (flag & READONLY) != 0;
	}

	/**
	 * @param readonly the readonly to set
	 */
	protected void setReadonly(boolean readonly) {
		if (readonly) {
			flag |= READONLY;
		}
		else {
			flag &= ~READONLY;
		}
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	protected void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean hasDefaultValue() {
		return Strings.isNotBlank(defaultValue);
	}
	public boolean isJoinField() {
		return Strings.isNotEmpty(joinName);
	}
	
	/**
	 * @return the joinName
	 */
	public String getJoinName() {
		return joinName;
	}

	/**
	 * @param joinName the joinName to set
	 */
	protected void setJoinName(String joinName) {
		this.joinName = joinName;
	}

	/**
	 * @return the joinField
	 */
	public String getJoinField() {
		return joinField;
	}

	/**
	 * @param joinField the joinField to set
	 */
	protected void setJoinField(String joinField) {
		this.joinField = joinField;
	}

	/**
	 * get field value of data
	 * @param data POJO
	 * @return field value
	 */
	public Object getValue(Object data) {
		return entity.getFieldValue(data, name);
	}

	/**
	 * get field value of data
	 * @param data POJO
	 * @return field value
	 */
	public boolean setValue(Object data, Object value) {
		return entity.setFieldValue(data, name, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("name", name)
				.append("type", type)
				.append("column", column)
				.append("comment", comment)
				.append("jdbcType", jdbcType)
				.append("nativeType", nativeType)
				.append("size", size)
				.append("scale", scale)
				.append("identity", isIdentity())
				.append("primaryKey", isPrimaryKey())
				.append("notNull", isNotNull())
				.append("unsigned", isUnsigned())
				.append("readonly", isReadonly())
				.append("defaultValue", defaultValue)
				.append("joinName", joinName)
				.append("joinField", joinField)
				.toString();
	}
}
