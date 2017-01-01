package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMssql2012Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2012");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
