package panda.dao.sql.executor;

import java.sql.Connection;

import panda.dao.sql.TestHelper;


/**
 * TestSupport
 */
public class TestSupport extends TestHelper {
	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initDerbyTestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "derby.sql", "\\;");
	}

	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initSqliteTestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "sqlite.sql", "\\;");
	}

	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initMysqlTestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "mysql.sql", "\\;");
	}

	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initMssqlTestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "mssql.sql", "\\;");
	}

	/**
	 * initOracleTestData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initOracleTestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "oracle.sql", "\\/");
	}

	/**
	 * initDB2TestData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initDB2TestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "db2.sql", "\\/");
	}

	/**
	 * initH2Data
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initH2TestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "h2.sql", "\\;");
	}

	/**
	 * initHsqldbData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initHsqldbTestData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "hsqldb.sql", "\\;");
	}
	
	/**
	 * initPostgreData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initPostgreData(Connection connection) throws Exception {
		execSQL(TestSupport.class, connection, "postgre.sql", "\\;");
	}
	
	/**
	 * @return derby connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getDerbyConnection() throws Exception {
		Connection connection = getConnection("derby");
		initDerbyTestData(connection);
		return connection;
	}
	
	/**
	 * @return sqlite connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getSqliteConnection() throws Exception {
		Connection connection = getConnection("sqlite");
		initSqliteTestData(connection);
		return connection;
	}
	
	/**
	 * @return h2 connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getH2Connection() throws Exception {
		Connection connection = getConnection("h2");
		initH2TestData(connection);
		return connection;
	}
	
	/**
	 * @return hsqldb connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getHsqldbConnection() throws Exception {
		Connection connection = getConnection("hsqldb");
		initHsqldbTestData(connection);
		return connection;
	}

	/**
	 * @return mssql connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getMssqlConnection() throws Exception {
		Connection connection = getConnection("mssql2005");
		connection.setAutoCommit(true);
		initMssqlTestData(connection);
		return connection;
	}

	/**
	 * @return mysql connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getMysqlConnection() throws Exception {
		Connection connection = getConnection("mysql");
		initMysqlTestData(connection);

//		connection.close();
//		connection = getConnection("mysql");
		return connection;
	}

	/**
	 * @return postgre connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getPostgreConnection() throws Exception {
		Connection connection = getConnection("postgre");
		initPostgreData(connection);
		return connection;
	}

	/**
	 * @return db2 connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getDB2Connection() throws Exception {
		Connection connection = getConnection("db2");
		initDB2TestData(connection);
		return connection;
	}

	/**
	 * @return oracle connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getOracleConnection() throws Exception {
		Connection connection = getConnection("oracle");
		initOracleTestData(connection);
		return connection;
	}
}
