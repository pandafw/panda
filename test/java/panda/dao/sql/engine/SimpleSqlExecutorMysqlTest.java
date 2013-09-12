package panda.dao.sql.engine;

import java.sql.Connection;

import org.junit.Test;

import panda.dao.sql.TestSupport;

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
