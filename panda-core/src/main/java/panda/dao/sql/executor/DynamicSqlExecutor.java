package panda.dao.sql.executor;



/**
 * SqlExecutor supports dynamic sql.
 * 
 * <pre>
 * Dynamic SQL: 
 *    &#64; --> (at mark) at something
 *    &#64;!  --> not at something
 * 
 *    1) SELECT * FROM SAMPLE
 *         &#64;[ WHERE
 *           &#64;[(&#64;id[ID=:id] &#64;name[OR NAME=:name] &#64;bool[OR BOOL=:bool])]
 *           &#64;list[AND LIST IN (:list)]
 *           &#64;kind[AND KIND=:kind]
 *           &#64;!kind[AND KIND IS NULL]
 *         ]
 *         &#64;orderCol[ ORDER BY ::orderCol ::orderDir ]
 * 
 *    descripton:
 *      &#64;id[ID=:id] --> append sql when id is not null
 *      &#64;bool[BOOL=:bool] --> append sql when bool is TRUE
 *      &#64;list[LIST IN (:list)]  --> append sql when list != null && list.size() > 0 (:list --> ?, ?, ?)
 *      &#64;[ WHERE ... ]  --> append sql when all the logical expression in &#64;[...] is true
 *      &#64;!kind[AND KIND IS NULL] --> append sql when kind is null
 * 
 *    2) UPDATE SAMPLE
 *         SET
 *           &#64;name[,NAME=:name]
 *           &#64;kind[,KIND=:kind]
 *           ,OTHER='@kind[,KIND=:kind]'
 *         WHERE
 *           ID=:id
 *
 *
 * @see SimpleSqlExecutor
 * 
 * </pre>
 * 
 */
public class DynamicSqlExecutor extends SimpleSqlExecutor {
	/**
	 * Constructor
	 * @param sqlExecutors sqlExecutors
	 */
	protected DynamicSqlExecutor(DynamicSqlExecutors sqlExecutors) {
		super(sqlExecutors);
	}

	/**
	 * createSqlParser
	 * @param sql sql
	 * @return JdbcSqlParser instance
	 */
	@Override
	protected JdbcSqlParser createSqlParser(String sql) {
		return new DynamicSqlParser(sql);
	}
}
