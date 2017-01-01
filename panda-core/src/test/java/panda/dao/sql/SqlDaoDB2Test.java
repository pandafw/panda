package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoDB2Test extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("db2");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
