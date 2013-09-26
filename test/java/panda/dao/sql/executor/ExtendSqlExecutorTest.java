package panda.dao.sql.executor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.TestA;
import panda.dao.sql.TestSupport;
import panda.dao.sql.executor.ExtendSqlManager;

/**
 * ExtendSqlExecutorTest
 */
public class ExtendSqlExecutorTest extends SqlExecutorTestCase {
	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getHsqldbConnection();
	}

	@Override
	protected SqlExecutor createExecutor(Connection c) throws Exception {
		return new ExtendSqlManager().getExecutor(c);
	}

	/**
	 * test01
	 */
	public void test01() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @name[AND NAME=:name] @!name[AND NAME IS NULL]]";
		
		testQueryForObject(sql, (Map)null, (Map)null);
	}
	
	/**
	 * test03
	 */
	public void test03() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @name[AND NAME=:name]]";
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("name", "NAME 1005");
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1005));
		expected.put("name", "NAME 1005");
		expected.put("kind", "5");
		expected.put("price", new BigDecimal("1005.05"));
		expected.put("updateTime", getExpectedTimestamp("2009-05-05"));
	
		testQueryForObject(sql, param, expected);
	}

	/**
	 * test04
	 */
	public void test04() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @name[AND NAME=:name] @!name[AND NAME IS NULL]]";
		
		testQueryForList(sql, null, HashMap.class, new ArrayList());
	}
	
	/**
	 * test05
	 */
	public void test05() {
		String sql = "SELECT * FROM TEST @[WHERE @idList[ID IN (:idList)] @id[OR id=:id]]";

		Map<String, Object> param = new HashMap<String, Object>();
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(1001);
		idList.add(1005);
		param.put("idList", idList);
		
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1001));
		expected.put("name", "NAME 1001");
		expected.put("kind", "1");
		expected.put("price", new BigDecimal("1001.01"));
		expected.put("updateTime", getExpectedTimestamp("2009-01-01"));
		list.add(expected);
		
		expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1005));
		expected.put("name", "NAME 1005");
		expected.put("kind", "5");
		expected.put("price", new BigDecimal("1005.05"));
		expected.put("updateTime", getExpectedTimestamp("2009-05-05"));
		list.add(expected);

		testQueryForList(sql, param, HashMap.class, list);
	}

	/**
	 * test05a
	 */
	public void test05a() {
		String sql = "SELECT * FROM TEST @[WHERE @idList[ID IN (:idList)] @id[OR id=:id]]";

		TestA param = new TestA();
		List<Integer> idList = new ArrayList<Integer>();
		param.setIdList(idList);
		param.setId(1001);
		
		TestA expected;
		List<TestA> list = new ArrayList<TestA>();
		expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToCalendar("2009-01-01"));
		list.add(expected);

		testQueryForList(sql, param, TestA.class, list);
	}

	/**
	 * test05a2
	 */
	public void test05a2() {
		String sql = "SELECT * FROM TEST @[WHERE @idArray[ID IN (:idArray)] @id[OR id=:id]]";

		TestA param = new TestA();
		param.setIdArray(new int[0]);
		param.setId(1001);
		
		TestA expected;
		List<TestA> list = new ArrayList<TestA>();
		expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToCalendar("2009-01-01"));
		list.add(expected);
		
		testQueryForList(sql, param, TestA.class, list);
	}

	/**
	 * test05a3
	 */
	public void test05a3() {
		String sql = "SELECT * FROM TEST @[WHERE @idArray[ID IN (:idArray)] @id[OR id=:id]]";

		TestA param = new TestA();
		param.setIdArray(new int[] { 1001, 1005 });
		
		TestA expected;
		List<TestA> list = new ArrayList<TestA>();
		
		expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToCalendar("2009-01-01"));
		list.add(expected);

		expected = new TestA();
		expected.setId(1005);
		expected.setName("NAME 1005");
		expected.setKind('5');
		expected.setPrice(new BigDecimal("1005.05"));
		expected.setUpdateTime(convertToCalendar("2009-05-05"));
		list.add(expected);

		testQueryForList(sql, param, TestA.class, list);
	}

	/**
	 * test05a4
	 */
	public void test05a4() {
		String sql = "SELECT * FROM TEST @[WHERE @idArray[ID IN (:idArray)] @id[OR id=:id]]";

		TestA param = new TestA();
		param.setIdArray(new int[] { 1001, 1002, 1004, 1005 });
		
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
		expected.setId(1004);
		expected.setName("NAME 1004");
		expected.setKind('4');
		expected.setPrice(new BigDecimal("1004.04"));
		expected.setUpdateTime(convertToCalendar("2009-04-04"));
		list.add(expected);

		testQueryForList(sql, param, 1, 2, TestA.class, list);
	}

	/**
	 * test06
	 */
	public void test06() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:id) @orderCol[ORDER BY ::orderCol ::orderDir]";

		Map<String, Object> param = new HashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.put("id", ids);
		param.put("orderCol", "ID");
		param.put("orderDir", "DESC");
		
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1005));
		expected.put("name", "NAME 1005");
		expected.put("kind", "5");
		expected.put("price", new BigDecimal("1005.05"));
		expected.put("updateTime", getExpectedTimestamp("2009-05-05"));
		list.add(expected);
		
		expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1001));
		expected.put("name", "NAME 1001");
		expected.put("kind", "1");
		expected.put("price", new BigDecimal("1001.01"));
		expected.put("updateTime", getExpectedTimestamp("2009-01-01"));
		list.add(expected);

		testQueryForList(sql, param, HashMap.class, list);
	}

	/**
	 * test06a
	 */
	public void test06a() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:idList) @orderCol[ORDER BY ::orderCol ::orderDir]";

		TestA param = new TestA();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.setIdList(ids);
		param.setOrderCol("ID");
		param.setOrderDir("DESC");
		
		List<TestA> list = new ArrayList<TestA>();
		TestA expected = new TestA();
		expected.setId(1005);
		expected.setName("NAME 1005");
		expected.setKind('5');
		expected.setPrice(new BigDecimal("1005.05"));
		expected.setUpdateTime(convertToCalendar("2009-05-05"));
		list.add(expected);
		
		expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToCalendar("2009-01-01"));
		list.add(expected);

		testQueryForList(sql, param, TestA.class, list);
	}
	
	/**
	 * test07
	 */
	public void test07() {
		String sql = "SELECT * FROM TEST @[WHERE @id[ID=:id] @name[AND NAME=:name] @!name[AND NAME IS NULL]]";

		testQueryForMap(sql, null, HashMap.class, new HashMap());
	}
	
}
