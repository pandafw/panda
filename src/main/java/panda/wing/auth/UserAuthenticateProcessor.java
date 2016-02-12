package panda.wing.auth;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.View;
import panda.mvc.processor.ViewProcessor;
import panda.mvc.util.TextProvider;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServlets;
import panda.wing.AppConstants;
import panda.wing.constant.RC;

@IocBean
public class UserAuthenticateProcessor extends ViewProcessor {
	@IocInject(value=AppConstants.PANDA_AUTH_UNLOGIN_VIEW, required=false)
	private String unloginView;
	
	@IocInject(value=AppConstants.PANDA_AUTH_UNSECURE_VIEW, required=false)
	private String unsecureView;
	
	@IocInject(value=AppConstants.PANDA_AUTH_UNLOGIN_URL, required=false)
	private String unloginUrl;
	
	@IocInject(value=AppConstants.PANDA_AUTH_UNSECURE_URL, required=false)
	private String unsecureUrl;
	
	/**
	 * add error to action
	 * @param ac the action context
	 * @param id msgId
	 */
	protected void addActionError(ActionContext ac, String id) {
		TextProvider tp = ac.getText();
		String msg = tp.getText(id);
		ac.getActionAlert().addError(msg);
	}
	

	protected void doView(ActionContext ac, String type) {
		View view = evalView(ac.getIoc(), type);
		if (view != null) {
			view.render(ac);
		}
	}

	public void process(ActionContext ac) {
		UserAuthenticator aa = ac.getIoc().get(UserAuthenticator.class);
		int r = aa.authenticate(ac);
		if (r <= UserAuthenticator.OK) {
			doNext(ac);
			return;
		}

		if (r == UserAuthenticator.UNLOGIN) {
			addActionError(ac, RC.ERROR_UNLOGIN);
			if (Strings.isNotEmpty(unloginView)) {
				doView(ac, unloginView);
				return;
			}
			if (Strings.isNotEmpty(unloginUrl)) {
				String url = Mvcs.translate(unloginUrl, ac);
				HttpServlets.sendRedirect(ac.getResponse(), url);
				return;
			}
		}
		if (r == UserAuthenticator.UNSECURE) {
			addActionError(ac, RC.ERROR_UNSECURE);
			if (Strings.isNotEmpty(unsecureView)) {
				doView(ac, unsecureView);
				return;
			}
			if (Strings.isNotEmpty(unsecureUrl)) {
				String url = Mvcs.translate(unsecureUrl, ac);
				HttpServlets.sendRedirect(ac.getResponse(), url);
				return;
			}
		}

		HttpServlets.sendError(ac.getResponse(), HttpStatus.SC_FORBIDDEN);
	}
}
