package panda.mvc.ioc.provider;

import panda.ioc.IocLoader;
import panda.ioc.aop.Mirrors;
import panda.ioc.impl.DefaultIoc;
import panda.lang.Classes;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;
import panda.mvc.annotation.IocBy;

public abstract class AbstractIocProvider implements IocProvider {
	protected DefaultIoc create(MvcConfig mcfg, IocLoader loader) {
		DefaultIoc ioc = new DefaultIoc(loader);

		IocBy ib = mcfg.getMainModule().getAnnotation(IocBy.class);
		if (ib != null) {
			Mirrors ms = Classes.born(ib.mirror());
			ioc.setMirrors(ms);
		}

		return ioc;
	}

}
