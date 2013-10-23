package panda.dao.sql.executor;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
import panda.dao.sql.adapter.TypeAdapter;
import panda.lang.Asserts;
import panda.log.Log;
import panda.log.Logs;


/**
 * @author yf.frank.wang@gmail.com
 */
public class JdbcSqlExecutor extends SqlExecutor {
	private static Log log = Logs.getLog(JdbcSqlExecutor.class);

	/**
	 * Constructor
	 * @param sqlManager sqlManager
	 */
	protected JdbcSqlExecutor(SqlManager sqlManager) {
		super(sqlManager);
	}

	/**
	 * retrieve output parameters
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
	 * @param params parameters
	 */
	protected static void logParameters(List<?> params) {
		if (params != null && !params.isEmpty() && log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Parameters: [");
			for (Object p : params) {
				sb.append(' ');
				if (p == null) {
					sb.append("NULL");
				}
				else {
					sb.append(p.getClass().getName()).append(": ").append(p);
				}
				sb.append(",");
			}
			
			sb.setCharAt(sb.length() - 1, ' ');
			sb.append(']');
			log.debug(sb.toString());
		}
	}

	/**
	 * getBeanHandler
	 * @param type class type
	 * @return bean handler
	 */
	@SuppressWarnings("unchecked")
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
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement prepareStatement(String sql) throws SQLException {
		return prepareStatement(sql, false);
	}

	/**
	 * prepareStatement
	 * @param sql sql
	 * @param returGK generated keys should be made available for retrieval
	 * @return JDBC PreparedStatement
	 * @throws SQLException if an sql error occurs
	 */
	protected PreparedStatement prepareStatement(String sql, boolean returnGK) throws SQLException {
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
			return ps;
		}
	}

	/**
	 * set parameters
	 * @param ps prepared statement
	 * @param params sql parameter list
	 * @throws SQLException if an sql error occurs
	 */
	protected void setStatementParams(PreparedStatement ps, List<?> params) throws SQLException {
		if (params == null) {
			return;
		}
		
		for (int i = 0; i < params.size(); i++) {
			Object p = params.get(i);
			if (p == null) {
				ps.setNull(i + 1, Types.VARCHAR);
			}
			else {
				TypeAdapter ta = typeAdapters.getTypeAdapter(p.getClass());
				if (ta == null) {
					
				}
				ta.setParameter(ps, i + 1, p);
			}
		}
		
		SqlLogger.logParameters(ps);
		logParameters(params);
	}

	/**
	 * retrieve output parameters
	 * @param ps statement
	 * @param parameter parameter
	 * @param sqlParams sql parameters
	 * @throws SQLException if a SQL error occurs
	 */
	@SuppressWarnings("unchecked")
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
	 * @param keyProp The primary key property of the class
	 * @return The primary key of the newly inserted row. This might be
	 *         automatically generated by the RDBMS, or selected from a sequence
	 *         table or other source.
	 * @throws java.sql.SQLException If an error occurs.
	 */
	@Override
	protected <T> T insert(String sql, Object parameter, Class<T> resultClass, T resultObject, String keyProp)
			throws SQLException {

		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);
		Asserts.notNull(resultClass, "resultClass is null");
		
		if (log.isDebugEnabled()) {
			log.debug("insert: " + sql);
		}

		PreparedStatement ps = prepareStatement(sql, true);
		try {
			setStatementParams(ps, (List)parameter);
			
			ps.execute();
			
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
			
			SqlResultSet<T> srs = new SqlResultSet<T>(this, rs, resultClass, resultObject, keyProp); 
			resultObject = srs.getResult(resultObject);

			return resultObject;
		}
		finally {
			Sqls.safeClose(ps);
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
	protected <T> T fetch(String sql, Object parameter, Class<T> resultClass, T resultObject) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("fetch: " + sql);
		}

		ResultSet rs = null;
		PreparedStatement ps = prepareStatement(sql);
		try {
			setStatementParams(ps, (List)parameter);
			
			ps.execute();

			rs = ps.getResultSet();
			if (rs == null || !rs.next()) {
				return null;
			}
			
			SqlLogger.logResultHeader(rs);
			SqlLogger.logResultValues(rs);
			
			SqlResultSet<T> srs = new SqlResultSet<T>(this, rs, resultClass, resultObject); 
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
			log.debug("selectList: " + sql);
		}

		ResultSet rs = null;
		PreparedStatement ps = prepareStatement(sql);
		try {
			setStatementParams(ps, (List)parameter);
			
			ps.execute();

			rs = ps.getResultSet();

			List<T> resultList = new ArrayList<T>();
			if (rs != null) {
				SqlLogger.logResultHeader(rs);
	
				Sqls.skipResultSet(rs, skip);
	
				SqlResultSet<T> srs = new SqlResultSet<T>(this, rs, resultClass);
				
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
	@SuppressWarnings("unchecked")
	public <T> Map selectMap(String sql, Object parameter, Class<T> resultClass, String keyPropertyName,
			String valuePropertyName, int skip, int max) throws SQLException {

		Asserts.notBlank(sql, BLANK_SQL_MESSAGE);

		if (log.isDebugEnabled()) {
			log.debug("selectMap: " + sql);
		}

		BeanHandler beanHandler = getBeanHandler(resultClass);

		ResultSet rs = null;
		PreparedStatement ps = prepareStatement(sql);
		try {
			setStatementParams(ps, (List)parameter);
			
			ps.execute();

			rs = ps.getResultSet();

			Map map = new HashMap();
			if (rs != null) {
				SqlLogger.logResultHeader(rs);
	
				Sqls.skipResultSet(rs, skip);
	
				SqlResultSet<T> srs = new SqlResultSet<T>(this, rs, resultClass);
				
				int cnt = 0;
				while (rs.next()) {
					SqlLogger.logResultValues(rs);
	
					T bean = srs.getResult();
					Object key = beanHandler.getBeanValue(bean, keyPropertyName);
					Object value = null;
					if (valuePropertyName == null) {
						value = bean;
					}
					else {
						value = beanHandler.getBeanValue(bean, valuePropertyName);
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

		PreparedStatement ps = prepareStatement(sql);
		setStatementParams(ps, (List)parameter);

		ResultSet rs = ps.executeQuery();
		
		return new SqlResultSet<T>(this, rs, resultClass);
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

		PreparedStatement ps = prepareStatement(sql);
		try {
			setStatementParams(ps, (List)parameter);
			
			ps.execute();
			
			int cnt = ps.getUpdateCount();
			log.debug("updated: " + cnt);
			return cnt;
		}
		finally {
			Sqls.safeClose(ps);
		}
	}

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
			log.debug("executed: " + cnt);
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

		PreparedStatement ps = prepareStatement(sql);
		try {
			setStatementParams(ps, (List)parameter);
			ps.execute();
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

		PreparedStatement ps = prepareStatement(sql);

		int[] rc = null;
		try {
			int cnt = 0;
			int pos = 0;
			for (Object param : parameters) {
				setStatementParams(ps, (List)param);
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
}
