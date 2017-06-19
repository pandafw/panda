package panda.app.action.tool;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.net.mail.Email;
import panda.net.mail.EmailAddress;
import panda.net.mail.EmailClient;


@At("${super_path}/sendmail")
@Auth(AUTH.SUPER)
@To(value=View.SFTL, error=View.SFTL)
public class SendMailAction extends AbstractAction {
	private static final Log log = Logs.getLog(SendMailAction.class);
	
	public static class Arg {
		private String host;
		private Integer port;
		private String username;
		private String password;
		private Integer connTimeout;
		private Integer readTimeout;
		private String dkimSelector;
		private String dkimPrivateKey;
		private EmailAddress from;
		private EmailAddress to;
		private EmailAddress cc;
		private String subject;
		private String message;
		private boolean html;

		/**
		 * @return the host
		 */
		public String getHost() {
			return host;
		}
		/**
		 * @param host the host to set
		 */
		public void setHost(String host) {
			this.host = Strings.stripToNull(host);
		}
		/**
		 * @return the port
		 */
		@Validates({
			@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER),
			@Validate(value=Validators.NUMBER, params="{min: 0, max: 65535}", msgId=Validators.MSGID_NUMBER_RANGE)
		})
		public Integer getPort() {
			return port;
		}
		/**
		 * @param port the port to set
		 */
		public void setPort(Integer port) {
			this.port = port;
		}
		/**
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}
		/**
		 * @param username the username to set
		 */
		public void setUsername(String username) {
			this.username = Strings.stripToNull(username);
		}
		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}
		/**
		 * @param password the password to set
		 */
		public void setPassword(String password) {
			this.password = Strings.stripToNull(password);
		}
		
		/**
		 * @return the connTimeout
		 */
		public Integer getConnTimeout() {
			return connTimeout;
		}
		/**
		 * @param connTimeout the connTimeout to set
		 */
		@Validates({
			@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER),
			@Validate(value=Validators.NUMBER, params="{min: 0}", msgId=Validators.MSGID_NUMBER_RANGE)
		})
		public void setConnTimeout(Integer connTimeout) {
			this.connTimeout = connTimeout;
		}
		/**
		 * @return the readTimeout
		 */
		@Validates({
			@Validate(value=Validators.CAST, msgId=Validators.MSGID_CAST_NUMBER),
			@Validate(value=Validators.NUMBER, params="{min: 0}", msgId=Validators.MSGID_NUMBER_RANGE)
		})
		public Integer getReadTimeout() {
			return readTimeout;
		}
		/**
		 * @param readTimeout the readTimeout to set
		 */
		public void setReadTimeout(Integer readTimeout) {
			this.readTimeout = readTimeout;
		}
		/**
		 * @return the dkimSelector
		 */
		public String getDkimSelector() {
			return dkimSelector;
		}
		/**
		 * @param dkimSelector the dkimSelector to set
		 */
		public void setDkimSelector(String dkimSelector) {
			this.dkimSelector = Strings.stripToNull(dkimSelector);
		}
		/**
		 * @return the dkimPrivateKey
		 */
		public String getDkimPrivateKey() {
			return dkimPrivateKey;
		}
		/**
		 * @param dkimPrivateKey the dkimPrivateKey to set
		 */
		public void setDkimPrivateKey(String dkimPrivateKey) {
			this.dkimPrivateKey = Strings.stripToNull(dkimPrivateKey);
		}
		/**
		 * @return the from
		 */
		@Validates({
			@Validate(value=Validators.CAST, msgId=Validators.MSGID_EMAIL)
		})
		public EmailAddress getFrom() {
			return from;
		}
		/**
		 * @param from the from to set
		 */
		public void setFrom(EmailAddress from) {
			this.from = from;
		}
		/**
		 * @return the to
		 */
		@Validates({
			@Validate(value=Validators.CAST, msgId=Validators.MSGID_EMAIL)
		})
		public EmailAddress getTo() {
			return to;
		}
		/**
		 * @param to the to to set
		 */
		public void setTo(EmailAddress to) {
			this.to = to;
		}
		/**
		 * @return the cc
		 */
		@Validates({
			@Validate(value=Validators.CAST, msgId=Validators.MSGID_EMAIL)
		})
		public EmailAddress getCc() {
			return cc;
		}
		/**
		 * @param cc the cc to set
		 */
		public void setCc(EmailAddress cc) {
			this.cc = cc;
		}
		/**
		 * @return the subject
		 */
		public String getSubject() {
			return subject;
		}
		/**
		 * @param subject the subject to set
		 */
		public void setSubject(String subject) {
			this.subject = subject;
		}
		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}
		/**
		 * @return the html
		 */
		public boolean isHtml() {
			return html;
		}
		/**
		 * @param html the html to set
		 */
		public void setHtml(boolean html) {
			this.html = html;
		}
	}
	
	/**
	 */
	public SendMailAction() {
	}

	/**
	 * input
	 */
	@At("")
	@Redirect(toslash=true)
	public Object input(@Param Arg a) {
		return null;
	}
	
	/**
	 * send mail
	 */
	@At
	public void send(@Param @Validates({
		@Validate(value=Validators.REQUIRED, params="{ fields: [ 'from', 'to' ] }", msgId=Validators.MSGID_REQUIRED),
		@Validate(value=Validators.VISIT)
		}) Arg a) {

		try {
			Email email = new Email();
			email.setFrom(a.getFrom());
			email.addTo(a.getTo());
			email.addCc(a.getCc());
			email.setSubject(a.getSubject());
			if (a.isHtml()) {
				email.setHtmlMsg(a.getMessage());
			}
			else {
				email.setTextMsg(a.getMessage());
			}
			email.signWithDomainKey(a.getFrom().getDomain(), a.getDkimSelector(), a.getDkimPrivateKey());
			
			EmailClient client = new EmailClient();
			client.setHost(a.getHost());
			if (a.getPort() != null) {
				client.setPort(a.getPort());
			}
			client.setUsername(a.getUsername());
			client.setPassword(a.getPassword());
			if (a.getConnTimeout() != null) {
				client.setConnectTimeout(a.getConnTimeout());
			}
			if (a.getReadTimeout() != null) {
				client.setDefaultTimeout(a.getReadTimeout());
			}
			client.send(email);
			
			addActionMessage(getText("success-mail-sent"));
		}
		catch (Exception e) {
			log.warn("send mail error", e);
			addActionError(Exceptions.getStackTrace(e));
		}
	}

}
