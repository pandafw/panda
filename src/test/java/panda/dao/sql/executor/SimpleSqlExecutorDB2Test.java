package panda.dao.sql.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * SimpleSqlExecutorDB2Test
 * 
 * create test database:
 * > db2 create database ptest using codeset UTF-8 territory en
 * 
 * db2 user is system level user"
 * "e.g.: useradd ptest"
 */
public class SimpleSqlExecutorDB2Test extends SimpleSqlExecutorTestCase {
	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getDB2Connection();
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
	protected Object getExpectedFloat(String num) {
		return new BigDecimal(num);
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

	/**
	 * testCall01
	 */
	@Test
	public void testCall01() {
		logTestMethod();

		String sql = "{call SELECT_TEST(:id)}";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1001);

		TestBean actual = null;
		try {
			actual = executor.fetch(sql, param, TestBean.class);
			prepareActualBean(actual);
		}
		catch (Exception e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}

		TestBean expect = createBean(1);
		Assert.assertEquals(expect, actual);
	}

	/**
	 * testCall02
	 */
	@Test
	public void testCall02() {
		logTestMethod();

		String sql = "{call GET_TEST_PRICE(:id,:price:DECIMAL.2:OUT)}";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1001);

		try {
			executor.execute(sql, param);
		}
		catch (Exception e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}

		Assert.assertEquals(new BigDecimal("1001.01"), param.get("price"));
	}
}
