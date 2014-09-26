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
import panda.log.Log;
import panda.log.Logs;

@IocBean
public class DaoClientMaker {
	private static final Log log = Logs.getLog(DaoClientMaker.class);

	@IocInject(required=false)
	protected ServletContext servlet;
	
	@IocInject
	protected Settings settings;

	protected DaoClient daoClient;
	

	/**
	 * @return the daoClient
	 */
	public DaoClient getDaoClient() throws Exception {
		if (daoClient == null) {
			daoClient = buildDaoClient();
		}
		return daoClient;
	}

	/**
	 * @param daoClient the daoClient to set
	 */
	public void setDaoClient(DaoClient daoClient) {
		this.daoClient = daoClient;
	}

	protected DaoClient buildDaoClient() throws Exception {
		String dstype = settings.getProperty("data.source");
		if ("gae".equalsIgnoreCase(dstype)) {
			GaeDaoClient gdc = GaeDaoClient.i();
			return gdc;
		}
		
		if ("mongo".equalsIgnoreCase(dstype)) {
			String url = settings.getProperty("mongo.url");

			log.info("mongo - " +  url);
			MongoDaoClient daoClient = new MongoDaoClient(url);
			
			return daoClient;
		}

		if ("jdbc".equalsIgnoreCase(dstype)) {
			log.info("jdbc - " + settings.getProperty("jdbc.driver") + ":" + settings.getProperty("jdbc.url"));

			DataSource ds = new SimpleDataSource(settings);
			SqlDaoClient sqlDaoClient = new SqlDaoClient();
			sqlDaoClient.setDataSource(ds);
			return sqlDaoClient;
		}

		if ("jndi".equalsIgnoreCase(dstype)) {
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

		throw new IllegalArgumentException("The data source type [" + dstype + "] is invalid.");
	}

}
