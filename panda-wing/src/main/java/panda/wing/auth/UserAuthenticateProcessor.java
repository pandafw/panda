package panda.wing.auth;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.View;
import panda.mvc.processor.AbstractProcessor;
import panda.mvc.util.TextProvider;
import panda.mvc.view.Views;
import panda.net.URLBuilder;
import panda.net.URLHelper;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServlets;
import panda.wing.AppConstants;
import panda.wing.constant.RC;

@IocBean
public class UserAuthenticateProcessor extends AbstractProcessor {
	@IocInject(value=AppConstants.AUTH_UNLOGIN_VIEW, required=false)
	private String unloginView;
	
	@IocInject(value=AppConstants.AUTH_UNSECURE_VIEW, required=false)
	private String unsecureView;
	
	@IocInject(value=AppConstants.AUTH_UNLOGIN_URL, required=false)
	private String unloginUrl;
	
	@IocInject(value=AppConstants.AUTH_UNSECURE_URL, required=false)
	private String unsecureUrl;
	
	/**
	 * add error to action
	 * @param ac the action context
	 * @param id msgId
	 */
	protected void addActionError(ActionContext ac, String id) {
		TextProvider tp = ac.getText();
		String msg = tp.getText(id, id);
		ac.getActionAlert().addError(msg);
	}
	

	protected void doView(ActionContext ac, String type) {
		View view = Views.evalView(ac.getIoc(), type);
		if (view != null) {
			view.render(ac);
		}
	}

	protected String getRedirectURL(ActionContext ac) {
		String uri = ac.getBase() + ac.getPath();
		String query = HttpServlets.getRequestQueryString(ac.getRequest());
		
		URLBuilder ub = new URLBuilder();
		ub.setPath(uri);
		ub.setQuery(query);
		String url = ub.build();

		String red = URLHelper.encodeURL(url);
		return red;
	}
	
	@Override
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
				String red = getRedirectURL(ac);
				String url = Mvcs.translate(ac, unloginUrl, red);
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
				String red = getRedirectURL(ac);
				String url = Mvcs.translate(ac, unsecureUrl, red);
				HttpServlets.sendRedirect(ac.getResponse(), url);
				return;
			}
		}

		HttpServlets.sendError(ac.getResponse(), HttpStatus.SC_FORBIDDEN);
	}
}
