package panda.mvc.ioc.loader;

import java.util.List;

import panda.ioc.IocLoader;
import panda.ioc.loader.ComboIocLoader;
import panda.lang.Arrays;
import panda.mvc.MvcConfig;

public class MvcComboIocLoader extends ComboIocLoader {
	public static final String DEFAULT = "default";
	
	private MvcConfig config;
	
	public MvcComboIocLoader(MvcConfig config, String... args) {
		super();
		this.config = config;
		initLoaders(args);
	}
	
	@Override
	protected void initAlias() {
		super.initAlias();
		alias.put(ANNO, MvcAnnotationIocLoader.class);
		alias.put(ANNOTATION, MvcAnnotationIocLoader.class);
		alias.put(DEFAULT, MvcDefaultIocLoader.class);
	}

	@Override
	protected IocLoader createIocLoader(String name, List<Object> args) {
		if (DEFAULT.equals(name)) {
			args = Arrays.asList((Object)config);
		}
		return super.createIocLoader(name, args); 
	}
	
	@Override
	protected Object getLoaderArgument(String a) {
		return "$config".equals(a) ? config : a;
	}

}
