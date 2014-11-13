package panda.mvc.ioc.loader;

import panda.ioc.IocLoader;
import panda.ioc.loader.ComboIocLoader;

public class MvcComboIocLoader extends ComboIocLoader {
	public MvcComboIocLoader(IocLoader... loaders) {
		super(loaders);
	}

	public MvcComboIocLoader(Object... args) {
		super(args);
	}

	public MvcComboIocLoader(String... packages) {
		super(packages);
	}
	
	@Override
	protected void initAlias() {
		super.initAlias();
		alias.put("anno", MvcAnnotationIocLoader.class);
		alias.put("annotation", MvcAnnotationIocLoader.class);
		alias.put("default", MvcDefaultIocLoader.class);
	}
}
