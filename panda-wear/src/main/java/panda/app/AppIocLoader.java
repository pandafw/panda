package panda.app;

import java.util.Collection;

import panda.app.auth.AppAuthenticator;
import panda.app.auth.UserAuthenticateProcessor;
import panda.app.index.gae.RevisionedGaeIndexes;
import panda.app.index.lucene.RevisionedLuceneIndexes;
import panda.app.task.ActionTaskSubmitter;
import panda.app.task.gae.GaeTaskExecutor;
import panda.app.task.gae.GaeTaskScheduler;
import panda.app.task.java.JavaTaskExecutor;
import panda.app.task.java.JavaTaskScheduler;
import panda.app.util.AppActionAssist;
import panda.app.util.AppActionConsts;
import panda.app.util.AppCache;
import panda.app.util.AppCacheFactory;
import panda.app.util.AppDao;
import panda.app.util.AppDaoClient;
import panda.app.util.AppDaoClientFactory;
import panda.app.util.AppFreemarkerTemplateLoader;
import panda.app.util.AppResourceBundleLoader;
import panda.app.util.AppSettings;
import panda.app.util.pdf.WkHtml2Pdf;
import panda.lang.Collections;
import panda.lang.Systems;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;

public class AppIocLoader extends MvcComboIocLoader {
	public static class AppDefaultIocLoader extends MvcDefaultIocLoader {
		public AppDefaultIocLoader(MvcConfig config) {
			super(config);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void addDefaults(Collection<Object> ss) {
			super.addDefaults(ss);
			
			Collections.addAll(ss, 
				AppAuthenticator.class,
				UserAuthenticateProcessor.class,
				AppActionAssist.class,
				AppActionConsts.class,
				AppResourceBundleLoader.class,
				AppFreemarkerTemplateLoader.class,
				AppCache.class,
				AppCacheFactory.class,
				AppDao.class,
				AppDaoClient.class,
				AppDaoClientFactory.class,
				AppSettings.class,
				WkHtml2Pdf.class);
			
			if (Systems.IS_OS_APPENGINE) {
				ss.add(GaeTaskExecutor.class);
				ss.add(GaeTaskScheduler.class);
				ss.add(RevisionedGaeIndexes.class);
			}
			else {
				ss.add(ActionTaskSubmitter.class);
				ss.add(JavaTaskExecutor.class);
				ss.add(JavaTaskScheduler.class);
				ss.add(RevisionedLuceneIndexes.class);
			}
		}
	}
	
	public AppIocLoader(MvcConfig config, String... args) {
		super(config, args);
	}
	
	@Override
	protected void initAlias() {
		super.initAlias();
		alias.put(DEFAULT, AppDefaultIocLoader.class);
	}
}

