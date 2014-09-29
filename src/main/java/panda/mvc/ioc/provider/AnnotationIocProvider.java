package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.MvcIoc;
import panda.mvc.ioc.loader.MvcAnnotationIocLoader;


public class AnnotationIocProvider implements IocProvider {
	
	public Ioc create(MvcConfig config, String... args) {
		if (args == null || args.length == 0) {
			args = new String[] { config.getMainModule().getPackage().getName() };
		}
		return new MvcIoc(new MvcAnnotationIocLoader(args));
	}

}
