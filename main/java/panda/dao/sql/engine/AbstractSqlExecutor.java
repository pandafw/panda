package panda.dao.sql.engine;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import panda.bean.Beans;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;
import panda.dao.sql.SqlResultSet;
import panda.dao.sql.adapter.TypeAdapters;


/**
 * AbstractSqlExecutor
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractSqlExecutor implements SqlExecutor {
	/**
	 * Constant to let us know not to skip anything
	 */
	public static final int NO_SKIPPED_RESULTS = 0;

	/**
	 * Constant to let us know to include all records
	 */
	public static final int NO_MAXIMUM_RESULTS = 0;

	/**
	 * Constant to let us know to batch execute all sqls at one time
	 */
	public static final int NO_MAXIMUM_SIZE = 0;

	/**
	 * Beans
	 */
	protected Beans beans;

	/**
	 * typeAdapters
	 */
	protected TypeAdapters typeAdapters;
	
	/**
	 * Connection
	 */
	protected Connection connection;
	
	/**
	 * resultSetType - one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 * ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 */
	protected int resultSetType;

	/**
	 * resultSetConcurrency - one of the following ResultSet constants: ResultSet.CONCUR_READ_ONLY
	 * or ResultSet.CONCUR_UPDATABLE
	 */
	protected int resultSetConcurrency;

	/**
	 * resultSetHoldability - one of the following ResultSet constants:
	 * ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
	 */
	protected int resultSetHoldability;

	protected final SqlManager sqlManager;
	
	/**
	 * Constructor
	 * @param sqlManager sqlManager
	 */
	protected AbstractSqlExecutor(SqlManager sqlManager) {
		this.sqlManager = sqlManager;
		this.beans = sqlManager.getBeans();
		this.typeAdapters = sqlManager.getTypeAdapters();
	}

	/**
	 * @return the sqlManager
	 */
	public SqlManager getSqlManager() {
		return sqlManager;
	}

	/**
	 * @return the beans
	 */
	public Beans getBeans() {
		return beans;
	}

	/**
	 * @param beans the beans to set
	 */
	public void setBeans(Beans beans) {
		this.beans = beans;
	}

	/**
	 * @return the typeAdapters
	 */
	public TypeAdapters getTypeAdapters() {
		return typeAdapters;
	}

	/**
	 * @param typeAdapters the typeAdapters to set
	 */
	public void setTypeAdapters(TypeAdapters typeAdapters) {
		this.typeAdapters = typeAdapters;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the resultSetType
	 */
	public int getResultSetType() {
		return resultSetType;
	}

	/**
	 * @param resultSetType the resultSetType to set
	 */
	public void setResultSetType(int resultSetType) {
		this.resultSetType = resultSetType;
	}

	/**
	 * @return the resultSetConcurrency
	 */
	public int getResultSetConcurrency() {
		return resultSetConcurrency;
	}

	/**
	 * @param resultSetConcurrency the resultSetConcurrency to set
	 */
	public void setResultSetConcurrency(int resultSetConcurrency) {
		this.resultSetConcurrency = resultSetConcurrency;
	}

	/**
	 * @return the resultSetHoldability
	 */
	public int getResultSetHoldability() {
		return resultSetHoldability;
	}

	/**
	 * @param resultSetHoldability the resultSetHoldability to set
	 */
	public void setResultSetHoldability(int resultSetHoldability) {
		this.resultSetHoldability = resultSetHoldability;
	}

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public void execute(String sql) throws SQLException {
		execute(sql, null);
	}

	/**
	 * Batch executes the given SQL statements, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sqls The SQL statements to execute.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int[] executeBatch(List<String> sqls) throws SQLException {
		return executeBatch(sqls, NO_MAXIMUM_SIZE);
	}

	/**
	 * Batch executes the given SQL statements, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sqls The SQL statements to execute.
	 * @param batchSize the batch size to execute at a time
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public abstract int[] executeBatch(List<String> sqls, int batchSize) throws SQLException;

	/**
	 * Batch executes the given SQL statement with parameter list, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameters The parameter object list (e.g. JavaBean, Map, XML etc.).
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int[] executeBatch(String sql, List<Object> parameters) throws SQLException {
		return executeBatch(sql, parameters, NO_MAXIMUM_SIZE);
	}

	/**
	 * Batch executes the given SQL statement with parameter list, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameters The parameter object list (e.g. JavaBean, Map, XML etc.).
	 * @param batchSize the batch size to execute at a time
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public abstract int[] executeBatch(String sql, List<Object> parameters, int batchSize) throws SQLException;

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public abstract void execute(String sql, Object parameter) throws SQLException;

	/**
	 * Executes a mapped SQL INSERT statement. Insert is a bit different from
	 * other update methods, as it provides facilities for returning the primary
	 * key of the newly inserted row (rather than the effected rows). This
	 * functionality is of course optional.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 * 
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> T insert(String sql, Class<T> resultClass) throws SQLException {
		return insert(sql, null, resultClass, null); 
	}

	/**
	 * Executes a mapped SQL INSERT statement. Insert is a bit different from
	 * other update methods, as it provides facilities for returning the primary
	 * key of the newly inserted row (rather than the effected rows). This
	 * functionality is of course optional.
	 * <p/>
	 * The parameter object is generally used to supply the input data for the
	 * INSERT values.
	 * 
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, etc.).
	 * @param resultClass The class of result object 
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> T insert(String sql, Object parameter, Class<T> resultClass)
			throws SQLException {
		return insert(sql, parameter, resultClass, null); 
	}

	/**
	 * Executes a mapped SQL INSERT statement. Insert is a bit different from
	 * other update methods, as it provides facilities for returning the primary
	 * key of the newly inserted row (rather than the effected rows). This
	 * functionality is of course optional.
	 * <p/>
	 * The parameter object is generally used to supply the input data for the
	 * INSERT values.
	 * 
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, etc.).
	 * @param resultObject    The result object instance that should be populated with result data.
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> T insert(String sql, Object parameter, T resultObject) throws SQLException {
		return insert(sql, parameter, 
				(Class<T>)resultObject.getClass(), resultObject);
	}

	/**
	 * Executes a mapped SQL INSERT statement. Insert is a bit different from
	 * other update methods, as it provides facilities for returning the primary
	 * key of the newly inserted row (rather than the effected rows). This
	 * functionality is of course optional.
	 * <p/>
	 * The parameter object is generally used to supply the input data for the
	 * INSERT values.
	 * 
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, etc.).
	 * @param resultClass The class of result object 
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	protected abstract <T> T insert(String sql, Object parameter,
			Class<T> resultClass, T resultObject) throws SQLException;

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @return The number of rows effected.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int update(String sql) throws SQLException {
		return update(sql, null);
	}

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @return The number of rows effected.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public abstract int update(String sql, Object parameter) throws SQLException;

    /**
	 * Executes the given SQL statement, which returns a single <code>ResultSetEx</code> object.
	 * 
	 * @param sql an SQL statement to be sent to the database, typically a static SQL
	 *            <code>SELECT</code> statement
	 * @return a <code>ResultSet</code> object that contains the data produced by the given query;
	 *         never <code>null</code>
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public SqlResultSet selectResultSet(String sql) throws SQLException {
		return selectResultSet(sql, null);
	}

    /**
     * Executes the given SQL statement, which returns a single 
     * <code>ResultSetEx</code> object.
     *
     * @param sql an SQL statement to be sent to the database, typically a 
     *        static SQL <code>SELECT</code> statement
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
     * @return a <code>ResultSet</code> object that contains the data produced 
     *         by the given query; never <code>null</code> 
	 * @throws java.sql.SQLException If an SQL error occurs.
     */
	public abstract SqlResultSet selectResultSet(String sql, Object parameter)
			throws SQLException;

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a single object instance.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @return The single result object populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
	public <T> T fetch(String sql, Class<T> resultClass) throws SQLException {
		return fetch(sql, null, resultClass);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a single object instance.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @return The single result object populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
	public <T> T fetch(String sql, Object parameter, Class<T> resultClass)
			throws SQLException {
		return queryForObject(sql, parameter, resultClass, null);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * the supplied result object.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultObject    The result object instance that should be populated with result data.
	 * @return The single result object as supplied by the resultObject parameter, populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> T fetch(String sql, Object parameter, T resultObject)
			throws SQLException {
		return queryForObject(sql, parameter, (Class<T>)resultObject.getClass(),
			resultObject);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a single object instance.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @param resultObject    The result object instance that should be populated with result data.
	 * @return The single result object populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
	protected abstract <T> T queryForObject(String sql, Object parameter,
			Class<T> resultClass, T resultObject) throws SQLException;

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> List<T> selectList(String sql, Object parameter, Class<T> resultClass)
			throws SQLException {
		return selectList(sql, parameter, resultClass, NO_SKIPPED_RESULTS,
			NO_MAXIMUM_RESULTS);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> List<T> selectList(String sql, Class<T> resultClass) throws SQLException {
		return selectList(sql, null, resultClass, NO_SKIPPED_RESULTS, NO_MAXIMUM_RESULTS);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects within a certain range.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @param skip            The number of results to ignore.
	 * @param max             The maximum number of results to return.
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> List<T> selectList(String sql, Class<T> resultClass, int skip, int max)
			throws SQLException {
		return selectList(sql, null, resultClass, skip, max);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects within a certain range.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @param skip            The number of results to ignore.
	 * @param max             The maximum number of results to return.
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public abstract <T> List<T> selectList(String sql, Object parameter,
			Class<T> resultClass, int skip, int max) throws SQLException;

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects that will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @param keyPropertyName The property to be used as the key in the Map.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Class<T> resultClass, String keyPropertyName)
			throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyPropertyName, null);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects that will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param keyPropertyName         The property to be used as the key in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyPropertyName) throws SQLException {
		return (Map<Object, T>) selectMap(sql, parameter, resultClass, keyPropertyName,
			null);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects from which one property will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @param keyPropertyName The property to be used as the key in the Map.
	 * @param valuePropertyName The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map selectMap(String sql, Class<T> resultClass, String keyPropertyName,
			String valuePropertyName) throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyPropertyName,
			valuePropertyName);
	}

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects from which one property will be keyed into a Map.
	 * <p/>
	 * The parameter object is generally used to supply the input
	 * data for the WHERE clause parameter(s) of the SELECT statement.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object 
	 * @param keyPropertyName The property to be used as the key in the Map.
	 * @param valuePropertyName The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public abstract <T> Map selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyPropertyName, String valuePropertyName) throws SQLException;
}
