package panda.dao.sql.adapter;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import panda.lang.Strings;

/**
 * Boolean implementation of TypeAdapter
 */
public abstract class BoolCharTypeAdapter<T> extends AbstractCastTypeAdapter<T, String> {
	public BoolCharTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, String.class);
	}

	protected char chTrue;
	protected char chFalse;

	protected T convertFromJdbc(String s) throws SQLException {
		if (Strings.isEmpty(s)) {
			return castToJava(null);
		}
		
		char c = s.charAt(0);
		if (c == chTrue) {
			return castToJava(Boolean.TRUE.toString());
		}
		if (c == chFalse) {
			return castToJava(Boolean.FALSE.toString());
		}
		throw new SQLException(
			"Unexpected value [" + s + "] found where " + chTrue + '/' + chFalse + " was expected.");
	}

	protected String convertFromJava(Object v) throws SQLException {
		if (v == null) {
			return null;
		}
		
		Boolean b;
		if (v instanceof Boolean) {
			b = (Boolean)v;
			return b ? String.valueOf(chTrue) : String.valueOf(chFalse);
		}
		
		String s = v.toString();
		if (Strings.isEmpty(s)) {
			return null;
		}

		char c = s.charAt(0);
		if (c == chTrue || c == chFalse) {
			return s;
		}
		
		throw new SQLException(
			"Unexpected value [" + s + "] found where " + chTrue + '/' + chFalse + " was expected.");
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
		String s = rs.getString(column);
		return convertFromJdbc(s);
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
		return convertFromJdbc(s);
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
		String s = cs.getString(column);
		return convertFromJdbc(s);
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
		String s = convertFromJava(value);
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
		String s = convertFromJava(value);
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
		String s = convertFromJava(value);
		ps.setString(i, s);
	}
	
	//--------------------------------------------------------------------------------------
	public static class YesNoBoolCharTypeAdapter<T> extends BoolCharTypeAdapter<T> {
		public YesNoBoolCharTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
			super(adapters, javaType);
			chTrue = 'Y';
			chFalse = 'N';
		}
	}
	
	public static class ZeroOneBoolCharTypeAdapter<T> extends BoolCharTypeAdapter<T> {
		public ZeroOneBoolCharTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
			super(adapters, javaType);
			chTrue = '1';
			chFalse = '0';
		}
	}
}
