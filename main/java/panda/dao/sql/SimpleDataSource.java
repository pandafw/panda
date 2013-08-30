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
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import panda.log.Log;
import panda.log.Logs;


/**
 * This is a simple, synchronous, thread-safe database connection pool.
 * <p/>
 * 
 * ---- REQUIRED PROPERTIES ----
 * <ul>
 * <li>JDBC.Driver</li>
 * <li>JDBC.ConnectionURL</li>
 * <li>JDBC.Username</li>
 * <li>JDBC.Password</li>
 * </ul>
 * <p/>
 * 
 * ---- POOLING PROPERTIES ----
 * <ul>
 * <li>JDBC.DefaultAutoCommit - default: false</li>
 * <li>Pool.MaximumActiveConnections - default: 10</li>
 * <li>Pool.MaximumIdleConnections - default: 5</li>
 * <li>Pool.MaximumCheckoutTime - default: 20000</li>
 * <li>Pool.TimeToWait - default: 20000</li>
 * <li>Pool.PingEnabled - default false</li>
 * <li>Pool.PingQuery</li>
 * <li>Pool.PingTimeout - default: 0</li>
 * <li>Pool.PingConnectionsOlderThan - default: 0</li>
 * <li>Pool.PingConnectionsNotUsedFor - default: 0</li>
 * </ul>
 * <p/>
 * 
 * @author yf.frank.wang@gmail.com
 */
public class SimpleDataSource implements DataSource {
	private static Log log = Logs.getLog(SimpleDataSource.class);

	//--------------------------------------------------------------------------------------
	// Required Properties
	//--------------------------------------------------------------------------------------
	/**
	 * PROP_JDBC_DRIVER = "JDBC.Driver";
	 */
	public static final String PROP_JDBC_DRIVER = "JDBC.Driver";

	/**
	 * PROP_JDBC_URL = "JDBC.ConnectionURL";
	 */
	public static final String PROP_JDBC_URL = "JDBC.ConnectionURL";

	/**
	 * PROP_JDBC_USERNAME = "JDBC.Username";
	 */
	public static final String PROP_JDBC_USERNAME = "JDBC.Username";

	/**
	 * PROP_JDBC_PASSWORD = "JDBC.Password";
	 */
	public static final String PROP_JDBC_PASSWORD = "JDBC.Password";

	//--------------------------------------------------------------------------------------
	// Optional Properties
	//--------------------------------------------------------------------------------------
	/**
	 * PROP_JDBC_DEFAULT_AUTOCOMMIT = "JDBC.DefaultAutoCommit"; // default: false
	 */
	public static final String PROP_JDBC_DEFAULT_AUTOCOMMIT = "JDBC.DefaultAutoCommit";

	/**
	 * PROP_POOL_MAX_ACTIVE_CONN = "Pool.MaximumActiveConnections"; // default: 10
	 */
	public static final String PROP_POOL_MAX_ACTIVE_CONN = "Pool.MaximumActiveConnections";

	/**
	 * PROP_POOL_MAX_IDLE_CONN = "Pool.MaximumIdleConnections"; // default: 5
	 */
	public static final String PROP_POOL_MAX_IDLE_CONN = "Pool.MaximumIdleConnections";

	/**
	 * PROP_POOL_MAX_CHECKOUT_TIME = "Pool.MaximumCheckoutTime"; // default: 20000
	 */
	public static final String PROP_POOL_MAX_CHECKOUT_TIME = "Pool.MaximumCheckoutTime";

	/**
	 * PROP_POOL_TIME_TO_WAIT = "Pool.TimeToWait"; // default: 20000
	 */
	public static final String PROP_POOL_TIME_TO_WAIT = "Pool.TimeToWait";

	/**
	 * PROP_POOL_PING_ENABLED = "Pool.PingEnabled"; // default: false
	 */
	public static final String PROP_POOL_PING_ENABLED = "Pool.PingEnabled";

	/**
	 * PROP_POOL_PING_QUERY = "Pool.PingQuery";
	 */
	public static final String PROP_POOL_PING_QUERY = "Pool.PingQuery";

	/**
	 * PROP_POOL_PING_TIMEOUT = "Pool.PingTimeout"; // default: 0
	 */
	public static final String PROP_POOL_PING_TIMEOUT = "Pool.PingTimeout";

	/**
	 * PROP_POOL_PING_CONN_OLDER_THAN = "Pool.PingConnectionsOlderThan"; // default: 0
	 */
	public static final String PROP_POOL_PING_CONN_OLDER_THAN = "Pool.PingConnectionsOlderThan";

	/**
	 * PROP_POOL_PING_CONN_NOT_USED_FOR = "Pool.PingConnectionsNotUsedFor"; // default: 0
	 */
	public static final String PROP_POOL_PING_CONN_NOT_USED_FOR = "Pool.PingConnectionsNotUsedFor";

	/**
	 * Additional Driver Properties prefix
	 */
	public static final String ADD_DRIVER_PROPS_PREFIX = "Driver.";

	private static final int ADD_DRIVER_PROPS_PREFIX_LENGTH = ADD_DRIVER_PROPS_PREFIX.length();

	//--------------------------------------------------------------------------------------
	// PROPERTY FIELDS FOR CONFIGURATION
	//--------------------------------------------------------------------------------------
	private String jdbcDriver;

	private String jdbcUrl;

	private String jdbcUsername;

	private String jdbcPassword;

	private boolean jdbcDefaultAutoCommit;

	private Properties driverProps;

	private boolean useDriverProps;

	private int poolMaximumActiveConnections;

	private int poolMaximumIdleConnections;

	private int poolMaximumCheckoutTime;

	private int poolTimeToWait;

	private boolean poolPingEnabled;

	private String poolPingQuery;

	private int poolPingTimeout;

	private int poolPingConnectionsOlderThan;

	private int poolPingConnectionsNotUsedFor;

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
		initialize((Map) props);
	}

	private void initialize(Map<String, String> props) {
		try {
			String prop_pool_ping_query = null;

			if (props == null) {
				throw new IllegalArgumentException(
						"SimpleDataSource: The properties map passed to the initializer was null.");
			}

			if (!(props.containsKey(PROP_JDBC_DRIVER) && props.containsKey(PROP_JDBC_URL)
					&& props.containsKey(PROP_JDBC_USERNAME) && props
					.containsKey(PROP_JDBC_PASSWORD))) {
				throw new IllegalArgumentException(
						"SimpleDataSource: JDBC properties were not set.");
			}
			else {
				jdbcDriver = props.get(PROP_JDBC_DRIVER);
				jdbcUrl = props.get(PROP_JDBC_URL);
				jdbcUsername = props.get(PROP_JDBC_USERNAME);
				jdbcPassword = props.get(PROP_JDBC_PASSWORD);

				poolMaximumActiveConnections = props.containsKey(PROP_POOL_MAX_ACTIVE_CONN) ? 
						Integer.parseInt(props.get(PROP_POOL_MAX_ACTIVE_CONN)) : 10;

				poolMaximumIdleConnections = props.containsKey(PROP_POOL_MAX_IDLE_CONN) ? 
						Integer.parseInt(props.get(PROP_POOL_MAX_IDLE_CONN)) : 5;

				poolMaximumCheckoutTime = props.containsKey(PROP_POOL_MAX_CHECKOUT_TIME) ? 
						Integer.parseInt(props.get(PROP_POOL_MAX_CHECKOUT_TIME)) : 20000;

				poolTimeToWait = props.containsKey(PROP_POOL_TIME_TO_WAIT) ? 
						Integer.parseInt(props.get(PROP_POOL_TIME_TO_WAIT)) : 20000;

				poolPingEnabled = props.containsKey(PROP_POOL_PING_ENABLED)
						&& Boolean.valueOf(props.get(PROP_POOL_PING_ENABLED)).booleanValue();

				prop_pool_ping_query = props.get(PROP_POOL_PING_QUERY);
				poolPingQuery = props.containsKey(PROP_POOL_PING_QUERY) ? 
						prop_pool_ping_query : "NO PING QUERY SET";

				poolPingTimeout = props.containsKey(PROP_POOL_PING_TIMEOUT) ? 
						Integer.parseInt(props.get(PROP_POOL_PING_TIMEOUT)) : 0;

				poolPingConnectionsOlderThan = props.containsKey(PROP_POOL_PING_CONN_OLDER_THAN) ? 
						Integer.parseInt(props.get(PROP_POOL_PING_CONN_OLDER_THAN)) : 0;

				poolPingConnectionsNotUsedFor = props.containsKey(PROP_POOL_PING_CONN_NOT_USED_FOR) ? 
						Integer.parseInt(props.get(PROP_POOL_PING_CONN_NOT_USED_FOR)) : 0;

				jdbcDefaultAutoCommit = props.containsKey(PROP_JDBC_DEFAULT_AUTOCOMMIT)
						&& Boolean.valueOf(props.get(PROP_JDBC_DEFAULT_AUTOCOMMIT)).booleanValue();

				useDriverProps = false;
				driverProps = new Properties();
				driverProps.put("user", jdbcUsername);
				driverProps.put("password", jdbcPassword);
				for (Entry<String, String> e : props.entrySet()) {
					String name = e.getKey();
					String value = e.getValue();
					if (name.startsWith(ADD_DRIVER_PROPS_PREFIX)) {
						driverProps.put(name.substring(ADD_DRIVER_PROPS_PREFIX_LENGTH), value);
						useDriverProps = true;
					}
				}

				expectedConnectionTypeCode = assembleConnectionTypeCode(
						jdbcUrl, jdbcUsername, jdbcPassword);

				Class.forName(jdbcDriver);

				if (poolPingEnabled
						&& (!props.containsKey(PROP_POOL_PING_QUERY) 
								|| prop_pool_ping_query.trim().length() == 0)) {
					throw new RuntimeException("SimpleDataSource: property '"
							+ PROP_POOL_PING_ENABLED + "' is true, but property '"
							+ PROP_POOL_PING_QUERY + "' is not set correctly.");
				}
			}

		}
		catch (Exception e) {
			throw new RuntimeException("SimpleDataSource: Error while loading properties. ", e);
		}
	}

	private int assembleConnectionTypeCode(String url, String username, String password) {
		return ("" + url + username + password).hashCode();
	}

	/**
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return popConnection(jdbcUsername, jdbcPassword);
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
	 * If a connection has not been used in this many milliseconds, ping the database to make sure
	 * the connection is still good.
	 * 
	 * @return the number of milliseconds of inactivity that will trigger a ping
	 */
	public int getPoolPingConnectionsNotUsedFor() {
		return poolPingConnectionsNotUsedFor;
	}

	/**
	 * Getter for the name of the JDBC driver class used
	 * 
	 * @return The name of the class
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * Getter of the JDBC URL used
	 * 
	 * @return The JDBC URL
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * Getter for the JDBC user name used
	 * 
	 * @return The user name
	 */
	public String getJdbcUsername() {
		return jdbcUsername;
	}

	/**
	 * Getter for the JDBC password used
	 * 
	 * @return The password
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}

	/**
	 * Getter for the maximum number of active connections
	 * 
	 * @return The maximum number of active connections
	 */
	public int getPoolMaximumActiveConnections() {
		return poolMaximumActiveConnections;
	}

	/**
	 * Getter for the maximum number of idle connections
	 * 
	 * @return The maximum number of idle connections
	 */
	public int getPoolMaximumIdleConnections() {
		return poolMaximumIdleConnections;
	}

	/**
	 * Getter for the maximum time a connection can be used before it *may* be given away again.
	 * 
	 * @return The maximum time
	 */
	public int getPoolMaximumCheckoutTime() {
		return poolMaximumCheckoutTime;
	}

	/**
	 * Getter for the time to wait before retrying to get a connection
	 * 
	 * @return The time to wait
	 */
	public int getPoolTimeToWait() {
		return poolTimeToWait;
	}

	/**
	 * Getter for the query to be used to check a connection
	 * 
	 * @return The query
	 */
	public String getPoolPingQuery() {
		return poolPingQuery;
	}

	/**
	 * Getter to tell if we should use the ping query
	 * 
	 * @return True if we need to check a connection before using it
	 */
	public boolean isPoolPingEnabled() {
		return poolPingEnabled;
	}

	/**
	 * @return poolPingTimeout
	 */
	public int getPoolPingTimeout() {
		return poolPingTimeout;
	}

	/**
	 * Getter for the age of connections that should be pinged before using
	 * 
	 * @return The age
	 */
	public int getPoolPingConnectionsOlderThan() {
		return poolPingConnectionsOlderThan;
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
		sb.append("\n jdbcDriver                     ").append(jdbcDriver);
		sb.append("\n jdbcUrl                        ").append(jdbcUrl);
		sb.append("\n jdbcUsername                   ").append(jdbcUsername);
		sb.append("\n jdbcPassword                   ").append(jdbcPassword);
		sb.append("\n jdbcDefaultAutoCommit          ").append(jdbcDefaultAutoCommit);
		sb.append("\n poolMaxActiveConnections       ").append(poolMaximumActiveConnections);
		sb.append("\n poolMaxIdleConnections         ").append(poolMaximumIdleConnections);
		sb.append("\n poolMaxCheckoutTime            ").append(poolMaximumCheckoutTime);
		sb.append("\n poolPingEnabled                ").append(poolPingEnabled);
		sb.append("\n poolPingQuery                  ").append(poolPingQuery);
		sb.append("\n poolTimeToWait                 ").append(poolTimeToWait);
		sb.append("\n poolPingConnectionsOlderThan   ").append(poolPingConnectionsOlderThan);
		sb.append("\n poolPingConnectionsNotUsedFor  ").append(poolPingConnectionsNotUsedFor);
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
				if (idleConnections.size() < poolMaximumIdleConnections
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
					if (activeConnections.size() < poolMaximumActiveConnections) {
						// Can create new connection
						if (useDriverProps) {
							conn = new SimplePooledConnection(DriverManager.getConnection(jdbcUrl,
								driverProps), this);
						}
						else {
							conn = new SimplePooledConnection(DriverManager.getConnection(jdbcUrl,
								jdbcUsername, jdbcPassword), this);
						}
						Connection realConn = conn.getRealConnection();
						if (realConn.getAutoCommit() != jdbcDefaultAutoCommit) {
							realConn.setAutoCommit(jdbcDefaultAutoCommit);
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
						if (longestCheckoutTime > poolMaximumCheckoutTime) {
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
									log.debug("Waiting as long as " + poolTimeToWait
											+ " milliseconds for connection.");
								}
								long wt = System.currentTimeMillis();
								POOL_LOCK.wait(poolTimeToWait);
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
						conn.setConnectionTypeCode(assembleConnectionTypeCode(jdbcUrl, username,
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
						if (localBadConnectionCount > (poolMaximumIdleConnections + 3)) {
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

		if (result && poolPingEnabled) {
			if ((poolPingConnectionsOlderThan > 0 
					&& conn.getAge() > poolPingConnectionsOlderThan)
					|| (poolPingConnectionsNotUsedFor > 0 
							&& conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor)) {

				try {
					if (log.isDebugEnabled()) {
						log.debug("Testing connection " + conn.getRealHashCode() + " ...");
					}
					Connection realConn = conn.getRealConnection();
					Statement statement = realConn.createStatement();
					ResultSet rs = statement.executeQuery(poolPingQuery);
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
					log.warn("Execution of ping query '" + poolPingQuery + "' failed: "
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
