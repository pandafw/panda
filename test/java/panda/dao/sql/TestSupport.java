package panda.dao.sql;

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
	private static Connection db2Connection;
	private static Connection hsqldbConnection;
	private static Connection mysqlConnection;
	private static Connection oracleConnection;
	private static Connection postgreConnection;

	/**
	 * @param connection connection
	 * @throws Exception if an error occurs
	 */
	public static void initMysqlTestData(Connection connection) throws Exception {
		execSQL(connection, "mysql.sql", "\\;");
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
			
			return DriverManager.getConnection(jdbcurl, username, password);
		}
		catch (Exception ex) {
			log.warn("Failed to load " + name + " driver: " + ex.getMessage());

			return new MockConnection();
		}
	}
	
	/**
	 * @return hsqldb connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getHsqldbConnection() throws Exception {
		if (hsqldbConnection == null) {
			hsqldbConnection = getConnection("hsqldb");
		}
		initHsqldbTestData(hsqldbConnection);
		return hsqldbConnection;
	}

	/**
	 * @return mysql connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getMysqlConnection() throws Exception {
		if (mysqlConnection == null) {
			mysqlConnection = getConnection("mysql");
		}
		initMysqlTestData(mysqlConnection);
		return mysqlConnection;
	}

	/**
	 * @return postgre connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getPostgreConnection() throws Exception {
		if (postgreConnection == null) {
			postgreConnection = getConnection("postgre");
		}
		initPostgreData(postgreConnection);
		return postgreConnection;
	}

	/**
	 * @return db2 connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getDB2Connection() throws Exception {
		if (db2Connection == null) {
			db2Connection = getConnection("db2");
		}
		initDB2TestData(db2Connection);
		return db2Connection;
	}

	/**
	 * @return oracle connection 
	 * @throws Exception if an error occurs
	 */
	public static Connection getOracleConnection() throws Exception {
		if (oracleConnection == null) {
			oracleConnection = getConnection("oracle");
		}
		initOracleTestData(oracleConnection);
		return oracleConnection;
	}
}
