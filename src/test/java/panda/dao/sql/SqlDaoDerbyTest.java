package panda.dao.sql;




/**
 */
public class SqlDaoDerbyTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("derby");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}

	protected String escapeColumn(String column) {
		return '"' + column + '"';
	}
	
}
