package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.Map;

import org.junit.Test;

import panda.lang.Strings;

/**
 */
public class SimpleSqlExecutorDerbyTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getDerbyConnection();
	}

	private String fixBit(String sql) {
		return Strings.replace(Strings.replace(sql, ":fbit", ":fbit:char"), ":fbool", ":fbool:char");
	}

	@Override
	protected String getUpdateSql() {
		return fixBit(updateSql);
	}
	
	@Override
	protected String getInsertSql() {
		return fixBit(insertSql);
	}

	@Override
	protected Object getExpectedBit(boolean b) {
		return b ? "1" : "0";
	}

	@Override
	protected Object getExpectedBool(boolean b) {
		return b ? "1" : "0";
	}

	@Override
	protected String nullColumn() {
		return "cast(null as CHAR(1))";
	}

	@Override
	protected void prepareActualBean(TestBean actual) {
		// fix for real precision error 
		Double n = trimDouble(actual.getFreal());
		actual.setFreal(n);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void prepareActualMap(Map actual) {
		super.prepareActualMap(actual);

		// fix for postgre real precision bug 
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
