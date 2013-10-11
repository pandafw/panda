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
public class DoubleTypeAdapter<T> extends AbstractCastTypeAdapter<T, Double> {
	public DoubleTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Double.class);
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
		double d = rs.getDouble(column);
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
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, int column) throws SQLException {
		double d = rs.getDouble(column);
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
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(CallableStatement cs, int column) throws SQLException {
		double d = cs.getDouble(column);
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
	 * @param column - the column name to get
	 * @param value - the value to update
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, String column, Object value) throws SQLException {
		Double d = castToJdbc(value);
		if (d == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateDouble(column, d);
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
		Double d = castToJdbc(value);
		if (d == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateDouble(column, d);
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
		Double d = castToJdbc(value);
		if (d == null) {
			ps.setNull(i, Types.DOUBLE);
		}
		else {
			ps.setDouble(i, d);
		}
	}
}
