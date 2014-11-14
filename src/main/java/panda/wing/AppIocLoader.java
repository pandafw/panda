package panda.wing;

import java.util.Set;

import panda.filepool.dao.DaoFilePool;
import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceBundleLoader;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;
import panda.mvc.util.ActionAssist;
import panda.mvc.util.ActionConsts;
import panda.mvc.view.ftl.FreemarkerTemplateLoader;
import panda.wing.lucene.LuceneProvider;
import panda.wing.util.AppActionAssist;
import panda.wing.util.AppActionConsts;
import panda.wing.util.AppCacheProvider;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;
import panda.wing.util.AppSettings;
import panda.wing.util.DaoClientProvider;

public class AppIocLoader extends MvcComboIocLoader {
	public static class AppDefaultIocLoader extends MvcDefaultIocLoader {
		public AppDefaultIocLoader(MvcConfig config) {
			super(config);
		}

		@Override
		protected Set<Object> getDefaults() {
			Set<Object> clss = super.getDefaults();
			
			clss.remove(LocalFilePool.class);
			clss.add(DaoFilePool.class);

			clss.remove(ResourceBundleLoader.class);
			clss.add(AppResourceBundleLoader.class);
			
			clss.remove(FreemarkerTemplateLoader.class);
			clss.add(AppFreemarkerTemplateLoader.class);
			clss.add(AppCacheProvider.class);

			clss.remove(ActionAssist.class);
			clss.add(AppActionAssist.class);
			clss.remove(ActionConsts.class);
			clss.add(AppActionConsts.class);
			
			clss.add(AppSettings.class);
			clss.add(DaoClientProvider.class);
			clss.add(LuceneProvider.class);
			
			return clss;
		}
	}
	
	public AppIocLoader(MvcConfig config, String... packages) {
		super(config, packages);
	}
	
	@Override
	protected void initAlias() {
		super.initAlias();
		alias.put(DEFAULT, AppDefaultIocLoader.class);
	}
}


