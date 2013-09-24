package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Enum implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
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
	 * @param columnName - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	@SuppressWarnings("unchecked")
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		String s = rs.getString(columnName);
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
	 * @param columnIndex - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	@SuppressWarnings("unchecked")
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		String s = rs.getString(columnIndex);
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
	 * @param columnIndex - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	@SuppressWarnings("unchecked")
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String s = cs.getString(columnIndex);
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
	 * @param columnName - the column name to get
	 * @param value - the value to update
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, String columnName, Object value, String jdbcType)
			throws SQLException {
		if (value == null) {
			rs.updateNull(columnName);
		}
		else {
			rs.updateString(columnName, value.toString());
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
		if (value == null) {
			rs.updateNull(columnIndex);
		}
		else {
			rs.updateString(columnIndex, value.toString());
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
		ps.setString(i, value == null ? null : value.toString());
	}
}
