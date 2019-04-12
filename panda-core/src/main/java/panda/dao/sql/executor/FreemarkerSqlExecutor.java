package panda.dao.sql.executor;

import java.util.List;

import panda.io.Streams;
import panda.io.stream.StringBuilderWriter;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import freemarker.template.Template;

/**
 * FreemarkerSqlExecutor
 */
public class FreemarkerSqlExecutor extends SimpleSqlExecutor {
	/**
	 * log
	 */
	private static Log log = Logs.getLog(FreemarkerSqlExecutor.class);

	/**
	 * Constructor
	 * @param sqlExecutors sqlExecutors
	 */
	protected FreemarkerSqlExecutor(FreemarkerSqlExecutors sqlExecutors) {
		super(sqlExecutors);
	}

	/**
	 * @return the freemarker sql executors
	 */
	public FreemarkerSqlExecutors getFreemarkerSqlExecutors() {
		return (FreemarkerSqlExecutors)getSqlExecutors();
	}

	/**
	 * parseSqlStatement
	 * @param sql sql
	 * @param parameterObject parameter object
	 * @param sqlParams sql parameter list
	 * @return translated sql
	 */
	@Override
	protected String parseSqlStatement(String sql, Object parameterObject, List<JdbcSqlParameter> sqlParams) {
		StringBuilderWriter sw = new StringBuilderWriter();
		try {
			Template template = getFreemarkerSqlExecutors().getTemplate(sql);
			if (template == null) {
				throw new IllegalArgumentException("Failed to load SQL template: " + sql);
			}
			template.process(parameterObject, sw);
			
			sql = sw.toString();
			if (log.isDebugEnabled()) {
				log.debug(sql);
			}
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
		finally {
			Streams.safeClose(sw);
		}

		return super.parseSqlStatement(sql, parameterObject, sqlParams);
	}
}
