package panda.mvc.ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import panda.ioc.Scope;
import panda.ioc.impl.ScopeIocContext;

public class SessionIocContext extends ScopeIocContext {
	private static final Map<String, SessionIocContext> sics = new ConcurrentHashMap<String, SessionIocContext>();
	
	private SessionIocContext() {
		super(Scope.SESSION);
	}

	public static SessionIocContext get(HttpSession session) {
		SessionIocContext sic = sics.get(session.getId());
		if (sic == null) {
			sic = new SessionIocContext();
			sics.put(session.getId(), sic);
		}
		return sic;
	}
	
	public static void depose(HttpSession session) {
		SessionIocContext sic = sics.get(session.getId());
		if (sic == null) {
			return;
		}
		sic.depose();
		sics.remove(sic);
	}
}
