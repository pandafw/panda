package panda.wing.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.bind.json.JsonDeserializer;
import panda.bind.json.Jsons;
import panda.io.Settings;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Encrypts;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.servlet.HttpServlets;
import panda.wing.AppConstants;
import panda.wing.constant.AUTH;
import panda.wing.constant.COOKIE;
import panda.wing.constant.REQ;
import panda.wing.constant.SC;
import panda.wing.constant.SES;
import panda.wing.constant.VC;

public class AuthHelper {
	private static final Log log = Logs.getLog(AuthHelper.class);

	@IocInject
	protected Settings settings;

	/**
	 * secure user session time (s): 30m 
	 */
	@IocInject(value=AppConstants.PANDA_AUTH_SECURE_USER_AGE, required=false)
	protected long secureUserAge = 30 * 60;
	
	/**
	 * auth user type
	 */
	@IocInject(value=AppConstants.PANDA_AUTH_USER_TYPE, required=false)
	protected Class userType;

	/**
	 * encrypt key
	 */
	@IocInject(value=AppConstants.PANDA_SECRET_ENCRYPT_KEY, required=false)
	protected String encKey = Encrypts.DEFAULT_KEY;
	
	/**
	 * encrypt cipher
	 */
	@IocInject(value=AppConstants.PANDA_SECRET_ENCRYPT_CIPHER, required=false)
	protected String encCipher = Encrypts.DEFAULT_CIPHER;

	/**
	 * ticket cookie age
	 */
	@IocInject(value=AppConstants.PANDA_AUTH_TICKET_COOKIE_AGE, required=false)
	protected int cookieAge = COOKIE.AUTH_TICKET_AGE;

	/**
	 * @param u user
	 * @param p permission
	 * @return true if user has specified permission
	 */
	public boolean hasPermission(IUser u, String p) {
		if (u == null) {
			return false;
		}
		if (u instanceof IPermits) {
			return Collections.contains(((IPermits)u).getPermits(), p);
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
	 * @return the super user name
	 */
	public String getSuperUsername() {
		return settings.getProperty(SC.SUPER_USERNAME);
	}

	/**
	 * @return the user user password
	 */
	public String getSuperPassword() {
		return settings.getProperty(SC.SUPER_PASSWORD);
	}

	/**
	* @param username username
	* @param password password
	* @return true if username & password equals properties setting
	*/
	public boolean isSuperUser(String username, String password) {
		return (getSuperUsername().equals(username) && getSuperPassword().equals(password));
	}


	public long getLoginUserId(ActionContext context) {
		IUser user = getLoginUser(context);
		return user == null ? VC.SYSTEM_UID : user.getId();
	}

	/**
	 * getLoginUser
	 * @return user
	 */
	public IUser getLoginUser(ActionContext context) {
		return (IUser)context.getRequest().getAttribute(REQ.USER);
	}

	//------------------------------------------------------
	public String encrypt(String value) {
		return Encrypts.encrypt(value, encKey, encCipher);
	}
	
	public String decrypt(String value) {
		return Encrypts.decrypt(value, encKey, encCipher);
	}
	
	protected int getCookieAge(ActionContext ac, Object user) {
		return cookieAge;
	}
	
	/**
	 * setLoginUser
	 * @param user user
	 */
	public void setLoginUser(ActionContext context, Object user) {
		int ca = getCookieAge(context, user);
		setLoginUser(context, user, ca);
	}

	/**
	 * setLoginUser
	 * @param user user
	 */
	protected void setLoginUser(ActionContext context, Object user, int cookieAge) {
		if (user instanceof ILogin) {
			ILogin iu = (ILogin)user;
			iu.setLoginTime(System.currentTimeMillis());
			if (!Boolean.TRUE.equals(iu.getAutoLogin())) {
				cookieAge = -1;
			}
		}

		saveUserToCookie(context, user, cookieAge);
		saveUserToSession(context, user);
	}

	protected void saveUserToSession(ActionContext context, Object user) {
		context.getRequest().setAttribute(REQ.USER, user);
	}

	/**
	 * setLoginUser
	 * @param user user
	 */
	protected void saveUserToCookie(ActionContext context, Object user, int cookieAge) {
		String ticket = Jsons.toJson(user);
		String eticket = encrypt(ticket);
		String cookiePath = context.getServlet().getContextPath() + "/";
		
		Cookie c = new Cookie(COOKIE.AUTH_TICKET, eticket);
		if (cookieAge >= 0) {
			c.setMaxAge(cookieAge);
		}
		c.setPath(cookiePath);

		context.getResponse().addCookie(c);

		context.getRequest().setAttribute(REQ.USER, user);
	}

	/**
	 * removeLoginUser
	 */
	public void removeLoginUser(ActionContext context) {
		context.getReq().remove(REQ.USER);
		context.getSes().remove(SES.USER);

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
	public Object getTicketUser(ActionContext ac) {
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
	
	public boolean isSecureSessionUser(Object su) {
		if (su instanceof ILogin) {
			Long lt = ((ILogin)su).getLoginTime();
			if (lt != null) {
				return System.currentTimeMillis() - lt < secureUserAge * 1000;
			}
		}
		return false;
	}
}
