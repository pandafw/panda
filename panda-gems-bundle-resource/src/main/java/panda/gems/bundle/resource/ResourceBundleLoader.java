package panda.gems.bundle.resource;

import java.util.List;

import panda.app.constant.VAL;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.gems.bundle.resource.entity.Resource;
import panda.gems.bundle.resource.entity.query.ResourceQuery;
import panda.io.Settings;
import panda.io.resource.BeanResourceMaker;
import panda.io.resource.ResourceLoader;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;


/**
 * A class for load database resource.
 */
@IocBean(type=ResourceLoader.class, create="initialize")
public class ResourceBundleLoader extends ResourceLoader {
	private static final Log log = Logs.getLog(ResourceBundleLoader.class);

	public static final String DATABASE_RESOURCE = "database-resource-load";

	@IocInject
	protected Settings settings;
	
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
	 * initial load external resources
	 * @throws Exception if an error occurs
	 */
	public void initialize() throws Exception {
		if (settings.getPropertyAsBoolean(DATABASE_RESOURCE)) {
			BeanResourceMaker mrbm = new BeanResourceMaker();

			mrbm.setClassColumn(Resource.CLAZZ);
			mrbm.setLocaleColumn(Resource.LOCALE);
			mrbm.setSourceColumn(Resource.SOURCE);
			mrbm.setTimestampColumn(Resource.UPDATED_AT);
			mrbm.setPackageName(getClass().getPackage().getName());

			databaseResourceLoader = mrbm;
			addResourceMaker(mrbm);
		}
		
		reload();
	}
	
	/**
	 * reload external resources
	 * @throws Exception if an error occurs
	 */
	public boolean reload() throws Exception {
		if (databaseResourceLoader == null) {
			return false;
		}
		
		log.info("Loading database resources ...");

		Dao dao = daoClient.getDao();
		ResourceQuery rq = new ResourceQuery();
		rq.status().eq(VAL.STATUS_ACTIVE);
		List<Resource> list = dao.select(rq);
		return databaseResourceLoader.loadResources(list);
	}
}
