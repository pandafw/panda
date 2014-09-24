package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.loader.JsonIocLoader;
import panda.lang.Strings;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.MvcIoc;

public class JsonIocProvider implements IocProvider {

	public Ioc create(MvcConfig config, Object... args) {
		return new MvcIoc(new JsonIocLoader(Strings.toStringArray(args)));
	}

}
