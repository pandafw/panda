package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMssql2014Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2014");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
