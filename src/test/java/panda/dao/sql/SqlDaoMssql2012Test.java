package panda.dao.sql;



/**
 */
public class SqlDaoMssql2012Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2012");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
