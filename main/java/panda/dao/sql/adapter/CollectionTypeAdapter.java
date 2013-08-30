package panda.dao.sql.adapter;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import panda.bind.json.Jsons;
import panda.lang.Strings;
import panda.lang.Types;

/**
 * base collection implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class CollectionTypeAdapter<T> implements TypeAdapter<T> {
	private Type type;
	
	public CollectionTypeAdapter(Type type) {
		if (!Types.isAssignable(type, Collection.class) && !Types.isAssignable(type, Map.class)) {
			throw new IllegalArgumentException("The argument is not a collection/map type: " + type);
		}
		this.type = Types.getDefaultImplType(type);
	}
	
	protected T toCollection(String value) throws SQLException {
		if (Strings.isEmpty(value)) {
			try {
				return Types.newInstance(type);
			}
			catch (InstantiationException e) {
				throw new SQLException(e);
			}
			catch (IllegalAccessException e) {
				throw new SQLException(e);
			}
		}
		else {
			try {
				return Jsons.fromJson(value, type);
			}
			catch (Exception e) {
				throw new SQLException(e);
			}
		}
	}

	protected String toString(Object v) throws SQLException {
		try {
			return Jsons.toJson(v);
		}
		catch (Exception e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param columnName - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, String columnName)
			throws SQLException {
		String s = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return toCollection(s);
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
		String s = rs.getString(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return toCollection(s);
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
	public T getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String s = cs.getString(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		else {
			return toCollection(s);
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
			rs.updateString(columnName, toString(value));
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
			rs.updateString(columnIndex, toString(value));
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
		String val = null;
		if (parameter != null) {
			val = toString(parameter);
		}
		ps.setString(i, val);
	}
}
