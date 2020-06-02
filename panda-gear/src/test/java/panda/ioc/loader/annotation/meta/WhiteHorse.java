package panda.ioc.loader.annotation.meta;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class WhiteHorse extends Horse {

	@IocInject("'white horse")
	public void setName(String name) {
		super.setName(name);
	}
	
}
