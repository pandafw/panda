package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.impl.PandaIoc;
import panda.ioc.loader.AnnotationIocLoader;
import panda.lang.Strings;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;

public class AnnotationIocProvider implements IocProvider {

	public Ioc create(MvcConfig config, Object... args) {
		String[] pkgs;
		if (args == null || args.length == 0) {
			pkgs = new String[] { config.getMainModule().getPackage().getName() };
		}
		else {
			pkgs = Strings.toStringArray(args);
		}
		return new PandaIoc(new AnnotationIocLoader(pkgs));
	}

}
