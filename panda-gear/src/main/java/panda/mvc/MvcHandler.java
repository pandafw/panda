package panda.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.Ioc;

public class MvcHandler {
	private boolean deposed;

	private MvcLoader loading;

	/**
	 * @return the loading
	 */
	public MvcLoader getLoading() {
		return loading;
	}

	/**
	 * @return the ioc
	 */
	public Ioc getIoc() {
		return loading.getIoc();
	}
	
	/**
	 * @param loading MvcLoader
	 */
	public MvcHandler(MvcLoader loading) {
		this.loading = loading;
	}

	public boolean handle(HttpServletRequest req, HttpServletResponse res) {
		return handle(req, res, null);
	}
	
	public boolean handle(HttpServletRequest req, HttpServletResponse res, ServletFilter next) {
		req.setAttribute(Mvcs.REQUEST_TIME, System.currentTimeMillis());

		ServletChain chain = getIoc().get(ServletChain.class);
		if (next != null) {
			chain.addFilter(next);
		}
		
		return chain.doChain(req, res);
	}

	public void depose() {
		if (!deposed) {
			loading.depose();
			deposed = true;
		}
	}

}
