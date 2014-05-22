package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;

public class EnvValue implements ValueProxy {

	private String name;

	public EnvValue(String name) {
		this.name = name;
	}

	public Object get(IocMaking ing) {
		return System.getenv(name);
	}

}
