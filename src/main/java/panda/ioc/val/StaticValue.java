package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;

public class StaticValue implements ValueProxy {

	private Object obj;

	public StaticValue(Object obj) {
		this.obj = obj;
	}

	public Object get(IocMaking ing) {
		return obj;
	}

}
