package panda.wing.auth;

import java.util.Collections;
import java.util.List;

import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;

public class TicketAuthenticator extends UserAuthenticator {

	@IocInject
	protected AuthHelper authHelper;
	
	@Override
	protected Object getSessionUser(ActionContext ac) {
		Object u = super.getSessionUser(ac);
		if (u != null) {
			return u;
		}
		return authHelper.getTicketUser(ac);
	}
	
	@Override
	protected boolean isSecureSessionUser(Object su) {
		return authHelper.isSecureSessionUser(su);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<String> getUserPermits(Object su) {
		if (su instanceof IUser) {
			return ((IUser)su).getPermits();
		}
		return Collections.EMPTY_LIST;
	}
}
