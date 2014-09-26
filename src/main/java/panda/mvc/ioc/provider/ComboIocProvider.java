package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.loader.ComboIocLoader;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.MvcIoc;

public class ComboIocProvider implements IocProvider {

	public Ioc create(MvcConfig config, String ... args) {
		return new MvcIoc(new ComboIocLoader(args));
	}

}
