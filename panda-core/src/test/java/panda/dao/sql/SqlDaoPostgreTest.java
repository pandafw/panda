package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoPostgreTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("postgre");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
