package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.TestBean;
import panda.dao.sql.TestSupport;
import panda.lang.Classes;

/**
 * FreemarkerSqlExecutorTest
 */
public class FreemarkerSqlExecutorTest extends SqlExecutorTestCase {
	private final static String TEST_SQL = Classes.getResourcePath(
			FreemarkerSqlExecutorTest.class, "/test.sql.ftl"); 

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getHsqldbConnection();
	}

	@Override
	protected SqlExecutor createExecutor(Connection c) throws Exception {
		return new FreemarkerSqlManager().getExecutor(c);
	}

	/**
	 * test01
	 */
	public void test01() {
		String sql = TEST_SQL;

		TestBean param = new TestBean();
		param.setId(1005);

		TestBean expect = createBean(5);

		testQueryForObject(sql, param, expect);
	}

	/**
	 * test02
	 */
	public void test02() {
		String sql = TEST_SQL;

		TestBean param = new TestBean();
		List<Integer> idList = new ArrayList<Integer>();
		param.setIntList(idList);
		param.setFstr("NAME 1001");

		TestBean expect;
		expect = createBean(1);

		testQueryForObject(sql, param, expect);
	}

	/**
	 * test03
	 */
	public void test03() {
		String sql = TEST_SQL;

		TestBean param = new TestBean();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(1002);
		idList.add(1003);
		param.setIntList(idList);

		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(2));
		expect.add(createBean(3));
		testQueryForList(sql, param, TestBean.class, expect);
	}

	/**
	 * test04
	 */
	public void test04() {
		String sql = TEST_SQL;

		TestBean param = new TestBean();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(1003);
		idList.add(1004);
		param.setIntList(idList);
		param.setOrderCol("FSTR");
		param.setOrderDir("DESC");

		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(4));
		expect.add(createBean(3));

		testQueryForList(sql, param, TestBean.class, expect);
	}
}
