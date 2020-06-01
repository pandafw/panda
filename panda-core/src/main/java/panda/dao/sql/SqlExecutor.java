package panda.dao.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * SqlExecutor
 */
public interface SqlExecutor {
	/**
	 * reset
	 */
	//public void reset();
	
	/**
	 * Batch executes the given SQL statements, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sqls The SQL statements to execute.
	 * @return the effected row count array
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int[] executeBatch(List<String> sqls) throws SQLException;

	/**
	 * Batch executes the given SQL statements, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sqls The SQL statements to execute.
	 * @param batchSize the batch size to execute at a time (&lt; 0: execute all sqls at one time)
	 * @return the effected row count array
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int[] executeBatch(List<String> sqls, int batchSize) throws SQLException;

	/**
	 * Batch executes the given SQL statement with parameter list, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameters The parameter object list (e.g. JavaBean, Map, XML etc.).
	 * @return the effected row count array
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int[] executeBatch(String sql, List<Object> parameters) throws SQLException;

	/**
	 * Batch executes the given SQL statement with parameter list, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameters The parameter object list (e.g. JavaBean, Map, XML etc.).
	 * @param batchSize the batch size to execute at a time (&lt; 0: execute all sqls at one time)
	 * @return the effected row count array
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int[] executeBatch(String sql, List<Object> parameters, int batchSize) throws SQLException;

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @return The number of rows effected.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int execute(String sql) throws SQLException;
	
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
	public int execute(String sql, Object parameter) throws SQLException;

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
	public <T> T fetch(String sql, Class<T> resultClass) throws SQLException;

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
	public <T> T fetch(String sql, Object parameter, Class<T> resultClass) throws SQLException;

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
	public <T> T fetch(String sql, Object parameter, T resultObject) throws SQLException;

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
	public <T> T insert(String sql, Class<T> resultClass) throws SQLException;

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
	 * @param keyProp The primary key property name of the resultClass.
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> T insert(String sql, Class<T> resultClass, String keyProp) throws SQLException;

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
	public <T> T insert(String sql, Object parameter, Class<T> resultClass) throws SQLException;

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
	 * @param keyProp The primary key property name of the resultClass.
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> T insert(String sql, Object parameter, Class<T> resultClass, String keyProp) throws SQLException;

	/**
	 * Executes a mapped SQL INSERT statement. Insert is a bit different from
	 * other update methods, as it provides facilities for returning the primary
	 * key of the newly inserted row (rather than the effected rows). This
	 * functionality is of course optional.
	 * <p/>
	 * The parameter object is generally used to supply the input data for the
	 * INSERT values. The primary key automatically generated by the RDBMS will 
	 * be retrieved and set to the parameter object according to the keyProp. 
	 * 
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, etc.).
	 * @param keyProp The primary key property name of the parameter.
	 * @return The supplied parameter object.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> T insert(String sql, T parameter, String keyProp) throws SQLException;

	/**
	 * Executes a mapped SQL INSERT statement. Insert is a bit different from
	 * other update methods, as it provides facilities for returning the primary
	 * key of the newly inserted row (rather than the effected rows). This
	 * functionality is of course optional.
	 * <p/>
	 * The parameter object is generally used to supply the input data for the
	 * INSERT values. The primary key automatically generated by the RDBMS will 
	 * be retrieved and set to the result object according to the keyProp. 
	 * 
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, etc.).
	 * @param result    The result object instance that should be populated with result data.
	 * @param keyProp The primary key property name of the parameter.
	 * @return The supplied parameter object.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> T insert(String sql, Object parameter, T result, String keyProp) throws SQLException;

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
			throws SQLException;

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
	public <T> List<T> selectList(String sql, Class<T> resultClass) throws SQLException;

	/**
	 * Executes a mapped SQL SELECT statement that returns data to populate
	 * a number of result objects within a certain range.
	 * <p/>
	 * This overload assumes no parameter is needed.
	 *
	 * @param <T> The type of result object 
	 * @param sql The SQL statement to execute.
	 * @param resultClass The class of result object 
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> List<T> selectList(String sql, Class<T> resultClass, int skip, int max)
			throws SQLException;

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
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A List of result objects.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> List<T> selectList(String sql, Object parameter, Class<T> resultClass, int skip,
			int max) throws SQLException;

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
	 * @param keyProp The property to be used as the key in the Map.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> Map<Object, T> selectMap(String sql, Class<T> resultClass, String keyProp)
			throws SQLException;

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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> Map<Object, T> selectMap(String sql, Class<T> resultClass, String keyProp,
			int skip, int max) throws SQLException;

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
	 * @param keyProp         The property to be used as the key in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> Map<Object, T> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp) throws SQLException;

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
	 * @param keyProp         The property to be used as the key in the Map.
	 * @param resultClass The class of result object 
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> Map<Object, T> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp, int skip, int max) throws SQLException;

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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> Map<?, ?> selectMap(String sql, Class<T> resultClass, String keyProp,
			String valProp) throws SQLException;

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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> Map<?, ?> selectMap(String sql, Class<T> resultClass, String keyProp,
			String valProp, int skip, int max) throws SQLException;

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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> Map<?, ?> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp, String valProp) throws SQLException;

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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> Map<?, ?> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp, String valProp, int skip, int max) throws SQLException;

	/**
	 * Executes the given SQL statement, which returns a single <code>ResultSetEx</code> object.
	 * 
	 * @param sql an SQL statement to be sent to the database, typically a static SQL
	 *            <code>SELECT</code> statement
	 * @param resultClass The class of result object
	 * @return a <code>ResultSet</code> object that contains the data produced by the given query;
	 *         never <code>null</code>
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> SqlResultSet<T> selectResultSet(String sql, Class<T> resultClass) throws SQLException;

	/**
	 * Executes the given SQL statement, which returns a single <code>ResultSetEx</code> object.
	 * 
	 * @param sql an SQL statement to be sent to the database, typically a static SQL
	 *            <code>SELECT</code> statement
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @param resultClass The class of result object
	 * @return a <code>ResultSet</code> object that contains the data produced by the given query;
	 *         never <code>null</code>
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> SqlResultSet<T> selectResultSet(String sql, Object parameter, Class<T> resultClass)
			throws SQLException;

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @return The number of rows effected.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public int update(String sql) throws SQLException;

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
	public int update(String sql, Object parameter) throws SQLException;

}
