package panda.ioc.loader.annotation.meta;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class HorseMP {
	protected String name;
	protected String alias;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@IocInject({ "'horse", "'alias" })
	public void setNameAndAlias(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}
}
