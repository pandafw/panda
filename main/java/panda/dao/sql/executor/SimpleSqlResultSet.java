package panda.dao.sql.executor;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import panda.bean.BeanHandler;
import panda.dao.sql.JdbcTypes;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlNamings;
import panda.dao.sql.SqlResultSet;
import panda.dao.sql.adapter.TypeAdapter;
import panda.lang.Types;
import panda.log.Log;
import panda.log.Logs;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleSqlResultSet<T> extends AbstractSqlResultSet<T> {
	/**
	 * log
	 */
	protected static Log log = Logs.getLog(SqlResultSet.class);

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
	private BeanHandler<T> beanHandler;
	private boolean immutableType;

	/**
	 * Constructor
	 * @param executor the sql executor
	 * @param resultSet the sql result set
	 * @param resultType the result bean type
	 * @throws SQLException 
	 */
	protected SimpleSqlResultSet(SqlExecutor executor, ResultSet resultSet, Class<T> resultType)
			throws SQLException {
		this(executor, resultSet, resultType, null, null);
	}
	
	/**
	 * Constructor
	 * @param executor the sql executor
	 * @param resultSet the sql result set
	 * @param resultType the result bean type
	 * @param resultObject the initial result object
	 * @throws SQLException 
	 */
	protected SimpleSqlResultSet(SqlExecutor executor, ResultSet resultSet, Class<T> resultType, T resultObject)
			throws SQLException {
		this(executor, resultSet, resultType, null, null);
	}
	
	/**
	 * Constructor
	 * @param executor the sql executor
	 * @param resultSet the sql result set
	 * @param resultType the result bean type
	 * @param resultObject the initial result object
	 * @param keyProp the generated key property
	 * @throws SQLException 
	 */
	protected SimpleSqlResultSet(SqlExecutor executor, ResultSet resultSet, Class<T> resultType, T resultObject, String keyProp)
			throws SQLException {
		super(executor, resultSet);
		init(resultType, resultObject, keyProp);
	}

	private void init(Class<T> beanType, T resultObject, String keyProp) throws SQLException {
		beanHandler = executor.getBeans().getBeanHandler(beanType);
		immutableType = executor.getBeans().isImmutableBeanHandler(beanHandler);
		resultColumns = new ArrayList<ResultColumn>();

		ResultSetMetaData meta = resultSet.getMetaData();
		if (keyProp == null && immutableType && meta.getColumnCount() != 1) {
			throw new IllegalArgumentException("Too many result columns for the result: " + beanType);
		}

		int cnt = meta.getColumnCount();
		for (int i = 1; i <= cnt; i++) {
			ResultColumn rc = new ResultColumn();
			rc.columnIndex = i;
			rc.columnLabel = meta.getColumnLabel(i);
			rc.columnType = meta.getColumnType(i);
			rc.jdbcType = JdbcTypes.getType(rc.columnType);
			if (keyProp == null) {
				rc.propertyName = SqlNamings.columnLabel2JavaName(rc.columnLabel);
			}
			else {
				if (cnt > 1) {
					rc.propertyName = SqlNamings.columnLabel2JavaName(rc.columnLabel);
					if (!keyProp.equals(rc.propertyName)) {
						continue;
					}
				}
				else {
					rc.propertyName = keyProp;
				}
			}

			rc.propertyType = beanHandler.getBeanType(resultObject, rc.propertyName);
			if (rc.propertyType == null) {
				if (rc.columnLabel.endsWith("_")) {
					// skip unmapping special column
					continue;
				}
				else {
					throw new IllegalArgumentException("Unknown Type for " + rc.propertyName);
				}
			}
			rc.typeAdapter = executor.getTypeAdapters().getTypeAdapter(Types.getRawType(rc.propertyType), rc.jdbcType);
			if (rc.typeAdapter == null) {
				throw new IllegalArgumentException("Unknown TypeAdapter for " + rc.propertyName + "["
						+ rc.propertyType + " <-> " + rc.jdbcType + "].");
			}
			resultColumns.add(rc);
		}
		
		if (resultColumns.isEmpty()) {
			throw new IllegalArgumentException("Failed to init column mapping for " + meta + " -> " + beanType);
		}
	}

	// ---------------------------------------------------------------------
	// Get/Update
	// ---------------------------------------------------------------------
	/**
	 * returns data to populate a single object instance.
	 * 
	 * @param <T> The type of result object 
	 * @param resultClass The class of result object
	 * @return The single result object populated with the result set data, or null if no result was
	 *         found
	 * @throws SQLException If an SQL error occurs.
	 */
	public T getResult() throws SQLException {
		return getResult((T)null);
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
	@SuppressWarnings("unchecked")
	public T getResult(T resultObject) throws SQLException {
		if (immutableType) {
			ResultColumn rc = resultColumns.get(0);
			resultObject = (T)rc.typeAdapter.getResult(resultSet, rc.columnIndex);
		}
		else {
			if (resultObject == null) {
				resultObject = beanHandler.createObject();
			}

			for (ResultColumn rc : resultColumns) {
				Object value = rc.typeAdapter.getResult(resultSet, rc.columnIndex);
				if (!beanHandler.setBeanValue(resultObject, rc.propertyName, value)) {
					log.warn("Failed to set " + rc.propertyName + " of " + resultObject.getClass());
				}
			}
		}
		return resultObject;
	}

	/**
	 * update data to result set.
	 * 
	 * @param resultObject The result data object.
	 * @throws SQLException If an SQL error occurs.
	 */
	public void updateResult(T resultObject) throws SQLException {
		for (ResultColumn rc : resultColumns) {
			Object value = beanHandler.getBeanValue(resultObject, rc.propertyName);
			rc.typeAdapter.updateResult(resultSet, rc.columnIndex, value, rc.jdbcType);
		}
	}
}
