package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.impl.PandaIoc;
import panda.ioc.loader.ComboIocLoader;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;

public class ComboIocProvider implements IocProvider {

	public Ioc create(MvcConfig config, Object ... args) {
		return new PandaIoc(new ComboIocLoader(args));
	}

}
