package panda.app;

import panda.app.lucene.LuceneProvider;
import panda.app.lucene.LuceneWrapper;
import panda.app.util.AppCacheProvider;
import panda.app.util.AppDaoClientProvider;
import panda.dao.DaoClient;
import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.Scope;
import panda.ioc.util.DelegateObjectProxy;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.provider.DefaultIocProvider;

public class AppIocProvider extends DefaultIocProvider {

	@Override
	protected void setIocBeans(Ioc ioc, MvcConfig config) {
		super.setIocBeans(ioc, config);

		ioc.getContext().save(Scope.APP, DaoClient.class.getName(), new DelegateObjectProxy(ioc, AppDaoClientProvider.class));
		
		ioc.getContext().save(Scope.APP, AppConstants.CACHE_IOCBEAN, new DelegateObjectProxy(ioc, AppCacheProvider.class));

		ioc.getContext().save(Scope.APP, LuceneWrapper.class.getName(), new DelegateObjectProxy(ioc, LuceneProvider.class));
	}

	@Override
	protected IocLoader getIocLoader(MvcConfig config, String ... args) {
		return new AppIocLoader(config, args);
	}
}

