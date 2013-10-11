package panda.dao.sql;



/**
 */
public class SqlDaoHsqldbTest extends SqlDaoTest {
	private static SqlDaoClient client = createSqlDaoClient("hsqldb");

	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
