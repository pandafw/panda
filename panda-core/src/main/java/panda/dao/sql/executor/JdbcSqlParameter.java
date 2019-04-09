package panda.dao.sql.executor;

import panda.dao.DaoTypes;
import panda.dao.sql.adapter.TypeAdapter;
import panda.dao.sql.adapter.TypeAdapters;
import panda.lang.Objects;

/**
 */
public class JdbcSqlParameter {
	public static enum Mode {
		IN,
		OUT,
		INOUT
	}
	
	private final String name;
	private final String jdbcType;
	private final Integer sqlType;
	private final Integer scale;
	private final Object value;
	private final Mode mode;
	@SuppressWarnings("rawtypes")
	private final TypeAdapter typeAdapter;

	/**
	 * Constructor.
	 * @param name name
	 * @param value value
	 */
	public JdbcSqlParameter(String name, Object value) {
		this(name, value, null, null, null);
	}

	/**
	 * Constructor.
	 * @param name name
	 * @param value value
	 * @param jdbcType jdbc type
	 */
	public JdbcSqlParameter(String name, Object value, String jdbcType) {
		this(name, value, jdbcType, null, null);
	}

	/**
	 * Constructor.
	 * @param name name
	 * @param value value
	 * @param jdbcType jdbc type
	 * @param mode mode
	 */
	public JdbcSqlParameter(String name, Object value, String jdbcType, String mode) {
		this(name, value, jdbcType, null, mode);
	}

	/**
	 * Constructor.
	 * @param name name
	 * @param value value
	 * @param jdbcType jdbc type
	 * @param scale scale
	 * @param mode mode
	 */
	public JdbcSqlParameter(String name, Object value, String jdbcType, Integer scale, String mode) {
		this(name, value, jdbcType, scale, mode, TypeAdapters.i());
	}

	/**
	 * Constructor.
	 * @param name name
	 * @param value value
	 * @param typeAdapters typeAdapters
	 */
	public JdbcSqlParameter(String name, Object value, TypeAdapters typeAdapters) {
		this(name, value, null, null, null, typeAdapters);
	}
	
	/**
	 * Constructor.
	 * @param name name
	 * @param value value
	 * @param jdbcType jdbc type
	 * @param scale scale
	 * @param mode mode
	 * @param typeAdapters typeAdapters
	 */
	public JdbcSqlParameter(String name, Object value, String jdbcType, Integer scale, String mode, TypeAdapters typeAdapters) {
		this.name = name;
		this.value = value;

		this.jdbcType = jdbcType;
		if (jdbcType != null) {
			this.sqlType = DaoTypes.getType(jdbcType);
			if (this.sqlType == null) {
				throw new IllegalArgumentException("Illegal parameter '" + name + "': unknown JDBC type [" + jdbcType + "].");
			}
		}
		else {
			this.sqlType = null;
		}
		this.scale = scale;
		
		if (mode != null) {
			this.mode = Mode.valueOf(mode);
			if (this.mode == null) {
				throw new IllegalArgumentException("Illegal parameter '" + name + "': unknown mode [" + mode + "], it must be IN/OUT/INOUT.");
			}
		}
		else {
			this.mode = Mode.IN;
		}
		
		Class<?> parameterValueClass = value == null ? null : value.getClass(); 
		this.typeAdapter = typeAdapters.getTypeAdapter(parameterValueClass, jdbcType);
		if (this.typeAdapter == null) {
			throw new IllegalArgumentException("Illegal parameter '" + name + "': unknown TypeAdapter [" + parameterValueClass + ", " + jdbcType + "].");
		}
	}

	/**
	 * isInputAllowed
	 * @return true if the parameter allowed to input
	 */
	public boolean isInputAllowed() {
		return !Mode.OUT.equals(mode);
	}
	
	/**
	 * isOutputAllowed
	 * @return true if the parameter allowed to output
	 */
	public boolean isOutputAllowed() {
		return (Mode.OUT.equals(mode) || Mode.INOUT.equals(mode));
	}
	
	/**
	 * @return the TypeAdapter of the parameter
	 */
	@SuppressWarnings("rawtypes")
	public TypeAdapter getTypeAdapter() {
		return typeAdapter;
	}

	/**
	 * @return sqlType
	 */
	public Integer getSqlType() {
		return sqlType;
	}

	/**
	 * @return jdbcType
	 */
	public String getJdbcType() {
		return jdbcType;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * @return scale
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("value", value)
				.append("jdbcType", jdbcType)
				.append("scale", scale)
				.append("mode", mode)
				.toString();
	}

	/**
	 * @return a hash code value for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, jdbcType, sqlType, scale, mode, value, typeAdapter);
	}

	/**
	 * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
	 *         otherwise.
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

		final JdbcSqlParameter rhs = (JdbcSqlParameter) obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(jdbcType, rhs.jdbcType)
				.append(sqlType, rhs.sqlType)
				.append(scale, rhs.scale)
				.append(mode, rhs.mode)
				.append(value, rhs.value)
				.append(typeAdapter, rhs.typeAdapter)
				.isEquals();
	}


}
