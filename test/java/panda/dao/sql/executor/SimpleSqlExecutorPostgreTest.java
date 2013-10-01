package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.Map;

import org.junit.Test;

import panda.dao.sql.TestBean;
import panda.dao.sql.TestSupport;
import panda.lang.Strings;

/**
 */
public class SimpleSqlExecutorPostgreTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getPostgreConnection();
	}

	@Override
	protected String getUpdateSql() {
		return Strings.replace(updateSql, ":fbit", "cast('1' as bit)");
	}
	
	@Override
	protected String getInsertSql() {
		return Strings.replace(insertSql, ":fbit", "cast('1' as bit)");
	}
	
	@Override
	protected void prepareActualBean(TestBean actual) {
		// fix for real precision error 
		Double n = trimDouble(actual.getFreal());
		actual.setFreal(n);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void prepareActualMap(Map actual) {
		// fix for real precision error 
		Double d = (Double)actual.get("freal");
		if (d != null) {
			Double n = trimDouble(d);
			actual.put("freal", n);
		}
	}
	
	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
