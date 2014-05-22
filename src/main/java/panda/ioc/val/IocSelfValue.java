package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;

public class IocSelfValue implements ValueProxy {

	private static final IocSelfValue i = new IocSelfValue();
	
	public static IocSelfValue i() {
		return i;
	}

	public Object get(IocMaking ing) {
		return ing.getIoc();
	}

}
