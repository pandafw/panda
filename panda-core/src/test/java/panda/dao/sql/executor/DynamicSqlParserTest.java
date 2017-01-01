package panda.dao.sql.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import panda.dao.DaoTypes;
import panda.dao.sql.SqlExecutor;


/**
 */
public class DynamicSqlParserTest extends SimpleSqlParserTest {
	protected SqlExecutor createExecutor() {
		return new DynamicSqlManager().getExecutor();
	}
	
	protected JdbcSqlParser createParser(String sql) {
		return new DynamicSqlParser(sql);
	}

	/**
	 * test for '@'
	 */
	@Test
	public void testAtMark() {
		String originalSql = "SELECT * FROM SAMPLE @orderCol[ORDER BY ::orderCol ::orderDir]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("orderCol", "NAME");

		String translatedSql = "SELECT * FROM SAMPLE ORDER BY NAME";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test for '@' List
	 */
	@Test
	public void testAtList() {
		String originalSql = "SELECT * FROM SAMPLE WHERE @list[LIST IN (:list:list)]";

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		map.put("list", list);

		String translatedSql = "SELECT * FROM SAMPLE WHERE LIST IN( ?,?,?)";
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("list", "a", DaoTypes.LIST));
		parameters.add(new JdbcSqlParameter("list", "b", DaoTypes.LIST));
		parameters.add(new JdbcSqlParameter("list", "c", DaoTypes.LIST));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test for not at
	 */
	@Test
	public void testNotAt() {
		String originalSql = "SELECT * FROM SAMPLE WHERE @kind[KIND=:kind] @!kind[KIND IS NULL]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("kind", "a");

		String translatedSql = "SELECT * FROM SAMPLE WHERE KIND= ?";
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("kind", "a"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test for not at(null)
	 */
	@Test
	public void testNotAtNull() {
		String originalSql = "SELECT * FROM SAMPLE WHERE @kind[KIND=:kind] @!kind[KIND IS NULL]";

		Map<String, String> map = new HashMap<String, String>();

		String translatedSql = "SELECT * FROM SAMPLE WHERE KIND IS NULL";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test for at where
	 */
	@Test
	public void testAtWhere() {
		String originalSql = "SELECT * FROM SAMPLE @[WHERE @id[AND ID=:id] @name[AND NAME=:name] @!kind[AND KIND IS NULL]]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "Id");
		map.put("name", "Name");

		String translatedSql = "SELECT * FROM SAMPLE WHERE ID= ? AND NAME= ? AND KIND IS NULL";
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("id", "Id"));
		parameters.add(new JdbcSqlParameter("name", "Name"));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * test for at where (null)
	 */
	@Test
	public void testAtWhereNull() {
		String originalSql = "SELECT * FROM SAMPLE @[WHERE @id[AND ID=:id] @name[AND NAME=:name] @!kind[AND KIND IS NULL]]";

		Map<String, String> map = new HashMap<String, String>();
		map.put("kind", "Kind");

		String translatedSql = "SELECT * FROM SAMPLE";
		
		testTranslate(originalSql, map, translatedSql, null);
	}

	/**
	 * test for at where with paren
	 */
	@Test
	public void testAtWhereParen() {
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
			+ " WHERE( NAME= ?) AND KIND= ? ORDER BY ID";
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("name", "Name"));
		parameters.add(new JdbcSqlParameter("kind", 'K'));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * testAtSet
	 */
	@Test
	public void testAtSet() {
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
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("kind", 'K'));
		parameters.add(new JdbcSqlParameter("id", null));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}

	/**
	 * testAtSet2
	 */
	@Test
	public void testAtSet2() {
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
		
		List<JdbcSqlParameter> parameters = new ArrayList<JdbcSqlParameter>();
		parameters.add(new JdbcSqlParameter("kind", 'K', "CHAR"));
		parameters.add(new JdbcSqlParameter("id", null));
		
		testTranslate(originalSql, map, translatedSql, parameters);
	}
}
