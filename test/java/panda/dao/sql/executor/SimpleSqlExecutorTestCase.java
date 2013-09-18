package panda.dao.sql.executor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.dao.sql.SqlResultSet;
import panda.dao.sql.TestA;

/**
 * SimpleSqlExecutorTestCase
 */
public abstract class SimpleSqlExecutorTestCase extends SqlExecutorTestCase {

	/**
	 */
	@Test
	public void testSelectResultSet() {
		log.debug("");
		log.debug(this.getClass().getSimpleName() + "." + "testSelectResultSet()");

		String selectSql = "SELECT * FROM TEST WHERE ID < 1003 ORDER BY ID";
		TestA expected;
		TestA actual;
		
		try {
			SqlResultSet rs = executor.selectResultSet(selectSql);
			
			expected = new TestA();
			expected.setId(1001);
			expected.setName("NAME 1001");
			expected.setKind('1');
			expected.setPrice(new BigDecimal("1001.01"));
			expected.setUpdateTime(convertToDate("2009-01-01"));
			
			Assert.assertTrue(rs.next());
			actual = rs.getResult(TestA.class);
			Assert.assertEquals(expected, actual);
			
			expected = new TestA();
			expected.setId(1002);
			expected.setName("NAME 1002");
			expected.setKind('2');
			expected.setPrice(new BigDecimal("1002.02"));
			expected.setUpdateTime(convertToDate("2009-02-02"));
			
			Assert.assertTrue(rs.next());
			actual = rs.getResult(TestA.class);
			Assert.assertEquals(expected, actual);
			
			Assert.assertFalse(rs.next());
			
			rs.close();
		}
		catch (SQLException e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 */
	@Test
	public void testQueryForObject() {
		String sql = "SELECT * FROM TEST WHERE NAME IS NULL";
		
		testQueryForObject(sql, (Map)null, (Map)null);
	}
	
	/**
	 */
	@Test
	public void testQueryForObjectByMap() {
		String sql = "SELECT * FROM TEST WHERE ID=:id";
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1005);
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1005));
		expected.put("name", "NAME 1005");
		expected.put("kind", "5");
		expected.put("price", new BigDecimal("1005.05"));
		expected.put("updateTime", getExpectedTimestamp("2009-05-05"));
	
		testQueryForObject(sql, param, expected);
	}
	
	/**
	 */
	@Test
	public void testQueryForObjectByBean() {
		String sql = "SELECT * FROM TEST WHERE ID=:a.id";
		
		TestA param = new TestA();
		param.setA(new TestA());
		param.getA().setId(1005);
		
		TestA expected = new TestA();
		expected.setId(1005);
		expected.setName("NAME 1005");
		expected.setKind('5');
		expected.setPrice(new BigDecimal("1005.05"));
		expected.setUpdateTime(convertToDate("2009-05-05"));
	
		testQueryForObject(sql, param, expected);
	}
	
	/**
	 */
	@Test
	public void testQueryForObjectByParam() {
		String sql = "SELECT * FROM TEST WHERE ID=?";
		
		Object param = new int[] { 1005 };
		
		TestA expected = new TestA();
		expected.setId(1005);
		expected.setName("NAME 1005");
		expected.setKind('5');
		expected.setPrice(new BigDecimal("1005.05"));
		expected.setUpdateTime(convertToDate("2009-05-05"));
	
		testQueryForObject(sql, param, expected);
	}
	
	/**
	 */
	@Test
	public void testQueryForListByInArray() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:idArray)";

		TestA param = new TestA();
		param.setIdArray(new int[] { 1001, 1002, 1004, 1005 });
		
		TestA expected;
		List<TestA> list = new ArrayList<TestA>();
		expected = new TestA();
		expected.setId(1002);
		expected.setName("NAME 1002");
		expected.setKind('2');
		expected.setPrice(new BigDecimal("1002.02"));
		expected.setUpdateTime(convertToDate("2009-02-02"));
		list.add(expected);

		expected = new TestA();
		expected.setId(1004);
		expected.setName("NAME 1004");
		expected.setKind('4');
		expected.setPrice(new BigDecimal("1004.04"));
		expected.setUpdateTime(convertToDate("2009-04-04"));
		list.add(expected);

		testQueryForList(sql, param, 1, 2, TestA.class, list);
	}

	/**
	 */
	@Test
	public void testQueryForListByInArrayOrder() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:id) ORDER BY ::orderCol ::orderDir";

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
	 */
	@Test
	public void testQueryForListByInListOrder() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:idList) ORDER BY ::orderCol ::orderDir";

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
		expected.setUpdateTime(convertToDate("2009-05-05"));
		list.add(expected);
		
		expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToDate("2009-01-01"));
		list.add(expected);

		testQueryForList(sql, param, TestA.class, list);
	}

	/**
	 */
	@Test
	public void testQueryForMap() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:id)";

		Map<String, Object> param = new HashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.put("id", ids);
		
		Map<Object, Map> map = new HashMap<Object, Map>();
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1001));
		expected.put("name", "NAME 1001");
		expected.put("kind", "1");
		expected.put("price", new BigDecimal("1001.01"));
		expected.put("updateTime", getExpectedTimestamp("2009-01-01"));
		map.put(getExpectedInteger(1001), expected);
		
		expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1005));
		expected.put("name", "NAME 1005");
		expected.put("kind", "5");
		expected.put("price", new BigDecimal("1005.05"));
		expected.put("updateTime", getExpectedTimestamp("2009-05-05"));
		map.put(getExpectedInteger(1005), expected);

		testQueryForMap(sql, param, HashMap.class, map);
	}
	
	/**
	 */
	@Test
	public void testQueryForMapByInList() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:idList)";

		TestA param = new TestA();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.setIdList(ids);
		
		Map<Integer, TestA> map = new HashMap<Integer, TestA>();
		TestA expected = new TestA();
		expected.setId(1005);
		expected.setName("NAME 1005");
		expected.setKind('5');
		expected.setPrice(new BigDecimal("1005.05"));
		expected.setUpdateTime(convertToDate("2009-05-05"));
		map.put(new Integer(1005), expected);
		
		expected = new TestA();
		expected.setId(1001);
		expected.setName("NAME 1001");
		expected.setKind('1');
		expected.setPrice(new BigDecimal("1001.01"));
		expected.setUpdateTime(convertToDate("2009-01-01"));
		map.put(new Integer(1001), expected);

		testQueryForMap(sql, param, TestA.class,  map);
	}
	
	/**
	 */
	@Test
	public void testUpdateString() {
		String updateSql = "UPDATE TEST SET NAME=:name WHERE ID=:id";
		String selectSql = "SELECT * FROM TEST WHERE ID=:id";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1001);
		param.put("name", "TEST 1001");
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1001));
		expected.put("name", "TEST 1001");
		expected.put("kind", "1");
		expected.put("price", new BigDecimal("1001.01"));
		expected.put("updateTime", getExpectedTimestamp("2009-01-01"));
		
		testExecuteUpdate(updateSql, param, selectSql, expected);
	}
	
	/**
	 */
	@Test
	public void testUpdateDecimal() {
		String updateSql = "UPDATE TEST SET PRICE=:price WHERE ID=:id";
		String selectSql = "SELECT * FROM TEST WHERE ID=:id";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1001);
		param.put("price", new BigDecimal("1002.02"));
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1001));
		expected.put("name", "NAME 1001");
		expected.put("kind", "1");
		expected.put("price", new BigDecimal("1002.02"));
		expected.put("updateTime", getExpectedTimestamp("2009-01-01"));
		
		testExecuteUpdate(updateSql, param, selectSql, expected);
	}

	@Test
	public void testInsert() throws Exception {
		String insertSql = "INSERT INTO TEST VALUES(:id, :name, :kind, :price, :updateTime)";
		String selectSql = "SELECT * FROM TEST WHERE ID=:id";

		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(2001));
		expected.put("name", "NAME 2001");
		expected.put("kind", "2");
		expected.put("price", new BigDecimal("2001.01"));
		expected.put("updateTime", getExpectedTimestamp("2013-01-01"));
		testExecuteInsert(insertSql, expected, selectSql, expected);
	}

	protected void testInsertIdAuto() throws Exception {
		String insertSql = "INSERT INTO IDTEST(NAME, KIND, PRICE, UPDATE_TIME) VALUES(:name, :kind, :price, :updateTime)";
		String selectSql = "SELECT * FROM IDTEST WHERE ID=:id";

		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("id", getExpectedInteger(1));
		expected.put("name", "NAME 1001");
		expected.put("kind", "1");
		expected.put("price", new BigDecimal("1001.01"));
		expected.put("updateTime", getExpectedTimestamp("2001-01-01"));
		testExecuteInsert(insertSql, expected, selectSql, expected);

		expected.put("id", getExpectedInteger(2));
		expected.put("name", "NAME 2002");
		expected.put("kind", "2");
		expected.put("price", new BigDecimal("2002.02"));
		expected.put("updateTime", getExpectedTimestamp("2002-02-02"));
		testExecuteInsert(insertSql, expected, selectSql, expected);
	}

}
