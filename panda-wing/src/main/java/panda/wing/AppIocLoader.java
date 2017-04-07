package panda.wing;

import java.util.Set;

import panda.lang.Collections;
import panda.lang.Systems;
import panda.mvc.MvcConfig;
import panda.mvc.filepool.MvcDaoFilePool;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;
import panda.wing.auth.AppAuthenticator;
import panda.wing.auth.UserAuthenticateProcessor;
import panda.wing.lucene.LuceneProvider;
import panda.wing.task.gae.GaeTaskExecutor;
import panda.wing.task.gae.GaeTaskScheduler;
import panda.wing.task.java.JavaTaskExecutor;
import panda.wing.task.java.JavaTaskScheduler;
import panda.wing.util.AppActionAssist;
import panda.wing.util.AppActionConsts;
import panda.wing.util.AppCacheProvider;
import panda.wing.util.AppDaoClientProvider;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;
import panda.wing.util.AppSettings;
import panda.wing.util.pdf.WkHtml2Pdf;

public class AppIocLoader extends MvcComboIocLoader {
	public static class AppDefaultIocLoader extends MvcDefaultIocLoader {
		public AppDefaultIocLoader(MvcConfig config) {
			super(config);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void addDefaults(Set<Object> ss) {
			super.addDefaults(ss);
			
			Collections.addAll(ss, 
				MvcDaoFilePool.class,
				AppActionAssist.class,
				AppActionConsts.class,
				AppResourceBundleLoader.class,
				AppFreemarkerTemplateLoader.class,
				AppCacheProvider.class,
				AppSettings.class,
				AppDaoClientProvider.class,
				LuceneProvider.class,
				AppAuthenticator.class,
				UserAuthenticateProcessor.class,
				WkHtml2Pdf.class);
			
			if (Systems.IS_OS_APPENGINE) {
				ss.add(GaeTaskExecutor.class);
				ss.add(GaeTaskScheduler.class);
			}
			else {
				ss.add(JavaTaskExecutor.class);
				ss.add(JavaTaskScheduler.class);
			}
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


