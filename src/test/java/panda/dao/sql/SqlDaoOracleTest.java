package panda.dao.sql;



/**
 */
public class SqlDaoOracleTest extends SqlDaoTest {
	private static SqlDaoClient client = createSqlDaoClient("oracle");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}
}
