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
		if (value == null) {
			return null;
		}
		else if (Strings.isEmpty(value)) {
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
		if (v == null) {
			return null;
		}
		
		try {
			return Jsons.toJson(v);
		}
		catch (Exception e) {
			throw new SQLException("Failed to serialize to json", e);
		}
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param column - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, String column)
			throws SQLException {
		String s = rs.getString(column);
		return toCollection(s);
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
		String s = rs.getString(column);
		return toCollection(s);
	}

	/**
	 * Gets a column from a callable statement
	 * 
	 * @param cs - the statement
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(CallableStatement cs, int column)
			throws SQLException {
		String s = cs.getString(column);
		return toCollection(s);
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
		String s = toString(value);
		if (s == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateString(column, s);
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
		String s = toString(value);
		if (s == null) {
			rs.updateNull(column);
		}
		else {
			rs.updateString(column, s);
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
		String s = toString(value);
		ps.setString(i, s);
	}
}
