package panda.wing.auth;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.ioc.annotation.IocBean;
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
import panda.wing.constant.REQ;

@IocBean
public class UserAuthenticator {
	//--------------------------------------------------------
	// result code
	//--------------------------------------------------------
	public final static int UNKNOWN = -1;
	public final static int OK = 0;
	public final static int UNLOGIN = 2;
	public final static int UNSECURE = 3;
	public final static int DENIED = 4;
	
	//--------------------------------------------------------
	// permission
	//--------------------------------------------------------
	/**
	 * PERMISSION_ALL = "*";
	 */
	public final static String PERMISSION_ALL = "*";
	
	/**
	 * PERMISSION_NONE = "-";
	 */
	public final static String PERMISSION_NONE = "-";

	public final static String PERM_LOCAL = "~local";
	
	public final static String PERM_SECURE = "+secure";
	
	/**
	 * allow unknown uri: true (default), false
	 */
	@IocInject(value=AppConstants.PANDA_AUTH_ALLOW_UNKNOWN_URL, required=false)
	protected boolean allowUnknownUri = true;

	@IocInject
	protected UrlMapping urlmapping;
	

	//----------------------------------------------------
	// public methods
	//
	/**
	 * @return true if user has permit to access the method
	 */
	public int authenticate(ActionContext ac) {
		return authenticate(ac, null, true);
	}
	
	/**
	 * @return true if user has permit to access the action
	 */
	public int authenticate(ActionContext ac, String path) {
		return authenticate(ac, path, false);
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
	
	protected List<String> getUserPermits(Object su) {
		return null;
	}

	protected boolean isSecureSessionUser(Object su) {
		return false;
	}
	//----------------------------------------------------

	protected int authenticate(ActionContext ac, String path, boolean special) {
		Method method = ac.getMethod();
		if (Strings.isNotEmpty(path)) {
			// find action info
			ActionInfo ai = urlmapping.getActionInfo(path);
			if (ai == null) {
				return allowUnknownUri ? OK : UNKNOWN;
			}
			method = ai.getMethod();
		}
		
		String[] defines = getMethodPermission(method);

		// no permission defined for this path
		if (Arrays.isEmpty(defines)) {
			return OK;
		}
		
		Object su = getSessionUser(ac);
		
		Collection<String> uperms = getUserPermits(su);

		// user has 'deny all' permits
		if (Collections.contains(uperms, PERMISSION_NONE)) {
			return DENIED;
		}

		int r = OK;
		// user does not has 'allow all' permits
		if (!Collections.contains(uperms, PERMISSION_ALL)) {
			for (String d : defines) {
				if (Strings.isEmpty(d)) {
					continue;
				}
				if (d.charAt(0) != '~') {
					int a = authenticatePermission(su, uperms, d);
					if (a != OK) {
						r = a;
						break;
					}
				}
			}
		}
		if (r == OK) {
			return r;
		}

		if (special) {
			for (String d : defines) {
				if (Strings.isEmpty(d)) {
					continue;
				}
				if (d.charAt(0) == '~') {
					int a = authenticateSpecial(ac, su, d);
					if (a == OK) {
						return a;
					}
				}
			}
		}
		return r;
	}

	protected int authenticatePermission(Object su, Collection<String> uperms, String define) {
		if (PERM_SECURE.equals(define)) {
			return isSecureSessionUser(su) ? OK : UNSECURE;
		}
		return uperms.contains(define) ? OK : DENIED;
	}

	protected int authenticateSpecial(ActionContext ac, Object su, String define) {
		if (PERM_LOCAL.equals(define)) {
			HttpServletRequest req = ac.getRequest();
			return Inets.isIntranetAddr(HttpServlets.getRemoteAddr(req)) ? OK : DENIED;
		}
		return UNKNOWN;
	}

	protected String[] getMethodPermission(Method method) {
		Auth mp = method.getAnnotation(Auth.class);
		if (mp == null) {
			mp = method.getDeclaringClass().getAnnotation(Auth.class);
		}
		if (mp != null) {
			return mp.value();
		}
		return null;
	}
}
