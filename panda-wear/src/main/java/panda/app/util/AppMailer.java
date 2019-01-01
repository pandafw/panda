package panda.app.util;

import panda.app.constant.SET;
import panda.cast.Castors;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.mvc.ActionContext;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.net.mail.Email;
import panda.net.mail.EmailException;
import panda.net.mail.JavaMailClient;
import panda.net.mail.MailClient;
import panda.net.mail.SmtpMailClient;

@IocBean(scope=Scope.REQUEST)
public class AppMailer {
	@IocInject
	protected ActionContext context;

	@IocInject
	protected AppSettings settings;

	@IocInject
	protected FreemarkerHelper freemarker;
	
	//-------------------------------------------------------------
	// Template mail
	//
	/**
	 * send email
	 * @param email email
	 * @param tpl template name
	 * @param model data
	 * @throws EmailException if an email error occurs
	 */
	public void sendTemplateMail(Email email, String tpl, Object model) throws EmailException {
		String subject = "";
		String content;
		try {
			content = freemarker.execTemplate(tpl, model);
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
		email.setMessage(content);

		sendMail(email);
	}

	/**
	 * send email
	 * @param email email
	 * @param tpl template name
	 * @throws EmailException if an email error occurs
	 */
	public void sendTemplateMail(Email email, String tpl) throws EmailException {
		sendTemplateMail(email, tpl, null);
	}
	
	/**
	 * send email
	 * @param email email
	 * @throws EmailException if an email error occurs
	 */
	public void sendMail(Email email) throws EmailException {
		if (email.getSender() == null) {
			String s = getMailSetting(SET.MAIL_SENDER, null);
			if (Strings.isNotEmpty(s)) {
				email.setSender(s);
			}
		}

		if (email.getFrom() == null) {
			email.setFrom(getMailSetting(SET.MAIL_FROM, null));
		}

		String charset = getMailSetting(SET.MAIL_CHARSET, Charsets.UTF_8);
		if (Strings.isNotEmpty(charset)) {
			email.setCharset(charset);
		}

		MailClient client = getMailClient();
		String helo = getMailSetting(SET.MAIL_SMTP_HELO, null);
		if (Strings.isNotEmpty(helo)) {
			client.setHelo(helo);
		}
		
		String host = getMailSetting(SET.MAIL_SMTP_HOST, null);
		if (Strings.isNotEmpty(host)) {
			client.setHost(host);
		}
		
		int port = getMailSettingAsInt(SET.MAIL_SMTP_PORT, 0);
		if (port > 0) {
			client.setPort(port);
		}

		client.setDebug(getMailSettingAsBoolean(SET.MAIL_DEBUG, false));
		client.setSsl(getMailSettingAsBoolean(SET.MAIL_SMTP_SSL, false));
		client.setStartTls(getMailSettingAsBoolean(SET.MAIL_SMTP_STARTTLS, true));

		int timeout = getMailSettingAsInt(SET.MAIL_SMTP_CONN_TIMEOUT, 0);
		if (timeout > 0) {
			client.setConnectTimeout(timeout);
		}

		timeout = getMailSettingAsInt(SET.MAIL_SMTP_SEND_TIMEOUT, 0);
		if (timeout > 0) {
			client.setDefaultTimeout(timeout);
		}

		String username = getMailSetting(SET.MAIL_SMTP_USER, null);
		if (Strings.isNotEmpty(username)) {
			client.setUsername(username);
			client.setPassword(getMailSetting(SET.MAIL_SMTP_PASSWORD, ""));
		}
		
		String bounce = getMailSetting(SET.MAIL_SMTP_BOUNCE, null);
		if (Strings.isNotEmpty(bounce)) {
			email.setSender(bounce);
		}

		client.send(email);
	}
	
	protected MailClient getMailClient() {
		String c = getMailSetting(SET.MAIL_CLIENT, null);
		if (Strings.equalsIgnoreCase("java", c)) {
			return new JavaMailClient();
		}
		
		if (Strings.equalsIgnoreCase("smtp", c)) {
			return new SmtpMailClient();
		}

		return Systems.IS_OS_APPENGINE ? new JavaMailClient() : new SmtpMailClient();
	}
	
	protected String getMailSetting(String key, String def) {
		String s = context.getText().getText(key, null);
		if (Strings.isNotEmpty(s)) {
			return s;
		}
		return settings.getProperty(key, def);
	}
	
	protected boolean getMailSettingAsBoolean(String key, boolean def) {
		String v = Strings.stripToNull(getMailSetting(key, null));
		if (Strings.isEmpty(v)) {
			return def;
		}
		return (Boolean)Castors.scast(v, boolean.class);
	}
	
	protected int getMailSettingAsInt(String key, int def) {
		String v = Strings.stripToNull(getMailSetting(key, null));
		if (Strings.isEmpty(v)) {
			return def;
		}
		return (Integer)Castors.scast(v, int.class);
	}

}
