package panda.mvc.ioc;

import javax.servlet.http.HttpSession;

import panda.ioc.Scope;
import panda.ioc.impl.ScopeIocContext;

public class SessionIocContext extends ScopeIocContext {
	private static final long serialVersionUID = 1L;

	private SessionIocContext() {
		super(Scope.SESSION);
	}

	public static SessionIocContext get(HttpSession session) {
		SessionIocContext sic = (SessionIocContext)session.getAttribute(SessionIocContext.class.getName());
		if (sic == null) {
			sic = new SessionIocContext();
			session.setAttribute(SessionIocContext.class.getName(), sic);
		}
		return sic;
	}
	
	public static void depose(HttpSession session) {
		SessionIocContext sic = (SessionIocContext)session.getAttribute(SessionIocContext.class.getName());
		if (sic == null) {
			return;
		}
		sic.depose();
		session.removeAttribute(SessionIocContext.class.getName());
	}
}
