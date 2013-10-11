package panda.dao.sql;



/**
 */
public class SqlDaoSqliteTest extends SqlDaoTest {
	private static SqlDaoClient client = createSqlDaoClient("sqlite");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
