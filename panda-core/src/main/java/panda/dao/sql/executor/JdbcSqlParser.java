package panda.dao.sql.executor;

import java.util.List;

/**
 * @author yf.frank.wang@gmail.com
 */
abstract class JdbcSqlParser {
	/**
	 * parse sql by parameter
	 * @param executor sql executor
	 * @param parameter parameter
	 * @param sqlParams parameter list (output)
	 * @return jdbc sql  
	 */
	public abstract String parse(JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams);
}
