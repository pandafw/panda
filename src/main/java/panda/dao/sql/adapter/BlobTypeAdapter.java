package panda.dao.sql.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Blob implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class BlobTypeAdapter<T> extends AbstractTypeAdapter<T, InputStream> {
	public BlobTypeAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, InputStream.class);
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
		Blob blob = rs.getBlob(columnName);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return castToJava(blob);
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
		Blob blob = rs.getBlob(columnIndex);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return castToJava(blob);
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
		Blob blob = cs.getBlob(columnIndex);
		if (cs.wasNull()) {
			return null;
		}
		else {
			return castToJava(blob);
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
			InputStream is = castToJdbc(value);
			try {
				rs.updateBinaryStream(columnName, is, is.available());
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
			InputStream is = castToJdbc(value);
			try {
				// NOTE: some InputStream does not return total available size
				rs.updateBinaryStream(columnIndex, is, is.available());
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
			ps.setNull(i, Types.BLOB);
		}
		else {
			InputStream is = castToJdbc(parameter);
			ps.setBinaryStream(i, is);
		}
	}
}
