package panda.ioc.val;

import java.util.Properties;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;

public class SysPropValue implements ValueProxy {

	private String name;

	public SysPropValue(String name) {
		this.name = name;
	}

	public Object get(IocMaking ing) {
		Properties properties = System.getProperties();
		if (properties != null)
			return properties.get(name);
		return null;
	}

}
