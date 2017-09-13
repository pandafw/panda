package panda.app.auth;

import java.util.List;

import javax.servlet.http.Cookie;

import panda.app.AppConstants;
import panda.app.constant.AUTH;
import panda.app.constant.REQ;
import panda.app.constant.SES;
import panda.app.constant.VAL;
import panda.bind.json.JsonDeserializer;
import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;
import panda.lang.crypto.Cryptor;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.net.http.HttpHeader;
import panda.servlet.HttpServlets;

@IocBean(type=UserAuthenticator.class)
public class AppAuthenticator extends UserAuthenticator {
	
	private static final Log log = Logs.getLog(AppAuthenticator.class);

	public static final String DEFAULT_COOKIE_NAME = "WW_TICKET";
	
	@IocInject
	protected Cryptor cryptor;
	
	/**
	 * secure user session time (s): 30m 
	 */
	@IocInject(value=AppConstants.AUTH_SECURE_USER_AGE, required=false)
	protected long secureUserAge = 30 * 60;
	
	/**
	 * auth user type
	 */
	@IocInject(value=AppConstants.AUTH_USER_TYPE, required=false)
	protected Class userType;

	/**
	 * ticket parameter name
	 */
	@IocInject(value=AppConstants.AUTH_TICKET_PARAM_NAME, required=false)
	protected String paramName = "_ticket_";

	/**
	 * ticket cookie name (default: WW_TICKET + CONTEXT_PATH.replace('/', '_'))
	 */
	@IocInject(value=AppConstants.AUTH_TICKET_COOKIE_NAME, required=false)
	protected String cookieName;

	/**
	 * ticket cookie path (default: CONTEXT_PATH)
	 */
	@IocInject(value=AppConstants.AUTH_TICKET_COOKIE_PATH, required=false)
	protected String cookiePath;

	/**
	 * ticket cookie age (default: 60 * 60 * 24 * 7days)
	 */
	@IocInject(value=AppConstants.AUTH_TICKET_COOKIE_AGE, required=false)
	protected Integer cookieAge = 60 * 60 * 24 * 7;

	//------------------------------------------------------------------------
	@Override
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

	@Override
	public boolean isSecureAuthenticatedUser(Object su) {
		if (su instanceof ILogin) {
			Long lt = ((ILogin)su).getLoginTime();
			if (lt != null) {
				return System.currentTimeMillis() - lt < secureUserAge * 1000;
			}
		}
		return false;
	}

	//------------------------------------------------------
	@Override
	protected Object getAuthenticatedUser(ActionContext ac) {
		Object u = getUserFromParameter(ac);
		if (u != null) {
			saveUserToContext(ac, u);
			return u;
		}

		u = getUserFromContext(ac);
		if (u != null) {
			return u;
		}

		u = getUserFromClient(ac);
		if (u != null) {
			saveUserToContext(ac, u);
		}
		return u;
	}

	/**
	 * setAuthenticatedUser
	 * @param ac action context
	 * @param user user object
	 */
	public void setAuthenticatedUser(ActionContext ac, Object user) {
		saveUserToContext(ac, user);
		saveUserToClient(ac, user);
	}

	/**
	 * removeAuthenticatedUser
	 * @param ac action context
	 */
	public void removeAuthenticatedUser(ActionContext ac) {
		removeUserFromContext(ac);
		removeUserFromClient(ac);
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
			return Collections.contains(((IPermission)u).getPermissions(), p);
		}
		if (u instanceof IRole) {
			return Strings.equals(((IRole)u).getRole(), p);
		}
		
		return false;
	}
	
	/**
	 * @param u user
	 * @return true - if the user is admin
	 */
	public boolean isAdminUser(IUser u) {
		return hasPermission(u, AUTH.ADMIN);
	}

	/**
	 * @param u user
	 * @return true - if the user is super
	 */
	public boolean isSuperUser(IUser u) {
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
	 * get Login User From Request Attributes
	 * @param ac action context
	 * @return user object
	 */
	public IUser getLoginUser(ActionContext ac) {
		return (IUser)ac.getRequest().getAttribute(REQ.USER);
	}

	//------------------------------------------------------
	protected void saveUserToContext(ActionContext ac, Object user) {
		ac.getRequest().setAttribute(REQ.USER, user);
	}

	protected void removeUserFromContext(ActionContext ac) {
		ac.getReq().remove(REQ.USER);
		ac.getSes().remove(SES.USER);
	}

	//-------------------------------------------------------------
	// overrideable method 
	//
	protected Integer getCookieAge(ActionContext ac, Object user) {
		if (user instanceof ILogin) {
			ILogin iu = (ILogin)user;
			iu.setLoginTime(System.currentTimeMillis());
			if (!Boolean.TRUE.equals(iu.getAutoLogin())) {
				// expired when browser close
				return -1;
			}
		}
		return cookieAge;
	}
	
	protected String getCookieName(ActionContext ac) {
		if (Strings.isNotEmpty(cookieName)) {
			return cookieName;
		}
		
		if (Strings.isEmpty(ac.getBase())) {
			return DEFAULT_COOKIE_NAME;
		}
		return DEFAULT_COOKIE_NAME + Strings.replaceChars(ac.getBase(), '/', '_').toUpperCase();
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

		ac.getRequest().setAttribute(REQ.USER, user);
	}

	protected void removeUserFromCookie(ActionContext ac) {
		HttpServlets.removeCookie(
			ac.getResponse(), 
			getCookieName(ac),
			getCookiePath(ac));
	}

	protected Object getUserFromCookie(ActionContext ac) {
		if (userType == null) {
			return null;
		}
		
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
			log.warn("Invalid AUTH Cookie " + n + ": " + ticket, e);
		}
		return null;
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
		if (userType == null) {
			return null;
		}
		
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
			log.warn("Invalid AUTH Parameter " + paramName + ": " + ticket, e);
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
	 * @param username 
	 * @return user object
	 */
	protected Object getUserFromNameAndPwd(String username, String password) {
		return null;
	}
	
	//------------------------------------------------------
	/**
	 * save user object to client
	 * @param ac action context
	 * @param user user object
	 */
	protected void saveUserToClient(ActionContext ac, Object user) {
		saveUserToCookie(ac, user);
	}

	/**
	 * get user object from client
	 * @param ac action context
	 * @return user object
	 */
	protected Object getUserFromClient(ActionContext ac) {
		return getUserFromCookie(ac);
	}

	/**
	 * get user object from client
	 * @param ac action context
	 */
	protected void removeUserFromClient(ActionContext ac) {
		removeUserFromCookie(ac);
	}

	//------------------------------------------------------
	public String encrypt(String value) {
		return cryptor.encrypt(value);
	}
	
	public String decrypt(String value) {
		return cryptor.decrypt(value);
	}
	
	protected String serializeUser(Object user) {
		JsonSerializer js = Jsons.newJsonSerializer();
		js.setDateToMillis(true);
		return js.serialize(user);
	}
	
	protected Object deserializeUser(String ticket) {
		if (userType == null) {
			return null;
		}
		
		JsonDeserializer jd = new JsonDeserializer();
		jd.setIgnoreMissingProperty(true);
		jd.setIgnoreReadonlyProperty(true);
		jd.setIgnoreNullProperty(true);
		return jd.deserialize(ticket, userType);
	}
}
