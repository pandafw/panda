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

import panda.cast.Castors;
import panda.lang.Asserts;
import panda.lang.Strings;
import panda.lang.Threads;
import panda.lang.time.DateTimes;
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
 * <li>pool.maxActive - default: 10</li>
 * <li>pool.maxIdle - default: 5</li>
 * <li>pool.maxCheckoutTime - default: 1 week (milliseconds)</li>
 * <li>pool.pingQuery</li>
 * <li>pool.pingTimeout - default: 1 second (milliseconds)</li>
 * <li>pool.pingOlderThan - default: 0 (milliseconds)</li>
 * <li>pool.pingNotUsedFor - default: 600000 (milliseconds)</li>
 * <li>pool.timeToWait - default: 20000 (milliseconds)</li>
 * </ul>
 * <p/>
 * 
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
		private int maxActive = 10;
		private int maxIdle = 5;
		private long maxCheckoutTime = DateTimes.MS_WEEK;
		private String pingQuery = "";
		private int pingTimeout = 1;
		private long pingOlderThan;
		private long pingNotUsedFor = 600000;
		private long timeToWait = 20000;

		public int getMaxActive() {
			return maxActive;
		}
		public void setMaxActive(int maxActive) {
			this.maxActive = maxActive;
		}
		public int getMaxIdle() {
			return maxIdle;
		}
		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}
		public long getMaxCheckoutTime() {
			return maxCheckoutTime;
		}
		public void setMaxCheckoutTime(long maxCheckoutTime) {
			this.maxCheckoutTime = maxCheckoutTime;
		}
		public String getPingQuery() {
			return pingQuery;
		}
		public void setPingQuery(String pingQuery) {
			this.pingQuery = Strings.stripToEmpty(pingQuery);
		}
		public long getPingTimeout() {
			return pingTimeout * 1000L;
		}
		public void setPingTimeout(long pingTimeout) {
			this.pingTimeout = (int)(pingTimeout / 1000);
			if (this.pingTimeout < 1) {
				this.pingTimeout = 1;
			}
		}
		public long getPingOlderThan() {
			return pingOlderThan;
		}
		public void setPingOlderThan(long pingOlderThan) {
			this.pingOlderThan = pingOlderThan;
		}
		public long getPingNotUsedFor() {
			return pingNotUsedFor;
		}
		public void setPingNotUsedFor(long pingNotUsedFor) {
			this.pingNotUsedFor = pingNotUsedFor;
		}
		public long getTimeToWait() {
			return timeToWait;
		}
		public void setTimeToWait(long timeToWait) {
			this.timeToWait = timeToWait;
		}
	}

	private JdbcConf jdbc = new JdbcConf();
	private PoolConf pool = new PoolConf();
	private Properties props = new Properties();

	//--------------------------------------------------------------------------------------
	// FIELDS LOCKED BY POOL_LOCK
	//--------------------------------------------------------------------------------------
	private final Object POOL_LOCK = new Object();

	private List<SimplePooledConnection> idles = new ArrayList<SimplePooledConnection>();

	private List<SimplePooledConnection> actives = new ArrayList<SimplePooledConnection>();

	private long requestCount = 0;

	private long accumulatedRequestTime = 0;

	private long accumulatedCheckoutTime = 0;

	private long claimedOverdueConnectionCount = 0;

	private long accumulatedCheckoutTimeOfOverdueConnections = 0;

	private long accumulatedWaitTime = 0;

	private long hadToWaitCount = 0;

	private long badConnectionCount = 0;

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
	public SimpleDataSource(Properties props) {
		initialize((Map<?, ?>)props);
	}

	private void initialize(Map<?, ?> props) {
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
	public int getActives() {
		return actives.size();
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
		sb.append("\n pool.maxActive                  ").append(pool.getMaxActive());
		sb.append("\n pool.maxIdle                    ").append(pool.getMaxIdle());
		sb.append("\n pool.maxCheckoutTime            ").append(pool.getMaxCheckoutTime());
		sb.append("\n pool.pingQuery                  ").append(pool.getPingQuery());
		sb.append("\n pool.pingTimeout                ").append(pool.getPingTimeout());
		sb.append("\n pool.pingOlderThan              ").append(pool.getPingOlderThan());
		sb.append("\n pool.pingNotUsedFor             ").append(pool.getPingNotUsedFor());
		sb.append("\n pool.timeToWait                 ").append(pool.getTimeToWait());
		sb.append("\n --------------------------------------------------------------");
		sb.append("\n activeConnections              ").append(actives.size());
		sb.append("\n idleConnections                ").append(idles.size());
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
	 * close connection and remove from pool
	 * @param conns connection list
	 */
	private void close(List<SimplePooledConnection> conns) {
		for (int i = conns.size(); i > 0; i--) {
			try {
				SimplePooledConnection conn = conns.remove(i - 1);
				conn.invalidate();

				Connection rcon = conn.getRealConnection();
				try {
					if (!rcon.getAutoCommit()) {
						rcon.rollback();
					}
				}
				catch (SQLException e) {
					// ignore
				}
				finally {
					Sqls.safeClose(rcon);
				}
			}
			catch (Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Closes all of the connections in the pool
	 */
	public void close() {
		synchronized (POOL_LOCK) {
			close(actives);
			close(idles);
		}

		if (log.isDebugEnabled()) {
			log.debug("SimpleDataSource forcefully closed/removed all connections.");
		}
	}

	protected void pushConnection(SimplePooledConnection conn) throws SQLException {
		synchronized (POOL_LOCK) {
			actives.remove(conn);
			if (!conn.isValid()) {
				if (log.isDebugEnabled()) {
					log.debug("A bad connection (" + conn.getRealHashCode()
							+ ") attempted to return to the pool, discarding connection.");
				}
				badConnectionCount++;
				return;
			}
			
			accumulatedCheckoutTime += conn.getCheckoutTime();

			boolean valid = true;
			Connection rcon = conn.getRealConnection();
			try {
				if (!rcon.getAutoCommit()) {
					rcon.rollback();
				}
			}
			catch (SQLException e) {
				if (log.isDebugEnabled()) {
					log.debug("Failed to rollback returned connection " + conn.getRealHashCode() + ": " + e.getMessage());
				}
				valid = false;
			}
			
			if (valid && idles.size() < pool.maxIdle) {
				SimplePooledConnection newConn = new SimplePooledConnection(rcon, this);
				newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
				newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
				idles.add(newConn);

				conn.invalidate();
				if (log.isDebugEnabled()) {
					log.debug("Returned connection " + newConn.getRealHashCode() + " to pool.");
				}
				POOL_LOCK.notifyAll();
			}
			else {
				Sqls.safeClose(rcon);

				conn.invalidate();

				if (log.isDebugEnabled()) {
					log.debug("Closed connection " + conn.getRealHashCode() + ".");
				}
			}
		}
	}

	private SimplePooledConnection popConnection() throws SQLException {
		long start = System.currentTimeMillis();
		int bad = 0, wait = 0;

		synchronized (POOL_LOCK) {
			while (true) {
				if (idles.size() > 0) {
					// Pool has available connection
					SimplePooledConnection conn = idles.remove(0);
					if (log.isDebugEnabled()) {
						log.debug("Checked out connection " + conn.getRealHashCode() + " from pool.");
					}
					if (conn.testValid()) {
						checkoutConnection(conn, start);
						return conn;
					}

					bad++;
					badConnectionCount++;
					if (log.isDebugEnabled()) {
						log.debug("A bad connection (" + conn.getRealHashCode()
								+ ") was returned from the pool, getting another connection " + bad);
					}
					continue;
				}

				// Pool does not have available connection
				if (actives.size() < pool.maxActive) {
					// create new connection
					SimplePooledConnection conn = new SimplePooledConnection(DriverManager.getConnection(jdbc.url, props), this);
					Connection rcon = conn.getRealConnection();
					if (rcon.getAutoCommit() != jdbc.autoCommit) {
						rcon.setAutoCommit(jdbc.autoCommit);
					}
					if (log.isDebugEnabled()) {
						log.debug("Created connection " + conn.getRealHashCode() + ".");
					}
					checkoutConnection(conn, start);
					return conn;
				}

				// Cannot create new connection
				SimplePooledConnection oldest = (SimplePooledConnection)actives.get(0);
				long longestCheckoutTime = oldest.getCheckoutTime();
				if (longestCheckoutTime > pool.maxCheckoutTime) {
					// Can claim overdue connection
					claimedOverdueConnectionCount++;
					accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
					accumulatedCheckoutTime += longestCheckoutTime;
					actives.remove(oldest);
					
					SimplePooledConnection conn = new SimplePooledConnection(oldest.getRealConnection(), this);
					oldest.invalidate();
					if (log.isDebugEnabled()) {
						log.debug("Claimed overdue connection " + conn.getRealHashCode() + ".");
					}
					if (conn.testValid()) {
						checkoutConnection(conn, start);
						return conn;
					}

					bad++;
					badConnectionCount++;
					if (log.isDebugEnabled()) {
						log.debug("A bad connection (" + conn.getRealHashCode()
								+ ") was returned from the pool, getting another connection " + bad);
					}
					continue;
				}

				// waited and timeout??
				if (wait > 1 && start + pool.timeToWait < System.currentTimeMillis()) {
					if (log.isDebugEnabled()) {
						log.debug("SimpleDataSource: Failed to get a connection due to wait timeout.");
					}
					throw new SQLException("Failed to get a connection due to wait timeout.");
				}

				// Must wait
				if (log.isDebugEnabled()) {
					log.debug("Waiting as long as " + pool.timeToWait + " milliseconds for connection.");
				}

				wait++;
				hadToWaitCount++;
				long wt = System.currentTimeMillis();
				Threads.safeWait(POOL_LOCK, pool.timeToWait);
				accumulatedWaitTime += System.currentTimeMillis() - wt;
			}
		}
	}

	private void checkoutConnection(SimplePooledConnection conn, long start) {
		conn.setCheckoutTimestamp(System.currentTimeMillis());
		conn.setLastUsedTimestamp(System.currentTimeMillis());
		actives.add(conn);
		requestCount++;
		accumulatedRequestTime += System.currentTimeMillis() - start;
	}

	/**
	 * Method to check to see if a connection is still usable
	 * 
	 * @param conn - the connection to check
	 * @return True if the connection is still usable
	 */
	protected boolean pingConnection(SimplePooledConnection conn) {
		Connection rcon = conn.getRealConnection();
		try {
			if (rcon.isClosed()) {
				return false;
			}
		}
		catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
			}
			return false;
		}

		if (Strings.isEmpty(pool.pingQuery)
				|| ((pool.pingOlderThan <= 0 || conn.getAge() <= pool.pingOlderThan)
					&& (pool.pingNotUsedFor <= 0 || conn.getTimeElapsedSinceLastUse() <= pool.pingNotUsedFor))) {
			return true;
		}

		if (log.isDebugEnabled()) {
			log.debug("Testing connection " + conn.getRealHashCode() + " ...");
		}

		try {
			Statement statement = rcon.createStatement();
			statement.setQueryTimeout(pool.pingTimeout);
			ResultSet rs = statement.executeQuery(pool.pingQuery);
			rs.close();
			statement.close();

			if (log.isDebugEnabled()) {
				log.debug("Connection " + conn.getRealHashCode() + " is GOOD!");
			}
			return true;
		}
		catch (Exception e) {
			if (log.isWarnEnabled()) {
				log.warn("Execution of ping query '" + pool.pingQuery + "' failed: " + e.getMessage());
			}
			
			Sqls.safeClose(rcon);

			if (log.isDebugEnabled()) {
				log.debug("Connection " + conn.getRealHashCode() + " is BAD: " + e.getMessage());
			}
			
			return false;
		}
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
		close();
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
