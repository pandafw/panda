package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.loader.JsonIocLoader;
import panda.mvc.MvcConfig;

public class JsonIocProvider extends AbstractIocProvider {

	public Ioc create(MvcConfig config, String... args) {
		return create(new JsonIocLoader(args));
	}

}
