package panda.mvc.ioc.loader;

import java.util.HashMap;
import java.util.Map;

import panda.ioc.IocLoader;
import panda.ioc.loader.ComboIocLoader;
import panda.ioc.loader.JsonIocLoader;
import panda.ioc.loader.XmlIocLoader;

public class MvcComboIocLoader extends ComboIocLoader {
	/**
	 * 类别名
	 */
	private static Map<String, Class<? extends IocLoader>> alias = new HashMap<String, Class<? extends IocLoader>>();
	static {
		alias.put("js", JsonIocLoader.class);
		alias.put("json", JsonIocLoader.class);
		alias.put("xml", XmlIocLoader.class);
		alias.put("anno", MvcAnnotationIocLoader.class);
		alias.put("annotation", MvcAnnotationIocLoader.class);
	}

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
	protected Class<? extends IocLoader> getAliasClass(String name) {
		return alias.get(name);
	}
}
