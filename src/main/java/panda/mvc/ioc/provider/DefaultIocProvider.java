package panda.mvc.ioc.provider;

import panda.io.resource.ResourceBundleLoader;
import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcAnnotationIocLoader;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.util.CookieStateProvider;
import panda.mvc.util.DefaultTextProvider;

public class DefaultIocProvider extends AbstractIocProvider {
	protected static final Class<?>[] BEANS = {
		ResourceBundleLoader.class,
		CookieStateProvider.class,
		DefaultTextProvider.class
	};
	
	public Ioc create(MvcConfig config, String ... args) {
		IocLoader il1 = new MvcAnnotationIocLoader(BEANS);
		IocLoader il2 = new MvcAnnotationIocLoader(AnnotationIocProvider.getDefaultPackages(config));

		IocLoader ic = new MvcComboIocLoader(il1, il2);
		return create(ic);
	}

}
