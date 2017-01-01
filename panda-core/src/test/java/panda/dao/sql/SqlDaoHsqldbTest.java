package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoHsqldbTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("hsqldb");

	protected DaoClient getDaoClient() {
		return client;
	}
}
