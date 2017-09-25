package panda.ioc.loader.annotation.meta;

import panda.ioc.annotation.IocInject;
import panda.ioc.annotation.IocBean;

@IocBean
public class DogMaster {

	@IocInject
	public Dog dog;
	
	
}
