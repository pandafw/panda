package panda.gems.admin.action;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.CastErrorValidate;
import panda.mvc.annotation.validate.NumberValidate;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.validator.Validators;
import panda.mvc.view.Views;
import panda.net.mail.Email;
import panda.net.mail.EmailAddress;
import panda.net.mail.EmailAttachment;
import panda.net.mail.MailClient;
import panda.vfs.FileItem;


@At("${!!super_path|||'/super'}/sendmail")
@Auth(AUTH.SUPER)
@To(value=Views.SFTL, error=Views.SFTL)
public class SendMailAction extends BaseAction {
	private static final Log log = Logs.getLog(SendMailAction.class);
	
	public static class Arg {
		private String client = "auto";
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
		private FileItem[] files;

		/**
		 * @return the client
		 */
		public String getClient() {
			return client;
		}
		/**
		 * @param client the client to set
		 */
		public void setClient(String client) {
			this.client = Strings.stripToNull(client);
		}
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
		@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
		@NumberValidate(min="0", max="65535")
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
		@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
		@NumberValidate(min="0")
		public void setConnTimeout(Integer connTimeout) {
			this.connTimeout = connTimeout;
		}
		/**
		 * @return the readTimeout
		 */
		@CastErrorValidate(msgId=Validators.MSGID_INTEGER)
		@NumberValidate(min="0")
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
		@CastErrorValidate(msgId=Validators.MSGID_EMAIL)
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
		@CastErrorValidate(msgId=Validators.MSGID_EMAIL)
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
		@CastErrorValidate(msgId=Validators.MSGID_EMAIL)
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
		 * @return the files
		 */
		public FileItem[] getFiles() {
			return files;
		}
		/**
		 * @param files the files to set
		 */
		public void setFiles(FileItem[] files) {
			this.files = files;
		}
	}
	
	/**
	 */
	public SendMailAction() {
	}

	/**
	 * input
	 * @param arg the input argument
	 */
	@At("")
	@Redirect(toslash=true)
	public void input(@Param Arg arg) {
	}
	
	/**
	 * send mail
	 * @param arg the input argument
	 */
	@At
	public void send(@Param
			@RequiredValidate(fields={ "from", "to" })
			@VisitValidate
		Arg arg) {

		try {
			Email email = new Email();
			email.setFrom(arg.getFrom());
			email.addTo(arg.getTo());
			email.addCc(arg.getCc());
			email.setSubject(arg.getSubject());
			email.setHtmlMsg(arg.getMessage());
			if (Arrays.isNotEmpty(arg.getFiles())) {
				for (FileItem fi : arg.getFiles()) {
					email.addAttachment(new EmailAttachment(fi));
				}
			}
			email.signWithDomainKey(arg.getFrom().getDomain(), arg.getDkimSelector(), arg.getDkimPrivateKey());
			
			MailClient client = createMailClient(arg);
			client.setHost(arg.getHost());
			if (arg.getPort() != null) {
				client.setPort(arg.getPort());
			}
			client.setUsername(arg.getUsername());
			client.setPassword(arg.getPassword());
			if (arg.getConnTimeout() != null) {
				client.setConnectTimeout(arg.getConnTimeout());
			}
			if (arg.getReadTimeout() != null) {
				client.setDefaultTimeout(arg.getReadTimeout());
			}
			client.send(email);
			
			addActionMessage(getText("success-mail-sent"));
		}
		catch (Exception e) {
			log.warn("send mail error", e);
			addActionError(Exceptions.getStackTrace(e));
		}
	}

	protected MailClient createMailClient(Arg arg) {
		return MailClient.create(arg.client);
	}
}
