package panda.dao.sql.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.dao.sql.SqlExecutor;
import panda.log.Log;
import panda.log.Logs;


/**
 */
public class SimpleSqlParserTest {
	/**
	 * log
	 */
	protected static Log log = Logs.getLog(SimpleSqlParserTest.class);

	protected String getTestMethodName() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StackTraceElement ste = stack[2];
		return this.getClass().getSimpleName() + "." + ste.getMethodName() + "()";
	}

	protected SqlExecutor createExecutor() {
		return new SimpleSqlManager().getExecutor();
	}
	
	protected JdbcSqlParser createParser(String sql) {
		return new SimpleSqlParser(sql);
	}

	protected void testTranslate(String originalSql, Object paramObject, String translatedSql, List<JdbcSqlParameter> parameters) {
		log.debug("");
		log.debug(getTestMethodName());
		log.debug("original SQL: [" + originalSql + "]");

		String actTranslatedSql = null;
		List<JdbcSqlParameter> actParameters = null;
		try {
			JdbcSqlExecutor executor = (JdbcSqlExecutor)createExecutor();
			JdbcSqlParser parser = createParser(originalSql);
			
			actParameters = new ArrayList<JdbcSqlParameter>();
			actTranslatedSql = parser.parse(executor, paramObject, actParameters);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		log.debug("expected SQL: [" + translatedSql + "]");
		log.debug("actual SQL  : [" + actTranslatedSql + "]");

		Assert.assertEquals(translatedSql, actTranslatedSql);
		
		if (parameters == null) {
			Assert.assertTrue(actParameters.isEmpty());
		}
		else {
			log.debug("expected parameters: " + parameters);
			log.debug("actual   parameters: " + actParameters);
			Assert.assertEquals(parameters, actParameters);
		}
	}
	
	/**
	 * test for alias
	 */
	@Test
	public void testAlias() {
		String originalSql = "SELECT ID AS a.id, NAME AS a.idName, ITEM_NAME AS a.item.name, B_ITEM_NAME as a.b.itemName FROM SAMPLE";
		String translatedSql = "SELECT ID AS A_0_ID , NAME AS A_0_ID_NAME , ITEM_NAME AS A_0_ITEM_0_NAME , B_ITEM_NAME as A_0_B_0_ITEM_NAME FROM SAMPLE";

		testTranslate(originalSql, null, translatedSql, null);
	}
	
	/**
	 * test for comment
	 */
	@Test
	public void testComment() {
		String originalSql = "SELECT /* SELECT */ ID AS a.id, NAME AS a.name, ITEM_NAME AS a.item.name FROM SAMPLE";
		String translatedSql = "SELECT ID AS A_0_ID , NAME AS A_0_NAME , ITEM_NAME AS A_0_ITEM_0_NAME FROM SAMPLE";

		testTranslate(originalSql, null, translatedSql, null);
	}
	
	/**
	 * test variable
	 */
	@Test
	public void testVariable() {
		String originalSql = " SELECT * FROM SAMPLE WHERE NAME=:a.name";

		Map<String, Map> map = new HashMap<String, Map>();
		Map<String, String> a = new HashMap<String, String>();
		a.put("name", "test");
		map.put("a", a);

		String translatedSql = "SELECT * FROM SAMPLE WHERE NAME= ?";
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("a.name", "test"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}
	
	/**
	 * test parameter
	 */
	@Test
	public void testParameter() {
		String originalSql = "SELECT * FROM SAMPLE WHERE ID=? and NAME=?";

		String a[] = new String[] { "id", "name" };

		String translatedSql = "SELECT * FROM SAMPLE WHERE ID= ? and NAME= ?";
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("0", "id"));
		parameters.add(new JdbcSqlParameter("1", "name"));
		
		testTranslate(originalSql, a, translatedSql, parameters);
	}

	/**
	 * test replace
	 */
	@Test
	public void testReplace() {
		String originalSql = "SELECT * FROM SAMPLE ORDER BY ::orderCol ::orderDir";

		Map<String, String> map = new HashMap<String, String>();
		map.put("orderCol", "NAME");

		String translatedSql = "SELECT * FROM SAMPLE ORDER BY NAME";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test collection
	 */
	@Test
	public void testCollection() {
		String originalSql = "SELECT * FROM SAMPLE WHERE LIST IN (:list)";

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("list", list);

		String translatedSql = "SELECT * FROM SAMPLE WHERE LIST IN( ?,?,?)";
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("list", "a"));
		parameters.add(new JdbcSqlParameter("list", "b"));
		parameters.add(new JdbcSqlParameter("list", "c"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}
}
