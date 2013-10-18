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
 * @author yf.frank.wang@gmail.com
 */
public class FreemarkerSqlExecutor extends SimpleSqlExecutor {
	/**
	 * log
	 */
	private static Log log = Logs.getLog(FreemarkerSqlExecutor.class);

	/**
	 * Constructor
	 * @param sqlManager sqlManager
	 */
	protected FreemarkerSqlExecutor(FreemarkerSqlManager sqlManager) {
		super(sqlManager);
	}

	/**
	 * @return the sqlManager
	 */
	public FreemarkerSqlManager getFreemarkerSqlManager() {
		return (FreemarkerSqlManager)getSqlManager();
	}

	/**
	 * parseSqlStatement
	 * @param sql sql
	 * @param parameterObject parameter object
	 * @param sqlParams sql parameter list
	 * @return translated sql
	 */
	@Override
	protected String parseSqlStatement(String sql, Object parameterObject, List<SqlParameter> sqlParams) {
		StringBuilderWriter sw = new StringBuilderWriter();
		try {
			Template template = getFreemarkerSqlManager().getConfiguration().getTemplate(sql);
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
