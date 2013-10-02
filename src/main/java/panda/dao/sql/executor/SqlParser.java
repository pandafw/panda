package panda.dao.sql.executor;

import java.util.List;

import panda.dao.sql.SqlExecutor;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface SqlParser {
	/**
	 * parse sql by parameter
	 * @param executor sql executor
	 * @param parameter parameter
	 * @param sqlParams parameter list (output)
	 * @return jdbc sql  
	 */
	String parse(SqlExecutor executor, Object parameter, List<SqlParameter> sqlParams);
}
