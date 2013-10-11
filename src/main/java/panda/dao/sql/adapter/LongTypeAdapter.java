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
public class LongTypeAdapter<T> extends AbstractCastTypeAdapter<T, Long> {
	public LongTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Long.class);
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
		long l = rs.getLong(column);
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
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, int column) throws SQLException {
		long l = rs.getLong(column);
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
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(CallableStatement cs, int column) throws SQLException {
		long l = cs.getLong(column);
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
	 * @param column - the column name to get
	 * @param value - the value to update
	 * @param value - the value to update
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, String column, Object value) throws SQLException {
		Long l = castToJdbc(value);
		if (l == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateLong(column, l);
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
		Long l = castToJdbc(value);
		if (l == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateLong(column, l);
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
		Long l = castToJdbc(value);
		if (l == null) {
			ps.setNull(i, Types.BIGINT);
		}
		else {
			ps.setLong(i, l);
		}
	}
}
