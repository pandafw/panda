package panda.wing.util;

import java.util.Map;
import java.util.Map.Entry;

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
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.wing.constant.SC;

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
		
		String prefix = settings.getProperty(SC.DATA_PREFIX);
		if (Strings.isNotEmpty(prefix)) {
			daoClient.setPrefix(prefix);
		}
	}
	
	protected DaoClient buildDaoClient() throws Exception {
		String dstype = settings.getProperty(SC.DATA_SOURCE);
		if (GAE.equalsIgnoreCase(dstype)) {
			GaeDaoClient gdc = GaeDaoClient.i();
			return gdc;
		}
		
		if (MONGO.equalsIgnoreCase(dstype)) {
			String url = settings.getProperty(SC.DATA_MONGO_URL);

			log.info("MONGO - " +  url);
			MongoDaoClient daoClient = new MongoDaoClient(url);
			
			return daoClient;
		}

		if (JNDI.equalsIgnoreCase(dstype)) {
			SqlDaoClient sqlDaoClient = new SqlDaoClient();

			String jndi = settings.getProperty(SC.DATA_JNDI_RESOURCE);
			log.info("JNDI - " + jndi);
			try {
				DataSource ds = Sqls.lookupJndiDataSource(jndi);
				sqlDaoClient.setDataSource(ds);
			}
			catch (Exception e) {
				log.warn("Failed to use jndi resource - " + jndi + " : " + e.getMessage());
				DataSource ds = createSimpleDataSource();
				sqlDaoClient.setDataSource(ds);
			}
			return sqlDaoClient;
		}

		if (JDBC.equalsIgnoreCase(dstype)) {
			DataSource ds = createSimpleDataSource();
			SqlDaoClient sqlDaoClient = new SqlDaoClient();
			sqlDaoClient.setDataSource(ds);
			return sqlDaoClient;
		}

		throw new IllegalArgumentException("The data source type [" + dstype + "] is invalid.");
	}

	private DataSource createSimpleDataSource() {
		log.info("JDBC - " + settings.getProperty(SC.DATA_JDBC_DRIVER) + ":" + settings.getProperty(SC.DATA_JDBC_URL));

		Map<String, String> dps = Collections.subMap(settings, SC.DATA_PREFIX);
		if (servlet != null) {
			String web = servlet.getRealPath("/");
			for (Entry<String, String> en : dps.entrySet()) {
				if (en.getValue().contains("${web}")) {
					String v = en.getValue().replace("${web}", web);
					en.setValue(v);
				}
			}
		}
		DataSource ds = new SimpleDataSource(dps);
		
		return ds;
	}
}
