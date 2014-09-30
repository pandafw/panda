package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.loader.XmlIocLoader;
import panda.mvc.MvcConfig;

public class XmlIocProvider extends AbstractIocProvider {
	public Ioc create(MvcConfig config, String... args) {
		return create(new XmlIocLoader(args));
	}
}
