package panda.wing.auth;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.UrlMapping;
import panda.net.Inets;
import panda.servlet.HttpServlets;
import panda.wing.AppConstants;
import panda.wing.constant.AUTH;
import panda.wing.constant.REQ;

public abstract class UserAuthenticator {
	//--------------------------------------------------------
	// result code
	//--------------------------------------------------------
	public static final int UNKNOWN = -1;
	public static final int OK = 0;
	public static final int UNLOGIN = 2;
	public static final int UNSECURE = 3;
	public static final int DENIED = 4;
	
	/**
	 * allow unknown uri: true (default), false
	 */
	@IocInject(value=AppConstants.PANDA_AUTH_ALLOW_UNKNOWN_URL, required=false)
	protected boolean allowUnknownUri = true;

	@IocInject
	protected UrlMapping urlmapping;
	
	//----------------------------------------------------
	// abstract methods
	//
	protected abstract List<String> getUserPermits(Object su);

	protected abstract boolean isSecureSessionUser(Object su);
	

	//----------------------------------------------------
	// public methods
	//
	/**
	 * @return true if user has permit to access the method
	 */
	public int authenticate(ActionContext ac) {
		return authenticate(ac, null);
	}
	
	//----------------------------------------------------
	// override methods
	//
	protected Object getSessionUser(ActionContext ac) {
		Object u = ac.getRequest().getAttribute(REQ.USER);
		if (u == null) {
			HttpSession session = ac.getRequest().getSession(false);
			if (session != null) {
				u = session.getAttribute(REQ.USER);
			}
		}
		return u;
	}
	
	/**
	 * @return true if user has permit to access the action
	 */
	public int authenticate(ActionContext ac, String path) {
		// get user for later use
		Object su = getSessionUser(ac);

		Method method = ac.getMethod();
		Class<?> clazz = ac.getAction().getClass();
		if (Strings.isNotEmpty(path)) {
			// find action info
			ActionInfo ai = urlmapping.getActionInfo(path);
			if (ai == null) {
				return allowUnknownUri ? OK : UNKNOWN;
			}
			method = ai.getMethod();
			clazz = ai.getActionType();
		}
		

		Collection<String> defines = getMethodPermission(clazz, method);

		// no permission defined for this path
		if (Collections.isEmpty(defines)) {
			return OK;
		}
		
		Collection<String> uperms = getUserPermits(su);

		// user has 'deny all' permits
		if (Collections.contains(uperms, AUTH.NONE)) {
			return DENIED;
		}

		// user has 'allow all' permits
		if (Collections.contains(uperms, AUTH.ALL)) {
			return OK;
		}

		// check AND @Auth(...)
		for (String d : defines) {
			if (Strings.isEmpty(d)) {
				continue;
			}
			
			if (d.charAt(0) == AUTH.AND) {
				int a = authenticatePermission(ac, su, uperms, d.substring(1));
				if (a != OK) {
					return a;
				}
			}
		}

		// check  @Auth(...)
		int r = UNKNOWN;
		for (String d : defines) {
			if (Strings.isEmpty(d) || d.charAt(0) != AUTH.AND) {
				int a = authenticatePermission(ac, su, uperms, d);
				if (a == OK) {
					return a;
				}
				r = a;
			}
		}

		return r;
	}

	protected int authenticatePermission(ActionContext ac, Object su, Collection<String> uperms, String define) {
		// @Auth("") means login check only
		if (Strings.isEmpty(define)) {
			return su == null ? UNLOGIN : OK;
		}

		if (AUTH.LOCAL.equals(define)) {
			HttpServletRequest req = ac.getRequest();
			return Inets.isIntranetAddr(HttpServlets.getRemoteAddr(req)) ? OK : DENIED;
		}
		if (AUTH.SECURE.equals(define)) {
			return isSecureSessionUser(su) ? OK : UNSECURE;
		}
		return Collections.contains(uperms, define) ? OK : (su == null ? UNLOGIN : DENIED);
	}

	protected Collection<String> getMethodPermission(Class<?> clazz, Method method) {
		Auth mp = method.getAnnotation(Auth.class);
		if (mp == null) {
			mp = clazz.getAnnotation(Auth.class);
		}
		if (mp != null) {
			return Arrays.asList(mp.value());
		}
		return null;
	}
}
