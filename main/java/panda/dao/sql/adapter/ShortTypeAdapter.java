package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Object implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class ShortTypeAdapter<T> extends AbstractTypeAdapter<T, Short> {
	public ShortTypeAdapter(TypeAdapters adapters, Class<T> toType) {
		super(adapters, toType, Short.class);
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
		short s = rs.getShort(columnName);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(s);
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
		short s = rs.getShort(columnIndex);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(s);
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
		short s = cs.getShort(columnIndex);
		if (cs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(s);
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
		Short s = castToJdbc(value);
		if (s == null) {
			rs.updateNull(columnName);
		}
		else {
			rs.updateShort(columnName, s);
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
		Short s = castToJdbc(value);
		if (s == null) {
			rs.updateNull(columnIndex);
		}
		else {
			rs.updateShort(columnIndex, s);
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
		Short s = castToJdbc(value);
		if (s == null) {
			ps.setNull(i, Types.SMALLINT);
		}
		else {
			ps.setShort(i, s);
		}
	}
}
