package panda.mvc.ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.ioc.IocContext;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.ioc.impl.ScopeIocContext;

public class SessionIocContext implements IocContext {
	private static final Map<String, ScopeIocContext> sics = new ConcurrentHashMap<String, ScopeIocContext>();

	private HttpServletRequest request;
	
	private SessionIocContext(HttpServletRequest request) {
		this.request = request;
	}

	public static SessionIocContext get(HttpServletRequest request) {
		return new SessionIocContext(request);
	}

	private static ScopeIocContext get(HttpSession session) {
		ScopeIocContext sic = sics.get(session.getId());
		if (sic == null) {
			synchronized (sics) {
				sic = sics.get(session.getId());
				if (sic == null) {
					sic = new ScopeIocContext(Scope.SESSION);
					sics.put(session.getId(), sic);
				}
			}
		}
		return sic;
	}
	
	public static void depose(HttpSession session) {
		ScopeIocContext sic = sics.remove(session.getId());
		if (sic == null) {
			return;
		}

		sic.depose();
	}
	
	public static int size() {
		return sics.size();
	}
	
	public static String dump(HttpSession session) {
		ScopeIocContext sic = null;
		if (session != null) {
			sic = sics.get(session.getId());
		}
		return SessionIocContext.class.getName() + '[' + sics.size() + "]: " 
				+ (session == null ? "NULL" : session.getId() + ": " + sic);
	}
	
	public static String dump() {
		return SessionIocContext.class.getName() + '[' + sics.size() + "]: " + sics;
	}

	
	//------------------------------------
	private boolean accept(String scope) {
		return Scope.SESSION.equals(scope);
	}

	@Override
	public boolean save(String scope, String name, ObjectProxy obj) {
		if (accept(scope)) {
			return get(request.getSession()).save(scope, name, obj);
		}
		return false;
	}

	@Override
	public boolean remove(String scope, String name) {
		if (accept(scope)) {
			return get(request.getSession()).remove(scope, name);
		}
		return false;
	}

	@Override
	public ObjectProxy fetch(String name) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

		ScopeIocContext sic = sics.get(session.getId());
		if (sic == null) {
			return null;
		}
	
		return sic.fetch(name);
	}

	@Override
	public void clear() {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		ScopeIocContext sic = sics.get(session.getId());
		if (sic == null) {
			return;
		}

		sic.clear();
	}

	@Override
	public void depose() {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}

		ScopeIocContext sic = sics.get(session.getId());
		if (sic == null) {
			return;
		}

		sic.depose();
	}
}
