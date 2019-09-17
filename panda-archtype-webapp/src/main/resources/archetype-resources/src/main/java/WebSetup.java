package ${package};


import panda.app.AppHelper;
import panda.app.AppIocProvider;
import panda.app.AppSetup;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.gems.users.entity.User;
import panda.gems.users.util.UserHelper;
import panda.io.Settings;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Setup;
import panda.mvc.annotation.IocBy;
import panda.mvc.annotation.Modules;

import ${package}.entity.Pet;

@Modules(scan = true, packages = { "panda.app.action", "panda.gems" })
@IocBy(type = AppIocProvider.class, args = { "*default", "*json", "mvc.json" })
@IocBean(type = Setup.class)
public class WebSetup extends AppSetup {
	private static final Log log = Logs.getLog(WebSetup.class);

	@IocInject
	private DaoClient daoClient;
	
	@IocInject
	private Settings settings;
	
	@IocInject
	private UserHelper userHelper;
	
	public static final Class[] ENTITIES = {
			User.class,
			Pet.class
	};
	
	/**
	 * initialize
	 */
	@Override
	public void initialize() {
		super.initialize();
		
		initEntities();
		
		initDatabase();
		
		userHelper.initSystemUser();
		userHelper.initSuperUser();
	}

	@SuppressWarnings("unchecked")
	private void initEntities() {
		// initialize entities
		for (Class c : ENTITIES) {
			daoClient.getEntity(c);
		}
	}

	private void initDatabase() {
		try {
			log.info("Initialize database");

			Dao dao = daoClient.getDao();
			AppHelper.createTables(dao, ENTITIES);
		}
		catch (Exception e) {
			log.error("Failed to init database", e);
		}
	}

	/**
	 * destroy
	 */
	@Override
	public void destroy() {
		super.destroy();
	}
}
