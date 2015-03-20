package panda.wing.auth;

import java.lang.reflect.Method;
import java.util.Collection;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.UrlMapping;
import panda.wing.AppConstants;

@IocBean
public class AppAuthenticater {
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

	/**
	 * allow unknown uri: true (default), false
	 */
	@IocInject(value=AppConstants.PANDA_AUTHENTICATE_ALLOW_UNKNOWN_URL, required=false)
	protected boolean allowUnknownUri = true;

	@IocInject
	protected UrlMapping urlmapping;
	

	/**
	 * allow
	 * @param permits permits
	 * @return true if user has permit to access the action
	 */
	public boolean allow(String[] permits, String uri) {
		return allow(Arrays.asList(permits), uri);
	}
	
	/**
	 * allow
	 * @param permits permits
	 * @return true if user has permit to access the method
	 */
	public boolean allow(Collection<String> permits, ActionContext ac) {
		return allow(permits, ac.getMethod(), ac);
	}
	
	/**
	 * allow
	 * @param permits permits
	 * @param method method
	 * @return true if user has permit to access the action
	 */
	protected boolean allow(Collection<String> permits, Method method, ActionContext ac) {
		// user has 'allow all' permits
		if (permits.contains(PERMISSION_ALL)) {
			return true;
		}

		// user has 'deny all' permits
		if (permits.contains(PERMISSION_NONE)) {
			return false;
		}

		String[] defines = getMethodPermission(method);

		return allow(permits, defines, null);
	}
	
	protected String[] getMethodPermission(Method method) {
		Permission mp = method.getAnnotation(Permission.class);
		if (mp == null) {
			mp = method.getDeclaringClass().getAnnotation(Permission.class);
		}
		if (mp != null) {
			return mp.value();
		}
		return null;
	}
	
	/**
	 * allow
	 * @param permits permits
	 * @param path path
	 * @return true if user has permit to access the action
	 */
	public boolean allow(Collection<String> permits, String path) {
		// user has 'allow all' permits
		if (permits.contains(PERMISSION_ALL)) {
			return true;
		}

		// user has 'deny all' permits
		if (permits.contains(PERMISSION_NONE)) {
			return false;
		}

		// find action info
		ActionInfo ai = urlmapping.getActionInfo(path);
		if (ai == null) {
			return allowUnknownUri;
		}

		String[] defines = getMethodPermission(ai.getMethod());

		return allow(permits, defines, null);
	}
	
	protected boolean allow(Collection<String> permits, String[] defines, ActionContext ac) {
		// no permission defined for this path
		if (Arrays.isEmpty(defines)) {
			return true;
		}
		
		// user has empty permits
		if (Collections.isEmpty(permits)) {
			return false;
		}

		for (String d : defines) {
			if (Strings.isEmpty(d)) {
				continue;
			}
			if (!allow(permits, d, ac)) {
				return false;
			}
		}
		return true;
	}

	protected boolean allow(Collection<String> permits, String define, ActionContext ac) {
		return permits.contains(define);
	}
}
