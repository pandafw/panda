package panda.dao.sql;



/**
 */
public class SqlDaoMssql2008Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2008");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
