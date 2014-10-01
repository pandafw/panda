package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.impl.DefaultIoc;
import panda.mvc.IocProvider;

public abstract class AbstractIocProvider implements IocProvider {
	protected Ioc create(IocLoader loader) {
		return new DefaultIoc(loader);
	}

}
