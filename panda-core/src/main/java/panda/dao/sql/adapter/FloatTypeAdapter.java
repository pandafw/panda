package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Float implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class FloatTypeAdapter<T> extends AbstractCastTypeAdapter<T, Float> {
	public FloatTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Float.class);
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
		float f = rs.getFloat(column);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(f);
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
		float f = rs.getFloat(column);
		if (rs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(f);
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
		float f = cs.getFloat(column);
		if (cs.wasNull()) {
			return castToJava(null);
		}
		else {
			return castToJava(f);
		}
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
		Float f = castToJdbc(value);
		if (f == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateFloat(column, f);
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
		Float f = castToJdbc(value);
		if (f == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateFloat(column, f);
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
		Float f = castToJdbc(value);
		if (f == null) {
			ps.setNull(i, Types.FLOAT);
		}
		else {
			ps.setFloat(i, f);
		}
	}
}
