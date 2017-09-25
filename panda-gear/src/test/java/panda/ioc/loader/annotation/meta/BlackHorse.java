package panda.ioc.loader.annotation.meta;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class BlackHorse extends Horse {

	@IocInject("'black horse")
	public void setName(String name) {
		super.setName(name);
	}
	
}
