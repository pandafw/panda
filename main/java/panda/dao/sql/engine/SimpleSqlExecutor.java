package panda.dao.sql.engine;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlLogger;
import panda.dao.sql.SqlManager;
import panda.dao.sql.SqlResultSet;
import panda.dao.sql.SqlUtils;
import panda.lang.Asserts;
import panda.log.Log;
import panda.log.Logs;


/**
 * SimpleSqlExecutor.
 * 
 * <pre>
 * <b>SQL GRAMMER:</b>
 * 
 * 1. SELECT column name auto mapping
 *    1.1 column name --> JavaBean property name
 *      ex.) SELECT ID, NAME, ITEM_NAME FROM SAMPLE
 *        ID --> id
 *        NAME --> name
 *        ITEM_NAME --> itemName
 * 
 *    1.2 AS ALIAS --> JavaBean property name
 *      ex.) SELECT ID AS a.id, NAME AS a.name, ITEM_NAME AS a.item.name, B_ITEM_NAME AS a.b.itemName FROM SAMPLE
 *           -->
 *           SELECT ID AS A_0_ID, NAME AS A_0_NAME, ITEM_NAME AS A_0_ITEM_0_NAME, ITEM_NAME AS A_0_B_0_ITEM_NAME FROM SAMPLE
 *      
 *       mapping: 
 *         A_0_ID --> a.id
 *         A_0_NAME --> a.name
 *         A_0_ITEM_0_NAME --> a.item.name
 * 
 * 2. SQL Parameter
 *    :xxx --> PreparedStatement binding parameter (xxx: JavaBean property name)
 *    ::yyy --> replacement (yyy: JavaBean property name) [SQL injection!]
 * 
 *    ex.) SELECT * FROM SAMPLE
 *         WHERE
 *               ID=:id
 *           AND NAME=:name
 *           AND LIST IN (:list)
 *           AND KIND=:kind
 *         ORDER BY 
 *           ::orderCol ::orderDir
 * 
 *    ex.) UPDATE SAMPLE
 *         SET
 *           NAME=:name,
 *           KIND=:kind,
 *           STR='@kind[,KIND=:kind]'
 *         WHERE
 *           ID=:id
 * 
 * 3. SQL Amendment
 *    1) Delete 'AND/OR' after 'WHERE' or '('
 *     ex.) WHERE AND ID IS NULL --> WHERE ID IS NULL
 *          (AND NAME IS NULL) --> (NAME IS NULL)
 *  
 *    2) Delete ',' after 'SET'
 *     ex.) SET ,ID=1 ,NAME='a' --> SET ID=1 , NAME='a'
 *     
 * 4. Call Procedure/Function
 *    ex.) {:count:INTEGER:OUT = call SET_TEST_PRICE(:id,:price:DECIMAL.2:INOUT)}
 *   
 *    Grammer: 
 *      :price:DECIMAL.2:INOUT
 *     
 *      :price --> JavaBean property name
 *      :DECIMAL.2 --> JDBC type & scale
 *      :INOUT --> parameter type
 *         IN: input only (default)
 *         OUT: output only
 *         INOUT: input & output
 *         
 *    @see java.sql.CallableStatement 
 *
 *
 *
 * <b>EXAMPLE:</b>
 *   Class.forName("org.hsqldb.jdbcDriver" );
 *   Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "");
 *   SqlExecutor executor = new SimpleSqlExecutor(connection);
 *   
 *   
 *   // queryForObject - Map
 *   String sql = "SELECT * FROM TEST WHERE ID=:id";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("id", 1001);
 *   
 *   Map result = executor.queryForObject(sql, param, HashMap.class);
 *   // result: { "id": 1001, "name": "test1001", ... }
 *   
 *   
 *   // queryForList - Map
 *   String sql = "SELECT * FROM TEST WHERE ID IN (:idArray)";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("idArray", new int[] { 1001, 1003 });
 *   
 *   List<Map> resultList = executor.queryForList(sql, param, HashMap.class);
 *   // resultList: [{ "id": 1001, "name": "test1001", ... }, { "id": 1003, "name": "test1003", ... }]
 *   
 *   
 *   // queryForObject - JavaBean
 *   String sql = "SELECT * FROM TEST WHERE ID=:id";
 *   TestA param = new TestA();
 *   param.id = 1001;
 *   
 *   TestB result = executor.queryForObject(sql, param, TestB.class);
 *   // result: { "id": 1001, "name": "test1001", ... }
 *
 *   
 *   // queryForList - JavaBean
 *   String sql = "SELECT * FROM TEST WHERE ID IN (:idArray)";
 *   TestA param = new TestA();
 *   param.idArray = new int[] { 1001, 1003 };
 *   
 *   List<TestB> resultList = executor.queryForList(sql, param, TestB.class);
 *   // resultList: [{ "id": 1001, "name": "test1001", ... }, { "id": 1003, "name": "test1003", ... }]
 *
 *
 *   // execute - Call Function
 *   String sql = "{:price:DECIMAL.2:OUT = call GET_TEST_PRICE(:id)}";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("id", 1001);
 *   
 *   executor.execute(sql, param);
 *   // param: { "id": 1001, "price": 1001.01 }
 *   
 *   
 *   // execute - Call Function
 *   String sql = "{:count:INTEGER:OUT = call SET_TEST_PRICE(:id, :price:DECIMAL.2:INOUT)}";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("id", 1001);
 *   param.put("price", new BigDecimal("9999"));
 *   
 *   executor.execute(sql, param);
 *   // param: { "count": 1, "price": 1001.01 }
 *   
 *   
 *   // queryForObject - Call Function
 *   String sql = "{call SELECT_TEST(:id)}";
 *   TestA param = new TestA();
 *   param.id = 1001;
 *   
 *   TestB result = executor.queryForObject(sql, param, TestB.class);
 *   // result: { "id": 1001, "name": "test1001", ... }
 * 
 * </pre>
 * 
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("unchecked")
public class SimpleSqlExecutor extends AbstractSqlExecutor {
	/**
	 * log
	 */
	protected static Log log = Logs.getLog(SqlExecutor.class);

	private static final String BLANK_SQL_MESSAGE = "The sql is blank";
	
	/**
	 * Constructor
	 * @param sqlManager sqlManager
	 */
	protected SimpleSqlExecutor(SqlManager sqlManager) {
		super(sqlManager);
	}

	/**
	 * @return the sqlManager
	 */
	public SimpleSqlManager getSimpleSqlManager() {
		return (SimpleSqlManager)getSqlManager();
	}

	/**
	 * retrive output parameters
	 * @param cs statement
	 * @param parameters parameters 
	 * @throws SQLException if a SQL exception occurs
	 */
	protected static void logOutputParameters(CallableStatement cs, List<SqlParameter> parameters) throws SQLException {
		if (log.isDebugEnabled()) {
			StringBuilder sb = null;
			for (int i = 0; i < parameters.size(); i++) {
				SqlParameter parameter = parameters.get(i);
	
				if (parameter.isOutputAllowed()) {
					Object value = parameter.getTypeAdapter().getResult(cs, i + 1);
					if (sb == null) { 
						sb = new StringBuilder();
						sb.append("Output Parameters: { ");
					}
					else {
						sb.append(", ");
					}
					sb.append(parameter.getName());
					sb.append(": ");
					sb.append(value);
					if (value != null) {
						sb.append('[');
						sb.append(value.getClass().getName());
						sb.append(']');
					}
				}
			}
			
			if (sb != null) {
				sb.append(" }");
				log.debug(sb.toString());
			}
		}
	}

	/**
	 * logParameters
	 * @param parameters parameters
	 */
	protected static void logParameters(List<SqlParameter> parameters) {
		if (log.isDebugEnabled()) {
			for (int i = 0; i < parameters.size(); i++) {
				SqlParameter parameter = parameters.get(i);
				
				StringBuilder sb = new StringBuilder();
				sb.append("Parameters[" + (i + 1) + "]: { ");
				sb.append("name: ");
				sb.append(parameter.getName());
				sb.append(", jdbcType: ");
				sb.append(parameter.getJdbcType());
				sb.append(", mode: ");
				sb.append(parameter.getMode());
				sb.append(", value: ");
				sb.append(parameter.getValue());
				sb.append(", valueClass: ");
				sb.append(parameter.getValue() == null ? null : parameter.getValue().getClass().getName());
				sb.append(", typeAdapter: ");
				sb.append(parameter.getTypeAdapter().getClass().getName());
				sb.append(" }");

				log.debug(sb.toString());
			}
		}
	}

	/**
	 * getBeanHandler
	 * @param type class type
	 * @return bean handler
	 */
	protected BeanHandler getBeanHandler(Class type) {
		return Beans.me().getBeanHandler(type);
	}

	/**
	 * @param sql sql
	 * @return true if the sql is a callable sql statement
	 */
	protected boolean isCallableSql(String sql) {
		for (int i = 0; i < sql.length(); i++) {
			char c = sql.charAt(i);
			if (!Character.isWhitespace(c)) {
				if (c == '{') {
					return true;
				}
				break;
			}
		}
		return false;
	}

	/**
	 * parseSqlStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @return translated sql
	 * @throws Exception if an error occurs
	 */
	protected String parseSqlStatement(String sql, Object parameter, List<SqlParameter> sqlParams) {
		Map<String, SqlParser> sqlParserCache = getSimpleSqlManager().getSqlParserCache();
		SqlParser parser = sqlParserCache.get(sql);
		if (parser == null) {
			synchronized (sqlParserCache) {
				parser = sqlParserCache.get(sql);
				if (parser == null) {
					parser = createSqlParser(sql);
					sqlParserCache.put(sql, parser);
				}
			}
		}
		return parser.parse(this, parameter, sqlParams);
	}

	/**
	 * createSqlParser
	 * @param sql sql
	 * @return SqlParser instance
	 */
	protected SqlParser createSqlParser(String sql) {
		return new SimpleSqlParser(sql);
	}
	
	/**
	 * createStatement
	 * @return JDBC Statement
	 * @throws SQLException if an sql error occurs
	 */
	protected Statement createStatement() throws SQLException {
		Statement s;
		if (resultSetHoldability != 0) {
			s = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}
		else {
			s = connection.createStatement(resultSetType, resultSetConcurrency);
		}
		return s;
	}

	/**
	 * prepareStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @param returGK generated keys should be made available for retrieval
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement createStatement(String sql, Object parameter,
			List<SqlParameter> sqlParams, boolean returnGK) throws SQLException {
		String jdbcSql = parseSqlStatement(sql, parameter, sqlParams);
		
		SqlLogger.logSatement(jdbcSql);

		if (returnGK) {
			Asserts.isFalse(isCallableSql(jdbcSql), "SQL is a callable sql");
		}

		if (isCallableSql(jdbcSql)) {
			CallableStatement cs;
			
			if (resultSetHoldability != 0) {
				cs = connection.prepareCall(jdbcSql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}
			else {
				cs = connection.prepareCall(jdbcSql, resultSetType, resultSetConcurrency);
			}
			return cs;
		}
		else {
			PreparedStatement ps;

			if (returnGK) {
				ps = connection.prepareStatement(jdbcSql, Statement.RETURN_GENERATED_KEYS);
			}
			else if (resultSetHoldability != 0) {
				ps = connection.prepareStatement(jdbcSql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}
			else {
				ps = connection.prepareStatement(jdbcSql, resultSetType, resultSetConcurrency);
			}
			return ps;
		}
	}

	/**
	 * prepareStatement
	 * @param ps prepared statement
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @throws SQLException if an sql error occurs
	 */
	protected void setStatementParams(PreparedStatement ps, Object parameter,
			List<SqlParameter> sqlParams) throws SQLException {
		if (ps instanceof CallableStatement) {
			CallableStatement cs = (CallableStatement)ps;
			
			for (int i = 0; i < sqlParams.size(); i++) {
				SqlParameter sqlParam = sqlParams.get(i);
	
				if (sqlParam.isInputAllowed()) {
					sqlParam.getTypeAdapter().setParameter(cs, i + 1, sqlParam.getValue(), sqlParam.getJdbcType());
				}
				if (sqlParam.isOutputAllowed()) {
					if (sqlParam.getScale() != null) {
						cs.registerOutParameter(i + 1, sqlParam.getSqlType(), sqlParam.getScale());
					}
					else {
						cs.registerOutParameter(i + 1, sqlParam.getSqlType());
					}
				}
			}
			
			SqlLogger.logParameters(cs);
			logParameters(sqlParams);
		}
		else {
			for (int i = 0; i < sqlParams.size(); i++) {
				SqlParameter sqlParam = sqlParams.get(i);
				sqlParam.getTypeAdapter().setParameter(ps, i + 1, sqlParam.getValue(), sqlParam.getJdbcType());
			}
			
			SqlLogger.logParameters(ps);
			logParameters(sqlParams);
		}
	}

	/**
	 * prepareStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement prepareStatement(String sql, Object parameter,
			List<SqlParameter> sqlParams) throws SQLException {
		return prepareStatement(sql, parameter, sqlParams, false);
	}

	/**
	 * prepareStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement prepareStatement(String sql, Object parameter,
			List<SqlParameter> sqlParams, boolean returnGK) throws SQLException {
		PreparedStatement ps = createStatement(sql, parameter, sqlParams, returnGK);
		setStatementParams(ps, parameter, sqlParams);
		return ps;
	}

	/**
	 * retrieve output parameters
	 * @param ps statement
	 * @param parameter parameter
	 * @param sqlParams sql parameters
	 * @throws SQLException if a SQL error occurs
	 */
	protected void retrieveOutputParameters(PreparedStatement ps, Object parameter, List<SqlParameter> sqlParams) throws SQLException {
		if (ps instanceof CallableStatement) {
			CallableStatement cs = (CallableStatement)ps;

			logOutputParameters(cs, sqlParams);

			if (parameter != null) {
				BeanHandler beanHandler = getBeanHandler(parameter.getClass());
				for (int i = 0; i < sqlParams.size(); i++) {
					SqlParameter sqlParam = sqlParams.get(i);
		
					if (sqlParam.isOutputAllowed()) {
						Object value = sqlParam.getTypeAdapter().getResult(cs, i + 1);
						beanHandler.setBeanValue(parameter, sqlParam.getName(), value);
					}
				}
			}
		}
	}

	//------------------------------------------------------------------------------
	// Override & Implements
	//------------------------------------------------------------------------------
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
	@Override
	protected <T> T insert(String sql, Object parameter,
			Class<T> resultClass, T resultObject) throws SQLException {

		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);
		Asserts.notNull(resultClass, "resultClass is null");
		
		if (log.isDebugEnabled()) {
			log.debug("insert: " + sql);
		}

		List<SqlParameter> sqlParams = new ArrayList<SqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams, true);
		try {
			ps.execute();
			
			retrieveOutputParameters(ps, parameter, sqlParams);

			ResultSet rs = ps.getGeneratedKeys();
			if (rs == null || !rs.next()) {
				return resultObject;
			}
			
			SqlLogger.logResultHeader(rs);
			SqlLogger.logResultValues(rs);
			
			SimpleSqlResultSet rse = new SimpleSqlResultSet(this, rs); 
			BeanHandler beanHandler = getBeanHandler(resultClass);
			resultObject = rse.getResult(beanHandler, resultObject);

			return resultObject;
		}
		finally {
			SqlUtils.safeClose(ps);
		}
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
	 * @param resultObject    The result object instance that should be populated with result data.
	 * @return The single result object populated with the result set data,
	 *         or null if no result was found
	 * @throws java.sql.SQLException If more than one result was found, or if any other error occurs.
	 */
	@Override
	protected <T> T queryForObject(String sql, Object parameter, Class<T> resultClass, T resultObject) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("queryForObject: " + sql);
		}

		List<SqlParameter> sqlParams = new ArrayList<SqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);
		ResultSet rs = null;
		try {
			ps.execute();

			retrieveOutputParameters(ps, parameter, sqlParams);

			rs = ps.getResultSet();
			if (rs == null || !rs.next()) {
				return null;
			}
			
			SqlLogger.logResultHeader(rs);
			SqlLogger.logResultValues(rs);
			
			SimpleSqlResultSet rse = new SimpleSqlResultSet(this, rs); 
			BeanHandler beanHandler = getBeanHandler(resultClass);
			resultObject = rse.getResult(beanHandler, resultObject);

			if (rs.next()) {
		        throw new SQLException("Error: queryForObject returned too many results.");
			}
			return resultObject;
		}
		finally {
			SqlUtils.safeClose(rs);
			SqlUtils.safeClose(ps);
		}
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
	@Override
	public <T> List<T> selectList(String sql, Object parameter, Class<T> resultClass, int skip, int max) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("queryForList: " + sql);
		}

		List<SqlParameter> sqlParams = new ArrayList<SqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);
		ResultSet rs = null;

		try {
			List<T> resultList = new ArrayList<T>();

			ps.execute();

			retrieveOutputParameters(ps, parameter, sqlParams);

			rs = ps.getResultSet();
			if (rs != null) {
				SqlLogger.logResultHeader(rs);
	
				SqlUtils.skipResultSet(rs, skip);
	
				SimpleSqlResultSet rse = new SimpleSqlResultSet(this, rs);
				BeanHandler beanHandler = getBeanHandler(resultClass);
				
				int cnt = 0;
				while (rs.next()) {
					SqlLogger.logResultValues(rs);
	
					T resultObject = rse.getResult(beanHandler, (T)null);
					resultList.add(resultObject);
					
					if (max > 0) {
						cnt++;
						if (cnt >= max) {
							break;
						}
					}
				}
			}

			return resultList;
		}
		finally {
			SqlUtils.safeClose(rs);
			SqlUtils.safeClose(ps);
		}
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
	 * @param keyPropertyName The property to be used as the key in the Map.
	 * @param valuePropertyName The property to be used as the value in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@Override
	public <T> Map selectMap(String sql, Object parameter, Class<T> resultClass, String keyPropertyName, String valuePropertyName) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		List<T> list = selectList(sql, parameter, resultClass);

		Map map = new HashMap();
		BeanHandler beanHandler = getBeanHandler(resultClass);

		for (int i = 0, n = list.size(); i < n; i++) {
			T bean = list.get(i);
			Object key = beanHandler.getBeanValue(bean, keyPropertyName);
			Object value = null;
			if (valuePropertyName == null) {
				value = bean;
			} else {
				value = beanHandler.getBeanValue(bean, valuePropertyName);
			}
			map.put(key, value);
		}

		return map;
	}

	/**
	 * Executes the given SQL statement, which returns a single <code>SqlResultSet</code> object.
	 * 
	 * @param sql an SQL statement to be sent to the database, typically a static SQL
	 *            <code>SELECT</code> statement
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @return a <code>ResultSet</code> object that contains the data produced by the given query;
	 *         never <code>null</code>
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@Override
	public SqlResultSet selectResultSet(String sql, Object parameter) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("queryForResultSet: " + sql);
		}

		List<SqlParameter> sqlParams = new ArrayList<SqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);

		ResultSet rs = ps.executeQuery();
		
		retrieveOutputParameters(ps, parameter, sqlParams);

		SqlResultSet rse = new SimpleSqlResultSet(this, rs);
		return rse;
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
	@Override
	public int update(String sql, Object parameter) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("update: " + sql);
		}

		List<SqlParameter> sqlParams = new ArrayList<SqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);
		try {
			ps.execute();
			
			retrieveOutputParameters(ps, parameter, sqlParams);

			int updateCount = ps.getUpdateCount();
			return updateCount;
		}
		finally {
			SqlUtils.safeClose(ps);
		}
	}

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @param parameter The parameter object (e.g. JavaBean, Map, XML etc.).
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@Override
	public void execute(String sql, Object parameter) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("execute: " + sql);
		}

		List<SqlParameter> sqlParams = new ArrayList<SqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);
		try {
			ps.execute();
			retrieveOutputParameters(ps, parameter, sqlParams);
		}
		finally {
			SqlUtils.safeClose(ps);
		}
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
	public int[] executeBatch(List<String> sqls, int batchSize) throws SQLException {
		Asserts.notEmpty(sqls);
		
		if (log.isDebugEnabled()) {
			log.debug("executeBatch: " + sqls.size());
		}

		if (batchSize >= sqls.size()) {
			batchSize = 0;
		}

		Statement st = createStatement(); 

		int[] rc = null;
		try {
			int cnt = 0;
			int pos = 0;
			for (String sql : sqls) {
				SqlLogger.logSatement(sql);

				st.addBatch(sql);
				if (batchSize > 0) {
					cnt++;
					if (cnt >= batchSize) {
						if (rc == null) {
							rc = new int[sqls.size()];
						}

						int[] r = st.executeBatch();;
						System.arraycopy(r, 0, rc, pos, r.length);
						pos += cnt;
						cnt = 0;
					}
				}
			}

			if (cnt > 0) {
				int[] r = st.executeBatch();
				if (rc == null) {
					rc = r;
				}
				else {
					System.arraycopy(r, 0, rc, pos, r.length);
				}
			}
			return rc;
		}
		finally {
			SqlUtils.safeClose(st);
		}
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
	public int[] executeBatch(String sql, List<Object> parameters, int batchSize) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);
		Asserts.notEmpty(parameters);

		if (log.isDebugEnabled()) {
			log.debug("executeBatch: " + parameters.size() + ": " + sql);
		}

		if (batchSize >= parameters.size()) {
			batchSize = 0;
		}

		List<SqlParameter> sqlParams = new ArrayList<SqlParameter>();
		PreparedStatement ps = createStatement(sql, parameters.get(0), sqlParams, false);

		int[] rc = null;
		try {
			int cnt = 0;
			int pos = 0;
			for (Object param : parameters) {
				setStatementParams(ps, param, sqlParams);
				ps.addBatch();
				if (batchSize > 0) {
					cnt++;
					if (cnt >= batchSize) {
						if (rc == null) {
							rc = new int[parameters.size()];
						}

						int[] r = ps.executeBatch();
						System.arraycopy(r, 0, rc, pos, r.length);
						pos += cnt;
						cnt = 0;
					}
				}
			}

			if (cnt > 0) {
				int[] r = ps.executeBatch();
				if (rc == null) {
					rc = r;
				}
				else {
					System.arraycopy(r, 0, rc, pos, r.length);
				}
			}
			return rc;
		}
		finally {
			SqlUtils.safeClose(ps);
		}
	}
}
