package panda.ioc.loader.xml.meta;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class BlackHorse extends Horse {

	@IocInject("'black horse")
	public void setName(String name) {
		super.setName(name);
	}
	
}
