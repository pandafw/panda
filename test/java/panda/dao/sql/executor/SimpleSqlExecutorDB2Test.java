package panda.dao.sql.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.dao.sql.TestA;
import panda.dao.sql.TestSupport;

/**
 * SimpleSqlExecutorDB2Test
 */
public class SimpleSqlExecutorDB2Test extends SimpleSqlExecutorTestCase {
	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getDB2Connection();
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

		TestA actual = null;
		try {
			actual = executor.fetch(sql, param, TestA.class);
		}
		catch (Exception e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}

		TestA expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToCalendar("2009-01-01"));

		Assert.assertEquals(expected, actual);
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
