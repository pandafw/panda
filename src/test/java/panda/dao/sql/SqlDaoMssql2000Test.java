package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMssql2000Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2000");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
