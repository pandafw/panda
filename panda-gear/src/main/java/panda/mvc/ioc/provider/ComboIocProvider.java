package panda.mvc.ioc.provider;

import panda.ioc.IocLoader;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcComboIocLoader;

public class ComboIocProvider extends DefaultIocProvider {

	@Override
	protected IocLoader getIocLoader(MvcConfig config, String ... args) {
		return new MvcComboIocLoader(config, args);
	}
}
