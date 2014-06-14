package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMysqlTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mysql");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
