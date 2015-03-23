package panda.wing.auth;

import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.processor.ViewProcessor;
import panda.mvc.util.TextProvider;
import panda.wing.AppConstants;
import panda.wing.action.ActionRC;

@IocBean
public class UserAuthenticateProcessor extends ViewProcessor {
	@IocInject(value=AppConstants.PANDA_AUTH_UNLOGIN_VIEW, required=false)
	private String unloginView;
	
	@IocInject(value=AppConstants.PANDA_AUTH_UNSECURE_VIEW, required=false)
	private String unsecureView;
	
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
	

	protected void doView(ActionContext ac, String type) throws Throwable {
		View view = evalView(ac.getIoc(), type);
		if (view != null) {
			view.render(ac);
		}
	}

	public void process(ActionContext ac) throws Throwable {
		UserAuthenticator aa = ac.getIoc().get(UserAuthenticator.class);
		int r = aa.authenticate(ac);
		if (r == UserAuthenticator.OK) {
			doNext(ac);
			return;
		}
		if (r == UserAuthenticator.UNLOGIN) {
			addActionError(ac, ActionRC.ERROR_UNLOGIN);
			if (Strings.isNotEmpty(unloginView)) {
				doView(ac, unloginView);
				return;
			}
		}
		if (r == UserAuthenticator.UNSECURE) {
			addActionError(ac, ActionRC.ERROR_UNSECURE);
			if (Strings.isNotEmpty(unsecureView)) {
				doView(ac, unsecureView);
				return;
			}
		}

		ac.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}
