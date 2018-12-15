package panda.dao.sql.dbcp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import panda.cast.Castors;
import panda.lang.Asserts;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * This is a simple, synchronous, thread-safe database connection pool.
 * <p/>
 * 
 * ---- REQUIRED PROPERTIES ----
 * <ul>
 * <li>jdbc.driver</li>
 * <li>jdbc.url</li>
 * <li>jdbc.username</li>
 * <li>jdbc.password</li>
 * <li>jdbc.autoCommit - default: false</li>
 * </ul>
 * <p/>
 * 
 */
public abstract class AbstractDataSource implements DataSource {
	private static Log log = Logs.getLog(AbstractDataSource.class);

	protected JdbcConfig jdbc = new JdbcConfig();
	protected Properties props = new Properties();

	/**
	 * Constructor
	 */
	public AbstractDataSource() {
	}

	/**
	 * Constructor to allow passing in a map of properties for configuration
	 * 
	 * @param props - the configuration parameters
	 */
	public void initialize(Properties props) {
		initialize((Map<?, ?>)props);
	}

	/**
	 * Constructor to allow passing in a map of properties for configuration
	 * 
	 * @param props - the configuration parameters
	 */
	public void initialize(Map<?, ?> props) {
		if (props == null) {
			throw new NullPointerException("The properties passed to the initializer was null.");
		}

		Castors.scastTo(props, this);
		
		initialize();
	}
	
	public void initialize() {
		Asserts.notEmpty(jdbc.driver, "The jdbc.driver property is empty.");
		Asserts.notEmpty(jdbc.url, "The jdbc.url property is empty.");

		try {
			Class.forName(jdbc.driver);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to initialize jdbc driver: " + jdbc.driver, e);
		}

		if (jdbc.username != null) {
			props.put("user", jdbc.username);
		}
		if (jdbc.password != null) {
			props.put("password", jdbc.password);
		}
	}

	/**
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return popConnection();
	}

	/**
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		if (Strings.equals(jdbc.username, username) && Strings.equals(jdbc.password, password)) {
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
	public void setLoginTimeout(int loginTimeout) throws SQLException {
		DriverManager.setLoginTimeout(loginTimeout);
	}

	/**
	 * @see javax.sql.DataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	/**
	 * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		DriverManager.setLogWriter(logWriter);
	}

	/**
	 * @see javax.sql.DataSource#getLogWriter()
	 */
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

	/**
	 * @return the driver
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setProps(Properties driver) {
		this.props = driver;
	}

	/**
	 * Returns the status of the connection pool
	 * 
	 * @return The status
	 */
	public String getStatus() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n===============================================================");
		sb.append("\n jdbc.driver                     ").append(jdbc.getDriver());
		sb.append("\n jdbc.url                        ").append(jdbc.getUrl());
		sb.append("\n jdbc.username                   ").append(jdbc.getUsername());
		sb.append("\n jdbc.password                   ").append(jdbc.getPassword());
		sb.append("\n jdbc.autoCommit                 ").append(jdbc.isAutoCommit());
		sb.append("\n===============================================================");

		return sb.toString();
	}

	protected abstract Connection popConnection() throws SQLException;

	/**
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	/**
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	//--------------------------------------------------------------------
	// JDK 1.7 Methods below
	//--------------------------------------------------------------------
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
}
