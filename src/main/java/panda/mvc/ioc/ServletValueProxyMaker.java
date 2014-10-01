package panda.mvc.ioc;

import javax.servlet.ServletContext;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.ValueProxyMaker;
import panda.ioc.meta.IocValue;
import panda.ioc.val.StaticValue;
import panda.lang.Arrays;
import panda.lang.Strings;

public class ServletValueProxyMaker implements ValueProxyMaker {

	private ServletContext sc;

	public ServletValueProxyMaker(ServletContext sc) {
		this.sc = sc;
	}

	public String[] supportedTypes() {
		return Arrays.toArray(IocValue.TYPE_REF);
	}

	public ValueProxy make(IocMaking ing, IocValue iv) {
		if (IocValue.TYPE_REF.equals(iv.getType())) {
			String name = iv.getValue().toString();
			if (Strings.isEmpty(name)) {
				return null;
			}

			if (ServletContext.class.getName().equals(name) || "$servlet".equalsIgnoreCase(name)) {
				return new StaticValue(sc);
			}
			
			Object obj = sc.getAttribute(name);
			if (obj != null) {
				return new StaticValue(obj);
			}
		}
		return null;
	}
}
