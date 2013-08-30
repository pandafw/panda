package panda.dao.sql.engine;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import panda.dao.sql.SqlExecutor;
import panda.lang.ClassLoaders;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;


/**
 * @author yf.frank.wang@gmail.com
 */
public class FreemarkerSqlManager extends SimpleSqlManager {
	/**
	 * configuration
	 */
	private Configuration configuration;

	/**
	 * ClassTemplateLoader
	 */
	public static class ClassTemplateLoader extends URLTemplateLoader {
		protected URL getURL(String name) {
			return ClassLoaders.getResourceAsURL(name, getClass());
		}
	}
	
	/**
	 * @return the configuration
	 * @throws Exception if an error occurs
	 */
	public Configuration getConfiguration() throws Exception {
		if (configuration == null) {
			configuration = new Configuration();

			List<TemplateLoader> tls = new ArrayList<TemplateLoader>();
			tls.add(new ClassTemplateLoader());
			tls.add(new FileTemplateLoader());
			
			MultiTemplateLoader mtl = new MultiTemplateLoader(tls.toArray(new TemplateLoader[tls.size()]));
			
			configuration.setTemplateLoader(mtl);
		}
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Constructor
	 * 
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @param resultSetConcurrency one of the following ResultSet constants:
	 *            ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * @param resultSetHoldability one of the following ResultSet constants:
	 *            ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
	 */
	@Override
	public SqlExecutor getExecutor(Connection connection, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) {
		FreemarkerSqlExecutor se = new FreemarkerSqlExecutor(this);
		se.setConnection(connection);
		se.setResultSetType(resultSetType);
		se.setResultSetConcurrency(resultSetConcurrency);
		se.setResultSetHoldability(resultSetHoldability);
		return se;
	}
}
