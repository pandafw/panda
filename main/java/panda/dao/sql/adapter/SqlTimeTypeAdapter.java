package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 * SQL time implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class SqlTimeTypeAdapter<T> extends AbstractTypeAdapter<T, Time> {
	public SqlTimeTypeAdapter(TypeAdapters adapters, Class<T> toType) {
		super(adapters, toType, Time.class);
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
		Object sqlTime = rs.getTime(columnName);
		return castToJava(sqlTime);
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
		Object sqlTime = rs.getTime(columnIndex);
		return castToJava(sqlTime);
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
		Object sqlTime = cs.getTime(columnIndex);
		return castToJava(sqlTime);
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
		Time time = castToJdbc(value);
		if (time == null) {
			rs.updateNull(columnName);
		}
		else {
			rs.updateTime(columnName, time);
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
		Time time = castToJdbc(value);
		if (time == null) {
			rs.updateNull(columnIndex);
		}
		else {
			rs.updateTime(columnIndex, time);
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
		Time time = castToJdbc(value);
		ps.setTime(i, time);
	}
}
