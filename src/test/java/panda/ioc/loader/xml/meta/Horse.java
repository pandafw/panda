package panda.ioc.loader.xml.meta;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;

@IocBean
public class Horse {

	protected String name;
	protected String alias;
	
	public String getName() {
		return name;
	}
	
	@IocInject("horse")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	@IocInject("alias")
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
