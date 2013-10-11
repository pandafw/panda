package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * SQL timestamp implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class SqlTimestampTypeAdapter<T> extends AbstractCastTypeAdapter<T, Timestamp> {
	public SqlTimestampTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Timestamp.class);
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
		Object sqlTimestamp = rs.getTimestamp(column);
		return castToJava(sqlTimestamp);
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
		Object sqlTimestamp = rs.getTimestamp(column);
		return castToJava(sqlTimestamp);
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
		Object sqlTimestamp = cs.getTimestamp(column);
		return castToJava(sqlTimestamp);
	}

	/**
	 * Update column value to result set
	 * 
	 * @param rs - the result set
	 * @param column - the column name to get
	 * @param value - the value to update
	 * @param value - the value to update
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, String column, Object value) throws SQLException {
		Timestamp ts = castToJdbc(value);
		if (ts == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateTimestamp(column, ts);
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
	public void updateResult(ResultSet rs, int column, Object value) throws SQLException {
		Timestamp ts = castToJdbc(value);
		if (ts == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateTimestamp(column, ts);
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
	public void setParameter(PreparedStatement ps, int i, Object value) throws SQLException {
		Timestamp ts = castToJdbc(value);
		ps.setTimestamp(i, ts);
	}
}
