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
public class ClobTypeAdapter<T> extends AbstractCastTypeAdapter<T, Reader> {
	public ClobTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, Reader.class);
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
		Clob clob = rs.getClob(column);
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
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(ResultSet rs, int column) throws SQLException {
		Clob clob = rs.getClob(column);
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
	 * @param column - the column to get (by index)
	 * @return - the column value
	 * @throws SQLException if getting the value fails
	 */
	public T getResult(CallableStatement cs, int column) throws SQLException {
		Clob clob = cs.getClob(column);
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
	 * @param column - the column name to get
	 * @param value - the value to update
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, String column, Object value) throws SQLException {
		if (value == null) {
			rs.updateNull(column);
		}
		else {
			Reader reader = castToJdbc(value);
			try {
				// use jdbc 4.0 api
				rs.updateCharacterStream(column, reader);
				return;
			}
			catch (Throwable e) {
			}
			
			try {
				int len = (int)Streams.available(reader);
				rs.updateCharacterStream(column, reader, len);
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
	 * @param column - the column to get (by index)
	 * @param value - the value to update
	 * @throws SQLException if getting the value fails
	 */
	public void updateResult(ResultSet rs, int column, Object value) throws SQLException {
		if (value == null) {
			rs.updateNull(column);
		}
		else {
			Reader reader = castToJdbc(value);
			try {
				// use jdbc 4.0 api
				rs.updateCharacterStream(column, reader);
				return;
			}
			catch (Throwable e) {
			}

			try {
				int len = (int)Streams.available(reader);
				rs.updateCharacterStream(column, reader, len);
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
	 * @param value - the parameter value
	 * @throws SQLException if setting the parameter fails
	 */
	public void setParameter(PreparedStatement ps, int i, Object value)
			throws SQLException {
		if (value == null) {
			ps.setNull(i, Types.CLOB);
		}
		else {
			Reader reader = castToJdbc(value);
			try {
				// use jdbc 4.0 api
				ps.setCharacterStream(i, reader);
				return;
			}
			catch (Throwable e) {
			}

			try {
				int len = (int)Streams.available(reader);
				ps.setCharacterStream(i, reader, len);
			}
			catch (IOException e) {
				throw new SQLException(e);
			}
		}
	}

}
