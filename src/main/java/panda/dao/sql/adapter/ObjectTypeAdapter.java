package panda.dao.sql.adapter;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Object implementation of TypeAdapter
 * @author yf.frank.wang@gmail.com
 */
public class ObjectTypeAdapter<T, P> extends AbstractTypeAdapter<T, P> {
	public ObjectTypeAdapter(TypeAdapters adapters, Class<T> javaType, Class<P> jdbcType) {
		super(adapters, javaType, jdbcType);
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
		Object object = rs.getObject(columnName);
		return castToJava(object);
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
		Object object = rs.getObject(columnIndex);
		return castToJava(object);
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
		Object object = cs.getObject(columnIndex);
		return castToJava(object);
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
		Object obj = castToJdbc(value);
		if (obj == null) {
			rs.updateNull(columnName);
		}
		else {
			if (obj instanceof Byte) {
				rs.updateByte(columnName, (Byte)obj);
			}
			else if (obj instanceof Short) {
				rs.updateShort(columnName, (Short)obj);
			}
			else if (obj instanceof Integer) {
				rs.updateInt(columnName, (Integer)obj);
			}
			else if (obj instanceof Long) {
				rs.updateLong(columnName, (Long)obj);
			}
			else if (obj instanceof Float) {
				rs.updateFloat(columnName, (Float)obj);
			}
			else if (obj instanceof Double) {
				rs.updateDouble(columnName, (Double)obj);
			}
			else if (obj instanceof BigDecimal) {
				rs.updateBigDecimal(columnName, (BigDecimal)obj);
			}
			else if (obj instanceof Date) {
				rs.updateDate(columnName, (Date)obj);
			}
			else if (obj instanceof Time) {
				rs.updateTime(columnName, (Time)obj);
			}
			else if (obj instanceof Timestamp) {
				rs.updateTimestamp(columnName, (Timestamp)obj);
			}
			else {
				rs.updateObject(columnName, obj);
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
		Object obj = castToJdbc(value);
		if (obj == null) {
			rs.updateNull(columnIndex);
		}
		else {
			if (obj instanceof Byte) {
				rs.updateByte(columnIndex, (Byte)obj);
			}
			else if (obj instanceof Short) {
				rs.updateShort(columnIndex, (Short)obj);
			}
			else if (obj instanceof Integer) {
				rs.updateInt(columnIndex, (Integer)obj);
			}
			else if (obj instanceof Long) {
				rs.updateLong(columnIndex, (Long)obj);
			}
			else if (obj instanceof Float) {
				rs.updateFloat(columnIndex, (Float)obj);
			}
			else if (obj instanceof Double) {
				rs.updateDouble(columnIndex, (Double)obj);
			}
			else if (obj instanceof BigDecimal) {
				rs.updateBigDecimal(columnIndex, (BigDecimal)obj);
			}
			else if (obj instanceof Date) {
				rs.updateDate(columnIndex, (Date)obj);
			}
			else if (obj instanceof Time) {
				rs.updateTime(columnIndex, (Time)obj);
			}
			else if (obj instanceof Timestamp) {
				rs.updateTimestamp(columnIndex, (Timestamp)obj);
			}
			else {
				rs.updateObject(columnIndex, obj);
			}
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
		Object obj = castToJdbc(value);
		if (obj == null) {
			ps.setObject(i, obj);
		}
		else {
			if (obj instanceof Byte) {
				ps.setByte(i, (Byte)obj);
			}
			else if (obj instanceof Short) {
				ps.setShort(i, (Short)obj);
			}
			else if (obj instanceof Integer) {
				ps.setInt(i, (Integer)obj);
			}
			else if (obj instanceof Long) {
				ps.setLong(i, (Long)obj);
			}
			else if (obj instanceof Float) {
				ps.setFloat(i, (Float)obj);
			}
			else if (obj instanceof Double) {
				ps.setDouble(i, (Double)obj);
			}
			else if (obj instanceof BigDecimal) {
				ps.setBigDecimal(i, (BigDecimal)obj);
			}
			else if (obj instanceof Date) {
				ps.setDate(i, (Date)obj);
			}
			else if (obj instanceof Time) {
				ps.setTime(i, (Time)obj);
			}
			else if (obj instanceof Timestamp) {
				ps.setTimestamp(i, (Timestamp)obj);
			}
			else {
				ps.setObject(i, obj);
			}
		}
	}
}
