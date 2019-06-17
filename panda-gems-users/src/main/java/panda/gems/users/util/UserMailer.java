package panda.gems.users.util;

import panda.app.util.AppMailer;
import panda.gems.users.entity.User;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.net.mail.Email;
import panda.net.mail.EmailException;
import panda.net.mail.HtmlEmail;

@IocBean(scope=Scope.REQUEST)
public class UserMailer extends AppMailer {
	/**
	 * send email
	 * @param user user 
	 * @param tpl template name
	 * @param context context object
	 * @throws EmailException  email error
	 */
	public void sendTemplateMail(User user, String tpl, Object context) throws EmailException {
		Email email = new HtmlEmail();

		email.addTo(user.getEmail(), user.getName());
		
		sendTemplateMail(email, tpl, context);
	}

}
