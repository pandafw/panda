package panda.dao.sql.engine;

import org.junit.Test;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.TestSupport;
import panda.dao.sql.engine.SimpleSqlManager;

/**
 */
public class SimpleSqlExecutorHsqldbTest extends SimpleSqlExecutorTestCase {

	@Override
	protected SqlExecutor createExecutor() throws Exception {
		return new SimpleSqlManager().getExecutor(TestSupport.getHsqldbConnection());
	}

	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
