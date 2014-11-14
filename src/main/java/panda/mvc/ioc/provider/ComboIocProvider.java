package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcComboIocLoader;

public class ComboIocProvider extends AbstractIocProvider {

	public Ioc create(MvcConfig config, String ... args) {
		return create(new MvcComboIocLoader(config, args));
	}

}
