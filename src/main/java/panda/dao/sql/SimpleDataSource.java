package panda.dao.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import panda.castor.Castors;
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
 * ---- POOLING PROPERTIES ----
 * <ul>
 * <li>pool.maximumActiveConnections - default: 10</li>
 * <li>pool.maximumIdleConnections - default: 5</li>
 * <li>pool.maximumCheckoutTime - default: 20000</li>
 * <li>pool.timeToWait - default: 20000</li>
 * <li>pool.pingEnabled - default false</li>
 * <li>pool.pingQuery</li>
 * <li>pool.pingTimeout - default: 0</li>
 * <li>pool.pingConnectionsOlderThan - default: 0</li>
 * <li>pool.pingConnectionsNotUsedFor - default: 0</li>
 * </ul>
 * <p/>
 * 
 * @author yf.frank.wang@gmail.com
 */
public class SimpleDataSource implements DataSource {
	private static Log log = Logs.getLog(SimpleDataSource.class);

	//--------------------------------------------------------------------------------------
	// PROPERTY FIELDS FOR CONFIGURATION
	//--------------------------------------------------------------------------------------
	public static class JdbcConf {
		private String driver;
		private String url;
		private String username;
		private String password;
		private boolean autoCommit = false;
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean isAutoCommit() {
			return autoCommit;
		}
		public void setAutoCommit(boolean autoCommit) {
			this.autoCommit = autoCommit;
		}
	}

	public static class PoolConf {
		private int maximumActiveConnections = 10;
		private int maximumIdleConnections = 5;
		private int maximumCheckoutTime = 20000;
		private int timeToWait = 20000;
		private boolean pingEnabled = false;
		private String pingQuery;
		private int pingTimeout;
		private int pingConnectionsOlderThan;
		private int pingConnectionsNotUsedFor;
		public int getMaximumActiveConnections() {
			return maximumActiveConnections;
		}
		public void setMaximumActiveConnections(int maximumActiveConnections) {
			this.maximumActiveConnections = maximumActiveConnections;
		}
		public int getMaximumIdleConnections() {
			return maximumIdleConnections;
		}
		public void setMaximumIdleConnections(int maximumIdleConnections) {
			this.maximumIdleConnections = maximumIdleConnections;
		}
		public int getMaximumCheckoutTime() {
			return maximumCheckoutTime;
		}
		public void setMaximumCheckoutTime(int maximumCheckoutTime) {
			this.maximumCheckoutTime = maximumCheckoutTime;
		}
		public int getTimeToWait() {
			return timeToWait;
		}
		public void setTimeToWait(int timeToWait) {
			this.timeToWait = timeToWait;
		}
		public boolean isPingEnabled() {
			return pingEnabled;
		}
		public void setPingEnabled(boolean pingEnabled) {
			this.pingEnabled = pingEnabled;
		}
		public String getPingQuery() {
			return pingQuery;
		}
		public void setPingQuery(String pingQuery) {
			this.pingQuery = pingQuery;
		}
		public int getPingTimeout() {
			return pingTimeout;
		}
		public void setPingTimeout(int pingTimeout) {
			this.pingTimeout = pingTimeout;
		}
		public int getPingConnectionsOlderThan() {
			return pingConnectionsOlderThan;
		}
		public void setPingConnectionsOlderThan(int pingConnectionsOlderThan) {
			this.pingConnectionsOlderThan = pingConnectionsOlderThan;
		}
		public int getPingConnectionsNotUsedFor() {
			return pingConnectionsNotUsedFor;
		}
		public void setPingConnectionsNotUsedFor(int pingConnectionsNotUsedFor) {
			this.pingConnectionsNotUsedFor = pingConnectionsNotUsedFor;
		}
	}

	private JdbcConf jdbc = new JdbcConf();
	private PoolConf pool = new PoolConf();
	private Properties driver;

	//--------------------------------------------------------------------------------------
	// FIELDS LOCKED BY POOL_LOCK
	//--------------------------------------------------------------------------------------
	private final Object POOL_LOCK = new Object();

	private List<Connection> idleConnections = new ArrayList<Connection>();

	private List<Connection> activeConnections = new ArrayList<Connection>();

	private long requestCount = 0;

	private long accumulatedRequestTime = 0;

	private long accumulatedCheckoutTime = 0;

	private long claimedOverdueConnectionCount = 0;

	private long accumulatedCheckoutTimeOfOverdueConnections = 0;

	private long accumulatedWaitTime = 0;

	private long hadToWaitCount = 0;

	private long badConnectionCount = 0;

	private int expectedConnectionTypeCode;

	/**
	 * Constructor
	 */
	public SimpleDataSource() {
	}

	/**
	 * Constructor to allow passing in a map of properties for configuration
	 * 
	 * @param props - the configuration parameters
	 */
	public SimpleDataSource(Map<String, String> props) {
		initialize(props);
	}

	/**
	 * Constructor to allow passing in a map of properties for configuration
	 * 
	 * @param props - the configuration parameters
	 */
	@SuppressWarnings("unchecked")
	public SimpleDataSource(Properties props) {
		initialize((Map)props);
	}

	private void initialize(Map<String, String> props) {
		if (props == null) {
			throw new IllegalArgumentException(
					"SimpleDataSource: The properties map passed to the initializer was null.");
		}

		Castors.scastTo(props, this);
		
		initialize();
	}
	
	public void initialize() {
		Asserts.notEmpty(jdbc.driver, "The jdbc.driver property is empty.");
		Asserts.notEmpty(jdbc.url, "The jdbc.url property is empty.");
		Asserts.notNull(jdbc.username, "The jdbc.username property is null.");
		Asserts.notNull(jdbc.password, "The jdbc.password property is null.");

		try {
			Class.forName(jdbc.driver);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to initialize jdbc driver: " + jdbc.driver, e);
		}

		if (driver != null) {
			driver.put("user", jdbc.username);
			driver.put("password", jdbc.password);

			expectedConnectionTypeCode = assembleConnectionTypeCode(
					jdbc.url, jdbc.username, jdbc.password);

			if (pool.pingEnabled && Strings.isEmpty(pool.pingQuery)) {
				throw new RuntimeException("SimpleDataSource: property 'pool.pingEnabled' is true, but property 'pool.pingQuery' is not set correctly.");
			}
		}
	}

	private int assembleConnectionTypeCode(String url, String username, String password) {
		return ("" + url + username + password).hashCode();
	}

	/**
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return popConnection(jdbc.username, jdbc.password);
	}

	/**
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return popConnection(username, password);
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
	public JdbcConf getJdbc() {
		return jdbc;
	}

	/**
	 * @param jdbc the jdbc to set
	 */
	public void setJdbc(JdbcConf jdbc) {
		this.jdbc = jdbc;
	}

	/**
	 * @return the pool
	 */
	public PoolConf getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(PoolConf pool) {
		this.pool = pool;
	}

	/**
	 * @return the driver
	 */
	public Properties getDriver() {
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(Properties driver) {
		this.driver = driver;
	}

	private int getExpectedConnectionTypeCode() {
		return expectedConnectionTypeCode;
	}

	/**
	 * Getter for the number of connection requests made
	 * 
	 * @return The number of connection requests made
	 */
	public long getRequestCount() {
		return requestCount;
	}

	/**
	 * Getter for the average time required to get a connection to the database
	 * 
	 * @return The average time
	 */
	public long getAverageRequestTime() {
		return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
	}

	/**
	 * Getter for the average time spent waiting for connections that were in use
	 * 
	 * @return The average time
	 */
	public long getAverageWaitTime() {
		return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
	}

	/**
	 * Getter for the number of requests that had to wait for connections that were in use
	 * 
	 * @return The number of requests that had to wait
	 */
	public long getHadToWaitCount() {
		return hadToWaitCount;
	}

	/**
	 * Getter for the number of invalid connections that were found in the pool
	 * 
	 * @return The number of invalid connections
	 */
	public long getBadConnectionCount() {
		return badConnectionCount;
	}

	/**
	 * Getter for the number of connections that were claimed before they were returned
	 * 
	 * @return The number of connections
	 */
	public long getClaimedOverdueConnectionCount() {
		return claimedOverdueConnectionCount;
	}

	/**
	 * Getter for the average age of overdue connections
	 * 
	 * @return The average age
	 */
	public long getAverageOverdueCheckoutTime() {
		return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections
				/ claimedOverdueConnectionCount;
	}

	/**
	 * Getter for the average age of a connection checkout
	 * 
	 * @return The average age
	 */
	public long getAverageCheckoutTime() {
		return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
	}

	/**
	 * @return The count of active connections
	 */
	public int getActiveConnections() {
		return activeConnections.size();
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
		sb.append("\n jdbc.username                   ").append(jdbc.username);
		sb.append("\n jdbc.password                   ").append(jdbc.password);
		sb.append("\n jdbc.autoCommit                 ").append(jdbc.autoCommit);
		sb.append("\n pool.maxActiveConnections       ").append(pool.maximumActiveConnections);
		sb.append("\n pool.maxIdleConnections         ").append(pool.maximumIdleConnections);
		sb.append("\n pool.maxCheckoutTime            ").append(pool.maximumCheckoutTime);
		sb.append("\n pool.pingEnabled                ").append(pool.pingEnabled);
		sb.append("\n pool.pingQuery                  ").append(pool.pingQuery);
		sb.append("\n pool.timeToWait                 ").append(pool.timeToWait);
		sb.append("\n pool.pingConnectionsOlderThan   ").append(pool.pingConnectionsOlderThan);
		sb.append("\n pool.pingConnectionsNotUsedFor  ").append(pool.pingConnectionsNotUsedFor);
		sb.append("\n --------------------------------------------------------------");
		sb.append("\n activeConnections              ").append(activeConnections.size());
		sb.append("\n idleConnections                ").append(idleConnections.size());
		sb.append("\n requestCount                   ").append(getRequestCount());
		sb.append("\n averageRequestTime             ").append(getAverageRequestTime());
		sb.append("\n averageCheckoutTime            ").append(getAverageCheckoutTime());
		sb.append("\n claimedOverdue                 ").append(getClaimedOverdueConnectionCount());
		sb.append("\n averageOverdueCheckoutTime     ").append(getAverageOverdueCheckoutTime());
		sb.append("\n hadToWait                      ").append(getHadToWaitCount());
		sb.append("\n averageWaitTime                ").append(getAverageWaitTime());
		sb.append("\n badConnectionCount             ").append(getBadConnectionCount());
		sb.append("\n===============================================================");

		return sb.toString();
	}

	/**
	 * Closes all of the connections in the pool
	 */
	public void forceCloseAll() {
		synchronized (POOL_LOCK) {
			for (int i = activeConnections.size(); i > 0; i--) {
				try {
					SimplePooledConnection conn = (SimplePooledConnection) activeConnections
							.remove(i - 1);
					conn.invalidate();

					Connection realConn = conn.getRealConnection();
					if (!realConn.getAutoCommit()) {
						realConn.rollback();
					}
					realConn.close();
				}
				catch (Exception e) {
					// ignore
				}
			}
			for (int i = idleConnections.size(); i > 0; i--) {
				try {
					SimplePooledConnection conn = (SimplePooledConnection) idleConnections
							.remove(i - 1);
					conn.invalidate();

					Connection realConn = conn.getRealConnection();
					if (!realConn.getAutoCommit()) {
						realConn.rollback();
					}
					realConn.close();
				}
				catch (Exception e) {
					// ignore
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("SimpleDataSource forcefully closed/removed all connections.");
		}
	}

	protected void pushConnection(SimplePooledConnection conn) throws SQLException {
		synchronized (POOL_LOCK) {
			activeConnections.remove(conn);
			if (conn.isValid()) {
				if (idleConnections.size() < pool.maximumIdleConnections
						&& conn.getConnectionTypeCode() == getExpectedConnectionTypeCode()) {
					accumulatedCheckoutTime += conn.getCheckoutTime();
					if (!conn.getRealConnection().getAutoCommit()) {
						conn.getRealConnection().rollback();
					}
					SimplePooledConnection newConn = new SimplePooledConnection(
						conn.getRealConnection(), this);
					idleConnections.add(newConn);
					newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
					newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
					conn.invalidate();
					if (log.isDebugEnabled()) {
						log.debug("Returned connection " + newConn.getRealHashCode() + " to pool.");
					}
					POOL_LOCK.notifyAll();
				}
				else {
					accumulatedCheckoutTime += conn.getCheckoutTime();
					if (!conn.getRealConnection().getAutoCommit()) {
						conn.getRealConnection().rollback();
					}
					conn.getRealConnection().close();
					if (log.isDebugEnabled()) {
						log.debug("Closed connection " + conn.getRealHashCode() + ".");
					}
					conn.invalidate();
				}
			}
			else {
				if (log.isDebugEnabled()) {
					log.debug("A bad connection (" + conn.getRealHashCode()
							+ ") attempted to return to the pool, discarding connection.");
				}
				badConnectionCount++;
			}
		}
	}

	private SimplePooledConnection popConnection(String username, String password)
			throws SQLException {
		boolean countedWait = false;
		SimplePooledConnection conn = null;
		long t = System.currentTimeMillis();
		int localBadConnectionCount = 0;

		while (conn == null) {
			synchronized (POOL_LOCK) {
				if (idleConnections.size() > 0) {
					// Pool has available connection
					conn = (SimplePooledConnection) idleConnections.remove(0);
					if (log.isDebugEnabled()) {
						log.debug("Checked out connection " + conn.getRealHashCode()
								+ " from pool.");
					}
				}
				else {
					// Pool does not have available connection
					if (activeConnections.size() < pool.maximumActiveConnections) {
						// Can create new connection
						if (driver != null) {
							conn = new SimplePooledConnection(DriverManager.getConnection(jdbc.url,
								driver), this);
						}
						else {
							conn = new SimplePooledConnection(DriverManager.getConnection(jdbc.url,
								jdbc.username, jdbc.password), this);
						}
						Connection realConn = conn.getRealConnection();
						if (realConn.getAutoCommit() != jdbc.autoCommit) {
							realConn.setAutoCommit(jdbc.autoCommit);
						}
						if (log.isDebugEnabled()) {
							log.debug("Created connection " + conn.getRealHashCode() + ".");
						}
					}
					else {
						// Cannot create new connection
						SimplePooledConnection oldestActiveConnection = 
							(SimplePooledConnection) activeConnections.get(0);
						long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
						if (longestCheckoutTime > pool.maximumCheckoutTime) {
							// Can claim overdue connection
							claimedOverdueConnectionCount++;
							accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
							accumulatedCheckoutTime += longestCheckoutTime;
							activeConnections.remove(oldestActiveConnection);
							if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
								oldestActiveConnection.getRealConnection().rollback();
							}
							conn = new SimplePooledConnection(oldestActiveConnection
									.getRealConnection(), this);
							oldestActiveConnection.invalidate();
							if (log.isDebugEnabled()) {
								log.debug("Claimed overdue connection " + conn.getRealHashCode()
										+ ".");
							}
						}
						else {
							// Must wait
							try {
								if (!countedWait) {
									hadToWaitCount++;
									countedWait = true;
								}
								if (log.isDebugEnabled()) {
									log.debug("Waiting as long as " + pool.timeToWait
											+ " milliseconds for connection.");
								}
								long wt = System.currentTimeMillis();
								POOL_LOCK.wait(pool.timeToWait);
								accumulatedWaitTime += System.currentTimeMillis() - wt;
							}
							catch (InterruptedException e) {
								break;
							}
						}
					}
				}
				if (conn != null) {
					if (conn.isValid()) {
						if (!conn.getRealConnection().getAutoCommit()) {
							conn.getRealConnection().rollback();
						}
						conn.setConnectionTypeCode(assembleConnectionTypeCode(jdbc.url, username,
							password));
						conn.setCheckoutTimestamp(System.currentTimeMillis());
						conn.setLastUsedTimestamp(System.currentTimeMillis());
						activeConnections.add(conn);
						requestCount++;
						accumulatedRequestTime += System.currentTimeMillis() - t;
					}
					else {
						if (log.isDebugEnabled()) {
							log.debug("A bad connection (" + conn.getRealHashCode()
									+ ") was returned from the pool, getting another connection.");
						}
						badConnectionCount++;
						localBadConnectionCount++;
						conn = null;
						if (localBadConnectionCount > (pool.maximumIdleConnections + 3)) {
							if (log.isDebugEnabled()) {
								log.debug("SimpleDataSource: Could not get a good connection to the database.");
							}
							throw new SQLException("SimpleDataSource: Could not get a good connection to the database.");
						}
					}
				}
			}

		}

		if (conn == null) {
			if (log.isDebugEnabled()) {
				log.debug("SimpleDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
			}
			throw new SQLException("SimpleDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
		}

		return conn;
	}

	/**
	 * Method to check to see if a connection is still usable
	 * 
	 * @param conn - the connection to check
	 * @return True if the connection is still usable
	 */
	protected boolean pingConnection(SimplePooledConnection conn) {
		boolean result = true;

		try {
			result = !conn.getRealConnection().isClosed();
		}
		catch (SQLException e) {
			if (log.isDebugEnabled()) {
				log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
			}
			result = false;
		}

		if (result && pool.pingEnabled) {
			if ((pool.pingConnectionsOlderThan > 0 
					&& conn.getAge() > pool.pingConnectionsOlderThan)
					|| (pool.pingConnectionsNotUsedFor > 0 
							&& conn.getTimeElapsedSinceLastUse() > pool.pingConnectionsNotUsedFor)) {

				try {
					if (log.isDebugEnabled()) {
						log.debug("Testing connection " + conn.getRealHashCode() + " ...");
					}
					Connection realConn = conn.getRealConnection();
					Statement statement = realConn.createStatement();
					ResultSet rs = statement.executeQuery(pool.pingQuery);
					rs.close();
					statement.close();
					if (!realConn.getAutoCommit()) {
						realConn.rollback();
					}
					result = true;
					if (log.isDebugEnabled()) {
						log.debug("Connection " + conn.getRealHashCode() + " is GOOD!");
					}
				}
				catch (Exception e) {
					log.warn("Execution of ping query '" + pool.pingQuery + "' failed: "
							+ e.getMessage());
					try {
						conn.getRealConnection().close();
					}
					catch (Exception e2) {
						// ignore
					}
					result = false;
					if (log.isDebugEnabled()) {
						log.debug("Connection " + conn.getRealHashCode() + " is BAD: "
								+ e.getMessage());
					}
				}
			}
		}
		return result;
	}

	/**
	 * Unwraps a pooled connection to get to the 'real' connection
	 * 
	 * @param conn - the pooled connection to unwrap
	 * @return The 'real' connection
	 */
	public static Connection unwrapConnection(Connection conn) {
		if (conn instanceof SimplePooledConnection) {
			return ((SimplePooledConnection) conn).getRealConnection();
		}
		else {
			return conn;
		}
	}

	protected void finalize() throws Throwable {
		forceCloseAll();
	}

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
