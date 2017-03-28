package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 * SQL time implementation of TypeAdapter
 * @param <T> Java Type
 */
public class SqlTimeTypeAdapter<T> extends AbstractCastTypeAdapter<T, Time> {
	public SqlTimeTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Time.class);
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param column - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, String column) throws SQLException {
		Time sqlTime = rs.getTime(column);
		return castToJava(sqlTime);
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, int column) throws SQLException {
		Time sqlTime = rs.getTime(column);
		return castToJava(sqlTime);
	}

	/**
	 * Gets a column from a callable statement
	 * 
	 * @param cs - the statement
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(CallableStatement cs, int column) throws SQLException {
		Time sqlTime = cs.getTime(column);
		return castToJava(sqlTime);
	}

	/**
	 * Update column value to result set
	 * 
	 * @param rs - the result set
	 * @param column - the column name to get
	 * @param value - the value to update
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, String column, T value) throws SQLException {
		Time time = castToJdbc(value);
		if (time == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateTime(column, time);
		}
	}

	/**
	 * Update column value to result set
	 * 
	 * @param rs - the result set
	 * @param column - the column to get (by index)
	 * @param value - the value to update
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, int column, T value) throws SQLException {
		Time time = castToJdbc(value);
		if (time == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateTime(column, time);
		}
	}

	/**
	 * Sets a parameter on a prepared statement
	 * 
	 * @param ps - the prepared statement
	 * @param i - the parameter index
	 * @param value - the parameter value
	 * @throws SQLException if setting the parameter fails
	 */
	public void setParameter(PreparedStatement ps, int i, T value) throws SQLException {
		Time time = castToJdbc(value);
		ps.setTime(i, time);
	}
}
