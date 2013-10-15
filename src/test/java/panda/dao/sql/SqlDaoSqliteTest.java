package panda.dao.sql;



/**
 */
public class SqlDaoSqliteTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("sqlite");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
