package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of TypeAdapter for dealing with unknown types
 * @author yf.frank.wang@gmail.com
 */
public class UnknownTypeAdapter implements TypeAdapter {

	private TypeAdapters adapters;

	/**
	 * Constructor to create via a handlers
	 * 
	 * @param adapters - the adapters to associate this with
	 */
	public UnknownTypeAdapter(TypeAdapters adapters) {
		this.adapters = adapters;
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param columnName - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		Object object = rs.getObject(columnName);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return object;
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
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		Object object = rs.getObject(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return object;
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
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		Object object = cs.getObject(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		else {
			return object;
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
			TypeAdapter adapter = adapters.getTypeAdapter(value.getClass(), jdbcType);
			adapter.updateResult(rs, columnName, value, jdbcType);
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
			TypeAdapter adapter = adapters.getTypeAdapter(value.getClass(), jdbcType);
			adapter.updateResult(rs, columnIndex, value, jdbcType);
		}
	}

	/**
	 * Sets a parameter on a prepared statement
	 * 
	 * @param ps - the prepared statement
	 * @param i - the parameter index
	 * @param parameter - the parameter value
	 * @param jdbcType - the JDBC type of the parameter
	 * @throws SQLException if setting the parameter fails
	 */
	public void setParameter(PreparedStatement ps, int i, Object parameter, String jdbcType)
			throws SQLException {
		if (parameter == null) {
			ps.setObject(i, null);
		}
		else {
			TypeAdapter adapter = adapters.getTypeAdapter(parameter.getClass(), jdbcType);
			adapter.setParameter(ps, i, parameter, jdbcType);
		}
	}
}
