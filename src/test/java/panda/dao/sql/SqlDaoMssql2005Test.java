package panda.dao.sql;



/**
 */
public class SqlDaoMssql2005Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2005");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
