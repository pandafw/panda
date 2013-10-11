package panda.dao.sql;



/**
 */
public class SqlDaoMssql2000Test extends SqlDaoTest {
	private static SqlDaoClient client = createSqlDaoClient("mssql2000");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
