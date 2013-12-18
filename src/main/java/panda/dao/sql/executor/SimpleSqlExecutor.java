package panda.dao.sql.executor;

import java.util.List;

import panda.dao.sql.SqlManager;


/**
 * SimpleSqlExecutor.
 * 
 * <pre>
 * <b>SQL GRAMMER:</b>
 * 
 * 1. SELECT column name auto mapping
 *    1.1 column name --> JavaBean property name
 *      ex.) SELECT ID, NAME, ITEM_NAME FROM SAMPLE
 *        ID --> id
 *        NAME --> name
 *        ITEM_NAME --> itemName
 * 
 *    1.2 AS ALIAS --> JavaBean property name
 *      ex.) SELECT ID AS a.id, NAME AS a.name, ITEM_NAME AS a.item.name, B_ITEM_NAME AS a.b.itemName FROM SAMPLE
 *           -->
 *           SELECT ID AS A_0_ID, NAME AS A_0_NAME, ITEM_NAME AS A_0_ITEM_0_NAME, ITEM_NAME AS A_0_B_0_ITEM_NAME FROM SAMPLE
 *      
 *       mapping: 
 *         A_0_ID --> a.id
 *         A_0_NAME --> a.name
 *         A_0_ITEM_0_NAME --> a.item.name
 * 
 * 2. SQL Parameter
 *    :xxx --> PreparedStatement binding parameter (xxx: JavaBean property name)
 *    ::yyy --> replacement (yyy: JavaBean property name) [SQL injection!]
 * 
 *    ex.) SELECT * FROM SAMPLE
 *         WHERE
 *               ID=:id
 *           AND NAME=:name
 *           AND LIST IN (:list)
 *           AND KIND=:kind
 *         ORDER BY 
 *           ::orderCol ::orderDir
 * 
 *    ex.) UPDATE SAMPLE
 *         SET
 *           NAME=:name,
 *           KIND=:kind,
 *           STR='@kind[,KIND=:kind]'
 *         WHERE
 *           ID=:id
 * 
 * 3. SQL Amendment
 *    1) Delete 'AND/OR' after 'WHERE' or '('
 *     ex.) WHERE AND ID IS NULL --> WHERE ID IS NULL
 *          (AND NAME IS NULL) --> (NAME IS NULL)
 *  
 *    2) Delete ',' after 'SET'
 *     ex.) SET ,ID=1 ,NAME='a' --> SET ID=1 , NAME='a'
 *     
 * 4. Call Procedure/Function
 *    ex.) {:count:INTEGER:OUT = call SET_TEST_PRICE(:id,:price:DECIMAL.2:INOUT)}
 *   
 *    Grammer: 
 *      :price:DECIMAL.2:INOUT
 *     
 *      :price --> JavaBean property name
 *      :DECIMAL.2 --> JDBC type & scale
 *      :INOUT --> parameter type
 *         IN: input only (default)
 *         OUT: output only
 *         INOUT: input & output
 *         
 *    @see java.sql.CallableStatement 
 *
 *
 *
 * <b>EXAMPLE:</b>
 *   Class.forName("org.hsqldb.jdbcDriver" );
 *   Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "");
 *   SqlExecutor executor = new SimpleSqlExecutor(connection);
 *   
 *   
 *   // queryForObject - Map
 *   String sql = "SELECT * FROM TEST WHERE ID=:id";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("id", 1001);
 *   
 *   Map result = executor.queryForObject(sql, param, HashMap.class);
 *   // result: { "id": 1001, "name": "test1001", ... }
 *   
 *   
 *   // queryForList - Map
 *   String sql = "SELECT * FROM TEST WHERE ID IN (:idArray)";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("idArray", new int[] { 1001, 1003 });
 *   
 *   List<Map> resultList = executor.queryForList(sql, param, HashMap.class);
 *   // resultList: [{ "id": 1001, "name": "test1001", ... }, { "id": 1003, "name": "test1003", ... }]
 *   
 *   
 *   // queryForObject - JavaBean
 *   String sql = "SELECT * FROM TEST WHERE ID=:id";
 *   TestA param = new TestA();
 *   param.id = 1001;
 *   
 *   TestB result = executor.queryForObject(sql, param, TestB.class);
 *   // result: { "id": 1001, "name": "test1001", ... }
 *
 *   
 *   // queryForList - JavaBean
 *   String sql = "SELECT * FROM TEST WHERE ID IN (:idArray)";
 *   TestA param = new TestA();
 *   param.idArray = new int[] { 1001, 1003 };
 *   
 *   List<TestB> resultList = executor.queryForList(sql, param, TestB.class);
 *   // resultList: [{ "id": 1001, "name": "test1001", ... }, { "id": 1003, "name": "test1003", ... }]
 *
 *
 *   // execute - Call Function
 *   String sql = "{:price:DECIMAL.2:OUT = call GET_TEST_PRICE(:id)}";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("id", 1001);
 *   
 *   executor.execute(sql, param);
 *   // param: { "id": 1001, "price": 1001.01 }
 *   
 *   
 *   // execute - Call Function
 *   String sql = "{:count:INTEGER:OUT = call SET_TEST_PRICE(:id, :price:DECIMAL.2:INOUT)}";
 *   Map<String, Object> param = new HashMap<String, Object>();
 *   param.put("id", 1001);
 *   param.put("price", new BigDecimal("9999"));
 *   
 *   executor.execute(sql, param);
 *   // param: { "count": 1, "price": 1001.01 }
 *   
 *   
 *   // queryForObject - Call Function
 *   String sql = "{call SELECT_TEST(:id)}";
 *   TestA param = new TestA();
 *   param.id = 1001;
 *   
 *   TestB result = executor.queryForObject(sql, param, TestB.class);
 *   // result: { "id": 1001, "name": "test1001", ... }
 * 
 * </pre>
 * 
 * @author yf.frank.wang@gmail.com
 */
public class SimpleSqlExecutor extends JdbcSqlExecutor {
	/**
	 * Constructor
	 * @param sqlManager sqlManager
	 */
	protected SimpleSqlExecutor(SqlManager sqlManager) {
		super(sqlManager);
	}

	/**
	 * @return the sqlManager
	 */
	public SimpleSqlManager getSimpleSqlManager() {
		return (SimpleSqlManager)getSqlManager();
	}

	/**
	 * parseSqlStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @return translated sql
	 * @throws Exception if an error occurs
	 */
	@Override
	protected String parseSqlStatement(String sql, Object parameter, List<JdbcSqlParameter> sqlParams) {
		JdbcSqlParser parser = getSimpleSqlManager().getSqlParser(sql);
		if (parser == null) {
			parser = createSqlParser(sql);
			getSimpleSqlManager().putSqlParser(sql, parser);
		}
		return parser.parse(this, parameter, sqlParams);
	}

	/**
	 * createSqlParser
	 * @param sql sql
	 * @return JdbcSqlParser instance
	 */
	protected JdbcSqlParser createSqlParser(String sql) {
		return new SimpleSqlParser(sql);
	}
}
