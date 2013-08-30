package panda.dao.entity;

import java.lang.reflect.Type;

import panda.lang.Objects;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class EntityField {
	private Entity<?> entity;
	private String name;
	private Type type;
	private String columnName;
	private String columnComment;
	private ColType columnType;
	private String dbType;
	private Integer size;
	private Integer scale;
	private boolean identity;
	private boolean primaryKey;
	private boolean notNull;
	private boolean unsigned;
	private String defaultValue;
	
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
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	protected void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * @return the columnComment
	 */
	public String getColumnComment() {
		return columnComment;
	}

	/**
	 * @param columnComment the columnComment to set
	 */
	protected void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	/**
	 * @return the columnType
	 */
	public ColType getColumnType() {
		return columnType;
	}

	/**
	 * @param columnType the columnType to set
	 */
	protected void setColumnType(ColType columnType) {
		this.columnType = columnType;
	}

	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	protected void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @return the scale
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	protected void setScale(Integer scale) {
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
		return defaultValue != null;
	}
	
	/**
	 * 根据实体的实例对象，获取默认值
	 * 
	 * @param obj
	 *            当前实体的实例对象
	 * 
	 * @return 数据库字段的默认值
	 * 
	 */
	public String getDefaultValue(Object obj) {
		//TODO: translate default value
		return defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("name", name)
				.append("type", type)
				.append("columnName", columnName)
				.append("columnComment", columnComment)
				.append("columnType", columnType)
				.append("dbType", dbType)
				.append("size", size)
				.append("scale", scale)
				.append("identity", identity)
				.append("primaryKey", primaryKey)
				.append("notNull", notNull)
				.append("unsigned", unsigned)
				.append("defaultValue", defaultValue)
				.toString();
	}
}
