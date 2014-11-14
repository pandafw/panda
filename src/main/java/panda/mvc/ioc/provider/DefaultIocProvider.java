package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;

public class DefaultIocProvider extends AbstractIocProvider {
	@Override
	public Ioc create(MvcConfig config, String... args) {
		return create(new MvcDefaultIocLoader(config));
	}
}
