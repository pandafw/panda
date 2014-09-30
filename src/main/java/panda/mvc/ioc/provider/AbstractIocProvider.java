package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.JsonIocLoader;
import panda.mvc.IocProvider;
import panda.mvc.ioc.loader.MvcComboIocLoader;

public abstract class AbstractIocProvider implements IocProvider {
	protected IocLoader createDefaultIocLoader() {
		return new JsonIocLoader("panda/mvc/ioc.json");
	}

	protected Ioc create(IocLoader loader) {
		IocLoader il = createDefaultIocLoader();
		return new DefaultIoc(new MvcComboIocLoader(loader, il));
	}

}
