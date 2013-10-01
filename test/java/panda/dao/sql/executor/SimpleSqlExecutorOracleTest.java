package panda.dao.sql.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import oracle.sql.DATE;
import oracle.sql.TIMESTAMP;

import org.junit.Assert;
import org.junit.Test;

import panda.dao.sql.TestSupport;

/**
 * PreparedSqlExecutorOracleTest
 */
public class SimpleSqlExecutorOracleTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getOracleConnection();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void prepareActualMap(Map actual) {
		for (Iterator<Entry> it = actual.entrySet().iterator(); it.hasNext();) {
			Entry e = it.next();
			Object val = e.getValue();
			if (val instanceof TIMESTAMP) {
				try {
					val = ((TIMESTAMP) val).timestampValue();
					actual.put(e.getKey(), val);
				}
				catch (SQLException ex) {
					log.error(ex.getMessage());
				}
			}
			else if (val instanceof DATE) {
				val = ((DATE) val).timestampValue();
				actual.put(e.getKey(), val);
			}
		}
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
	protected Object getExpectedReal(String num) {
		return new BigDecimal(num);
	}

	@Override
	protected Object getExpectedFloat(String num) {
		return new BigDecimal(num);
	}

	@Override
	protected Object getExpectedDouble(String num) {
		return new BigDecimal(num);
	}

	@Override
	protected Object getExpectedInteger(int num) {
		return new BigDecimal(num);
	}

	@Override
	protected Object getExpectedDate(String date) {
		Date d = convertToDate(date);
		return new java.sql.Timestamp(d.getTime());
	}
	
	@Override
	protected Object getExpectedTime(String time) {
		Date d = convertToTime(time);
		return new java.sql.Timestamp(d.getTime());
	}
	
	private static int id = 1000;

	private void insertAndSelectBLOB(String data) {
		logTestMethodName();

		id++;

		String sql = "INSERT INTO TESTB VALUES(:id, :data)";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("data", data == null ? null : data.getBytes());

		int cnt = 0;
		try {
			cnt = executor.update(sql, param);
		}
		catch (Exception e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(1, cnt);

		sql = "SELECT * FROM TESTB WHERE id=:id";

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", id);
		result.put("data", new byte[0]);

		try {
			executor.fetch(sql, result, result);
		}
		catch (Throwable e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}

		byte[] expected = (byte[]) result.get("data");

		log.debug("expected data: " + (expected == null ? "null" : new String(expected)));

		if (data == null) {
			Assert.assertNull(expected);
		}
		else {
			Assert.assertEquals(new String(data), new String(expected));
		}
	}

	/**
	 * testBLOB
	 */
	@Test
	public void testBLOB() {
		insertAndSelectBLOB("test data");
	}

	/**
	 * testBLOBNull
	 */
	@Test
	public void testBLOBNull() {
		insertAndSelectBLOB(null);
	}

	/**
	 * testCall01
	 */
	@Test
	public void testCall01() {
		logTestMethod();

		String sql = "{:price:DECIMAL.2:OUT = call GET_TEST_PRICE(:id)}";

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

	/**
	 * testCall02
	 */
	@Test
	public void testCall02() {
		logTestMethod();

		String sql = "{:count:INTEGER:OUT = call SET_TEST_PRICE(:id,:price:DECIMAL.2:INOUT)}";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1001);
		param.put("price", new BigDecimal("9999"));

		try {
			executor.execute(sql, param);
		}
		catch (Exception e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}

		Assert.assertEquals(new Long(1), param.get("count"));
		Assert.assertEquals(new BigDecimal("1001.01"), param.get("price"));

		param.put("price", new BigDecimal("1001.01"));
		try {
			executor.execute(sql, param);
		}
		catch (Exception e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}

		Assert.assertEquals(new Long(1), param.get("count"));
		Assert.assertEquals(new BigDecimal("9999"), param.get("price"));
	}

}
