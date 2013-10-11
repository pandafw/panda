package panda.dao.sql;



/**
 */
public class SqlDaoH2Test extends SqlDaoTest {
	private static SqlDaoClient client = createSqlDaoClient("h2");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
