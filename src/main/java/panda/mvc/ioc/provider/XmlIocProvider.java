package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.impl.PandaIoc;
import panda.ioc.loader.XmlIocLoader;
import panda.lang.Strings;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;

public class XmlIocProvider implements IocProvider {
	public Ioc create(MvcConfig config, Object... args) {
		return new PandaIoc(new XmlIocLoader(Strings.toStringArray(args)));
	}
}
