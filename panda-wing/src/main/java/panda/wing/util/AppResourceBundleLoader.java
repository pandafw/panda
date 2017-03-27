package panda.wing.util;

import java.util.List;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.io.resource.BeanResourceMaker;
import panda.io.resource.ResourceLoader;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.wing.AppConstants;
import panda.wing.constant.VC;
import panda.wing.entity.Property;
import panda.wing.entity.Resource;
import panda.wing.entity.query.PropertyQuery;
import panda.wing.entity.query.ResourceQuery;


/**
 * A class for load database resource.
 */
@IocBean(type=ResourceLoader.class, create="initialize")
public class AppResourceBundleLoader extends ResourceLoader {
	private static final Log log = Logs.getLog(AppResourceBundleLoader.class);

	@IocInject(value=AppConstants.DATABASE_RESOURCE, required=false)
	private boolean resource;

	@IocInject(value=AppConstants.DATABASE_PROPERTY, required=false)
	private boolean property;

	@IocInject
	protected DaoClient daoClient;

	protected BeanResourceMaker databaseResourceLoader;
	
	/**
	 * @return the databaseResourceLoader
	 */
	public BeanResourceMaker getDatabaseResourceLoader() {
		return databaseResourceLoader;
	}

	/**
	 * load external resources
	 */
	public void initialize() throws Exception {
		if (resource) {
			BeanResourceMaker mrbm = new BeanResourceMaker();

			mrbm.setClassColumn(Resource.CLAZZ);
			mrbm.setLanguageColumn(Resource.LANGUAGE);
			mrbm.setCountryColumn(Resource.COUNTRY);
			//databaseResourceLoader.setVariantColumn("variant");
			mrbm.setSourceColumn(Resource.SOURCE);
			mrbm.setPackageName(getClass().getPackage().getName());

			databaseResourceLoader = mrbm;
			addResourceMaker(mrbm);
		}
		else if (property) {
			BeanResourceMaker mrbm = new BeanResourceMaker();

			mrbm.setClassColumn(Property.CLAZZ);
			mrbm.setLanguageColumn(Property.LANGUAGE);
			mrbm.setCountryColumn(Property.COUNTRY);
			//databaseResourceLoader.setVariantColumn("variant");
			mrbm.setNameColumn(Property.NAME);
			mrbm.setValueColumn(Property.VALUE);
			mrbm.setPackageName(getClass().getPackage().getName());

			databaseResourceLoader = mrbm;
			addResourceMaker(mrbm);
		}
		
		reload();
	}
	
	public void reload() throws Exception {
		if (databaseResourceLoader == null) {
			return;
		}
		
		if (resource) {
			log.info("Loading database resources ...");

			Dao dao = daoClient.getDao();
			ResourceQuery rq = new ResourceQuery();
			rq.status().equalTo(VC.STATUS_ACTIVE);
			List<Resource> list = dao.select(rq);
			databaseResourceLoader.loadResources(list);
		}
		else if (property) {
			log.info("Loading database properties ...");

			Dao dao = daoClient.getDao();
			PropertyQuery pq = new PropertyQuery();
			pq.status().equalTo(VC.STATUS_ACTIVE);
			List<Property> list = dao.select(pq);
			databaseResourceLoader.loadResources(list);
		}
	}
}
