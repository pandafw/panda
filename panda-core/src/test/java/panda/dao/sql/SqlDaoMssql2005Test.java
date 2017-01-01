package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMssql2005Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2005");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
