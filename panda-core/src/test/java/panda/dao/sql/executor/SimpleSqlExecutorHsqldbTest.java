package panda.dao.sql.executor;

import java.sql.Connection;

import org.junit.Test;

/**
 */
public class SimpleSqlExecutorHsqldbTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getHsqldbConnection();
	}

	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
