package panda.ioc.loader.annotation.meta;

import panda.dao.Dao;
import panda.ioc.annotation.Inject;
import panda.ioc.annotation.Bean;

@Bean
public class ClassB {

	@Inject("ref:dao")
	public Dao dao;

	public void setDao(Dao dao) {
		this.dao = dao;
	}

}
