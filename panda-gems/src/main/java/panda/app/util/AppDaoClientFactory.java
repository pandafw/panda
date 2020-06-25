package panda.app.util;

import java.sql.Connection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.cast.CastContext;
import panda.cast.Castors;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.mongo.MongoDaoClient;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.Sqls;
import panda.dao.sql.dbcp.SimpleDataSource;
import panda.dao.sql.dbcp.ThreadLocalDataSource;
import panda.io.Settings;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.reflect.Methods;
import panda.log.Log;
import panda.log.Logs;

@IocBean(create="initialize", depose="destroy")
public class AppDaoClientFactory {
	private static final Log log = Logs.getLog(AppDaoClientFactory.class);

	private static final String GAE = "gae";
	private static final String MONGO = "mongo";
	private static final String JNDI = "jndi";
	private static final String SIMPLE = "simple";
	private static final String LOCAL = "local";
	private static final String DBCP = "dbcp";
	private static final String DBCP2 = "dbcp2";
	private static final String DBCP_CLASS = "org.apache.commons.dbcp.BasicDataSource";
	private static final String DBCP2_CLASS = "org.apache.commons.dbcp2.BasicDataSource";
	private static final String DRUID = "druid";
	private static final String DRUID_CLASS = "com.alibaba.druid.pool.DruidDataSource";
	
	
	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Ioc ioc;
	
	@IocInject
	protected Settings settings;

	@IocInject(value=MVC.DAO_QUERY_TIMEOUT, required=false)
	protected int timeout = Dao.DEFAULT_TIMEOUT;
	
	protected int transcationLevel = Connection.TRANSACTION_READ_COMMITTED;
	
	protected DaoClient daoClient;

	@IocInject(value=MVC.DAO_QUERY_TIMEOUT, required=false)
	public void setTranscationLevel(String transcationLevel) {
		this.transcationLevel = Sqls.parseTransactionLevel(transcationLevel);
	}

	/**
	 * @return the daoClient
	 */
	public DaoClient getDaoClient() {
		return daoClient;
	}

	public void initialize() throws Exception {
		String[] dss = Strings.split(settings.getProperty(SET.DATA_SOURCE), ", ");
		daoClient = buildDaoClient(dss);
		
		String prefix = settings.getProperty(SET.DATA_PREFIX);
		if (Strings.isNotEmpty(prefix)) {
			daoClient.setPrefix(prefix);
		}
		
		timeout = settings.getPropertyAsInt(SET.DATA_QUERY_TIMEOUT, timeout);
		daoClient.setTimeout(timeout);
	}
	
	public void destroy() {
		if (daoClient instanceof SqlDaoClient) {
			DataSource ds = ((SqlDaoClient)daoClient).getDataSource();
			Methods.safeCall(ds, "close");
		}
	}
	
	public DaoClient buildDaoClient(String... dss) {
		Throwable ex = null;
		for (String ds : dss) {
			if (Strings.isEmpty(ds)) {
				continue;
			}
	
			try {
				String factory = settings.getProperty(SET.DATA + '.' + ds, ds);
				if (GAE.equalsIgnoreCase(factory)) {
					Class cls = Classes.getClass("panda.gae.dao.GaeDaoClient");
					DaoClient gdc = (DaoClient)Methods.invokeStaticMethod(cls, "i");
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
						throw new DaoException("Failed to lookup jndi data source: " + jndi, e);
					}
					return sqlDaoClient;
				}

				if (DBCP.equalsIgnoreCase(factory)) {
					factory = DBCP_CLASS;
				}
				else if (DBCP2.equalsIgnoreCase(factory)) {
					factory = DBCP2_CLASS;
				}
				else if (DRUID.equalsIgnoreCase(factory)) {
					factory = DRUID_CLASS;
				}
				else if (LOCAL.equalsIgnoreCase(factory)) {
					factory = ThreadLocalDataSource.class.getName();
				}
				else if (SIMPLE.equalsIgnoreCase(factory) || ds.equalsIgnoreCase(factory)) {
					factory = SimpleDataSource.class.getName();
				}
				
				DataSource jds = createSqlDataSource(factory, SET.DATA + '.' + ds + '.');
				SqlDaoClient sqlDaoClient = new SqlDaoClient();
				sqlDaoClient.setDataSource(jds);
				return sqlDaoClient;
			}
			catch (Exception e) {
				ex = e;
				log.warn("Failed to build DaoClient for [" + ds + "]: " + e.getMessage());
			}
		}
		throw new DaoException("Failed to build DaoClient for [" + Strings.join(dss, ", ") + "]: " + ex.getMessage(), ex);
	}
	
	private DataSource createSqlDataSource(String clazz, String prefix) throws Exception {
		log.info("Create SQL JDBC DataSource: " + clazz + " - " + prefix);

		if (Strings.startsWithChar(clazz, '#')) {
			return ioc.get(DataSource.class, Strings.substring(clazz, 1));
		}
		
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

		DataSource ds = (DataSource)Classes.born(clazz);

		Castors cs = Castors.i();
		CastContext cc = cs.newCastContext(true);
		cs.castTo(dps, ds, cc);
		if (cc.hasError()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Failed to set properties to " + clazz);
			for (Entry<String, Object> en : cc.getErrorValues().entrySet()) {
				sb.append("\n  ").append(prefix).append(en.getKey()).append('=').append(en.getValue());
			}
			throw new IllegalArgumentException(sb.toString());
		}

		if (clazz.equals(DRUID_CLASS)) {
			Methods.invokeMethod(ds, "init");
		}
		return ds;
	}
}
