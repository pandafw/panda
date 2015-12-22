package panda.wing;

import java.util.Set;

import panda.filepool.dao.DaoFilePool;
import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceLoader;
import panda.lang.Systems;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;
import panda.mvc.view.ftl.FreemarkerTemplateLoader;
import panda.wing.lucene.LuceneProvider;
import panda.wing.task.gae.GaeTaskExecutor;
import panda.wing.task.gae.GaeTaskScheduler;
import panda.wing.task.java.JavaTaskExecutor;
import panda.wing.task.java.JavaTaskScheduler;
import panda.wing.util.AppCacheProvider;
import panda.wing.util.AppDaoClientProvider;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;
import panda.wing.util.AppSettings;

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

			clss.remove(ResourceLoader.class);
			clss.add(AppResourceBundleLoader.class);
			
			clss.remove(FreemarkerTemplateLoader.class);
			clss.add(AppFreemarkerTemplateLoader.class);
			clss.add(AppCacheProvider.class);

			clss.add(AppSettings.class);
			clss.add(AppDaoClientProvider.class);
			clss.add(LuceneProvider.class);
			
			if (Systems.IS_OS_APPENGINE) {
				clss.add(GaeTaskExecutor.class);
				clss.add(GaeTaskScheduler.class);
			}
			else {
				clss.add(JavaTaskExecutor.class);
				clss.add(JavaTaskScheduler.class);
			}
			
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

