package panda.app;

import java.util.Set;

import panda.app.auth.AppAuthenticator;
import panda.app.auth.UserAuthenticateProcessor;
import panda.app.lucene.LuceneProvider;
import panda.app.task.gae.GaeTaskExecutor;
import panda.app.task.gae.GaeTaskScheduler;
import panda.app.task.java.JavaTaskExecutor;
import panda.app.task.java.JavaTaskScheduler;
import panda.app.util.AppActionAssist;
import panda.app.util.AppActionConsts;
import panda.app.util.AppCacheProvider;
import panda.app.util.AppDaoClientProvider;
import panda.app.util.AppFreemarkerTemplateLoader;
import panda.app.util.AppResourceBundleLoader;
import panda.app.util.AppSettings;
import panda.app.util.pdf.WkHtml2Pdf;
import panda.lang.Collections;
import panda.lang.Systems;
import panda.mvc.MvcConfig;
import panda.mvc.filepool.MvcDaoFilePool;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;

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


