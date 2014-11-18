package panda.wing;

import panda.dao.DaoClient;
import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.ioc.impl.DefaultIoc;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.provider.AbstractIocProvider;
import panda.wing.util.AppCacheProvider;
import panda.wing.util.AppDaoClientProvider;

public class AppIocProvider extends AbstractIocProvider {

	public Ioc create(MvcConfig config, String ... args) {
		DefaultIoc ioc = create(getIocLoader(config, args));
		
		AppDaoClientProvider dcp = ioc.get(AppDaoClientProvider.class);
		ioc.getContext().save(Scope.APP, DaoClient.class.getName(), new ObjectProxy(dcp.getDaoClient()));
		
		AppCacheProvider acp = ioc.get(AppCacheProvider.class);
		ioc.getContext().save(Scope.APP, AppConstants.CACHE, new ObjectProxy(acp.getCache()));
		return ioc;
	}
	
	protected IocLoader getIocLoader(MvcConfig config, String ... args) {
		return new AppIocLoader(config, args);
	}
}

