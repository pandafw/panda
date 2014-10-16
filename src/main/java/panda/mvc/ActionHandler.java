package panda.mvc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.Ioc;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.ioc.impl.ComboIocContext;
import panda.ioc.impl.DefaultIoc;
import panda.mvc.config.AbstractMvcConfig;
import panda.mvc.impl.ActionInvoker;
import panda.mvc.impl.DefaultMvcLoading;
import panda.mvc.ioc.IocRequestListener;
import panda.mvc.ioc.IocSessionListener;
import panda.mvc.ioc.RequestIocContext;
import panda.mvc.ioc.SessionIocContext;

public class ActionHandler {

	private Loading loading;

	private UrlMapping mapping;

	private MvcConfig config;

	public ActionHandler(AbstractMvcConfig config) {
		this.config = config;
		this.loading = new DefaultMvcLoading();
		this.mapping = loading.load(config);
	}

	/**
	 * @return the loading
	 */
	public Loading getLoading() {
		return loading;
	}

	/**
	 * @return the mapping
	 */
	public UrlMapping getMapping() {
		return mapping;
	}

	/**
	 * @return the config
	 */
	public MvcConfig getConfig() {
		return config;
	}

	public boolean handle(HttpServletRequest req, HttpServletResponse res) {
		ActionContext ac = new ActionContext();

		Ioc ioc = config.getIoc();
		if (ioc instanceof DefaultIoc) {
			if (IocRequestListener.isRequestScopeEnable || IocSessionListener.isSessionScopeEnable) {
				DefaultIoc di = ((DefaultIoc)ioc).clone();

				ComboIocContext ctx = new ComboIocContext();
				if (IocRequestListener.isRequestScopeEnable) {
					RequestIocContext ric = RequestIocContext.get(req);

					ric.save(Scope.REQUEST, ActionContext.class.getName(), new ObjectProxy(ac));
					ric.save(Scope.REQUEST, ServletRequest.class.getName(), new ObjectProxy(req));
					ric.save(Scope.REQUEST, ServletResponse.class.getName(), new ObjectProxy(res));
					ric.save(Scope.REQUEST, HttpServletRequest.class.getName(), new ObjectProxy(req));
					ric.save(Scope.REQUEST, HttpServletResponse.class.getName(), new ObjectProxy(res));
					
					ctx.addContext(ric);
				}
				
				if (IocSessionListener.isSessionScopeEnable) {
					SessionIocContext sic = SessionIocContext.get(req.getSession());
					ctx.addContext(sic);
				}
				
				ctx.addContext(di.getContext());
				di.setContext(ctx);
				
				ioc = di;
			}
		}

		ac.setIoc(ioc);
		ac.setServlet(config.getServletContext());
		ac.setRequest(req);
		ac.setResponse(res);

		ActionInvoker invoker = mapping.get(ac);
		if (null == invoker) {
			return false;
		}
		
		return invoker.invoke(ac);
	}

	public void depose() {
		loading.depose(config);
	}

}
