package panda.mvc.ioc.provider;

import java.util.HashSet;
import java.util.Set;

import panda.ioc.Ioc;
import panda.mvc.MvcConfig;
import panda.mvc.annotation.Modules;
import panda.mvc.ioc.loader.MvcAnnotationIocLoader;


public class AnnotationIocProvider extends AbstractIocProvider {

	public Ioc create(MvcConfig config, String... args) {
		if (args == null || args.length == 0) {
			Set<String> pkgs = new HashSet<String>();

			Class<?> mm = config.getMainModule();
			pkgs.add(mm.getPackage().getName());
			Modules ms = mm.getAnnotation(Modules.class);
			if (ms != null) {
				for (Class<?> cls : ms.value()) {
					pkgs.add(cls.getPackage().getName());
				}
			}
			args = pkgs.toArray(new String[pkgs.size()]);
		}
		return create(new MvcAnnotationIocLoader(args));
	}

}
