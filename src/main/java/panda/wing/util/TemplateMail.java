package panda.wing.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import panda.io.Settings;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.net.SendMail;
import panda.wing.constant.SC;


@IocBean(scope=Scope.REQUEST)
public class TemplateMail {
	private static final Log log = Logs.getLog(TemplateMail.class);
	
	@IocInject
	private Settings settings;
	
	@IocInject
	protected FreemarkerHelper freemarker;

	//-------------------------------------------------------------
	public void addEmailCc(Email email, String cc) throws EmailException {
		for (String s : splitMail(cc)) {
			email.addCc(s);
		}
	}
	
	public void addEmailBcc(Email email, String bcc) throws EmailException {
		for (String s : splitMail(bcc)) {
			email.addBcc(s);
		}
	}
	
	public Collection<String> splitMail(String str) {
		Set<String> ms = new HashSet<String>();
		
		String[] ss = Strings.split(str, " \r\n\t,;\u3000");
		if (ss != null) {
			ms.addAll(Arrays.asList(ss));
		}

		return ms; 
	}


	/**
	 * send email
	 * @param email email
	 * @param name template name
	 * @param context context
	 */
	public void sendTemplateMail(Email email, String name, Object context) throws EmailException {
		String subject = "";
		String content;
		try {
			content = freemarker.execTemplate(name, context);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}

		int cr = content.indexOf('\n');
		if (cr > 0) {
			subject = Strings.strip(content.substring(0, cr));
			content = content.substring(cr + 1);
		}

		email.setSubject(subject);
		if (email instanceof HtmlEmail) {
			((HtmlEmail)email).setHtmlMsg(content);
		}
		else {
			email.setMsg(content);
		}

		sendMail(email);
	}

	/**
	 * send email
	 * @param email email
	 * @param name template name
	 */
	public void sendTemplateMail(Email email, String name) throws EmailException {
		sendTemplateMail(email, name, null);
	}
	
	/**
	 * send email
	 * @param email email
	 * @throws EmailException if an email error occurs
	 */
	public void sendMail(Email email) throws EmailException {
		try {
			if (settings.getPropertyAsBoolean(SC.MAIL_DEBUG, false)) {
				email.setDebug(true);
			}

			String charset = settings.getProperty(SC.MAIL_CHARSET, Charsets.UTF_8);
			if (Strings.isNotEmpty(charset)) {
				email.setCharset(charset);
			}

			email.setFrom(settings.getProperty(SC.MAIL_FROM_MAIL), settings.getProperty(SC.MAIL_FROM_NAME));

			String host = settings.getProperty(SC.MAIL_SMTP_HOST, "");
			if (Strings.isNotEmpty(host)) {
				email.setHostName(host);
			}
			
			int port = settings.getPropertyAsInt(SC.MAIL_SMTP_PORT, 0);
			if (port > 0) {
				email.setSmtpPort(port);
			}

			if (settings.getPropertyAsBoolean(SC.MAIL_SMTP_SSL, false)) {
				email.setSSL(true);
				if (port > 0) {
					email.setSslSmtpPort(String.valueOf(port));
				}
			}
			
			email.setTLS(settings.getPropertyAsBoolean(SC.MAIL_SMTP_TLS, false));
			
			String username = settings.getProperty(SC.MAIL_SMTP_USER, "");
			if (Strings.isNotEmpty(username)) {
				email.setAuthentication(username, settings.getProperty(SC.MAIL_SMTP_PASSWORD, ""));
			}
			
			String bounce = settings.getProperty(SC.MAIL_SMTP_BOUNCE, "");
			if (Strings.isNotEmpty(bounce)) {
				email.setBounceAddress(bounce);
			}

			SendMail.send(email);
		}
		catch (EmailException e) {
			log.warn("send mail failed!", e);
			throw e;
		}
	}
}

