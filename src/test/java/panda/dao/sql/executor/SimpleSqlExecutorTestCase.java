package panda.dao.sql.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlResultSet;

/**
 * SimpleSqlExecutorTestCase
 */
public abstract class SimpleSqlExecutorTestCase extends SqlExecutorTestCase {
	protected static String selectSql = "SELECT * FROM TEST WHERE ID=:id";
	protected static String updateSql = "UPDATE TEST SET "
			+ "FBIT        = :fbit      ,"
			+ "FBOOL       = :fbool     ,"
			+ "FCHAR       = :fchar     ,"
			+ "FSTR        = :fstr      ,"
			+ "FTINYINT    = :ftinyint  ,"
			+ "FSMALLINT   = :fsmallint ,"
			+ "FINTEGER    = :finteger  ,"
			+ "FBIGINT     = :fbigint   ,"
			+ "FREAL       = :freal     ,"
			+ "FFLOAT      = :ffloat    ,"
			+ "FDOUBLE     = :fdouble   ,"
			+ "FDECIMAL    = :fdecimal  ,"
			+ "FNUMERIC    = :fnumeric  ,"
			+ "FDATE       = :fdate     ,"
			+ "FTIME       = :ftime     ,"
			+ "FTIMESTAMP  = :ftimestamp "
			+ "WHERE ID=:id";
	protected static String insertSql = "INSERT INTO TEST ("
			+ "ID,            "
			+ "FBIT,          "
			+ "FBOOL,         "
			+ "FCHAR,         "
			+ "FSTR,          "
			+ "FTINYINT,      "
			+ "FSMALLINT,     "
			+ "FINTEGER,      "
			+ "FBIGINT,       "
			+ "FREAL,         "
			+ "FFLOAT,        "
			+ "FDOUBLE,       "
			+ "FDECIMAL,      "
			+ "FNUMERIC,      "
			+ "FDATE,         "
			+ "FTIME,         "
			+ "FTIMESTAMP)    "
			+ "VALUES (       "
			+ ":id,           "
			+ ":fbit,         "
			+ ":fbool,        "
			+ ":fchar,        "
			+ ":fstr,         "
			+ ":ftinyint,     "
			+ ":fsmallint,    "
			+ ":finteger,     "
			+ ":fbigint,      "
			+ ":freal,        "
			+ ":ffloat,       "
			+ ":fdouble,      "
			+ ":fdecimal,     "
			+ ":fnumeric,     "
			+ ":fdate,        "
			+ ":ftime,        "
			+ ":ftimestamp)   ";

	protected static String selectBlobSql = "SELECT ID, FBLOB FROM TEST WHERE ID=:id";
	protected static String updateBlobSql = "UPDATE TEST SET "
			+ "FBLOB  = :fblob "
			+ "WHERE ID=:id:BLOB";
	protected static String insertBlobSql = "INSERT INTO TEST ("
			+ "ID,            "
			+ "FBLOB)         "
			+ "VALUES (       "
			+ ":id,           "
			+ ":fblob:BLOB)   ";

	@Override
	protected SqlExecutor createExecutor(Connection c) throws Exception {
		return new SimpleSqlManager().getExecutor(c);
	}
	
	/**
	 */
	@Test
	public void testSelectResultSet() {
		log.debug("");
		log.debug(this.getClass().getSimpleName() + "." + "testSelectResultSet()");

		String selectSql = "SELECT * FROM TEST WHERE ID < 1003 ORDER BY ID";
		TestBean expect;
		TestBean actual;
		
		SqlResultSet<TestBean> rs = null;
		try {
			rs = executor.selectResultSet(selectSql, TestBean.class);
			
			expect = createBean(1);
			Assert.assertTrue(rs.next());
			actual = rs.getResult();
			prepareActualBean(actual);
			Assert.assertEquals(expect, actual);
			
			expect = createBean(2);
			Assert.assertTrue(rs.next());
			actual = rs.getResult();
			prepareActualBean(actual);
			Assert.assertEquals(expect, actual);
			
			Assert.assertFalse(rs.next());
			rs.close();
		}
		catch (SQLException e) {
			log.error("exception", e);
			Assert.fail(e.getMessage());
		}
		finally {
			rs.safeClose();
		}
	}

	/**
	 */
	@Test
	public void testQueryForInt() {
		String sql = "SELECT COUNT(*) FROM TEST WHERE ID=1001";
		
		testQueryForInt(sql, null, 1);
	}
	
	/**
	 */
	@Test
	public void testQueryForInteger() {
		String sql = "SELECT COUNT(*) FROM TEST WHERE ID=1001 or ID=1002";
		
		testQueryForObject(sql, null, new Integer(2));
	}
	
	
	/**
	 */
	@Test
	public void testQueryForNullObject() {
		String sql = "SELECT * FROM TEST WHERE ID IS NULL";
		
		testQueryForObject(sql, null, (Map)null);
	}

	protected String nullColumn() {
		return "NULL";
	}
	
	/**
	 */
	@Test
	public void testQueryForNullChar() {
		String sql = "SELECT ID, FSTR, " + nullColumn() + " AS fchar FROM TEST WHERE ID=1001";
		
		TestBean expected = new TestBean();
		expected.setId(1001);
		expected.setFstr("NAME 1001");

		testQueryForObject(sql, null, expected);
	}
	
	/**
	 */
	@Test
	public void testQueryForObjectByMap() {
		String sql = "SELECT * FROM TEST WHERE ID=:id";
		
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("id", 1005);
		
		Map<String, Object> expect = createMap(5);
	
		testQueryForObject(sql, param, expect);
	}
	
	/**
	 */
	@Test
	public void testQueryForObjectByBean() {
		String sql = "SELECT * FROM TEST WHERE ID=:a.id";
		
		TestBean param = new TestBean();
		param.setA(new TestBean());
		param.getA().setId(1005);
		
		TestBean expect = createBean(5);
	
		testQueryForObject(sql, param, expect);
	}
	
	/**
	 */
	@Test
	public void testQueryForObjectByParam() {
		String sql = "SELECT * FROM TEST WHERE ID=?";
		
		Object param = new int[] { 1005 };
		
		TestBean expect = createBean(5);
	
		testQueryForObject(sql, param, expect);
	}
	
	/**
	 */
	@Test
	public void testQueryForListByInArray() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:intArray)";

		TestBean param = new TestBean();
		param.setIntArray(new int[] { 1001, 1002, 1004, 1005 });
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(2));
		expect.add(createBean(4));

		testQueryForList(sql, param, 1, 2, TestBean.class, expect);
	}
	
	/**
	 */
	@Test
	public void testQueryForListByBetween() {
		String sql = "SELECT * FROM TEST WHERE ID BETWEEN :from AND :to";

		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("from", 1002);
		param.put("to", 1004);
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(2));
		expect.add(createBean(3));
		expect.add(createBean(4));

		testQueryForList(sql, param, TestBean.class, expect);

	}

	/**
	 */
	@Test
	public void testQueryForListByInArrayOrder() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:id) ORDER BY ::orderCol ::orderDir";

		Map<String, Object> param = new LinkedHashMap<String, Object>();
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
	 */
	@Test
	public void testQueryForListByInListOrder() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:intList) ORDER BY ::orderCol ::orderDir";

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
	 */
	@Test
	public void testQueryForMap() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:ids)";

		Map<String, Object> param = new LinkedHashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.put("ids", ids);
		
		Map<Object, Map> expect = new LinkedHashMap<Object, Map>();
		expect.put(getExpectedInteger(1001), createMap(1));
		expect.put(getExpectedInteger(1005), createMap(5));
		testQueryForMap(sql, param, Map.class, expect);
	}
	
	/**
	 */
	@Test
	public void testQueryForMapByInList() {
		String sql = "SELECT * FROM TEST WHERE ID IN (:intList)";

		TestBean param = new TestBean();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1001);
		ids.add(1005);
		param.setIntList(ids);
		
		Map<Integer, TestBean> expect = new LinkedHashMap<Integer, TestBean>();
		TestBean a5 = createBean(5);
		expect.put(a5.getId(), a5);
		
		TestBean a1 = createBean(1);
		expect.put(a1.getId(), a1);

		testQueryForMap(sql, param, TestBean.class,  expect);
	}
	
	protected String getUpdateSql() {
		return updateSql;
	}
	
	protected String getUpdateBlobSql() {
		return updateBlobSql;
	}
	
	protected String getSelectBlobSql() {
		return selectBlobSql;
	}
	
	/**
	 */
	@Test
	public void testUpdateMap() {
		Map<String, Object> param = createMap(9);
		param.put("id", getExpectedInteger(1001));
		
		testExecuteUpdate(getUpdateSql(), param, selectSql, param, param);
	}
	
	/**
	 */
	@Test
	public void testUpdateBean() {
		TestBean param = createBean(8);
		param.setId(1001);
		
		testExecuteUpdate(getUpdateSql(), param, selectSql, param, param);
	}

	protected String getInsertSql() {
		return insertSql;
	}
	
	protected String getInsertBlobSql() {
		return insertBlobSql;
	}
	
	@Test
	public void testInsertMap() throws Exception {
		Map<String, Object> expect = createMap(9);
		testExecuteUpdate(getInsertSql(), expect, selectSql, expect, expect);
	}

	@Test
	public void testInsertBean() throws Exception {
		TestBean expect = createBean(8);
		testExecuteUpdate(getInsertSql(), expect, selectSql, expect, expect);
	}

	@Test
	public void testInsertBlobBean() {
		TestBean expect = createBlobBean(8);
		testExecuteUpdate(getInsertBlobSql(), expect, getSelectBlobSql(), expect, expect);
	}

	@Test
	public void testInsertBlobBeanNull() {
		TestBean expect = createBlobBean(8);
		expect.setFblob(null);
		testExecuteUpdate(getInsertBlobSql(), expect, getSelectBlobSql(), expect, expect);
	}

	@Test
	public void testUpdateBlobBean() {
		TestBean expect = createBlobBean(1);
		testExecuteUpdate(getUpdateBlobSql(), expect, getSelectBlobSql(), expect, expect);
	}

	@Test
	public void testUpdateBlobBeanNull() {
		TestBean expect = createBlobBean(1);
		expect.setFblob(null);
		testExecuteUpdate(getUpdateBlobSql(), expect, getSelectBlobSql(), expect, expect);
	}

	protected void testInsertIdAuto() throws Exception {
		String insertSql = "INSERT INTO IDTEST(FSTR) VALUES(:fstr)";
		String selectSql = "SELECT * FROM IDTEST WHERE ID=:id";

		Map<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("fstr", "XXXX");

		Map<String, Object> expem = new LinkedHashMap<String, Object>();
		expem.put("id", getExpectedInteger(101));
		expem.putAll(param);
		testExecuteInsertAuto(insertSql, param, selectSql, expem);

		//-------------
		TestBean paraa = new TestBean(); 
		paraa.setFstr("YYYY");
		
		TestBean expea = paraa.clone(); 
		expea.setId(102);
		
		testExecuteInsertAuto(insertSql, paraa, selectSql, expea);
	}

}
