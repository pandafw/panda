package panda.mvc.impl;

import javax.servlet.ServletContext;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.ValueProxyMaker;
import panda.ioc.meta.IocValue;
import panda.ioc.val.StaticValue;
import panda.lang.Arrays;

public class ServletValueProxyMaker implements ValueProxyMaker {

	private ServletContext sc;

	public ServletValueProxyMaker(ServletContext sc) {
		this.sc = sc;
	}

	public String[] supportedTypes() {
		return Arrays.toArray(IocValue.TYPE_REF);
	}

	public ValueProxy make(IocMaking ing, IocValue iv) {
		if (iv.getValue() == null) {
			return null;
		}
		
		String value = iv.getValue().toString();
		if (IocValue.TYPE_REF.equals(iv.getType())) {
			if ("$servlet".equalsIgnoreCase(value)) {
				return new StaticValue(sc);
			}
			
			Object obj = sc.getAttribute(value);
			if (obj != null) {
				return new StaticValue(obj);
			}
		}
		return null;
	}
}
