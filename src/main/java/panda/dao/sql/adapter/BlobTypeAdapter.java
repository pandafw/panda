package panda.dao.sql.adapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import panda.io.Streams;

/**
 * Blob implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class BlobTypeAdapter<T> extends AbstractCastTypeAdapter<T, InputStream> {
	public BlobTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, InputStream.class);
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
		InputStream is;
		try {
			Blob blob = rs.getBlob(column);
			if (rs.wasNull()) {
				return null;
			}
			is = blob.getBinaryStream();
		}
		catch (SQLException e) {
			// patch for sqlite
			byte[] data = rs.getBytes(column);
			if (rs.wasNull()) {
				return null;
			}
			is = new ByteArrayInputStream(data);
		}

		try {
			return castToJava(is);
		}
		finally {
			Streams.safeClose(is);
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
		InputStream is;
		try {
			Blob blob = rs.getBlob(column);
			if (rs.wasNull()) {
				return null;
			}
			is = blob.getBinaryStream();
		}
		catch (SQLException e) {
			// patch for sqlite
			byte[] data = rs.getBytes(column);
			if (rs.wasNull()) {
				return null;
			}
			is = new ByteArrayInputStream(data);
		}

		try {
			return castToJava(is);
		}
		finally {
			Streams.safeClose(is);
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
		InputStream is;
		try {
			Blob blob = cs.getBlob(column);
			if (cs.wasNull()) {
				return null;
			}
			is = blob.getBinaryStream();
		}
		catch (SQLException e) {
			// patch for sqlite
			byte[] data = cs.getBytes(column);
			if (cs.wasNull()) {
				return null;
			}
			is = new ByteArrayInputStream(data);
		}

		try {
			return castToJava(is);
		}
		finally {
			Streams.safeClose(is);
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
		if (value == null) {
			rs.updateNull(column);
		}
		else {
			InputStream is = castToJdbc(value);
			try {
				// use jdbc 4.0 api
				rs.updateBinaryStream(column, is);
				return;
			}
			catch (Throwable e) {
			}

			try {
				int len = (int)Streams.available(is);
				rs.updateBinaryStream(column, is, len);
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
	public void updateResult(ResultSet rs, int column, T value) throws SQLException {
		if (value == null) {
			rs.updateNull(column);
		}
		else {
			InputStream is = castToJdbc(value);
			try {
				// use jdbc 4.0 api
				rs.updateBinaryStream(column, is);
				return;
			}
			catch (Throwable e) {
			}

			try {
				int len = (int)Streams.available(is);
				rs.updateBinaryStream(column, is, len);
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
	public void setParameter(PreparedStatement ps, int i, T value) throws SQLException {
		if (value == null) {
			ps.setNull(i, Types.BLOB);
		}
		else {
			InputStream is = castToJdbc(value);
			try {
				// use jdbc 4.0 api
				ps.setBinaryStream(i, is);
				return;
			}
			catch (Throwable e) {
			}

			try {
				int len = (int)Streams.available(is);
				ps.setBinaryStream(i, is, len);
			}
			catch (IOException e) {
				throw new SQLException(e);
			}
		}
	}
}
