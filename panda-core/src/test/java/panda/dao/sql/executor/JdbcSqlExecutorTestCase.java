package panda.dao.sql.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlResultSet;

public abstract class JdbcSqlExecutorTestCase extends SqlExecutorTestCase {
	protected static String selectSql = "SELECT * FROM TEST WHERE ID=?";
	protected static String updateSql = "UPDATE TEST SET "
			+ "FBIT        = ?,"
			+ "FBOOL       = ?,"
			+ "FCHAR       = ?,"
			+ "FSTR        = ?,"
			+ "FTINYINT    = ?,"
			+ "FSMALLINT   = ?,"
			+ "FINTEGER    = ?,"
			+ "FBIGINT     = ?,"
			+ "FREAL       = ?,"
			+ "FFLOAT      = ?,"
			+ "FDOUBLE     = ?,"
			+ "FDECIMAL    = ?,"
			+ "FNUMERIC    = ?,"
			+ "FDATE       = ?,"
			+ "FTIME       = ?,"
			+ "FTIMESTAMP  = ? "
			+ "WHERE ID    = ?";
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
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?,    "
			+ "?)    ";

	protected static String selectBlobSql = "SELECT ID, FBLOB FROM TEST WHERE ID=?";
	protected static String updateBlobSql = "UPDATE TEST SET "
			+ "FBLOB  = ? "
			+ "WHERE ID=?";
	protected static String insertBlobSql = "INSERT INTO TEST ("
			+ "ID,            "
			+ "FBLOB)         "
			+ "VALUES (       "
			+ "?,           "
			+ "?)   ";

	@Override
	protected SqlExecutor createExecutor(Connection c) throws Exception {
		return new JdbcSqlManager().getExecutor(c);
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
	public void testQueryForString() {
		String sql = "SELECT FSTR FROM TEST WHERE ID=?";
		
		testQueryForObject(sql, 1001, "NAME 1001");
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
	
	/**
	 */
	@Test
	public void testQueryForNullChar() {
		String sql = "SELECT ID, FSTR, NULL AS fchar FROM TEST WHERE ID=1001";
		
		TestBean expected = new TestBean();
		expected.setId(1001);
		expected.setFstr("NAME 1001");

		testQueryForObject(sql, null, expected);
	}
	
	/**
	 */
	@Test
	public void testQueryForObject() {
		String sql = "SELECT * FROM TEST WHERE ID=?";
		
		Map<String, Object> expect = createMap(5);
	
		testQueryForObject(sql, Arrays.asList(1005), expect);
	}
	
	/**
	 */
	@Test
	public void testQueryForListByBetween() {
		String sql = "SELECT * FROM TEST WHERE ID BETWEEN ? AND ?";

		
		Object param = Arrays.asList(1002, 1004);
		
		List<TestBean> expect = new ArrayList<TestBean>();
		expect.add(createBean(2));
		expect.add(createBean(3));
		expect.add(createBean(4));

		testQueryForList(sql, param, TestBean.class, expect);

	}

	/**
	 */
	@Test
	public void testQueryForMap() {
		String sql = "SELECT * FROM TEST WHERE ID IN (?,?)";

		Object param = Arrays.asList(1001, 1005);
		
		Map<Object, Map> expect = new LinkedHashMap<Object, Map>();
		expect.put(getExpectedInteger(1001), createMap(1));
		expect.put(getExpectedInteger(1005), createMap(5));
		testQueryForMap(sql, param, Map.class, expect);
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
		List<Object> param = createList(9, true);
		param.set(param.size() - 1, 1001);
		
		Map<String, Object> expect = createMap(9);
		expect.put("id", getExpectedInteger(1001));
		
		testExecuteUpdate(getUpdateSql(), param, selectSql, Arrays.asList(expect.get("id")), expect);
	}
	
	/**
	 */
	@Test
	public void testUpdateBean() {
		List<Object> param = createList(8, true);
		param.set(param.size() - 1, 1001);
		
		TestBean expect = createBean(8);
		expect.setId(1001);
		
		testExecuteUpdate(getUpdateSql(), param, selectSql, Arrays.asList(expect.getId()), expect);
	}

	protected String getInsertSql() {
		return insertSql;
	}
	
	protected String getInsertBlobSql() {
		return insertBlobSql;
	}
	
	@Test
	public void testInsertMap() throws Exception {
		List<Object> param = createList(9, false);
		Map<String, Object> expect = createMap(9);
		testExecuteUpdate(getInsertSql(), param, selectSql, Arrays.asList(expect.get("id")), expect);
	}

	@Test
	public void testInsertBean() throws Exception {
		List<Object> param = createList(8, false);
		TestBean expect = createBean(8);
		testExecuteUpdate(getInsertSql(), param, selectSql, Arrays.asList(expect.getId()), expect);
	}

	protected void testInsertIdAuto() throws Exception {
		String insertSql = "INSERT INTO IDTEST(FSTR) VALUES(?)";
		String selectSql = "SELECT * FROM IDTEST WHERE ID=?";

		Object param = Arrays.asList("XXXX");

		Map<String, Object> expem = new LinkedHashMap<String, Object>();
		expem.put("id", getExpectedInteger(101));
		expem.put("fstr", "XXXX");
		testExecuteInsertAuto(insertSql, param, selectSql, expem);

		//-------------
		TestBean expea = new TestBean(); 
		expea.setFstr("YYYY");
		expea.setId(102);
		
		testExecuteInsertAuto(insertSql, Arrays.asList("YYYY"), selectSql, expea);
	}

}
