package panda.wing.auth;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.bind.json.JsonDeserializer;
import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.crypto.Encrypts;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.servlet.HttpServlets;
import panda.wing.AppConstants;
import panda.wing.constant.AUTH;
import panda.wing.constant.COOKIE;
import panda.wing.constant.REQ;
import panda.wing.constant.SES;
import panda.wing.constant.VC;

public class AppAuthenticator extends UserAuthenticator {
	
	private static final Log log = Logs.getLog(AppAuthenticator.class);

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
	 * encrypt key
	 */
	@IocInject(value=AppConstants.AUTH_SECRET_KEY, required=false)
	protected String secret = Encrypts.DEFAULT_KEY;
	
	/**
	 * encrypt cipher
	 */
	@IocInject(value=AppConstants.AUTH_SECRET_CIPHER, required=false)
	protected String cipher = Encrypts.DEFAULT_CIPHER;

	/**
	 * ticket cookie age
	 */
	@IocInject(value=AppConstants.AUTH_TICKET_COOKIE_AGE, required=false)
	protected Integer cookieAge = COOKIE.AUTH_TICKET_AGE;

	//------------------------------------------------------------------------
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

	@Override
	protected Object getAuthenticatedUser(ActionContext ac) {
		Object u = super.getAuthenticatedUser(ac);
		if (u != null) {
			return u;
		}

		u = getUserFromClient(ac);
		saveUserToContext(ac, u);
		return u;
	}
	
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

	//------------------------------------------------------
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
		return user == null ? VC.SYSTEM_UID : user.getId();
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
	public String encrypt(String value) {
		return Encrypts.encrypt(value, secret, cipher);
	}
	
	public String decrypt(String value) {
		return Encrypts.decrypt(value, secret, cipher);
	}
	
	//------------------------------------------------------
	protected void saveUserToContext(ActionContext ac, Object user) {
		ac.getRequest().setAttribute(REQ.USER, user);
	}

	protected void removeUserFromContext(ActionContext ac) {
		ac.getReq().remove(REQ.USER);
		ac.getSes().remove(SES.USER);
	}

	//------------------------------------------------------
	/**
	 * override method 
	 * @param ac action context
	 * @param user user object
	 * @return cookie age
	 */
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
	
	/**
	 * saveUserToCookie
	 * @param user user
	 */
	protected void saveUserToCookie(ActionContext ac, Object user) {
		JsonSerializer js = Jsons.newJsonSerializer();
		js.setDateToMillis(true);

		String ticket = js.serialize(user);
		String eticket = encrypt(ticket);
		String cookiePath = ac.getServlet().getContextPath() + "/";
		Integer cookieAge = getCookieAge(ac, user);
		
		Cookie c = new Cookie(COOKIE.AUTH_TICKET, eticket);
		if (cookieAge != null) {
			c.setMaxAge(cookieAge);
		}
		c.setPath(cookiePath);

		ac.getResponse().addCookie(c);

		ac.getRequest().setAttribute(REQ.USER, user);
	}

	protected void removeUserFromCookie(ActionContext context) {
		HttpServletResponse res = context.getResponse();
		String cookiePath = context.getBase() + "/";
		
		HttpServlets.removeCookie(
			res, 
			COOKIE.AUTH_TICKET,
			cookiePath);
	}

	/**
	 * get user object from cookie
	 * @param ac action context
	 * @return user object
	 */
	protected Object getUserFromCookie(ActionContext ac) {
		if (userType == null) {
			return null;
		}
		
		HttpServletRequest req = ac.getRequest();
		Cookie c = HttpServlets.getCookie(req, COOKIE.AUTH_TICKET);
		if (c == null) {
			return null;
		}
		
		String ticket = c.getValue();
		try {
			ticket = decrypt(ticket);
			JsonDeserializer jd = new JsonDeserializer();
			jd.setIgnoreMissingProperty(true);
			jd.setIgnoreReadonlyProperty(true);
			jd.setIgnoreNullProperty(true);
			Object u = jd.deserialize(ticket, userType);
			return u;
		}
		catch (Exception e) {
			log.warn("Incorrect " + COOKIE.AUTH_TICKET + ": " + ticket, e);
		}
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
}
