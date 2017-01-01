package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoH2Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("h2");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
