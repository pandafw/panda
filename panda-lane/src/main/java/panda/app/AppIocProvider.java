package panda.app;

import panda.ioc.IocLoader;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.provider.DefaultIocProvider;

public class AppIocProvider extends DefaultIocProvider {

	@Override
	protected IocLoader getIocLoader(MvcConfig config, String ... args) {
		return new AppIocLoader(config, args);
	}
}

