package panda.mvc.ioc;

import javax.servlet.ServletContext;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.ValueProxyMaker;
import panda.ioc.meta.IocValue;
import panda.ioc.val.StaticValue;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.mvc.MvcConfig;

public class MvcValueProxyMaker implements ValueProxyMaker {

	private MvcConfig config;

	public MvcValueProxyMaker(MvcConfig config) {
		this.config = config;
	}

	public String[] supportedTypes() {
		return Arrays.toArray(IocValue.TYPE_REF);
	}

	public ValueProxy make(IocMaking ing, IocValue iv) {
		if (IocValue.TYPE_REF.equals(iv.getType())) {
			Object ivv = iv.getValue();
			if (ivv instanceof Class) {
				if (MvcConfig.class.equals(ivv)) {
					return new StaticValue(config);
				}
				if (ServletContext.class.equals(ivv)) {
					return new StaticValue(config.getServletContext());
				}
				return null;
			}

			String name = ivv.toString();
			if (Strings.isEmpty(name)) {
				return null;
			}

			if (ServletContext.class.getName().equals(name) || "$servlet".equalsIgnoreCase(name)) {
				return new StaticValue(config.getServletContext());
			}
		}
		return null;
	}
}
