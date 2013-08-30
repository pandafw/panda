package panda.dao.sql.engine;

import org.junit.Test;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.TestSupport;
import panda.dao.sql.engine.SimpleSqlManager;

/**
 */
public class SimpleSqlExecutorPostgreTest extends SimpleSqlExecutorTestCase {

	@Override
	protected SqlExecutor createExecutor() throws Exception {
		return new SimpleSqlManager().getExecutor(TestSupport.getPostgreConnection());
	}

	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
