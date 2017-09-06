package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMssql2016Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mssql2016");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
