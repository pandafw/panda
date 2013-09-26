package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Long implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class LongTypeAdapter<T> extends AbstractTypeAdapter<T, Long> {
	public LongTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Long.class);
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param columnName - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, String columnName) throws SQLException {
		long l = rs.getLong(columnName);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(l);
		}
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param columnIndex - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, int columnIndex) throws SQLException {
		long l = rs.getLong(columnIndex);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(l);
		}
	}

	/**
	 * Gets a column from a callable statement
	 * 
	 * @param cs - the statement
	 * @param columnIndex - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
		long l = cs.getLong(columnIndex);
		if (cs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(l);
		}
	}

	/**
	 * Update column value to result set
	 * 
	 * @param rs - the result set
	 * @param columnName - the column name to get
	 * @param value - the value to update
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, String columnName, Object value, String jdbcType)
			throws SQLException {
		Long l = castToJdbc(value);
		if (l == null) {
			rs.updateNull(columnName);
		}
		else {
			rs.updateLong(columnName, l);
		}
	}

	/**
	 * Update column value to result set
	 * 
	 * @param rs - the result set
	 * @param columnIndex - the column to get (by index)
	 * @param value - the value to update
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, int columnIndex, Object value, String jdbcType)
			throws SQLException {
		Long l = castToJdbc(value);
		if (l == null) {
			rs.updateNull(columnIndex);
		}
		else {
			rs.updateLong(columnIndex, l);
		}
	}

	/**
	 * Sets a parameter on a prepared statement
	 * 
	 * @param ps - the prepared statement
	 * @param i - the parameter index
	 * @param value - the parameter value
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if setting the parameter fails
	 */
	public void setParameter(PreparedStatement ps, int i, Object value, String jdbcType)
			throws SQLException {
		Long l = castToJdbc(value);
		if (l == null) {
			ps.setNull(i, Types.BIGINT);
		}
		else {
			ps.setLong(i, l);
		}
	}
}
