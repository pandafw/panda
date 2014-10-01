package panda.mvc.ioc;

import javax.servlet.ServletRequest;

import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.ioc.impl.ScopeIocContext;

public class RequestIocContext extends ScopeIocContext {
	private static final long serialVersionUID = 1L;

	private ServletRequest request;

	private RequestIocContext() {
		super(Scope.REQUEST);
	}

	public static RequestIocContext get(ServletRequest request) {
		RequestIocContext ric = (RequestIocContext)request.getAttribute(RequestIocContext.class.getName());
		if (ric == null) {
			ric = new RequestIocContext();
			request.setAttribute(RequestIocContext.class.getName(), ric);
		}
		ric.request = request;
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
	
	public ObjectProxy fetch(String name) {
		ObjectProxy op = super.fetch(name);
		if (op != null) {
			return op;
		}
		
		Object o = request.getAttribute(name);
		if (o == null) {
			return null;
		}

		return new ObjectProxy().setObj(o);
	}

}
