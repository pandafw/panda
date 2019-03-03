package panda.dao.sql;

import panda.dao.DaoClient;


public class SqlDaoDerbyTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("derby");

	protected DaoClient getDaoClient() {
		return client;
	}

	protected String concatSql(String a, String b) {
		return "(" + a + " || " + b + ")";
	}
}
