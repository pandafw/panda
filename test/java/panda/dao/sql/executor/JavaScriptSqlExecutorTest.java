package panda.dao.sql.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.TestA;
import panda.dao.sql.TestSupport;
import panda.dao.sql.executor.JavaScriptSqlManager;
import panda.io.Streams;

/**
 * JavaScriptSqlExecutorTest
 */
public class JavaScriptSqlExecutorTest extends SqlExecutorTestCase {
	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getHsqldbConnection();
	}

	@Override
	protected SqlExecutor createExecutor(Connection c) throws Exception {
		return new JavaScriptSqlManager().getExecutor(c);
	}

	private String loadSql(String id) {
		try {
			return Streams.toString(this.getClass().getResourceAsStream(id + ".sql.js"));
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * test01
	 */
	public void test01() {
		String sql = loadSql("test");

		TestA param = new TestA();
		param.setId(1005);

		TestA expected = new TestA();
		expected.setId(1005);
		expected.setName("NAME 1005");
		expected.setKind('5');
		expected.setPrice(new BigDecimal("1005.05"));
		expected.setUpdateTime(convertToCalendar("2009-05-05"));

		testQueryForObject(sql, param, expected);
	}

	/**
	 * test02
	 */
	public void test02() {
		String sql = loadSql("test");

		TestA param = new TestA();
		List<Integer> idList = new ArrayList<Integer>();
		param.setIdList(idList);
		param.setName("NAME 1001");

		TestA expected;
		expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToCalendar("2009-01-01"));

		testQueryForObject(sql, param, expected);
	}

	/**
	 * test03
	 */
	public void test03() {
		String sql = loadSql("test");

		TestA param = new TestA();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(1002);
		idList.add(1003);
		param.setIdList(idList);

		TestA expected;
		List<TestA> list = new ArrayList<TestA>();

		expected = new TestA();
		expected.setId(1002);
		expected.setName("NAME 1002");
		expected.setKind('2');
		expected.setPrice(new BigDecimal("1002.02"));
		expected.setUpdateTime(convertToCalendar("2009-02-02"));
		list.add(expected);

		expected = new TestA();
		expected.setId(1003);
		expected.setName("NAME 1003");
		expected.setKind('3');
		expected.setPrice(new BigDecimal("1003.03"));
		expected.setUpdateTime(convertToCalendar("2009-03-03"));
		list.add(expected);

		testQueryForList(sql, param, TestA.class, list);
	}

	/**
	 * test04
	 */
	public void test04() {
		String sql = loadSql("test");

		TestA param = new TestA();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(1003);
		idList.add(1004);
		param.setIdList(idList);
		param.setOrderCol("NAME");
		param.setOrderDir("DESC");

		TestA expected;
		List<TestA> list = new ArrayList<TestA>();

		expected = new TestA();
		expected.setId(1004);
		expected.setName("NAME 1004");
		expected.setKind('4');
		expected.setPrice(new BigDecimal("1004.04"));
		expected.setUpdateTime(convertToCalendar("2009-04-04"));
		list.add(expected);

		expected = new TestA();
		expected.setId(1003);
		expected.setName("NAME 1003");
		expected.setKind('3');
		expected.setPrice(new BigDecimal("1003.03"));
		expected.setUpdateTime(convertToCalendar("2009-03-03"));
		list.add(expected);

		testQueryForList(sql, param, TestA.class, list);
	}
}
