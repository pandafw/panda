package panda.dao.sql.dbcp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ThreadLocalDataSource extends AbstractDataSource {
	private ThreadLocal<ThreadLocalConnection> local = new ThreadLocal<ThreadLocalConnection>();

	/**
	 * Constructor
	 */
	public ThreadLocalDataSource() {
		super();
	}

	@Override
	protected Connection popConnection() throws SQLException {
		ThreadLocalConnection c = local.get();
		if (c == null || c.isClosed()) {
			c = newConnection();
			local.set(c);
		}
		return c;
	}

	protected ThreadLocalConnection newConnection() throws SQLException {
		ThreadLocalConnection c = new ThreadLocalConnection(DriverManager.getConnection(jdbc.url, jdbc.prop));
		Connection r = c.getRealConnection();
		if (r.isReadOnly() != jdbc.readOnly) {
			r.setReadOnly(jdbc.readOnly);
		}
		if (r.getAutoCommit() != jdbc.autoCommit) {
			r.setAutoCommit(jdbc.autoCommit);
		}
		return c;
	}
}
