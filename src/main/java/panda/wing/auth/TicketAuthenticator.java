package panda.wing.auth;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import panda.bind.json.JsonDeserializer;
import panda.ioc.annotation.IocInject;
import panda.lang.Encrypts;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.servlet.HttpServlets;
import panda.wing.AppConstants;
import panda.wing.constant.COOKIE;
import panda.wing.constant.REQ;

public class TicketAuthenticator extends UserAuthenticator {

	private static final Log log = Logs.getLog(TicketAuthenticator.class);

	/**
	 * secure user session time (s): 30m 
	 */
	@IocInject(value=AppConstants.PANDA_AUTH_SECURE_USER_AGE, required=false)
	protected long secureSessionAge = 30 * 60;
	
	/**
	 * auth user type
	 */
	@IocInject(value=AppConstants.PANDA_AUTH_USER_TYPE, required=false)
	protected Class userType; 

	@Override
	protected Object getSessionUser(ActionContext ac) {
		Object u = super.getSessionUser(ac);
		if (u == null && userType != null) {
			HttpServletRequest req = ac.getRequest();
			Cookie c = HttpServlets.getCookie(req, COOKIE.USER_TICKET);
			if (c != null) {
				String ticket = c.getValue();
				try {
					ticket = Encrypts.decrypt(ticket);
					JsonDeserializer jd = new JsonDeserializer();
					jd.setIgnoreMissingProperty(true);
					jd.setIgnoreReadonlyProperty(true);
					jd.setIgnoreNullProperty(true);
					u = jd.deserialize(ticket, userType);
					req.setAttribute(REQ.USER, u);
				}
				catch (Exception e) {
					log.warn("Incorrect " + COOKIE.USER_TICKET + ": " + ticket, e);
				}
			}
		}
		return u;
	}
	
	@Override
	protected boolean isSecureSessionUser(Object su) {
		if (su instanceof IUser) {
			Long lt = ((IUser)su).getLoginTime();
			if (lt != null) {
				return System.currentTimeMillis() - lt < secureSessionAge * 1000;
			}
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<String> getUserPermits(Object su) {
		if (su instanceof IUser) {
			return ((IUser)su).getGroupPermits();
		}
		return Collections.EMPTY_LIST;
	}
}
