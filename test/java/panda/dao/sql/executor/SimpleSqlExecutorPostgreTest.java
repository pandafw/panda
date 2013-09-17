package panda.dao.sql.executor;

import java.sql.Connection;

import org.junit.Test;

import panda.dao.sql.TestSupport;

/**
 */
public class SimpleSqlExecutorPostgreTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getPostgreConnection();
	}

	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
