package panda.dao.sql.executor;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.sql.JdbcTypes;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.adapter.TypeAdapter;
import panda.lang.Types;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleSqlResultSet extends AbstractSqlResultSet {
	/**
	 * ResultColumn
	 */
	private class ResultColumn {
		protected int columnIndex;
		protected String columnLabel;
		protected int columnType;
		protected String jdbcType;
		protected String propertyName;
		protected Type propertyType;
		protected TypeAdapter typeAdapter;
	}

	private List<ResultColumn> resultColumns;

	private BeanHandler beanHandler;
	private Type beanType;

	/**
	 * Constructor
	 * @param executor the sql executor
	 * @param resultSet the Result
	 */
	protected SimpleSqlResultSet(SqlExecutor executor, ResultSet resultSet) {
		super(executor, resultSet);
	}

	// ---------------------------------------------------------------------
	// Get/Update
	// ---------------------------------------------------------------------
	private BeanHandler getBeanHandler(Type type) {
		if (!type.equals(beanType)) {
			beanType = type;
			beanHandler = Beans.me().getBeanHandler(type);
		}
		return beanHandler;
	}

	@SuppressWarnings("unchecked")
	private List<ResultColumn> getResultColumnList(ResultSet resultSet, Object resultObject,
			BeanHandler beanHandler) throws SQLException {
		if (resultColumns == null) {
			resultColumns = new ArrayList<ResultColumn>();

			ResultSetMetaData meta = resultSet.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				ResultColumn rc = new ResultColumn();
				rc.columnIndex = i;
				rc.columnLabel = meta.getColumnLabel(i);
				rc.columnType = meta.getColumnType(i);
				rc.jdbcType = JdbcTypes.getType(rc.columnType);
				rc.propertyName = SqlNamings.columnLabel2JavaName(rc.columnLabel);
				rc.propertyType = beanHandler.getBeanType(resultObject, rc.propertyName);
				if (rc.propertyType == null) {
					throw new SQLException("Unknown Type for " + rc.propertyName);
				}
				rc.typeAdapter = executor.getTypeAdapters().getTypeAdapter(Types.getRawType(rc.propertyType), rc.jdbcType);
				if (rc.typeAdapter == null) {
					throw new SQLException("Unknown TypeAdapter for " + rc.propertyName + "["
							+ rc.propertyType + " <-> " + rc.jdbcType + "].");
				}
				resultColumns.add(rc);
			}
		}
		return resultColumns;
	}

	/**
	 * returns data to populate a single object instance.
	 * 
	 * @param <T> The type of result object 
	 * @param resultClass The class of result object
	 * @return The single result object populated with the result set data, or null if no result was
	 *         found
	 * @throws SQLException If an SQL error occurs.
	 */
	public <T> T getResult(Class<T> resultClass) throws SQLException {
		BeanHandler beanHandler = getBeanHandler(resultClass);
		return getResult(beanHandler, (T)null);
	}

	/**
	 * returns data to populate a single object instance.
	 * 
	 * @param <T> The type of result object 
	 * @param resultObject The result object instance that should be populated with result data.
	 * @return The single result object as supplied by the resultObject parameter, populated with
	 *         the result set data, or null if no result was found
	 * @throws SQLException If an SQL error occurs.
	 */
	public <T> T getResult(T resultObject) throws SQLException {
		BeanHandler beanHandler = getBeanHandler(resultObject.getClass());
		return getResult(beanHandler, resultObject);
	}


	/**
	 * returns data to populate a single object instance.
	 *
	 * @param beanHandler bean handler
	 * @param <T> The type of result object 
	 * @param resultObject The result object instance that should be populated with result data.
	 * @return The single result object as supplied by the resultObject parameter, populated with
	 *         the result set data, or null if no result was found
	 * @throws SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getResult(BeanHandler beanHandler, T resultObject) throws SQLException {
		if (resultObject == null) {
			resultObject = (T) beanHandler.createObject();
		}

		List<ResultColumn> rcs = getResultColumnList(resultSet, resultObject, beanHandler);
		for (ResultColumn rc : rcs) {
			Object value = rc.typeAdapter.getResult(resultSet, rc.columnIndex);
			beanHandler.setBeanValue(resultObject, rc.propertyName, value);
		}

		return resultObject;
	}

	/**
	 * update data to result set.
	 * 
	 * @param resultObject The result data object.
	 * @throws SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public void updateResult(Object resultObject) throws SQLException {
		BeanHandler beanHandler = getBeanHandler(resultObject.getClass());

		List<ResultColumn> rcs = getResultColumnList(resultSet, resultObject, beanHandler);
		for (ResultColumn rc : rcs) {
			Object value = beanHandler.getBeanValue(resultObject, rc.propertyName);
			rc.typeAdapter.updateResult(resultSet, rc.columnIndex, value, rc.jdbcType);
		}
	}
}
