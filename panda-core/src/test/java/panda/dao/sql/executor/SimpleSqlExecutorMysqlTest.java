package panda.dao.sql.executor;

import java.sql.Connection;

import org.junit.Test;

/**
 */
public class SimpleSqlExecutorMysqlTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getMysqlConnection();
	}

	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
