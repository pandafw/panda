package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcAnnotationIocLoader;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;

public class DefaultIocProvider extends AbstractIocProvider {
	public Ioc create(MvcConfig config, String ... args) {
		IocLoader il1 = new MvcDefaultIocLoader();
		IocLoader il2 = new MvcAnnotationIocLoader(AnnotationIocProvider.getDefaultPackages(config));

		IocLoader ic = new MvcComboIocLoader(il1, il2);
		return create(ic);
	}

}
