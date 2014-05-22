package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;

public class IocContextValue implements ValueProxy {

	private static final IocContextValue i = new IocContextValue();
	
	public static IocContextValue i() {
		return i;
	}
	
	public Object get(IocMaking ing) {
		return ing.getContext();
	}

}
