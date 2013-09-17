package panda.dao.sql.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.executor.ExtendSqlManager;
import panda.dao.sql.executor.ExtendSqlParser;
import panda.dao.sql.executor.SqlParameter;
import panda.log.Log;
import panda.log.Logs;
import junit.framework.TestCase;


/**
 * ExtendSqlStatementTest
 */
public class ExtendSqlParserTest extends TestCase {
	/**
	 * log
	 */
	private static Log log = Logs.getLog(ExtendSqlParserTest.class);

	private String getTestMethodName() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StackTraceElement ste = stack[2];
		return this.getClass().getSimpleName() + "." + ste.getMethodName() + "()";
	}

	private void testTranslate(String originalSql, Object paramObject, String translatedSql, List<SqlParameter> parameters) {
		log.debug("");
		log.debug(getTestMethodName());
		log.debug("original SQL: [" + originalSql + "]");

		String actTranslatedSql = null;
		List<SqlParameter> actParameters = null;
		try {
			SqlExecutor executor = new ExtendSqlManager().getExecutor();
			ExtendSqlParser parser = new ExtendSqlParser(originalSql);
			
			actParameters = new ArrayList<SqlParameter>();
			actTranslatedSql = parser.parse(executor, paramObject, actParameters);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		log.debug("expect SQL: [" + translatedSql + "]");
		log.debug("actual SQL: [" + actTranslatedSql + "]");

		assertEquals(translatedSql, actTranslatedSql);
		
		if (parameters == null) {
			assertTrue(actParameters.isEmpty());
		}
		else {
			log.debug("expect parameters: " + parameters);
			log.debug("actual parameters: " + actParameters);
			assertEquals(parameters, actParameters);
		}
	}
	
	/**
	 * test01_at
	 */
	public void test01_at() {
		String originalSql = "SELECT * FROM SAMPLE @orderCol[ORDER BY ::orderCol ::orderDir]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("orderCol", "NAME");

		String translatedSql = "SELECT * FROM SAMPLE ORDER BY NAME";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test02_atList
	 */
	public void test02_atList() {
		String originalSql = "SELECT * FROM SAMPLE WHERE @list[LIST IN (:list)]";

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("list", list);

		String translatedSql = "SELECT * FROM SAMPLE WHERE LIST IN ( ?,?,? )";
		
		List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		parameters.add(new SqlParameter("list", "a"));
		parameters.add(new SqlParameter("list", "b"));
		parameters.add(new SqlParameter("list", "c"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test03_notat
	 */
	public void test03_notat() {
		String originalSql = "SELECT * FROM SAMPLE WHERE @kind[KIND=:kind] @!kind[KIND IS NULL]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("kind", "a");

		String translatedSql = "SELECT * FROM SAMPLE WHERE KIND= ?";
		
		List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		parameters.add(new SqlParameter("kind", "a"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test04_notat
	 */
	public void test04_notat() {
		String originalSql = "SELECT * FROM SAMPLE WHERE @kind[KIND=:kind] @!kind[KIND IS NULL]";

		Map<String, String> map = new HashMap<String, String>();

		String translatedSql = "SELECT * FROM SAMPLE WHERE KIND IS NULL";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test05
	 */
	public void test05() {
		String originalSql = "SELECT * FROM SAMPLE @[WHERE @id[AND ID=:id] @name[AND NAME=:name] @!kind[AND KIND IS NULL]]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "Id");
		map.put("name", "Name");

		String translatedSql = "SELECT * FROM SAMPLE WHERE ID= ? AND NAME= ? AND KIND IS NULL";
		
		List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		parameters.add(new SqlParameter("id", "Id"));
		parameters.add(new SqlParameter("name", "Name"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test06
	 */
	public void test06() {
		String originalSql = "SELECT * FROM SAMPLE @[WHERE @id[AND ID=:id] @name[AND NAME=:name] @!kind[AND KIND IS NULL]]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("kind", "Kind");

		String translatedSql = "SELECT * FROM SAMPLE";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test07
	 */
	public void test07() {
		String originalSql = "SELECT * FROM SAMPLE"
			+ " @[ WHERE"
				+ " @[(@id[AND ID=:id] @name[OR NAME=:name])]"
				+ " @list[AND LIST IN (:list)]"
				+ " @kind[AND KIND=:kind]"
				+ " @!kind[AND KIND IS NULL]"
			+ " ]"
			+ " @orderCol[ORDER BY ::orderCol ::orderDir]";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Name");
		map.put("kind", 'K');
		map.put("orderCol", "ID");

		String translatedSql = "SELECT * FROM SAMPLE"
			+ " WHERE ( NAME= ? ) AND KIND= ? ORDER BY ID";
		
		List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		parameters.add(new SqlParameter("name", "Name"));
		parameters.add(new SqlParameter("kind", 'K'));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test08
	 */
	public void test08() {
		String originalSql = "UPDATE SAMPLE"
			+ " SET"
				+ " @name [,NAME=:name]"
				+ " @kind[,KIND=:kind]"
				+ " ,OTHER='@kind[,''KIND=:kind]'"
			+ " WHERE"
				+ " ID=:id";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kind", 'K');

		String translatedSql = "UPDATE SAMPLE SET KIND= ? , OTHER= '@kind[,''KIND=:kind]' WHERE ID= ?";
		
		List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		parameters.add(new SqlParameter("kind", 'K'));
		parameters.add(new SqlParameter("id", null));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test09
	 */
	public void test09() {
		String originalSql = "UPDATE SAMPLE"
			+ " SET"
				+ " @name[,NAME=:name]"
				+ " @kind[,KIND=:kind:CHAR]"
				+ " ,OTHER='@kind[,KIND=:kind]'"
			+ " WHERE"
				+ " ID=:id";

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("kind", 'K');

		String translatedSql = "UPDATE SAMPLE SET KIND= ? , OTHER= '@kind[,KIND=:kind]' WHERE ID= ?";
		
		List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		parameters.add(new SqlParameter("kind", 'K', "CHAR"));
		parameters.add(new SqlParameter("id", null));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}
}
