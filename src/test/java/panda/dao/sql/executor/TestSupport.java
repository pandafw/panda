package panda.dao.sql.executor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import panda.dao.sql.SqlUtils;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.log.Log;
import panda.log.Logs;
import panda.mock.sql.MockConnection;


/**
 * TestSupport
 */
public class TestSupport {
	private static Log log = Logs.getLog(TestSupport.class);
	private static Properties properties;

	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initSqliteTestData(Connection connection) throws Exception {
		execSQL(connection, "sqlite.sql", "\\;");
	}

	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initMysqlTestData(Connection connection) throws Exception {
		execSQL(connection, "mysql.sql", "\\;");
	}

	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initMssqlTestData(Connection connection) throws Exception {
		execSQL(connection, "mssql.sql", "\\;");
	}

	/**
	 * initOracleTestData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initOracleTestData(Connection connection) throws Exception {
		execSQL(connection, "oracle.sql", "\\/");
	}

	/**
	 * initDB2TestData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initDB2TestData(Connection connection) throws Exception {
		execSQL(connection, "db2.sql", "\\/");
	}

	/**
	 * initHsqldbData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initHsqldbTestData(Connection connection) throws Exception {
		execSQL(connection, "hsqldb.sql", "\\;");
	}
	
	/**
	 * initPostgreData
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initPostgreData(Connection connection) throws Exception {
		execSQL(connection, "postgre.sql", "\\;");
	}
	
	/**
	 * execSQL
	 * @param connection connection
	 * @param name resource name
	 * @param splitter splitter
	 * @throws Exception if an error occurs
	 */
	public static void execSQL(Connection connection, String name, String splitter) throws Exception {
		if (connection instanceof MockConnection) {
			return;
		}
		
		InputStream is = TestSupport.class.getResourceAsStream(name);

		StringBuilder sqls = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() < 1 || line.startsWith("--")) {
					sqls.append(" ");
				}
				else {
					sqls.append(" ").append(line);
				}
			}
		}
		finally {
			Streams.safeClose(is);
		}

		String all = sqls.toString();
		String[] ss = all.split(splitter);
		
		Statement stm = connection.createStatement();
		for (String sql : ss) {
			sql = sql.trim();
			if (sql.length() > 0) {
				try {
					log.debug("Execute SQL: " + sql);
					stm.executeUpdate(sql);
				}
				catch (Exception e) {
					log.error("Error: " + e.getMessage());
				}
			}
		}
		SqlUtils.safeClose(stm);
	}
	
	private static Connection getConnection(String name) throws Exception {
		if (properties == null) {
			properties = new Properties();
			properties.load(TestSupport.class.getResourceAsStream("jdbc.properties"));
		}
		
		String driver = properties.getProperty(name + ".driver");
		String jdbcurl = properties.getProperty(name + ".url");
		String username = properties.getProperty(name + ".username");
		String password = properties.getProperty(name + ".password");

		log.debug("Connect " + jdbcurl + " - " + username);
		
		try {
			Class.forName(driver);
			
			Connection c = DriverManager.getConnection(jdbcurl, username, password);
			c.setAutoCommit(true);
			return c;
		}
		catch (Exception ex) {
			log.warn("Failed to connect " + name + ": " + ex.getMessage());

			return new MockConnection();
		}
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
		Connection connection = getConnection("mssql");
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
