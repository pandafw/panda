package panda.dao.sql.engine;

import java.util.ArrayList;
import java.util.List;

import panda.bean.Beans;
import panda.bean.FastBeans;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.TestA;
import panda.dao.sql.TestSupport;
import panda.dao.sql.engine.ExtendSqlManager;
import panda.dao.sql.engine.FreemarkerSqlManager;
import panda.dao.sql.engine.JavaScriptSqlManager;
import panda.dao.sql.engine.SimpleSqlManager;
import panda.io.Streams;
import panda.lang.Classes;
import panda.log.Log;
import panda.log.Logs;
import junit.framework.TestCase;


/**
 * PerformanceTest
 */
public class PerformanceTest extends TestCase {
	private static Log log = Logs.getLog(PerformanceTest.class);

	private final static int PCOUNT = 10;
	private final static int ECOUNT = 1000;
	
	private String loadSql(String sql) throws Exception {
		return Streams.toString(this.getClass().getResourceAsStream(sql));
	}

	private void executeSql(SqlExecutor executor, String sql) throws Exception {
		String[] sqls = new String[] { sql, sql, sql, sql };
		executeSqls(executor, sqls);
	}
	
	private void executeSqls(SqlExecutor executor, String[] sqls) throws Exception {
		int i = 0;
		
		TestA param;
		List<Integer> idList;
		
		List<TestA> results;
		
		param = new TestA();
		param.setId(1005);
		results = executor.selectList(sqls[i++], param, TestA.class);
		assertEquals(1, results.size());

		param = new TestA();
		idList = new ArrayList<Integer>();
		param.setIdList(idList);
		param.setName("NAME 1001");
		results = executor.selectList(sqls[i++], param, TestA.class);
		assertEquals(1, results.size());

		param = new TestA();
		idList = new ArrayList<Integer>();
		idList.add(1002);
		idList.add(1003);
		param.setIdList(idList);
		results = executor.selectList(sqls[i++], param, TestA.class);
		assertEquals(2, results.size());

		param = new TestA();
		idList = new ArrayList<Integer>();
		idList.add(1003);
		idList.add(1004);
		param.setIdList(idList);
		param.setOrderCol("NAME");
		param.setOrderDir("DESC");
		results = executor.selectList(sqls[i++], param, TestA.class);
		assertEquals(2, results.size());
	}

	private void prepare() {
		Beans.setMe(new Beans());
	}

	private void _testSimple(String name) {
		try {
			String[] sqls = new String[] {
					"SELECT * FROM TEST WHERE ID=:id ORDER BY ID",
					"SELECT * FROM TEST WHERE NAME=:name ORDER BY ID", 
					"SELECT * FROM TEST WHERE ID IN (:idList) ORDER BY ID", 
					"SELECT * FROM TEST WHERE ID IN (:idList) ORDER BY ::orderCol ::orderDir"
			};

			SqlExecutor exe = new SimpleSqlManager().getExecutor(TestSupport.getHsqldbConnection());;
			for (int i = 0; i < PCOUNT; i++) {
				executeSqls(exe, sqls);
			}

			long start = System.currentTimeMillis();
			for (int i = 0; i < ECOUNT; i++) {
				executeSqls(exe, sqls);
			}
			long end = System.currentTimeMillis();

			log.info(name + " - elapsed - " + (end - start));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	private void _testExtend(String name) {
		try {
			SqlExecutor exe = new ExtendSqlManager().getExecutor(TestSupport.getHsqldbConnection());;

			String sql = loadSql("test.sql");
			for (int i = 0; i < PCOUNT; i++) {
				executeSql(exe, sql);
			}

			long start = System.currentTimeMillis();
			for (int i = 0; i < ECOUNT; i++) {
				executeSql(exe, sql);
			}
			long end = System.currentTimeMillis();

			log.info(name + " - elapsed - " + (end - start));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	
	private void _testJavascript(String name) {
		try {
			SqlExecutor exe = new JavaScriptSqlManager().getExecutor(TestSupport.getHsqldbConnection());;

			String sql = loadSql("test.sql.js");
			for (int i = 0; i < PCOUNT; i++) {
				executeSql(exe, sql);
			}

			long start = System.currentTimeMillis();
			for (int i = 0; i < ECOUNT; i++) {
				executeSql(exe, sql);
			}
			long end = System.currentTimeMillis();

			log.info(name + " - elapsed - " + (end - start));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	
	private void _testFreemarker(String name) {
		try {
			SqlExecutor exe = new FreemarkerSqlManager().getExecutor(TestSupport.getHsqldbConnection());;

			String sql = Classes.getResourcePath(this.getClass(), "test.sql.ftl");
			for (int i = 0; i < PCOUNT; i++) {
				executeSql(exe, sql);
			}

			long start = System.currentTimeMillis();
			for (int i = 0; i < ECOUNT; i++) {
				executeSql(exe, sql);
			}
			long end = System.currentTimeMillis();

			log.info(name + " - elapsed - " + (end - start));
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	private void prepareFast() {
		Beans.setMe(new FastBeans());
	}

	/**
	 * testSimple
	 */
	public void testSimple() {
		prepare();
		_testSimple("testSimple");
	}

	/**
	 * testExtend
	 */
	public void testExtend() {
		prepare();
		_testExtend("testExtend");
	}
	
	/**
	 * testJavascript
	 */
	public void testJavascript() {
		prepare();
		_testJavascript("testJavascript");
	}
	
	/**
	 * testFreemarker
	 */
	public void testFreemarker() {
		prepare();
		_testFreemarker("testFreemarker");
	}

	/**
	 * testSimpleFast
	 */
	public void testSimpleFast() {
		prepareFast();
		_testSimple("testSimpleFast");
	}

	/**
	 * testExtendFast
	 */
	public void testExtendFast() {
		prepareFast();
		_testExtend("testExtendFast");
	}
	
	/**
	 * testJavascriptFast
	 */
	public void testJavascriptFast() {
		prepareFast();
		_testJavascript("testJavascriptFast");
	}
	
	/**
	 * testFreemarkerFast
	 */
	public void testFreemarkerFast() {
		prepareFast();
		_testFreemarker("testFreemarkerFast");
	}


}
