package panda.app.util;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.dao.DaoClient;
import panda.dao.gae.GaeDaoClient;
import panda.dao.mongo.MongoDaoClient;
import panda.dao.sql.SimpleDataSource;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.Sqls;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

@IocBean(create="initialize")
public class AppDaoClientFactory {
	private static final Log log = Logs.getLog(AppDaoClientFactory.class);

	private static final String GAE = "gae";
	private static final String MONGO = "mongo";
	private static final String JNDI = "jndi";
	private static final String JDBC = "jdbc";
	
	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Settings settings;

	@IocInject(value=MVC.DATA_QUERY_TIMEOUT, required=false)
	protected int timeout;
	
	protected DaoClient daoClient;
	

	/**
	 * @return the daoClient
	 */
	public DaoClient getDaoClient() {
		return daoClient;
	}

	public void initialize() throws Exception {
		daoClient = buildDaoClient();
		
		String prefix = settings.getProperty(SET.DATA_PREFIX);
		if (Strings.isNotEmpty(prefix)) {
			daoClient.setPrefix(prefix);
		}
		
		timeout = settings.getPropertyAsInt(SET.DATA_QUERY_TIMEOUT, timeout);
		daoClient.setTimeout(timeout);
	}
	
	protected DaoClient buildDaoClient() throws Exception {
		String dstype = settings.getProperty(SET.DATA_SOURCE);
		if (GAE.equalsIgnoreCase(dstype)) {
			GaeDaoClient gdc = GaeDaoClient.i();
			return gdc;
		}
		
		if (MONGO.equalsIgnoreCase(dstype)) {
			String url = settings.getProperty(SET.DATA_MONGO_URL);

			log.info("MONGO - " +  url);
			MongoDaoClient mdc = new MongoDaoClient(url);
			
			return mdc;
		}

		if (JNDI.equalsIgnoreCase(dstype)) {
			SqlDaoClient sqlDaoClient = new SqlDaoClient();

			String jndi = settings.getProperty(SET.DATA_JNDI_RESOURCE);
			log.info("JNDI - " + jndi);
			try {
				DataSource ds = Sqls.lookupJndiDataSource(jndi);
				sqlDaoClient.setDataSource(ds);
				return sqlDaoClient;
			}
			catch (Exception e) {
				log.warn("Failed to use jndi resource - " + jndi + " : " + e.getMessage());
				dstype = JDBC;
			}
		}

		Exception ex = null;
		if (JDBC.equalsIgnoreCase(dstype)) {
			String[] dss = Strings.split(settings.getProperty(SET.DATA_JDBC), ", ");
			
			if (Arrays.isEmpty(dss)) {
				dss = new String[] { "" };
			}
			
			for (String dst : dss) {
				String dsn = SET.DATA + (Strings.isEmpty(dst) ? "" : "." + dst);
				try {
					DataSource ds = createSimpleDataSource(dsn + ".");
					SqlDaoClient sqlDaoClient = new SqlDaoClient();
					sqlDaoClient.setDataSource(ds);
					return sqlDaoClient;
				}
				catch (Exception e) {
					ex = e;
					log.warn("Failed to create data source - " + dsn + " : " + e.getMessage());
				}
			}
		}

		String msg = "Failed to create data source [" + dstype + "].";
		RuntimeException re = new RuntimeException(msg);
		if (ex != null) {
			log.error(msg, ex);
			re.initCause(ex);
		}
		throw re;
	}

	private DataSource createSimpleDataSource(String prefix) {
		log.info("Create Simple JDBC DataSource: " + prefix);

		Map<String, String> dps = Collections.subMap(settings, prefix);
		String web = servlet != null ? servlet.getRealPath("/") : "web";
		for (Entry<String, String> en : dps.entrySet()) {
			String v = en.getValue();
			if (Strings.contains(v, "${web}")) {
				v = v.replace("${web}", web);
				en.setValue(v);
			}
			else if (Strings.startsWith(v, "${") && Strings.endsWith(v, "}")) {
				String k = v.substring(2, v.length() - 1);
				v = settings.getProperty(k, v);
				en.setValue(v);
			}
			log.info("  " + en.getKey() + ": " + en.getValue());
		}

		DataSource ds = new SimpleDataSource(dps);
		return ds;
	}
}
