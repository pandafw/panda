package panda.wing.util;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import panda.dao.DaoClient;
import panda.dao.nosql.gae.GaeDaoClient;
import panda.dao.nosql.mongo.MongoDaoClient;
import panda.dao.sql.SimpleDataSource;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.Sqls;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

@IocBean(create="initialize")
public class AppDaoClientProvider {
	private static final Log log = Logs.getLog(AppDaoClientProvider.class);

	private static final String GAE = "gae";
	private static final String MONGO = "mongo";
	private static final String JNDI = "jndi";
	private static final String JDBC = "jdbc";
	
	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Settings settings;

	protected DaoClient daoClient;
	

	/**
	 * @return the daoClient
	 */
	public DaoClient getDaoClient() {
		return daoClient;
	}

	public void initialize() throws Exception {
		daoClient = buildDaoClient();
		
		String prefix = settings.getProperty("data.prefix");
		if (Strings.isNotEmpty(prefix)) {
			daoClient.getEntityMaker().setPrefix(prefix);
		}
	}
	
	protected DaoClient buildDaoClient() throws Exception {
		String dstype = settings.getProperty("data.source");
		if (GAE.equalsIgnoreCase(dstype)) {
			GaeDaoClient gdc = GaeDaoClient.i();
			return gdc;
		}
		
		if (MONGO.equalsIgnoreCase(dstype)) {
			String url = settings.getProperty("mongo.url");

			log.info("mongo - " +  url);
			MongoDaoClient daoClient = new MongoDaoClient(url);
			
			return daoClient;
		}

		if (JNDI.equalsIgnoreCase(dstype)) {
			SqlDaoClient sqlDaoClient = new SqlDaoClient();

			String jndi = settings.getProperty("jndi.resource");
			log.info("jndi.resource - " + jndi);
			try {
				DataSource ds = Sqls.lookupJndiDataSource(jndi);
				sqlDaoClient.setDataSource(ds);
			}
			catch (Exception e) {
				log.warn("Failed to use jndi resource - " + jndi + " : " + e.getMessage());
				log.warn("Try to use SimpleDataSource - " + settings.getProperty("jdbc.driver") + ":" + settings.getProperty("jdbc.url"));

				DataSource ds = new SimpleDataSource(settings);
				sqlDaoClient.setDataSource(ds);
			}
			return sqlDaoClient;
		}

		if (JDBC.equalsIgnoreCase(dstype)) {
			log.info("jdbc - " + settings.getProperty("jdbc.driver") + ":" + settings.getProperty("jdbc.url"));

			DataSource ds = new SimpleDataSource(settings);
			SqlDaoClient sqlDaoClient = new SqlDaoClient();
			sqlDaoClient.setDataSource(ds);
			return sqlDaoClient;
		}

		throw new IllegalArgumentException("The data source type [" + dstype + "] is invalid.");
	}

}
