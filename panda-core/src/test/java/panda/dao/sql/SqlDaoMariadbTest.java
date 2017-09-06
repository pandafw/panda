package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoMariadbTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("mariadb");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
