package panda.dao.sql;

import panda.dao.DaoClient;



/**
 */
public class SqlDaoOracleTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("oracle");
	
	protected DaoClient getDaoClient() {
		return client;
	}
}
