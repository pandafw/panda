package panda.wing.auth;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.processor.ViewProcessor;
import panda.mvc.util.TextProvider;
import panda.wing.action.ActionRC;
import panda.wing.constant.REQ;

@IocBean(create="initialize")
public class AuthenticateProcessor extends ViewProcessor {
	private static final int CHECK_NONE = 0;
	private static final int CHECK_LOGIN = 1;
	private static final int CHECK_SECURE = 2;
	
	/**
	 * check level: none, login(default), secure
	 */
	private int check = CHECK_LOGIN;

	/**
	 * loginView
	 */
	private String loginView;
	
	/**
	 * secureView
	 */
	private String secureView;
	
	/**
	 * @param check the check to set
	 */
	public void setCheck(String check) {
		if ("login".equalsIgnoreCase(check)) {
			this.check = CHECK_LOGIN;
		}
		else if ("secure".equals(check)) {
			this.check = CHECK_SECURE;
		}
		else {
			this.check = CHECK_NONE;
		}
	}

	/**
	 * @param loginView the loginView to set
	 */
	public void setLoginView(String loginView) {
		this.loginView = loginView;
	}

	/**
	 * @param secureView the secureView to set
	 */
	public void setSecureView(String secureView) {
		this.secureView = secureView;
	}
	
	/**
	 * add error to action
	 * @param ac the action context
	 * @param id msgId
	 */
	protected void addActionError(ActionContext ac, String id) {
		TextProvider tp = ac.getText();
		String msg = tp.getText(id);
		ac.getActionAware().addError(msg);
	}
	
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
	
	protected boolean isSecureSessionUser(Object su) {
		return false;
	}
	
	protected List<String> getUserPermits(Object su) {
		return null;
	}

	protected void doView(ActionContext ac, String type) throws Throwable {
		View view = evalView(ac.getIoc(), type);
		if (view != null) {
			view.render(ac);
		}
	}

	public void process(ActionContext ac) throws Throwable {
		Object su = getSessionUser(ac);
		if (check < CHECK_LOGIN) {
			doNext(ac);
			return;
		}

		if (su == null) {
			addActionError(ac, ActionRC.ERROR_UNLOGIN);
			doView(ac, loginView);
			return;
		}
		
		if (check == CHECK_SECURE && !isSecureSessionUser(su)) {
			addActionError(ac, ActionRC.ERROR_UNSECURE);
			doView(ac, secureView);
			return;
		}

		AppAuthenticater aa = ac.getIoc().get(AppAuthenticater.class);
		if (aa.allow(getUserPermits(su), ac)) {
			doNext(ac);
			return;
		}

		ac.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
