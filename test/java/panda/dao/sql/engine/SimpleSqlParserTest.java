package panda.dao.sql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.engine.SimpleSqlManager;
import panda.dao.sql.engine.SimpleSqlParser;
import panda.dao.sql.engine.SqlParameter;
import panda.log.Log;
import panda.log.Logs;
import junit.framework.TestCase;


/**
 * SimpleSqlStatementTest
 */
public class SimpleSqlParserTest extends TestCase {
	/**
	 * log
	 */
	private static Log log = Logs.getLog(SimpleSqlParserTest.class);

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
			SqlExecutor executor = new SimpleSqlManager().getExecutor();
			SimpleSqlParser parser = new SimpleSqlParser(originalSql);
			
			actParameters = new ArrayList<SqlParameter>();
			actTranslatedSql = parser.parse(executor, paramObject, actParameters);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		log.debug("expected SQL: [" + translatedSql + "]");
		log.debug("actual SQL  : [" + actTranslatedSql + "]");

		assertEquals(translatedSql, actTranslatedSql);
		
		if (parameters == null) {
			assertTrue(actParameters.isEmpty());
		}
		else {
			log.debug("expected parameters: " + parameters);
			log.debug("actual   parameters: " + actParameters);
			assertEquals(parameters, actParameters);
		}
	}
	
	/**
	 * test01
	 */
	public void test01() {
		String originalSql = "SELECT ID AS a.id, NAME AS a.idName, ITEM_NAME AS a.item.name, B_ITEM_NAME as a.b.itemName FROM SAMPLE";
		String translatedSql = "SELECT ID AS A_0_ID , NAME AS A_0_ID_NAME , ITEM_NAME AS A_0_ITEM_0_NAME , B_ITEM_NAME as A_0_B_0_ITEM_NAME FROM SAMPLE";

		testTranslate(originalSql, null, translatedSql, null);
	}
	
	/**
	 * test02
	 */
	public void test02() {
		String originalSql = "SELECT /* SELECT */ ID AS a.id, NAME AS a.name, ITEM_NAME AS a.item.name FROM SAMPLE";
		String translatedSql = "SELECT /* SELECT */ ID AS A_0_ID , NAME AS A_0_NAME , ITEM_NAME AS A_0_ITEM_0_NAME FROM SAMPLE";

		testTranslate(originalSql, null, translatedSql, null);
	}
	
	/**
	 * test03
	 */
	public void test03() {
		String originalSql = "SELECT * FROM SAMPLE WHERE NAME=:a.name";

		Map<String, Map> map = new HashMap<String, Map>();
		Map<String, String> a = new HashMap<String, String>();
		a.put("name", "test");
		map.put("a", a);

		String translatedSql = "SELECT * FROM SAMPLE WHERE NAME= ?";
		
		List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		parameters.add(new SqlParameter("a.name", "test"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test04
	 */
	public void test04() {
		String originalSql = "SELECT * FROM SAMPLE ORDER BY ::orderCol ::orderDir";

		Map<String, String> map = new HashMap<String, String>();
		map.put("orderCol", "NAME");

		String translatedSql = "SELECT * FROM SAMPLE ORDER BY NAME";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test05
	 */
	public void test05() {
		String originalSql = "SELECT * FROM SAMPLE WHERE LIST IN (:list)";

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
}
