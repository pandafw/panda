package panda.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.mvc.config.AbstractMvcConfig;
import panda.mvc.impl.ActionInvoker;

public class ActionHandler {

	private Loading loading;

	private UrlMapping mapping;

	private MvcConfig config;

	public ActionHandler(AbstractMvcConfig config) {
		this.config = config;
		this.loading = config.createLoading();
		this.mapping = loading.load(config);
	}

	public boolean handle(HttpServletRequest req, HttpServletResponse res) {
		ActionContext ac = new ActionContext();
		ac.setIoc(config.getIoc());
		ac.setServletContext(config.getServletContext());
		ac.setRequest(req);
		ac.setResponse(res);
		ac.setFilePool(Mvcs.getFilePool());

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
