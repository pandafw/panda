package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import panda.dao.sql.SqlExecutor;

/**
 * ExtendSqlExecutorTest
 */
public class DynamicSqlExecutorTest extends SqlExecutorTestCase {
	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getHsqldbConnection();
	}

	@Override
	protected SqlExecutor createExecutor(Connection c) throws Exception {
		return new DynamicSqlManager().getExecutor(c);
	}

	/**
	 * test01
	 */
	@Test
	public void test01() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @fstr[AND FSTR=:fstr] @!fstr[AND FSTR IS NULL]]";
		
		testQueryForObject(sql, (Map)null, (Map)null);
	}
	
	/**
	 * test03
	 */
	@Test
	public void test03() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @fstr[AND FSTR=:fstr]]";
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("fstr", "NAME 1005");
		
		Map<String, Object> expect = createMap(5);
		testQueryForObject(sql, param, expect);
	}

	/**
	 * test04
	 */
	@Test
	public void test04() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @fstr[AND FSTR=:fstr] @!fstr[AND FSTR IS NULL]]";
		
		testQueryForList(sql, null, Map.class, new ArrayList());
	}
	
	/**
	 * test05
	 */
	@Test
	public void test05() {
		String sql = "SELECT * FROM TEST @[WHERE @intList[ID IN (:intList)] @id[OR id=:id]]";

		Map<String, Object> param = new HashMap<String, Object>();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(1001);
		idList.add(1005);
		param.put("intList", idList);
		
		List<Map> expect = new ArrayList<Map>();
		expect.add(createMap(1));
		expect.add(createMap(5));

		testQueryForList(sql, param, Map.class, expect);
	}

	/**
	 * test05a
	 */
	@Test
	public void test05a() {
		String sql = "SELECT * FROM TEST @[WHERE @intList[ID IN (:intList)] @id[OR id=:id]]";

		TestBean param = new TestBean();
		List<Integer> idList = new ArrayList<Integer>();
		param.setIntList(idList);
		param.setId(1001);
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(1));

		testQueryForList(sql, param, TestBean.class, expect);
	}

	/**
	 * test05a2
	 */
	@Test
	public void test05a2() {
		String sql = "SELECT * FROM TEST @[WHERE @intArray[ID IN (:intArray)] @id[OR id=:id]]";

		TestBean param = new TestBean();
		param.setIntArray(new int[0]);
		param.setId(1001);
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(1));
		
		testQueryForList(sql, param, TestBean.class, expect);
	}

	/**
	 * test05a3
	 */
	@Test
	public void test05a3() {
		String sql = "SELECT * FROM TEST @[WHERE @intArray[ID IN (:intArray)] @id[OR id=:id]]";

		TestBean param = new TestBean();
		param.setIntArray(new int[] { 1001, 1005 });
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(1));
		expect.add(createBean(5));
		testQueryForList(sql, param, TestBean.class, expect);
	}

	/**
	 * test05a4
	 */
	@Test
	public void test05a4() {
		String sql = "SELECT * FROM TEST @[WHERE @intArray[ID IN (:intArray)] @id[OR id=:id]]";

		TestBean param = new TestBean();
		param.setIntArray(new int[] { 1001, 1002, 1004, 1005 });
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(2));
		expect.add(createBean(4));
		testQueryForList(sql, param, 1, 2, TestBean.class, expect);
	}

	/**
	 * test06
	 */
	@Test
	public void test06() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:id) @orderCol[ORDER BY ::orderCol ::orderDir]";

		Map<String, Object> param = new HashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.put("id", ids);
		param.put("orderCol", "ID");
		param.put("orderDir", "DESC");
		
		List<Map> expect = new ArrayList<Map>();
		expect.add(createMap(5));
		expect.add(createMap(1));

		testQueryForList(sql, param, Map.class, expect);
	}

	/**
	 * test06a
	 */
	@Test
	public void test06a() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:intList) @orderCol[ORDER BY ::orderCol ::orderDir]";

		TestBean param = new TestBean();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.setIntList(ids);
		param.setOrderCol("ID");
		param.setOrderDir("DESC");
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(5));
		expect.add(createBean(1));
		testQueryForList(sql, param, TestBean.class, expect);
	}
	
	/**
	 * test07
	 */
	@Test
	public void test07() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @fstr[AND FSTR=:fstr] @!fstr[AND FSTR IS NULL]]";

		testQueryForMap(sql, null, HashMap.class, new HashMap());
	}
	
}
