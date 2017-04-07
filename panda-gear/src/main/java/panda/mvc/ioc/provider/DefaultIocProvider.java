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
	public Ioc create(MvcConfig config, String... args) {
		Ioc ioc = create(getIocLoader(config, args));
		
		setIocBeans(ioc, config);
		
		return ioc;
	}
	
	protected void setIocBeans(Ioc ioc, MvcConfig config) {
		IocContext ictx = ioc.getContext();
		
		// save default beans
		ictx.save(Scope.APP, MvcConfig.class.getName(), new DefaultObjectProxy(config));
		ictx.save(Scope.APP, ServletContext.class.getName(), new DefaultObjectProxy(config.getServletContext()));
		ictx.save(Scope.APP, "$servlet", new DefaultObjectProxy(config.getServletContext()));
	}
	
	protected IocLoader getIocLoader(MvcConfig config, String ... args) {
		return new MvcDefaultIocLoader(config);
	}
}
