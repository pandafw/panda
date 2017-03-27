package panda.wing;

import panda.dao.DaoClient;
import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.provider.DefaultIocProvider;
import panda.wing.util.AppCacheProvider;
import panda.wing.util.AppDaoClientProvider;

public class AppIocProvider extends DefaultIocProvider {

	@Override
	protected void setIocBeans(Ioc ioc, MvcConfig config) {
		super.setIocBeans(ioc, config);

		AppDaoClientProvider dcp = ioc.get(AppDaoClientProvider.class);
		ioc.getContext().save(Scope.APP, DaoClient.class.getName(), new ObjectProxy(dcp.getDaoClient()));
		
		AppCacheProvider acp = ioc.get(AppCacheProvider.class);
		ioc.getContext().save(Scope.APP, AppConstants.CACHE_IOCBEAN, new ObjectProxy(acp.getCache()));
	}
	
	@Override
	protected IocLoader getIocLoader(MvcConfig config, String ... args) {
		return new AppIocLoader(config, args);
	}
}

