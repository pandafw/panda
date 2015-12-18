package panda.mvc.ioc;

import javax.servlet.ServletRequest;

import panda.ioc.Scope;
import panda.ioc.impl.ScopeIocContext;

public class RequestIocContext extends ScopeIocContext {
	private RequestIocContext() {
		super(Scope.REQUEST);
	}

	public static RequestIocContext get(ServletRequest request) {
		RequestIocContext ric = (RequestIocContext)request.getAttribute(RequestIocContext.class.getName());
		if (ric == null) {
			ric = new RequestIocContext();
			request.setAttribute(RequestIocContext.class.getName(), ric);
		}
		return ric;
	}
	
	public static void depose(ServletRequest request) {
		RequestIocContext ric = (RequestIocContext)request.getAttribute(RequestIocContext.class.getName());
		if (ric == null) {
			return;
		}
		ric.depose();
		request.removeAttribute(RequestIocContext.class.getName());
	}
}
