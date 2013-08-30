package panda.dao.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * This is a automatic datasource implementation.
 * 
 * @author yf.frank.wang@gmail.com
 */
public class AutoDataSource implements DataSource {
	private static final Log log = Logs.get();

	private String username;
	private String password;
	protected String driverClassName;
	private String jdbcUrl;

	public AutoDataSource() {
		log.warn("AutoDataSource is use for Test/Attempt, NOT Using in Production environment!");
		log.warn("AutoDataSource is NOT a Connection Pool, So it is slow but safe for debug/study");
	}

	public Connection getConnection() throws SQLException {
		return getConnection(username, password);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		Connection conn;
		if (username != null)
			conn = DriverManager.getConnection(jdbcUrl, username, password);
		else
			conn = DriverManager.getConnection(jdbcUrl);
		return conn;
	}

	public void close() {
	}

	public void setDriverClassName(String driverClassName) throws ClassNotFoundException {
		Class.forName(driverClassName);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	// add all supported jdbc drivers
	static {
		String[] drivers = { 
				"org.h2.Driver", 
				"org.hsqldb.jdbcDriver",
				"org.postgresql.Driver",
				"org.sqlite.JDBC",
				"oracle.jdbc.OracleDriver", 
				"com.ibm.db2.jcc.DB2Driver", 
				"com.microsoft.sqlserver.jdbc.SQLServerDriver", 
				"com.mysql.jdbc.Driver" 
				};
		for (String driverClassName : drivers) {
			try {
				Class.forName(driverClassName);
			}
			catch (Throwable e) {
			}
		}
	}

	// ---------------------------------------------------------------
	public PrintWriter getLogWriter() throws SQLException {
		throw Exceptions.unsupported();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		throw Exceptions.unsupported();
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		throw Exceptions.unsupported();
	}

	public int getLoginTimeout() throws SQLException {
		throw Exceptions.unsupported();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw Exceptions.unsupported();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw Exceptions.unsupported();
	}

	public Logger getParentLogger() {
		throw Exceptions.unsupported();
	}
}
