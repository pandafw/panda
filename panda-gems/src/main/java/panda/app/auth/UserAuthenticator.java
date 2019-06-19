package panda.app.auth;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import panda.app.constant.AUTH;
import panda.app.constant.MVC;
import panda.app.constant.SES;
import panda.app.constant.VAL;
import panda.bind.json.JsonDeserializer;
import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.codec.binary.Base64;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionConfig;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.net.IPs;
import panda.net.http.HttpHeader;
import panda.servlet.HttpServlets;
import panda.util.crypto.Cryptor;

public class UserAuthenticator {
	private static final Log log = Logs.getLog(UserAuthenticator.class);

	public static final String DEFAULT_COOKIE_NAME = "WW_TICKET";

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
	@IocInject(value=MVC.AUTH_ALLOW_UNKNOWN_URL, required=false)
	protected boolean allowUnknownUri = true;
	
	@IocInject(value=MVC.AUTH_TOKEN_HEADER_NAME, required=false)
	protected String tokenHeaderName = "X-AUTH-TOKEN";

	@IocInject(value=MVC.AUTH_TOKEN_PARAM_NAME, required=false)
	protected String tokenParamName = "__auth_token";

	@IocInject(value=MVC.AUTH_TOKEN_LIFE, required=false)
	protected long tokenLife = 60000;

	protected String tokenValue;
	
	protected long tokenTime;

	/**
	 * ticket parameter name
	 */
	@IocInject(value=MVC.AUTH_TICKET_PARAM_NAME, required=false)
	protected String paramName = "_ticket_";

	/**
	 * ticket cookie name (default: WW_TICKET + CONTEXT_PATH.replace('/', '_'))
	 */
	@IocInject(value=MVC.AUTH_TICKET_COOKIE_NAME, required=false)
	protected String cookieName;

	/**
	 * ticket cookie name (default: WW_TICKET + CONTEXT_PATH.replace('/', '_'))
	 */
	@IocInject(value=MVC.AUTH_TICKET_COOKIE_SUFFIX, required=false)
	protected boolean cookieSuffix = true;

	/**
	 * ticket cookie path (default: CONTEXT_PATH)
	 */
	@IocInject(value=MVC.AUTH_TICKET_COOKIE_PATH, required=false)
	protected String cookiePath;

	/**
	 * ticket cookie age (default: 60 * 60 * 24 * 7days)
	 */
	@IocInject(value=MVC.AUTH_TICKET_COOKIE_AGE, required=false)
	protected Integer cookieAge = 60 * 60 * 24 * 7;

	
	/**
	 * secure user session time (s): 30m 
	 */
	@IocInject(value=MVC.AUTH_SECURE_USER_AGE, required=false)
	protected long secureUserAge = 30 * 60;

	@IocInject
	protected ActionMapping urlmapping;
	
	@IocInject
	protected Cryptor cryptor;

	//----------------------------------------------------
	// public methods
	//
	/**
	 * @param ac action context
	 * @return true if user has permit to access the method
	 */
	public int authenticate(ActionContext ac) {
		return authenticate(ac, null, allowUnknownUri);
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

	protected boolean isValidToken() {
		return Strings.isNotEmpty(tokenValue) && tokenTime + tokenLife > System.currentTimeMillis();
	}

	/**
	 * @return the tokenHeaderName
	 */
	public String getTokenHeaderName() {
		return tokenHeaderName;
	}

	/**
	 * @return token parameter name
	 */
	public String getTokenParamName() {
		return tokenParamName;
	}

	/**
	 * get the exists valid token. if the token is invalid, a new token will be generated.
	 * 
	 * @return a valid token
	 */
	public String getTokenValue() {
		if (!isValidToken()) {
			tokenValue = Randoms.randUUID32();
			tokenTime = System.currentTimeMillis();
		}
		return tokenValue;
	}

	/**
	 * @param ac action context
	 * @param path action path
	 * @param allowUnknownUri OK_NO_MAPPING or UNKNOWN
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
		if (Collections.isItemsEmpty(defines)) {
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
			if (Strings.isEmpty(d) || d.charAt(0) != AUTH.AND) {
				continue;
			}
			
			int a = authenticatePermission(ac, su, uperms, d.substring(1));
			if (a > OK) {
				return a;
			}
		}

		// check OR @Auth(...)
		int r = UNKNOWN;
		for (String d : defines) {
			if (Strings.isEmpty(d) || d.charAt(0) == AUTH.AND) {
				continue;
			}

			int a = authenticatePermission(ac, su, uperms, d);
			if (a <= OK) {
				return a;
			}
			r = a;
		}

		return r;
	}

	protected int authenticatePermission(ActionContext ac, Object su, Collection<String> uperms, String define) {
		if (AUTH.SIGNIN.equals(define)) {
			return su == null ? UNLOGIN : OK;
		}

		if (AUTH.LOCAL.equals(define)) {
			HttpServletRequest req = ac.getRequest();
			return IPs.isPrivateIP(HttpServlets.getRemoteAddr(req)) ? OK : DENIED;
		}

		if (AUTH.TOKEN.equals(define)) {
			if (isValidToken()) {
				if (Strings.isNotEmpty(tokenParamName)) {
					String token = ac.getRequest().getParameter(tokenParamName);
					if (tokenValue.equals(token)) {
						return OK;
					}
				}
				if (Strings.isNotEmpty(tokenHeaderName)) {
					String token = ac.getRequest().getHeader(tokenHeaderName);
					if (tokenValue.equals(token)) {
						return OK;
					}
				}
			}

			return DENIED;
		}

		if (AUTH.SECURE.equals(define)) {
			return isSecureAuthenticatedUser(ac, su) ? OK : UNSECURE;
		}

		return Collections.contains(uperms, define) ? OK : (su == null ? UNLOGIN : DENIED);
	}

	//---------------------------------------------------------------------------------
	// permissions
	//
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

	@SuppressWarnings("unchecked")
	protected List<String> getUserPermissions(Object su) {
		if (su == null) {
			return Collections.EMPTY_LIST;
		}
		if (su instanceof IPermission) {
			return ((IPermission)su).getPermissions();
		}
		if (su instanceof IRole) {
			return Arrays.asList(((IRole)su).getRole());
		}
		return Collections.EMPTY_LIST;
	}

	//------------------------------------------------------------------------
	/**
	 * @param u user
	 * @param p permission
	 * @return true if user has specified permission
	 */
	public boolean hasPermission(IUser u, String p) {
		if (u == null) {
			return false;
		}

		if (u instanceof IPermission) {
			List<String> ps = ((IPermission)u).getPermissions();
			if (Collections.contains(ps, p)) {
				return true;
			}
			if (!AUTH.SUPER.equals(p) && Collections.contains(ps, AUTH.SUPER)) {
				return true;
			}
			return false;
		}
		
		if (u instanceof IRole) {
			String r = ((IRole)u).getRole();
			return Strings.equals(r, p) || AUTH.SUPER.equals(r);
		}
		
		return false;
	}
	
	/**
	 * @param u user
	 * @return true - if the user is admin
	 */
	public boolean hasAdminRole(IUser u) {
		return hasPermission(u, AUTH.ADMIN);
	}

	/**
	 * @param u user
	 * @return true - if the user is super
	 */
	public boolean hasSuperRole(IUser u) {
		return hasPermission(u, AUTH.SUPER);
	}

	/**
	 * @param ac action context
	 * @return login user ID or SYSTEM_UID
	 */
	public long getLoginUserId(ActionContext ac) {
		IUser user = getLoginUser(ac);
		return user == null ? VAL.SYSTEM_UID : user.getId();
	}

	/**
	 * @param ac action context
	 * @return login user ID or SYSTEM_UID
	 */
	public String getLoginUserName(ActionContext ac) {
		IUser user = getLoginUser(ac);
		return user == null ? VAL.SYSTEM_UNM : user.getName();
	}

	/**
	 * @param ac action context
	 * @return login user ID or SYSTEM_UID
	 */
	public String getLoginUserPassword(ActionContext ac) {
		IUser user = getLoginUser(ac);
		return user == null ? Strings.EMPTY : user.getPassword();
	}

	/**
	 * get Login User From Request Attributes
	 * @param ac action context
	 * @return user object
	 */
	public IUser getLoginUser(ActionContext ac) {
		return (IUser)ac.getSes().get(SES.USER);
	}

	public boolean isSecureAuthenticatedUser(ActionContext ac, Object su) {
		Date lt = (Date)ac.getSes().get(SES.USER_LOGIN_TIME);
		if (lt != null) {
			return System.currentTimeMillis() - lt.getTime() < secureUserAge * 1000;
		}
		return false;
	}

	//------------------------------------------------------------------------------
	// Users
	//
	protected String serializeUser(Object user) {
		JsonSerializer js = Jsons.newJsonSerializer();
		js.setDateToMillis(true);
		return js.serialize(user);
	}
	
	protected <T> T deserializeUser(String ticket, Class<T> type) {
		JsonDeserializer jd = new JsonDeserializer();
		jd.setIgnoreMissingProperty(true);
		jd.setIgnoreReadonlyProperty(true);
		jd.setIgnoreNullProperty(true);
		return jd.deserialize(ticket, type);
	}
	
	protected Object deserializeUser(String ticket) {
		return deserializeUser(ticket, Map.class);
	}

	protected Object getAuthenticatedUser(ActionContext ac) {
		Object u = getUserFromParameter(ac);
		if (u != null) {
			saveUserToContext(ac, u, null);
			return u;
		}

		u = getUserFromContext(ac);
		if (u != null) {
			return u;
		}

		u = getUserFromClient(ac);
		if (u != null) {
			saveUserToContext(ac, u, null);
		}
		return u;
	}

	//------------------------------------------------------
	/**
	 * get user object from request
	 * @param ac action context
	 * @return user object
	 */
	protected Object getUserFromRequest(ActionContext ac) {
		Object u = getUserFromParameter(ac);
		if (u != null) {
			return u;
		}
		
		return getUserFromAuthorization(ac);
	}
	
	/**
	 * get user object from request parameter
	 * @param ac action context
	 * @return user object
	 */
	protected Object getUserFromParameter(ActionContext ac) {
		String ticket = ac.getRequest().getParameter(paramName);
		if (Strings.isEmpty(ticket)) {
			return null;
		}
		
		try {
			ticket = decrypt(ticket);
			Object u = deserializeUser(ticket);
			return u;
		}
		catch (Exception e) {
			log.warn("Invalid AUTH Parameter " + paramName + ": " + ticket + " - " + e.getMessage());
			if (log.isDebugEnabled()) {
				log.debug("Failed to decrypt AUTH parameter", e);
			}
		}
		return null;
	}

	/**
	 * get user object from Authorization Header
	 * @param ac action context
	 * @return user object
	 */
	protected Object getUserFromAuthorization(ActionContext ac) {
		String auth = ac.getRequest().getHeader(HttpHeader.AUTHORIZATION);
		if (Strings.isEmpty(auth)) {
			return null;
		}
		
		if (!Strings.startsWithIgnoreCase(auth, "Basic ")) {
			return null;
		}
		
		auth = Base64.decodeBase64String(auth.substring(6));

		int c = auth.indexOf(':');
		if (c < 0) {
			return null;
		}
		
		String username = auth.substring(6, c);
		String password = auth.substring(c + 1);
		return getUserFromNameAndPwd(username, password);
	}

	/**
	 * get user object from Authorization Header
	 * @param username user name
	 * @param password password
	 * @return user object
	 */
	protected Object getUserFromNameAndPwd(String username, String password) {
		return null;
	}

	//--------------------------------------------------------------
	protected Object getUserFromContext(ActionContext ac) {
		return ac.getSes().get(SES.USER);
	}

	/**
	 * save user to context
	 * @param ac action context
	 * @param user user object
	 * @param time login time
	 */
	public void saveUserToContext(ActionContext ac, Object user, Date time) {
		ac.getSes().put(SES.USER, user);
		if (time != null) {
			ac.getSes().put(SES.USER_LOGIN_TIME, user);
		}
	}

	public void removeUserFromContext(ActionContext ac) {
		ac.getSes().remove(SES.USER);
		ac.getSes().remove(SES.USER_LOGIN_TIME);
	}

	//------------------------------------------------------
	/**
	 * get user object from client
	 * @param ac action context
	 * @return user object
	 */
	protected Object getUserFromClient(ActionContext ac) {
		return getUserFromCookie(ac);
	}

	/**
	 * save user object to client
	 * @param ac action context
	 * @param user user object
	 */
	public void saveUserToClient(ActionContext ac, Object user) {
		saveUserToCookie(ac, user);
	}

	/**
	 * remove user object from client
	 * @param ac action context
	 */
	public void removeUserFromClient(ActionContext ac) {
		removeUserFromCookie(ac);
	}

	//-------------------------------------------------------------
	protected Integer getCookieAge(ActionContext ac, Object user) {
		return cookieAge;
	}
	
	protected String getCookieName(ActionContext ac) {
		String cn = cookieName;
		if (Strings.isEmpty(cn)) {
			cn = DEFAULT_COOKIE_NAME;
		}
		
		if (cookieSuffix && Strings.isNotEmpty(ac.getBase())) {
			cn += Strings.replaceChars(ac.getBase(), '/', '_').toUpperCase();
		}
		return cn;
	}
	
	protected String getCookiePath(ActionContext ac) {
		if (Strings.isNotEmpty(cookiePath)) {
			return cookiePath;
		}
		return ac.getBase() + '/';
	}

	protected void saveUserToCookie(ActionContext ac, Object user) {
		JsonSerializer js = Jsons.newJsonSerializer();
		js.setDateToMillis(true);

		String ticket = serializeUser(user);
		String eticket = encrypt(ticket);
		Integer cookieAge = getCookieAge(ac, user);
		
		Cookie c = new Cookie(getCookieName(ac), eticket);
		if (cookieAge != null) {
			c.setMaxAge(cookieAge);
		}
		c.setPath(getCookiePath(ac));

		ac.getResponse().addCookie(c);
	}

	protected void removeUserFromCookie(ActionContext ac) {
		HttpServlets.removeCookie(
			ac.getResponse(), 
			getCookieName(ac),
			getCookiePath(ac));
	}

	protected Object getUserFromCookie(ActionContext ac) {
		String n = getCookieName(ac);
		Cookie c = HttpServlets.getCookie(ac.getRequest(), n);
		if (c == null) {
			return null;
		}
		
		String ticket = c.getValue();
		try {
			ticket = decrypt(ticket);
			Object u = deserializeUser(ticket);
			return u;
		}
		catch (Throwable e) {
			log.warn("Invalid AUTH Cookie " + n + ": " + ticket + " - " + e.getMessage());
			if (log.isDebugEnabled()) {
				log.debug("Failed to decrypt AUTH Cookie", e);
			}
		}
		return null;
	}

	//------------------------------------------------------
	public String encrypt(String value) {
		return cryptor.encrypt(value);
	}
	
	public String decrypt(String value) {
		return cryptor.decrypt(value);
	}
	
}
