package panda.wing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.filepool.dao.DaoFilePool;
import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceBundleLoader;
import panda.ioc.IocLoader;
import panda.ioc.loader.ComboIocLoader;
import panda.ioc.loader.JsonIocLoader;
import panda.ioc.loader.XmlIocLoader;
import panda.lang.Arrays;
import panda.mvc.ioc.loader.MvcAnnotationIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.ActionConsts;
import panda.mvc.view.ftl.FreemarkerTemplateLoader;
import panda.wing.action.BaseActionAssist;
import panda.wing.action.BaseActionConsts;
import panda.wing.lucene.LuceneProvider;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;
import panda.wing.util.AppSettings;
import panda.wing.util.DaoClientProvider;

public class AppIocLoader extends ComboIocLoader {
	public static class DefaultIocLoader extends MvcAnnotationIocLoader {
		public DefaultIocLoader() {
			List<Object> clss = new ArrayList<Object>();
			clss.addAll(Arrays.asList(MvcDefaultIocLoader.DEFAULTS));
			
			clss.remove(LocalFilePool.class);
			clss.add(DaoFilePool.class);

			clss.remove(ResourceBundleLoader.class);
			clss.add(AppResourceBundleLoader.class);
			
			clss.remove(FreemarkerTemplateLoader.class);
			clss.add(AppFreemarkerTemplateLoader.class);

			clss.remove(ActionAssist.class);
			clss.add(BaseActionAssist.class);
			clss.remove(ActionConsts.class);
			clss.add(BaseActionConsts.class);
			
			clss.add(AppSettings.class);
			clss.add(DaoClientProvider.class);
			clss.add(LuceneProvider.class);
			
			init(clss);
		}
	}
	
	private static Map<String, Class<? extends IocLoader>> alias = new HashMap<String, Class<? extends IocLoader>>();
	static {
		alias.put("js", JsonIocLoader.class);
		alias.put("json", JsonIocLoader.class);
		alias.put("xml", XmlIocLoader.class);
		alias.put("anno", MvcAnnotationIocLoader.class);
		alias.put("annotation", MvcAnnotationIocLoader.class);
		alias.put("default", DefaultIocLoader.class);
	}

	public AppIocLoader(IocLoader... loaders) {
		super(loaders);
	}

	public AppIocLoader(Object... args) {
		super(args);
	}

	public AppIocLoader(String... packages) {
		super(packages);
	}
	
	@Override
	protected Class<? extends IocLoader> getAliasClass(String name) {
		return alias.get(name);
	}
}


