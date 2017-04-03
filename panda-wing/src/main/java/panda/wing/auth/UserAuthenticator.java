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
import panda.mvc.ActionConfig;
import panda.mvc.UrlMapping;
import panda.net.IPs;
import panda.servlet.HttpServlets;
import panda.wing.AppConstants;
import panda.wing.constant.AUTH;
import panda.wing.constant.REQ;
import panda.wing.constant.SES;

public abstract class UserAuthenticator {
	//--------------------------------------------------------
	// result code
	//--------------------------------------------------------
	public static final int OK_NO_DEFINES = -2;
	public static final int OK_NO_MAPPING = -1;
	public static final int OK = 0;
	public static final int UNKNOWN = 1;
	public static final int UNLOGIN = 2;
	public static final int UNSECURE = 3;
	public static final int DENIED = 4;
	
	/**
	 * allow unknown uri: true (default), false
	 */
	@IocInject(value=AppConstants.AUTH_ALLOW_UNKNOWN_URL, required=false)
	protected boolean allowUnknownUri = true;

	@IocInject
	protected UrlMapping urlmapping;
	
	//----------------------------------------------------
	// abstract methods
	//
	protected abstract List<String> getUserPermissions(Object su);

	protected abstract boolean isSecureAuthenticatedUser(Object su);
	

	//----------------------------------------------------
	// public methods
	//
	/**
	 * @return true if user has permit to access the method
	 */
	public int authenticate(ActionContext ac) {
		return authenticate(ac, null, allowUnknownUri);
	}
	
	//----------------------------------------------------
	// override methods
	//
	protected Object getAuthenticatedUser(ActionContext ac) {
		Object u = ac.getRequest().getAttribute(REQ.USER);
		if (u == null) {
			HttpSession session = ac.getRequest().getSession(false);
			if (session != null) {
				u = session.getAttribute(SES.USER);
				if (u != null) {
					ac.getRequest().setAttribute(REQ.USER, u);
				}
			}
		}
		return u;
	}

	/**
	 * can access the specified action
	 * @param ac action context
	 * @param action action path
	 * @return true if action has access permit
	 */
	public boolean canAccess(ActionContext ac, String action) {
		int r= authenticate(ac, action, false);
		return r <= OK || r == UNSECURE;
	}

	/**
	 * @return true if user has permit to access the action
	 */
	protected int authenticate(ActionContext ac, String path, boolean allowUnknownUri) {
		// get user for later use
		Object su = getAuthenticatedUser(ac);

		Method method = ac.getMethod();
		Class<?> clazz = ac.getAction().getClass();
		if (Strings.isNotEmpty(path)) {
			// find action config
			ActionConfig am = urlmapping.getActionConfig(path);
			if (am == null) {
				return allowUnknownUri ? OK_NO_MAPPING : UNKNOWN;
			}
			method = am.getActionMethod();
			clazz = am.getActionType();
		}
		

		Collection<String> defines = getMethodPermission(clazz, method);

		// no permission defined for this path
		if (Collections.isEmpty(defines)) {
			return OK_NO_DEFINES;
		}
		
		Collection<String> uperms = getUserPermissions(su);

		// user has 'deny all' permission
		if (Collections.contains(uperms, AUTH.NONE)) {
			return DENIED;
		}

		// user has 'allow all' permission
		if (Collections.contains(uperms, AUTH.SUPER)) {
			return OK;
		}

		// check AND @Auth(...)
		for (String d : defines) {
			if (Strings.isEmpty(d)) {
				continue;
			}
			
			if (d.charAt(0) == AUTH.AND) {
				int a = authenticatePermission(ac, su, uperms, d.substring(1));
				if (a > OK) {
					return a;
				}
			}
		}

		// check  @Auth(...)
		int r = UNKNOWN;
		for (String d : defines) {
			if (Strings.isEmpty(d) || d.charAt(0) != AUTH.AND) {
				int a = authenticatePermission(ac, su, uperms, d);
				if (a <= OK) {
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
			return IPs.isPrivateIP(HttpServlets.getRemoteAddr(req)) ? OK : DENIED;
		}
		if (AUTH.SECURE.equals(define)) {
			return isSecureAuthenticatedUser(su) ? OK : UNSECURE;
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
