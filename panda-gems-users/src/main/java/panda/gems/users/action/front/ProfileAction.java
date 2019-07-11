package panda.gems.users.action.front;

import java.util.List;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.RES;
import panda.dao.entity.EntityDao;
import panda.gems.users.T;
import panda.gems.users.auth.AppAuthenticator;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.gems.users.util.UserMailer;
import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.view.Views;
import panda.net.mail.EmailException;

@At("/user/profile")
@Auth(AUTH.SIGNIN)
@To(value=Views.SFTL, error=Views.SFTL_INPUT)
public class ProfileAction extends BaseAction {

	@IocInject
	private UserMailer mailer;

	@IocInject
	private AppAuthenticator auth;

	/**
	 * public for getText(...)
	 * @return the type
	 */
	public Class<?> getT() {
		return User.class;
	}
	
	/**
	 * check email
	 * @param email the email
	 * @return true/false
	 */
	protected boolean checkEmail(String email) {
		User c = auth.getLoginUser(context);
		if (c.getEmail().equals(email)) {
			return true;
		}

		EntityDao<User> udao = getDaoClient().getEntityDao(User.class);

		UserQuery uq = new UserQuery();
		uq.email().eq(email);

		List<User> list = udao.select(uq);

		if (list.size() > 0) {
			addFieldError("email", getText("error-email-is-used"));
			return false;
		}

		return true;
	}
	

	/**
	 * input
	 * @param user the input user data
	 * @return user object
	 */
	@At
	public Object input(@Param User user) {
		User lu = auth.getLoginUser(context);
		if (user == null) {
			user = lu;
		}
		else {
			user.copy(lu);
		}
		return user;
	}
	
	/**
	 * confirm
	 * @param user the input user data
	 * @return user object or view
	 */
	@At
	public Object confirm(@Param
			@RequiredValidate(fields={ "name", "email" })
			@VisitValidate
			User user) {
		if (!checkEmail(user.getEmail())) {
			context.setResult(user);
			return Views.sftlInput(context);
		}

		addActionConfirm(getText("message-confirm"));
		return user;
	}

	/**
	 * execute
	 * @param user the input user data
	 * @return view or user object
	 */
	@At
	public Object execute(final @Param
			@RequiredValidate(fields={ "name", "email" })
			@VisitValidate
		User user) {

		if (!checkEmail(user.getEmail())) {
			context.setResult(user);
			return Views.sftlInput(context);
		}

		final User lu = auth.getLoginUser(context);

		final EntityDao<User> udao = getDaoClient().getEntityDao(User.class);
		udao.exec(new Runnable() {
			public void run() {
				user.setId(lu.getId());
				assist().setUpdatedByFields(user);
				udao.updateIgnoreNull(user);

				if (!lu.getEmail().equals(user.getEmail())) {
					try {
						mailer.sendTemplateMail(user, T.MAIL_PROFILE_UPDATE, user);
					}
					catch (EmailException e) {
						String msg = getText(RES.ERROR_SENDMAIL, RES.ERROR_SENDMAIL, user);
						addActionWarning(msg);
					}
				}
			}
		});

		if (hasActionErrors()) {
			context.setResult(user);
			return Views.sftlInput(context);
		}

		user.setPassword(lu.getPassword());
		assist().setLoginUser(user);

		addActionMessage(getText(RES.MESSAGE_SUCCESS));
		return user;
	}
}
