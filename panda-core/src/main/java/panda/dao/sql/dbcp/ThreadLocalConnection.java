package panda.dao.sql.dbcp;

import java.sql.Connection;
import java.sql.SQLException;

public class ThreadLocalConnection extends ProxyConnection {
	public ThreadLocalConnection(Connection connection) {
		super(connection);
	}

	@Override
	public void close() throws SQLException {
		rollback();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.close();
	}
}
