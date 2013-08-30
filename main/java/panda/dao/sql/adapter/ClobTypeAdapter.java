package panda.dao.sql.adapter;

import java.io.IOException;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import panda.io.Streams;

/**
 * Clob implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class ClobTypeAdapter<T> extends AbstractTypeAdapter<T, Reader> {
	public ClobTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Reader.class);
	}

	/**
	 * Gets a column from a result set
	 * 
	 * @param rs - the result set
	 * @param columnName - the column name to get
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, String columnName) throws SQLException {
		Clob clob = rs.getClob(columnName);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return castToJava(clob);
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
		Clob clob = rs.getClob(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return castToJava(clob);
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
	public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
		Clob clob = cs.getClob(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		else {
			return castToJava(clob);
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
			try {
				Reader reader = castToJdbc(value);
				long len = Streams.available(reader);
				rs.updateCharacterStream(columnName, reader, len);
			}
			catch (IOException e) {
				throw new SQLException(e);
			}
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
			try {
				Reader reader = castToJdbc(value);
				long len = Streams.available(reader);
				rs.updateCharacterStream(columnIndex, reader, len);
			}
			catch (IOException e) {
				throw new SQLException(e);
			}
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
			ps.setNull(i, Types.CLOB);
		}
		else {
			try {
				Reader reader = castToJdbc(parameter);
				long len = Streams.available(reader);
				ps.setCharacterStream(i, reader, len);
			}
			catch (IOException e) {
				throw new SQLException(e);
			}
		}
	}

}
