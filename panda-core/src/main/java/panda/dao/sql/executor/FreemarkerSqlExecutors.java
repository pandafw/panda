package panda.dao.sql.executor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import panda.dao.sql.SqlExecutor;
import panda.lang.Systems;
import panda.tpl.ftl.ClassTemplateLoader;


/**
 */
public class FreemarkerSqlExecutors extends SimpleSqlExecutors {
	/**
	 * configuration
	 */
	private Configuration configuration;

	/**
	 * template file location
	 */
	private File location;
	
	
	public FreemarkerSqlExecutors() throws IOException {
		setLocation(Systems.getUserDir());
	}

	/**
	 * @param location template file location
	 * @throws IOException if an error occurs
	 */
	public FreemarkerSqlExecutors(File location) throws IOException {
		setLocation(location);
	}

	/**
	 * @return the location
	 */
	public File getLocation() {
		return location;
	}


	/**
	 * @param location the location to set
	 */
	public void setLocation(File location) throws IOException {
		this.location = location;
		this.configuration = getConfiguration();
	}

	protected Template getTemplate(String name) throws IOException {
		return configuration.getTemplate(name);
	}
	
	/**
	 * @return the configuration
	 * @throws IOException if an error occurs
	 */
	@SuppressWarnings("deprecation")
	protected Configuration getConfiguration() throws IOException {
		Configuration configuration = new Configuration();

		List<TemplateLoader> tls = new ArrayList<TemplateLoader>();
		tls.add(new ClassTemplateLoader());
		tls.add(new FileTemplateLoader(location));
		
		MultiTemplateLoader mtl = new MultiTemplateLoader(tls.toArray(new TemplateLoader[tls.size()]));
		
		configuration.setTemplateLoader(mtl);
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
