package panda.gems.users.action.front;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import panda.app.action.BaseAction;
import panda.app.constant.RES;
import panda.bind.json.Jsons;
import panda.dao.Dao;
import panda.dao.entity.EntityDao;
import panda.gems.users.T;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.gems.users.util.PasswordHelper;
import panda.gems.users.util.UserMailer;
import panda.ioc.annotation.IocInject;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.EmailValidate;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.StringValidate;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.view.Views;
import panda.net.mail.EmailException;

@At("/user/password/reset")
@To(Views.SFTL)
public class PasswordResetAction extends BaseAction {
	private static final long DURATION = DateTimes.MS_HOUR * 2;
	
	@IocInject
	private PasswordHelper pwdHelper;

	@IocInject
	private UserMailer mailer;
	
	public static class Token {
		private Long uid;
		private Date time;
		
		public Token() {
		}

		public Token(Long uid, Date time) {
			super();
			this.uid = uid;
			this.time = time;
		}
		public Long getUid() {
			return uid;
		}
		public void setUid(Long uid) {
			this.uid = uid;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
	}
	
	public static class Arg {
		private String email;
	
		/**
		 * @return the email
		 */
		@RequiredValidate
		@StringValidate(maxLength=100)
		@EmailValidate
		public String getEmail() {
			return email;
		}

		/**
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = Strings.stripToLowerNull(email);
		}
	}
	
	@At("")
	@Redirect(toslash=true)
	public void input(@Param Arg arg) {
	}

	@At
	@To(error=Views.SFTL_INPUT)
	public Object send(@Param @VisitValidate Arg arg) {
		
		EntityDao<User> dao = getDaoClient().getEntityDao(User.class);

		UserQuery uq = new UserQuery();

		uq.email().eq(arg.email);

		User user = dao.fetch(uq);
		if (user == null) {
			addActionError(getText("error-input"));
			return Views.sftlInput(context);
		}

		Token t = new Token(user.getId(), DateTimes.getDate());
		
		String sk = Jsons.toJson(t);
		String ek = assist().encrypt(sk);
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("name", user.getName());
		m.put("token", ek);
		try {
			mailer.sendTemplateMail(user, T.MAIL_PASSWORD_SEND, m);
		}
		catch (EmailException e) {
			String msg = getText(RES.ERROR_SENDMAIL, RES.ERROR_SENDMAIL, user.getEmail());
			addActionError(msg);
			return Views.sftlInput(context);
		}

		addActionMessage(getText("message-sent", "", user.getEmail()));
		return null;
	}

	@At
	public Object reset(@Param("token") String token) {
		Token t = parseToken(token);
		if (t == null) {
			addActionError(getText("error-link"));
			return null;
		}

		if (DateTimes.getDate().getTime() - t.getTime().getTime() > DURATION) {
			addActionError(getText("error-time"));
			return null;
		}
		
		String np = String.valueOf(Randoms.randInt(111111, 999999));
		
		Dao dao = getDaoClient().getDao();

		User user = dao.fetch(User.class, t.getUid());
		if (user == null) {
			addActionError(getText("error-link"));
			return null;
		}
		
		User u = new User();
		u.setId(t.getUid());
		u.setPassword(pwdHelper.hashPassword(np));
		u.setUpdatedAt(DateTimes.getDate());
		u.setUpdatedBy(t.getUid());
		
		int i = dao.updateIgnoreNull(u);
		if (i < 1) {
			addActionError(getText("error-link"));
			return null;
		}

		user.setPassword(np);
		try {
			mailer.sendTemplateMail(user, T.MAIL_PASSWORD_RESET, user);
		}
		catch (EmailException e) {
			String msg = getText(RES.ERROR_SENDMAIL, RES.ERROR_SENDMAIL, user.getEmail());
			addActionError(msg);
			return null;
		}

		addActionMessage(getText("message-reset", "", user.getEmail()));
		return token;
	}

	private Token parseToken(String key) {
		if (Strings.isEmpty(key)) {
			return null;
		}
		
		Token k = null;
		String raw = assist().decrypt(key);
		try {
			k = Jsons.fromJson(raw, Token.class);
		} 
		catch (Exception e) {
			return null;
		}
		
		if (k.getUid() == null || k.getTime() == null) {
			return null;
		}

		return k;
	}

}
