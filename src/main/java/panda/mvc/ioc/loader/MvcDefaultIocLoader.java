package panda.mvc.ioc.loader;

import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceBundleLoader;
import panda.mvc.impl.DefaultActionChainMaker;
import panda.mvc.impl.DefaultUrlMapping;
import panda.mvc.util.CookieStateProvider;
import panda.mvc.util.DefaultTextProvider;

public class MvcDefaultIocLoader extends MvcAnnotationIocLoader {
	protected static final Class<?>[] DEFAULTS = {
		DefaultUrlMapping.class,
		DefaultActionChainMaker.class,
		LocalFilePool.class,
		ResourceBundleLoader.class,
		CookieStateProvider.class,
		DefaultTextProvider.class
	};
	
	public MvcDefaultIocLoader() {
		super(DEFAULTS);
	}
}
