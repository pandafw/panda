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
public class ObjectTypeAdapter<T, P> extends AbstractCastTypeAdapter<T, P> {
	public ObjectTypeAdapter(TypeAdapters adapters, Class<T> javaType, Class<P> jdbcType) {
		super(adapters, javaType, jdbcType);
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
		Object object = rs.getObject(column);
		return castToJava(object);
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
		Object object = rs.getObject(column);
		return castToJava(object);
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
		Object object = cs.getObject(column);
		return castToJava(object);
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
		Object obj = castToJdbc(value);
		if (obj == null) {
			rs.updateNull(column);
		}
		else {
			if (obj instanceof Byte) {
				rs.updateByte(column, (Byte)obj);
			}
			else if (obj instanceof Short) {
				rs.updateShort(column, (Short)obj);
			}
			else if (obj instanceof Integer) {
				rs.updateInt(column, (Integer)obj);
			}
			else if (obj instanceof Long) {
				rs.updateLong(column, (Long)obj);
			}
			else if (obj instanceof Float) {
				rs.updateFloat(column, (Float)obj);
			}
			else if (obj instanceof Double) {
				rs.updateDouble(column, (Double)obj);
			}
			else if (obj instanceof BigDecimal) {
				rs.updateBigDecimal(column, (BigDecimal)obj);
			}
			else if (obj instanceof Date) {
				rs.updateDate(column, (Date)obj);
			}
			else if (obj instanceof Time) {
				rs.updateTime(column, (Time)obj);
			}
			else if (obj instanceof Timestamp) {
				rs.updateTimestamp(column, (Timestamp)obj);
			}
			else {
				rs.updateObject(column, obj);
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
		Object obj = castToJdbc(value);
		if (obj == null) {
			rs.updateNull(column);
		}
		else {
			if (obj instanceof Byte) {
				rs.updateByte(column, (Byte)obj);
			}
			else if (obj instanceof Short) {
				rs.updateShort(column, (Short)obj);
			}
			else if (obj instanceof Integer) {
				rs.updateInt(column, (Integer)obj);
			}
			else if (obj instanceof Long) {
				rs.updateLong(column, (Long)obj);
			}
			else if (obj instanceof Float) {
				rs.updateFloat(column, (Float)obj);
			}
			else if (obj instanceof Double) {
				rs.updateDouble(column, (Double)obj);
			}
			else if (obj instanceof BigDecimal) {
				rs.updateBigDecimal(column, (BigDecimal)obj);
			}
			else if (obj instanceof Date) {
				rs.updateDate(column, (Date)obj);
			}
			else if (obj instanceof Time) {
				rs.updateTime(column, (Time)obj);
			}
			else if (obj instanceof Timestamp) {
				rs.updateTimestamp(column, (Timestamp)obj);
			}
			else {
				rs.updateObject(column, obj);
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
	public void setParameter(PreparedStatement ps, int i, Object value) throws SQLException {
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
