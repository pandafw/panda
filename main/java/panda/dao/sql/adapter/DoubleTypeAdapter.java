package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Double implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class DoubleTypeAdapter<T> extends AbstractTypeAdapter<T, Double> {
	public DoubleTypeAdapter(TypeAdapters adapters, Class<T> toType) {
		super(adapters, toType, Double.class);
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
		double d = rs.getDouble(columnName);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(d);
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
		double d = rs.getDouble(columnIndex);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(d);
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
		double d = cs.getDouble(columnIndex);
		if (cs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(d);
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
		Double d = castToJdbc(value);
		if (d == null) {
			rs.updateNull(columnName);
		}
		else {
			rs.updateDouble(columnName, d);
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
		Double d = castToJdbc(value);
		if (d == null) {
			rs.updateNull(columnIndex);
		}
		else {
			rs.updateDouble(columnIndex, d);
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
		Double d = castToJdbc(value);
		if (d == null) {
			ps.setNull(i, Types.DOUBLE);
		}
		else {
			ps.setDouble(i, d);
		}
	}
}
