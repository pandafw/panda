package panda.wing;

import panda.dao.DaoClient;
import panda.ioc.Ioc;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.ioc.impl.DefaultIoc;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcComboIocLoader;
import panda.mvc.ioc.provider.AbstractIocProvider;
import panda.wing.util.DaoClientProvider;

public class AppIocProvider extends AbstractIocProvider {

	public Ioc create(MvcConfig config, String ... args) {
		DefaultIoc ioc = create(new MvcComboIocLoader(args));
		
		DaoClientProvider dcp = ioc.get(DaoClientProvider.class);
		ioc.getContext().save(Scope.APP, DaoClient.class.getName(), new ObjectProxy(dcp.getDaoClient()));
		return ioc;
	}
}

