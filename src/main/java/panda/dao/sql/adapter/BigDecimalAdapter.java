package panda.dao.sql.adapter;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BigDecimal implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class BigDecimalAdapter<T> extends AbstractTypeAdapter<T, BigDecimal> {
	public BigDecimalAdapter(TypeAdapters adapters, Class<T> javaType) {
		super(adapters, javaType, BigDecimal.class);
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
		BigDecimal bigdec = rs.getBigDecimal(columnName);
		return castToJava(bigdec);
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
		BigDecimal bigdec = rs.getBigDecimal(columnIndex);
		return castToJava(bigdec);
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
		BigDecimal bigdec = cs.getBigDecimal(columnIndex);
		return castToJava(bigdec);
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
		BigDecimal bd = castToJdbc(value);
		if (value == null) {
			rs.updateNull(columnName);
		}
		else {
			rs.updateBigDecimal(columnName, bd);
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
		BigDecimal bd = castToJdbc(value);
		if (value == null) {
			rs.updateNull(columnIndex);
		}
		else {
			rs.updateBigDecimal(columnIndex, bd);
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
		BigDecimal bd = castToJdbc(value);
		ps.setBigDecimal(i, bd);
	}
}
