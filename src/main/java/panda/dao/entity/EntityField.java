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
	private Entity<?> entity;
	private String name;
	private Type type;
	private String column;
	private String comment;
	private String jdbcType;
	private String nativeType;
	private int size;
	private int scale;
	private boolean identity;
	private boolean autoIncrement;
	private int startWith;
	private boolean primaryKey;
	private boolean notNull;
	private boolean unsigned;
	private String defaultValue;
	private boolean readonly;
	
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
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	protected void setIdentity(boolean identity) {
		this.identity = identity;
	}

	/**
	 * @return the autoIncrement
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	/**
	 * @param autoIncrement the autoIncrement to set
	 */
	protected void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	/**
	 * @return the startWith
	 */
	public int getStartWith() {
		return startWith;
	}

	/**
	 * @param startWith the startWith to set
	 */
	protected void setStartWith(int startWith) {
		this.startWith = startWith;
	}

	/**
	 * @return the primaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey the primaryKey to set
	 */
	protected void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * @return the notNull
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * @param notNull the notNull to set
	 */
	protected void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * @return the unsigned
	 */
	public boolean isUnsigned() {
		return unsigned;
	}

	/**
	 * @param unsigned the unsigned to set
	 */
	protected void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
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
	
	/**
	 * @return the readonly
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/**
	 * @param readonly the readonly to set
	 */
	protected void setReadonly(boolean readonly) {
		this.readonly = readonly;
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
				.append("identity", identity)
				.append("primaryKey", primaryKey)
				.append("notNull", notNull)
				.append("unsigned", unsigned)
				.append("defaultValue", defaultValue)
				.append("readonly", readonly)
				.toString();
	}
}
