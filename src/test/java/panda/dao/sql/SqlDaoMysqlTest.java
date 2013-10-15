package panda.dao.sql;



/**
 */
public class SqlDaoMysqlTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mysql");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
