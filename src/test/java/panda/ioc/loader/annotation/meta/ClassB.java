package panda.ioc.loader.annotation.meta;

import panda.dao.Dao;
import panda.ioc.annotation.IocInject;
import panda.ioc.annotation.IocBean;

@IocBean
public class ClassB {

	@IocInject("ref:dao")
	public Dao dao;

	public void setDao(Dao dao) {
		this.dao = dao;
	}

}
