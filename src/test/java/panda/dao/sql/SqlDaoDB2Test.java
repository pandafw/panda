package panda.dao.sql;



/**
 */
public class SqlDaoDB2Test extends SqlDaoTest {
	private static SqlDaoClient client = createSqlDaoClient("db2");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
