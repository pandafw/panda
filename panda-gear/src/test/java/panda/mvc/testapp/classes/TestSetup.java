package panda.mvc.testapp.classes;

import panda.dao.DaoClient;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.Setup;
import panda.vfs.dao.DaoFileData;
import panda.vfs.dao.DaoFileItem;

@IocBean(type=Setup.class)
public class TestSetup implements Setup {

	@IocInject
	private DaoClient daoClient;
	
	public void initialize() {
		daoClient.getDao().create(DaoFileItem.class);
		daoClient.getDao().create(DaoFileData.class);
	}

	public void destroy() {

	}

}
