package panda.dao.sql;



/**
 */
public class SqlDaoHsqldbTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("hsqldb");

	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
