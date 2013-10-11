package panda.dao.sql;



/**
 */
public class SqlDaoPostgreTest extends SqlDaoTest {
	private static SqlDaoClient client = createSqlDaoClient("postgre");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
