package panda.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.Ioc;
import panda.ioc.impl.ComboIocContext;
import panda.ioc.impl.DefaultIoc;
import panda.mvc.config.AbstractMvcConfig;
import panda.mvc.impl.ActionInvoker;
import panda.mvc.impl.DefaultMvcLoading;
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
					ctx.addContext(RequestIocContext.get(req));
				}
				
				if (IocSessionListener.isSessionScopeEnable) {
					ctx.addContext(SessionIocContext.get(req.getSession()));
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
		req.setAttribute(ActionContext.class.getName(), ac);

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
