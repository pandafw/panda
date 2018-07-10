package panda.dao.sql.executor;

import java.sql.CallableStatement;
import java.sql.Connection;
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
import panda.dao.sql.Sqls;
import panda.dao.sql.adapter.TypeAdapters;
import panda.lang.Asserts;
import panda.lang.Iterators;
import panda.log.Log;
import panda.log.Logs;


public class JdbcSqlExecutor implements SqlExecutor {
	private static final Log log = Logs.getLog(JdbcSqlExecutor.class);

	protected static final String BLANK_SQL_MESSAGE = "The sql is blank";

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
	
	/**
	 * query timeout (seconds)
	 */
	protected int timeout;

	/**
	 * SQL Manager
	 */
	protected final SqlManager sqlManager;

	/**
	 * Constructor
	 * @param sqlManager sqlManager
	 */
	protected JdbcSqlExecutor(SqlManager sqlManager) {
		this.sqlManager = sqlManager;
		reset();
	}

	//------------------------------------------------------------------
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
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	//------------------------------------------------------------------
	/**
	 * getBeanHandler
	 * @param type class type
	 * @return bean handler
	 */
	protected <T> BeanHandler<T> getBeanHandler(Class<T> type) {
		return beans.getBeanHandler(type);
	}

	/**
	 * retrieve output parameters
	 * @param cs statement
	 * @param parameters parameters 
	 * @throws SQLException if a SQL exception occurs
	 */
	protected static void logOutputParameters(CallableStatement cs, List<JdbcSqlParameter> parameters) throws SQLException {
		if (log.isDebugEnabled()) {
			StringBuilder sb = null;
			for (int i = 0; i < parameters.size(); i++) {
				JdbcSqlParameter parameter = parameters.get(i);
	
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
	protected static void logParameters(List<JdbcSqlParameter> parameters) {
		if (log.isDebugEnabled()) {
			for (int i = 0; i < parameters.size(); i++) {
				JdbcSqlParameter parameter = parameters.get(i);
				
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
		s.setQueryTimeout(timeout);
		return s;
	}

	/**
	 * createStatement
	 * @param sql sql
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement createStatement(String sql) throws SQLException {
		return createStatement(sql, false);
	}

	/**
	 * createStatement
	 * @param sql sql
	 * @param returnGK generated keys should be made available for retrieval
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement createStatement(String sql, boolean returnGK) throws SQLException {
		if (isCallableSql(sql)) {
			if (returnGK) {
				throw new IllegalArgumentException("Callable sql can not return generated keys");
			}

			CallableStatement cs;
			if (resultSetHoldability != 0) {
				cs = connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}
			else {
				cs = connection.prepareCall(sql, resultSetType, resultSetConcurrency);
			}
			cs.setQueryTimeout(timeout);
			return cs;
		}
		else {
			PreparedStatement ps;
			if (returnGK) {
				ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			}
			else if (resultSetHoldability != 0) {
				ps = connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
			}
			else {
				ps = connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
			}
			ps.setQueryTimeout(timeout);
			return ps;
		}
	}
	
	/**
	 * createStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @param returnGK generated keys should be made available for retrieval
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement createStatement(String sql, Object parameter,
			List<JdbcSqlParameter> sqlParams, boolean returnGK) throws SQLException {

		String jdbcSql = parseSqlStatement(sql, parameter, sqlParams);
		
		SqlLogger.logSql(jdbcSql);

		return createStatement(jdbcSql, returnGK);
	}

	/**
	 * parseSqlStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @return translated sql
	 */
	protected String parseSqlStatement(String sql, Object parameter, List<JdbcSqlParameter> sqlParams) {
		if (parameter != null) {
			if (parameter instanceof Iterable) {
				int i = 0;
				for (Object v : (Iterable<?>)parameter) {
					sqlParams.add(new JdbcSqlParameter(String.valueOf(i++), v, typeAdapters));
				}
			}
			else if (parameter instanceof Map) {
				int i = 0;
				for (Object v : ((Map<?, ?>)parameter).values()) {
					sqlParams.add(new JdbcSqlParameter(String.valueOf(i++), v, typeAdapters));
				}
			}
			else if (parameter instanceof byte[] || parameter instanceof char[]) {
				sqlParams.add(new JdbcSqlParameter("0", parameter, typeAdapters));
			}
			else {
				int i = 0;
				for (Object v : Iterators.asIterable(parameter)) {
					sqlParams.add(new JdbcSqlParameter(String.valueOf(i++), v, typeAdapters));
				}
			}
		}
		return sql;
	}

	/**
	 * setStatementParams
	 * @param ps prepared statement
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @throws SQLException if an sql error occurs
	 */
	@SuppressWarnings("unchecked")
	protected void setStatementParams(PreparedStatement ps, Object parameter, List<JdbcSqlParameter> sqlParams) throws SQLException {
		if (ps instanceof CallableStatement) {
			CallableStatement cs = (CallableStatement)ps;
			
			for (int i = 0; i < sqlParams.size(); i++) {
				JdbcSqlParameter sqlParam = sqlParams.get(i);
	
				if (sqlParam.isInputAllowed()) {
					sqlParam.getTypeAdapter().setParameter(cs, i + 1, sqlParam.getValue());
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
				JdbcSqlParameter sqlParam = sqlParams.get(i);
				sqlParam.getTypeAdapter().setParameter(ps, i + 1, sqlParam.getValue());
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
			List<JdbcSqlParameter> sqlParams) throws SQLException {
		return prepareStatement(sql, parameter, sqlParams, false);
	}

	/**
	 * prepareStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @param returnGK generated keys should be made available for retrieval
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement prepareStatement(String sql, Object parameter,
			List<JdbcSqlParameter> sqlParams, boolean returnGK) throws SQLException {
		PreparedStatement ps = createStatement(sql, parameter, sqlParams, returnGK);
		setStatementParams(ps, parameter, sqlParams);
		SqlLogger.logStatement(ps);
		return ps;
	}

	/**
	 * retrieve output parameters
	 * @param ps statement
	 * @param parameter parameter
	 * @param sqlParams sql parameters
	 * @throws SQLException if a SQL error occurs
	 */
	@SuppressWarnings("unchecked")
	protected void retrieveOutputParameters(PreparedStatement ps, Object parameter, List<JdbcSqlParameter> sqlParams) throws SQLException {
		if (ps instanceof CallableStatement) {
			CallableStatement cs = (CallableStatement)ps;

			logOutputParameters(cs, sqlParams);

			if (parameter != null) {
				BeanHandler beanHandler = getBeanHandler(parameter.getClass());
				for (int i = 0; i < sqlParams.size(); i++) {
					JdbcSqlParameter sqlParam = sqlParams.get(i);
		
					if (sqlParam.isOutputAllowed()) {
						Object value = sqlParam.getTypeAdapter().getResult(cs, i + 1);
						beanHandler.setBeanValue(parameter, sqlParam.getName(), value);
					}
				}
			}
		}
	}

	//------------------------------------------------------------------------------
	/**
	 * reset
	 */
	public void reset() {
		beans = sqlManager.getBeans();
		typeAdapters = sqlManager.getTypeAdapters();
		
		connection = null;
		resultSetType = ResultSet.TYPE_FORWARD_ONLY;
		resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
		resultSetHoldability = 0;
		timeout = 0;
	}

	//------------------------------------------------------------------------------
	// Override & Implements
	//------------------------------------------------------------------------------
	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE, 
	 * or DELETE statement or an SQL statement that returns nothing, 
	 * such as an SQL DDL statement. 
	 *
	 * @param sql The SQL statement to execute.
	 * @return The number of rows effected.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@Override
	public int execute(String sql) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("execute: " + sql);
		}

		Statement st = null;
		try {
			st = createStatement(); 
			st.execute(sql);

			int cnt = st.getUpdateCount();
			if (log.isDebugEnabled()) {
				log.debug("updated: " + sql);
			}
			return cnt;
		}
		finally {
			Sqls.safeClose(st);
		}
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
	public int execute(String sql, Object parameter) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("execute: " + sql);
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);
		try {
			ps.execute();
			retrieveOutputParameters(ps, parameter, sqlParams);

			int cnt = ps.getUpdateCount();
			log.debug("executed: " + cnt);
			return cnt;
		}
		finally {
			Sqls.safeClose(ps);
		}
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
				SqlLogger.logSql(sql);

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
			Sqls.safeClose(st);
		}
	}

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
	public int[] executeBatch(String sql, List<Object> parameters, int batchSize) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);
		Asserts.notEmpty(parameters);

		if (log.isDebugEnabled()) {
			log.debug("executeBatch: " + parameters.size() + ": " + sql);
		}

		if (batchSize >= parameters.size()) {
			batchSize = 0;
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
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
			Sqls.safeClose(ps);
		}
	}

	//---------------------------------------------------------------------------
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
		return fetch(sql, parameter, resultClass, null);
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
		return fetch(sql, parameter, (Class<T>)resultObject.getClass(),
			resultObject);
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
	protected <T> T fetch(String sql, Object parameter, Class<T> resultClass, T resultObject) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("fetch: " + sql);
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
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
			
			SqlResultSet<T> srs = new JdbcSqlResultSet<T>(this, rs, resultClass, resultObject); 
			resultObject = srs.getResult(resultObject);

			if (rs.next()) {
				log.warn("Too many results returned: " + sql);
			}
			return resultObject;
		}
		finally {
			Sqls.safeClose(rs);
			Sqls.safeClose(ps);
		}
	}

	//---------------------------------------------------------------------------
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
		return insert(sql, null, resultClass, null, null); 
	}

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
	public <T> T insert(String sql, Class<T> resultClass, String keyProp) throws SQLException {
		return insert(sql, null, resultClass, null, keyProp); 
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
	public <T> T insert(String sql, Object parameter, Class<T> resultClass) throws SQLException {
		return insert(sql, parameter, resultClass, null, null); 
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
	 * @param keyProp The primary key property name of the resultClass.
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	public <T> T insert(String sql, Object parameter, Class<T> resultClass, String keyProp) throws SQLException {
		return insert(sql, parameter, resultClass, null, keyProp); 
	}

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
	@SuppressWarnings("unchecked")
	public <T> T insert(String sql, T parameter, String keyProp) throws SQLException {
		return insert(sql, parameter, (Class<T>)parameter.getClass(), parameter, keyProp);
	}

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
	@SuppressWarnings("unchecked")
	public <T> T insert(String sql, Object parameter, T result, String keyProp) throws SQLException {
		return insert(sql, parameter, (Class<T>)result.getClass(), result, keyProp);
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
	 * @param resultObject The result object
	 * @param keyProp The primary key property of the class
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	protected <T> T insert(String sql, Object parameter, Class<T> resultClass, T resultObject, String keyProp)
			throws SQLException {

		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);
		Asserts.notNull(resultClass, "resultClass is null");
		
		if (log.isDebugEnabled()) {
			log.debug("insert: " + sql);
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams, true);
		try {
			ps.execute();
			
			retrieveOutputParameters(ps, parameter, sqlParams);

			int cnt = ps.getUpdateCount();
			log.debug("inserted: " + cnt);

			if (cnt != 1) {
				return null;
			}
			
			ResultSet rs = ps.getGeneratedKeys();
			if (rs == null || !rs.next()) {
				return getTypeAdapters().getCastors().cast(resultObject, resultClass);
			}
			
			SqlLogger.logResultHeader(rs);
			SqlLogger.logResultValues(rs);
			
			SqlResultSet<T> srs = new JdbcSqlResultSet<T>(this, rs, resultClass, resultObject, keyProp); 
			resultObject = srs.getResult(resultObject);

			return resultObject;
		}
		finally {
			Sqls.safeClose(ps);
		}
	}

	//---------------------------------------------------------------------------
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
	public <T> List<T> selectList(String sql, Class<T> resultClass, long skip, long max)
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
	@Override
	public <T> List<T> selectList(String sql, Object parameter, Class<T> resultClass, int skip, int max) throws SQLException {
		return selectList(sql, parameter, resultClass, (long)skip, (long)max);
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
	public <T> List<T> selectList(String sql, Object parameter, Class<T> resultClass, long skip, long max) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("selectList: " + sql);
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);
		
		ResultSet rs = null;
		try {
			ps.execute();

			retrieveOutputParameters(ps, parameter, sqlParams);
			
			rs = ps.getResultSet();

			List<T> resultList = new ArrayList<T>();
			if (rs != null) {
				SqlLogger.logResultHeader(rs);
	
				Sqls.skipResultSet(rs, skip);
	
				SqlResultSet<T> srs = new JdbcSqlResultSet<T>(this, rs, resultClass);
				
				int cnt = 0;
				while (rs.next()) {
					SqlLogger.logResultValues(rs);
	
					T resultObject = srs.getResult();
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
			Sqls.safeClose(rs);
			Sqls.safeClose(ps);
		}
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
	 * @param resultClass The class of result object 
	 * @param keyProp The property to be used as the key in the Map.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Class<T> resultClass, String keyProp)
			throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyProp, null);
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
	 * @param resultClass The class of result object 
	 * @param keyProp The property to be used as the key in the Map.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Class<T> resultClass, String keyProp, int skip, int max)
			throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyProp, null, skip, max);
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
	 * @param resultClass The class of result object 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Class<T> resultClass, String keyProp, long skip, long max)
			throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyProp, null, skip, max);
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
	 * @param keyProp         The property to be used as the key in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp) throws SQLException {
		return (Map<Object, T>) selectMap(sql, parameter, resultClass, keyProp, null);
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
	 * @param keyProp         The property to be used as the key in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp, int skip, int max) throws SQLException {
		return (Map<Object, T>) selectMap(sql, parameter, resultClass, keyProp, null, skip, max);
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
	 * @param keyProp         The property to be used as the key in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values being the result object instance.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<Object, T> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp, long skip, long max) throws SQLException {
		return (Map<Object, T>) selectMap(sql, parameter, resultClass, keyProp, null, skip, max);
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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<?, ?> selectMap(String sql, Class<T> resultClass, String keyProp,
			String valProp) throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyProp,
			valProp);
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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<?, ?> selectMap(String sql, Class<T> resultClass, String keyProp,
			String valProp, int skip, int max) throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyProp,
			valProp, skip, max);
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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param skip The number of results to ignore.
	 * @param max  The maximum number of results to return.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<?, ?> selectMap(String sql, Class<T> resultClass, String keyProp,
			String valProp, long skip, long max) throws SQLException {
		return (Map<Object, T>) selectMap(sql, null, resultClass, keyProp,
			valProp, skip, max);
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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an SQL error occurs.
	 */
	public <T> Map<?, ?> selectMap(String sql, Object parameter, Class<T> resultClass,
			String keyProp, String valProp) throws SQLException {
		return selectMap(sql, parameter, resultClass, keyProp, valProp, NO_SKIPPED_RESULTS,
			NO_MAXIMUM_RESULTS);
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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@Override
	public <T> Map<?, ?> selectMap(String sql, Object parameter, Class<T> resultClass, String keyProp,
			String valProp, int skip, int max) throws SQLException {
		return selectMap(sql, parameter, resultClass, keyProp,
			valProp, (long)skip, (long)max);
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
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param resultClass The class of result object 
	 * @return A Map keyed by keyProp with values of valueProp.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@Override
	public <T> Map<?, ?> selectMap(String sql, Object parameter, Class<T> resultClass, String keyProp,
			String valProp, long skip, long max) throws SQLException {

		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("selectMap: " + sql);
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);

		BeanHandler<T> beanHandler = getBeanHandler(resultClass);
		
		ResultSet rs = null;
		try {
			ps.execute();

			retrieveOutputParameters(ps, parameter, sqlParams);
			
			rs = ps.getResultSet();

			Map<Object, Object> map = new HashMap<Object, Object>();
			if (rs != null) {
				SqlLogger.logResultHeader(rs);
	
				Sqls.skipResultSet(rs, skip);
	
				SqlResultSet<T> srs = new JdbcSqlResultSet<T>(this, rs, resultClass);
				
				int cnt = 0;
				while (rs.next()) {
					SqlLogger.logResultValues(rs);
	
					T bean = srs.getResult();
					Object key = beanHandler.getBeanValue(bean, keyProp);
					Object value = null;
					if (valProp == null) {
						value = bean;
					}
					else {
						value = beanHandler.getBeanValue(bean, valProp);
					}
					map.put(key, value);
					
					if (max > 0) {
						cnt++;
						if (cnt >= max) {
							break;
						}
					}
				}
			}

			return map;
		}
		finally {
			Sqls.safeClose(rs);
			Sqls.safeClose(ps);
		}
	}

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
	public <T> SqlResultSet<T> selectResultSet(String sql, Class<T> resultClass) throws SQLException {
		return selectResultSet(sql, null, resultClass);
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
	public <T> SqlResultSet<T> selectResultSet(String sql, Object parameter, Class<T> resultClass) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("selectResultSet: " + sql);
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);

		ResultSet rs = ps.executeQuery();
		
		retrieveOutputParameters(ps, parameter, sqlParams);

		return new JdbcSqlResultSet<T>(this, rs, resultClass);
	}

	//---------------------------------------------------------------------------
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
	@Override
	public int update(String sql, Object parameter) throws SQLException {
		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("update: " + sql);
		}

		List<JdbcSqlParameter> sqlParams = new ArrayList<JdbcSqlParameter>();
		PreparedStatement ps = prepareStatement(sql, parameter, sqlParams);
		try {
			ps.execute();
			
			retrieveOutputParameters(ps, parameter, sqlParams);

			int cnt = ps.getUpdateCount();
			log.debug("updated: " + cnt);
			return cnt;
		}
		finally {
			Sqls.safeClose(ps);
		}
	}
}
