package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMssql2008Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2008");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
