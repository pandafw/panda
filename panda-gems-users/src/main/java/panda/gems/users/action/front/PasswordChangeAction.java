package panda.gems.users.action.front;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.RES;
import panda.dao.entity.EntityDao;
import panda.gems.users.auth.AppAuthenticator;
import panda.gems.users.entity.User;
import panda.gems.users.util.PasswordHelper;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.ELValidate;
import panda.mvc.annotation.validate.RegexValidate;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.validator.Validators;
import panda.mvc.view.Views;

/**
 * PasswordChangeAction
 */
@At("/user/password/change")
@Auth(AUTH.SIGNIN)
@To(Views.SFTL)
public class PasswordChangeAction extends BaseAction {
	@IocInject
	private PasswordHelper pwHelper;

	@IocInject
	private AppAuthenticator auth;

	public static class Arg {
		private String opwd;
		private String npwd;
		private String cpwd;
		/**
		 * @return the opwd
		 */
		@RequiredValidate
		@ELValidate(el="action.isLoginPassword(top.value)", msgId=Validators.MSGID_PASSWORD_INCORRECT)
		public String getOpwd() {
			return opwd;
		}

		/**
		 * @param opwd the opwd to set
		 */
		public void setOpwd(String opwd) {
			this.opwd = Strings.stripToNull(opwd);
		}
		/**
		 * @return the npwd
		 */
		@RequiredValidate
		@StringValidate(minLength=6, maxLength=16) 
		@RegexValidate(regex="#(regex-password)", msgId=Validators.MSGID_PASSWORD)
		public String getNpwd() {
			return npwd;
		}

		/**
		 * @param npwd the npwd to set
		 */
		public void setNpwd(String npwd) {
			this.npwd = Strings.stripToNull(npwd);
		}

		/**
		 * @return the cpwd
		 */
		@RequiredValidate
		@ELValidate(el="top.value == top.parent.value.npwd", msgId=Validators.MSGID_PASSWORD_NOT_SAME)
		public String getCpwd() {
			return cpwd;
		}
		/**
		 * @param cpwd the cpwd to set
		 */
		public void setCpwd(String cpwd) {
			this.cpwd = Strings.stripToNull(cpwd);
		}
	}

	/**
	 * used by validate
	 * @param password old password
	 * @return true if old password is same as login password
	 */
	public boolean isLoginPassword(String password) {
		String hash = pwHelper.hashPassword(password);
		return Strings.equals(hash, assist().getLoginUserPassword());
	}
	
	@At("")
	@Redirect(toslash=true)
	public void input(@Param Arg arg) throws Exception {
	}
	
	@At
	@To(error=Views.SFTL_INPUT)
	public void update(@Param @VisitValidate Arg arg) throws Exception {
		EntityDao<User> dao = getDaoClient().getEntityDao(User.class);

		User lu = auth.getLoginUser(context);

		User nu = new User();
		nu.setPassword(pwHelper.hashPassword(arg.npwd));
		nu.setId(lu.getId());
		assist().setUpdatedByFields(nu);
		dao.updateIgnoreNull(nu);

		lu.setPassword(nu.getPassword());
		lu.setUpdatedAt(nu.getUpdatedAt());
		lu.setUpdatedBy(nu.getUpdatedBy());
		assist().setLoginUser(lu, DateTimes.getDate());

		addActionMessage(getText(RES.MESSAGE_SUCCESS));
	}

}
