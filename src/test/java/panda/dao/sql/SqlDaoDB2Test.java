package panda.dao.sql;



/**
 */
public class SqlDaoDB2Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("db2");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
