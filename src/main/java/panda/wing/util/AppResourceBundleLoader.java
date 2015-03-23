package panda.wing.util;

import java.util.List;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.io.Settings;
import panda.io.resource.ExternalResourceBundleMaker;
import panda.io.resource.ResourceBundleLoader;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.wing.constant.SC;
import panda.wing.constant.VC;
import panda.wing.entity.Property;
import panda.wing.entity.Resource;
import panda.wing.entity.query.PropertyQuery;
import panda.wing.entity.query.ResourceQuery;


/**
 * A class for load database resource.
 * @author yf.frank.wang@gmail.com
 */
@IocBean(type=ResourceBundleLoader.class, create="initialize")
public class AppResourceBundleLoader extends ResourceBundleLoader {
	private static final Log log = Logs.getLog(AppResourceBundleLoader.class);

	protected static String DB = "db";
	
	@IocInject
	protected Settings settings;

	@IocInject
	protected DaoClient daoClient;

	protected ExternalResourceBundleMaker databaseResourceLoader;
	
	/**
	 * @return the databaseResourceLoader
	 */
	public ExternalResourceBundleMaker getDatabaseResourceLoader() {
		return databaseResourceLoader;
	}

	/**
	 * load external resources
	 */
	public void initialize() throws Exception {
		if (settings.getPropertyAsBoolean(SC.DATABASE_RESOURCE)) {
			ExternalResourceBundleMaker mrbm = new ExternalResourceBundleMaker();

			mrbm.setClassColumn(Resource.CLAZZ);
			mrbm.setLanguageColumn(Resource.LANGUAGE);
			mrbm.setCountryColumn(Resource.COUNTRY);
			//databaseResourceLoader.setVariantColumn("variant");
			mrbm.setSourceColumn(Resource.SOURCE);
			mrbm.setPackageName(getClass().getPackage().getName());

			databaseResourceLoader = mrbm;
			addResourceBundleMaker(DB, mrbm);
		}
		else if (settings.getPropertyAsBoolean(SC.DATABASE_PROPERTY)) {
			ExternalResourceBundleMaker mrbm = new ExternalResourceBundleMaker();

			mrbm.setClassColumn(Property.CLAZZ);
			mrbm.setLanguageColumn(Property.LANGUAGE);
			mrbm.setCountryColumn(Property.COUNTRY);
			//databaseResourceLoader.setVariantColumn("variant");
			mrbm.setNameColumn(Property.NAME);
			mrbm.setValueColumn(Property.VALUE);
			mrbm.setPackageName(getClass().getPackage().getName());

			databaseResourceLoader = mrbm;
			addResourceBundleMaker(DB, mrbm);
		}
		
		reload();
	}
	
	public void reload() throws Exception {
		if (databaseResourceLoader == null) {
			return;
		}
		
		if (settings.getPropertyAsBoolean(SC.DATABASE_RESOURCE)) {
			log.info("Loading database resources ...");

			Dao dao = daoClient.getDao();
			ResourceQuery rq = new ResourceQuery();
			rq.status().equalTo(VC.STATUS_0);
			List<Resource> list = dao.select(rq);
			databaseResourceLoader.loadResources(list);
		}
		else if (settings.getPropertyAsBoolean(SC.DATABASE_PROPERTY)) {
			log.info("Loading database properties ...");

			Dao dao = daoClient.getDao();
			PropertyQuery pq = new PropertyQuery();
			pq.status().equalTo(VC.STATUS_0);
			List<Property> list = dao.select(pq);
			databaseResourceLoader.loadResources(list);
		}
	}
}
