package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Enum implementation of TypeAdapter
 */
public class EnumTypeAdapter implements TypeAdapter {

	private Class type;

	/**
	 * Constructor
	 * @param type type
	 */
	public EnumTypeAdapter(Class type) {
		this.type = type;
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param column - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	@SuppressWarnings("unchecked")
	public Object getResult(ResultSet rs, String column) throws SQLException {
		String s = rs.getString(column);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return Enum.valueOf(type, s);
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
	@SuppressWarnings("unchecked")
	public Object getResult(ResultSet rs, int column) throws SQLException {
		String s = rs.getString(column);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return Enum.valueOf(type, s);
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
	@SuppressWarnings("unchecked")
	public Object getResult(CallableStatement cs, int column) throws SQLException {
		String s = cs.getString(column);
		if (cs.wasNull()) {
			return null;
		}
		else {
			return Enum.valueOf(type, s);
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
		if (value == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateString(column, value.toString());
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
		if (value == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateString(column, value.toString());
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
		ps.setString(i, value == null ? null : value.toString());
	}
}
