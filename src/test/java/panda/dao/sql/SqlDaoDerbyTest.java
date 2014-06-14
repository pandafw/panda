package panda.dao.sql;

import panda.dao.DaoClient;




/**
 */
public class SqlDaoDerbyTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("derby");
	
	protected DaoClient getDaoClient() {
		return client;
	}

	protected String escapeColumn(String column) {
		return '"' + column + '"';
	}
	
}
