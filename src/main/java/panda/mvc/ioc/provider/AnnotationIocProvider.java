package panda.mvc.ioc.provider;

import java.util.HashSet;
import java.util.Set;

import panda.ioc.Ioc;
import panda.lang.Arrays;
import panda.mvc.MvcConfig;
import panda.mvc.annotation.Modules;
import panda.mvc.ioc.loader.MvcAnnotationIocLoader;


public class AnnotationIocProvider extends AbstractIocProvider {

	public Ioc create(MvcConfig config, String... args) {
		Set<Object> as = getDefaultPackages(config);
		if (Arrays.isNotEmpty(args)) {
			as.addAll(Arrays.asList(args));
		}
		return create(new MvcAnnotationIocLoader(as));
	}

	public static Set<Object> getDefaultPackages(MvcConfig config) {
		Set<Object> pkgs = new HashSet<Object>();

		Class<?> mm = config.getMainModule();
		pkgs.add(mm);
		
		Modules ms = mm.getAnnotation(Modules.class);
		if (ms != null) {
			if (ms.scan()) {
				pkgs.remove(mm);
				pkgs.add(mm.getPackage().getName());
			}
			
			for (Class<?> cls : ms.value()) {
				pkgs.add(cls);
			}
	
			for (String pkg : ms.packages()) {
				pkgs.add(pkg);
			}
		}
		return pkgs;
	}
}
