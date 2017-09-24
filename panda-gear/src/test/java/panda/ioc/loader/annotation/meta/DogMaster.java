package panda.ioc.loader.xml.meta;

import panda.ioc.annotation.IocInject;
import panda.ioc.annotation.IocBean;

@IocBean
public class DogMaster {

	@IocInject
	public Dog dog;
	
	
}
