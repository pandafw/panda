package panda.mvc.ioc.provider;

import javax.servlet.ServletContext;

import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.IocLoader;
import panda.ioc.Scope;
import panda.ioc.impl.DefaultObjectProxy;
import panda.mvc.MvcConfig;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;

public class DefaultIocProvider extends AbstractIocProvider {
	@Override
	public Ioc create(MvcConfig mcfg, String... args) {
		Ioc ioc = create(mcfg, getIocLoader(mcfg, args));
		
		setIocBeans(ioc, mcfg);
		
		return ioc;
	}
	
	protected void setIocBeans(Ioc ioc, MvcConfig mcfg) {
		IocContext ictx = ioc.getContext();
		
		// save default beans
		ictx.save(Scope.APP, MvcConfig.class.getName(), new DefaultObjectProxy(mcfg));
		ictx.save(Scope.APP, ServletContext.class.getName(), new DefaultObjectProxy(mcfg.getServletContext()));
		ictx.save(Scope.APP, "$servlet", new DefaultObjectProxy(mcfg.getServletContext()));
	}
	
	protected IocLoader getIocLoader(MvcConfig config, String ... args) {
		return new MvcDefaultIocLoader(config);
	}
}
