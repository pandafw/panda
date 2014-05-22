package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;

public class IocBeanNameValue implements ValueProxy {

	private static final IocBeanNameValue i = new IocBeanNameValue();
	
	public static IocBeanNameValue i() {
		return i;
	}

	public Object get(IocMaking ing) {
		return ing.getName();
	}

}
