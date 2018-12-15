package panda.app.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.gae.GaeDaoClient;
import panda.dao.mongo.MongoDaoClient;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.Sqls;
import panda.dao.sql.dbcp.AbstractDataSource;
import panda.dao.sql.dbcp.SimpleDataSource;
import panda.dao.sql.dbcp.ThreadLocalDataSource;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

@IocBean(create="initialize")
public class AppDaoClientFactory {
	private static final Log log = Logs.getLog(AppDaoClientFactory.class);

	private static final String GAE = "gae";
	private static final String MONGO = "mongo";
	private static final String JNDI = "jndi";
	
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
	
	protected DaoClient buildDaoClient() {
		String[] dss = Strings.split(settings.getProperty(SET.DATA_SOURCE), ", ");
		return buildDaoClient(dss);
	}

	public DaoClient buildDaoClient(String... dss) {
		for (int i = 0; i < dss.length; i++) {
			String ds = dss[i];
			if (Strings.isEmpty(ds)) {
				continue;
			}
	
			try {
				String factory = settings.getProperty(SET.DATA + '.' + ds, ds);
				if (GAE.equalsIgnoreCase(factory)) {
					GaeDaoClient gdc = GaeDaoClient.i();
					return gdc;
				}
				
				if (MONGO.equalsIgnoreCase(factory)) {
					String url = settings.getProperty(String.format(SET.DATA_MONGO_URL, ds));
		
					log.info("MONGO - " +  url);
					MongoDaoClient mdc = new MongoDaoClient(url);
					
					return mdc;
				}
		
				if (JNDI.equalsIgnoreCase(factory)) {
					SqlDaoClient sqlDaoClient = new SqlDaoClient();
		
					String jndi = settings.getProperty(String.format(SET.DATA_JNDI_RESOURCE, ds));
					log.info("JNDI - " + jndi);
					try {
						DataSource jds = Sqls.lookupJndiDataSource(jndi);
						sqlDaoClient.setDataSource(jds);
					}
					catch (Exception e) {
						throw new DaoException("Failed to lookup jndc data source: " + jndi);
					}
					return sqlDaoClient;
				}
				
				// default JDBC
				DataSource jds = createSimpleDataSource(SET.DATA + '.' + ds + '.');
				SqlDaoClient sqlDaoClient = new SqlDaoClient();
				sqlDaoClient.setDataSource(jds);
				return sqlDaoClient;
			}
			catch (Exception e) {
				if (i < dss.length) {
					log.warn("Failed to build DaoClient for [" + ds + "]: " + e.getMessage());
				}
				else {
					log.error("Failed to build DaoClient for [" + ds + "]", e);
					throw Exceptions.wrapThrow(e);
				}
			}
		}
		throw new DaoException("Failed to build DaoClient for [" + Strings.join(dss, ", ") + "]");
	}
	
	private DataSource createSimpleDataSource(String prefix) {
		log.info("Create Simple JDBC DataSource: " + prefix);

		Map<String, String> dps = new TreeMap<String, String>(Collections.subMap(settings, prefix));
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

		AbstractDataSource ds = null;
		String driver = dps.get("jdbc.driver");
		if ("org.sqlite.JDBC".equals(driver)) {
			ds = new ThreadLocalDataSource();
		}
		else {
			ds = new SimpleDataSource();
		}
		ds.initialize(dps);
		return ds;
	}
}
