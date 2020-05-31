package panda.dao.sql.dbcp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.sql.DataSource;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * This is a simple, synchronous, thread-safe database connection pool.
 * <p/>
 * 
 * ---- JDBC PROPERTIES ----
 * <ul>
 * <li>jdbc.driver</li>
 * <li>jdbc.url</li>
 * <li>jdbc.username</li>
 * <li>jdbc.password</li>
 * <li>jdbc.autoCommit - default: false</li>
 * <li>jdbc.readOnly - default: false</li>
 * <li>jdbc.transactionLevel - default: none</li>
 * </ul>
 * <p/>
 * 
 */
public abstract class AbstractDataSource implements DataSource {
	private static Log log = Logs.getLog(AbstractDataSource.class);

	protected JdbcConfig jdbc = new JdbcConfig();

	/**
	 * Constructor
	 */
	public AbstractDataSource() {
	}

	/**
	 * @see javax.sql.DataSource#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return popConnection();
	}

	/**
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		if (Strings.equals(jdbc.getUsername(), username) && Strings.equals(jdbc.getPassword(), password)) {
			return popConnection();
		}

		if (log.isWarnEnabled()) {
			log.warn("Create a non-pooled connection due to different username/password.");
		}
		return DriverManager.getConnection(jdbc.url, username, password);
	}

	/**
	 * @see javax.sql.DataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int loginTimeout) throws SQLException {
		DriverManager.setLoginTimeout(loginTimeout);
	}

	/**
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	/**
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		DriverManager.setLogWriter(logWriter);
	}

	/**
	 * @see javax.sql.DataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	/**
	 * @return the jdbc
	 */
	public JdbcConfig getJdbc() {
		return jdbc;
	}

	/**
	 * @param jdbc the jdbc to set
	 */
	public void setJdbc(JdbcConfig jdbc) {
		this.jdbc = jdbc;
	}

	protected void checkoutConnection(Connection con) throws SQLException {
		if (con.isReadOnly() != jdbc.readOnly) {
			con.setReadOnly(jdbc.readOnly);
		}
		if (con.getAutoCommit() != jdbc.autoCommit) {
			con.setAutoCommit(jdbc.autoCommit);
		}
		if (jdbc.transactionIsolation != Connection.TRANSACTION_NONE) {
			if (con.getTransactionIsolation() != jdbc.transactionIsolation) {
				con.setTransactionIsolation(jdbc.transactionIsolation);
			}
		}
	}
	
	/**
	 * Returns the status of the connection pool
	 * 
	 * @return The status
	 */
	public String getStatus() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n===============================================================");
		sb.append("\n jdbc.driver                     ").append(jdbc.driver);
		sb.append("\n jdbc.url                        ").append(jdbc.url);
		sb.append("\n jdbc.autoCommit                 ").append(jdbc.autoCommit);
		sb.append("\n jdbc.readOnly                   ").append(jdbc.readOnly);
		sb.append("\n jdbc.transactionLevel           ").append(jdbc.getTransactionLevel());
		sb.append("\n===============================================================");
		for (Entry en : jdbc.prop.entrySet()) {
			String k = (String)en.getKey();
			sb.append("\n jdbc.prop.").append(Strings.rightPad(k, 22)).append(en.getValue());
		}
		sb.append("\n===============================================================");

		return sb.toString();
	}

	protected abstract Connection popConnection() throws SQLException;

	/**
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	/**
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	//--------------------------------------------------------------------
	// JDK 1.7 Methods below
	//--------------------------------------------------------------------
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
