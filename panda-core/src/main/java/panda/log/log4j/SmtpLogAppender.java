package panda.log.log4j;

import java.util.List;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import panda.log.LogLevel;
import panda.log.impl.DefaultLog;
import panda.net.mail.Email;
import panda.net.mail.EmailAddress;
import panda.net.mail.EmailException;
import panda.net.mail.MailClient;

public class SmtpLogAppender extends AbstractAppender {
	/** subject layout */
	protected Layout subjectLayout;

	private String from;

	/**
	 * Comma separated list of to recipients.
	 */
	private String to;
	/**
	 * Comma separated list of cc recipients.
	 */
	private String cc;
	/**
	 * Comma separated list of bcc recipients.
	 */
	private String bcc;
	/**
	 * Comma separated list of replyTo addresses.
	 */
	private String replyTo;

	private String subject;

	private String smtpHost;
	private int smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	private boolean smtpSsl;
	private boolean smtpDebug = false;

	private MailClient client;
	private Email email;

	/**
	 * Construct
	 */
	public SmtpLogAppender() {
	}

	/**
	 * Activate the specified options, such as the smtp host, the recipient, from, etc.
	 */
	public void activateOptions() {
		super.activateOptions();
		
		email = new Email();
		try {
			addressMessage(email);
			if (subject != null) {
				email.setSubject(subject);
			}
		}
		catch (EmailException e) {
			LogLog.error("Could not activate SMTPAppender options.", e);
		}
		
		client = createClient();
	}

	/**
	 * Address message.
	 * 
	 * @param msg message, may not be null.
	 * @throws EmailException thrown if error addressing message.
	 */
	protected void addressMessage(final Email msg) throws EmailException {
		if (from != null) {
			msg.setFrom(getAddress(from));
		}

		// Add ReplyTo addresses if defined.
		if (replyTo != null && replyTo.length() > 0) {
			msg.setReplyTos(parseAddress(replyTo));
		}

		if (to != null && to.length() > 0) {
			msg.setTos(parseAddress(to));
		}

		// Add CC receipients if defined.
		if (cc != null && cc.length() > 0) {
			msg.setCcs(parseAddress(cc));
		}

		// Add BCC receipients if defined.
		if (bcc != null && bcc.length() > 0) {
			msg.setBccs(parseAddress(bcc));
		}
	}

	/**
	 * Create mail client.
	 * 
	 * @return mail client, may not be null.
	 */
	protected MailClient createClient() {
		MailClient mc = new MailClient();
		
		mc.setHost(smtpHost);
		if (smtpPort > 0) {
			mc.setPort(smtpPort);
		}
		mc.setUsername(smtpUsername);
		mc.setPassword(smtpPassword);
		mc.setSsl(smtpSsl);
		if (smtpDebug) {
			DefaultLog log = new DefaultLog(getClass().getName());
			log.setLogLevel(LogLevel.DEBUG);
			mc.setLog(log);
		}
		return mc;
	}

	/**
	 * Perform SMTPAppender specific appending actions, mainly adding the event to a cyclic buffer
	 * and checking if the event triggers an e-mail to be sent.
	 */
	protected void subAppend(LoggingEvent event) {
		sendMail(event);
	}

	/**
	 * This method determines if there is a sense in attempting to append.
	 * <p>
	 * It checks whether there is a set output target and also if there is a set layout. If these
	 * checks fail, then the boolean value <code>false</code> is returned.
	 */
	protected boolean checkEntryConditions() {
		if (this.email == null) {
			errorHandler.error("Message object not configured.");
			return false;
		}

		return super.checkEntryConditions();
	}

	EmailAddress getAddress(String addressStr) {
		try {
			return EmailAddress.parse(addressStr);
		}
		catch (EmailException e) {
			errorHandler.error("Could not parse address [" + addressStr + "].", e, ErrorCode.ADDRESS_PARSE_FAILURE);
			return null;
		}
	}

	List<EmailAddress> parseAddress(String addressStr) {
		try {
			return EmailAddress.parseList(addressStr);
		}
		catch (EmailException e) {
			errorHandler.error("Could not parse address [" + addressStr + "].", e, ErrorCode.ADDRESS_PARSE_FAILURE);
			return null;
		}
	}

	/**
	 * @return the subjectLayout
	 */
	public Layout getSubjectLayout() {
		return subjectLayout;
	}

	/**
	 * @param subjectLayout the subjectLayout to set
	 */
	public void setSubjectLayout(Layout subjectLayout) {
		this.subjectLayout = subjectLayout;
	}

	/**
	 * @return value of the <b>To</b> option.
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Layout body of email message.
	 */
	protected String formatBody(LoggingEvent event) {
		StringBuilder sb = new StringBuilder();
		outputLogEvent(sb, event);
		return sb.toString();
	}

	/**
	 * Send the contents of the cyclic buffer as an e-mail message.
	 */
	protected void sendMail(LoggingEvent event) {
		try {
			email.setMsgId(null);
			if (subjectLayout != null) {
				String s = subjectLayout.format(event);
				email.setSubject(s);
			}
			else {
				email.setSubject(subject);
			}

			String body = formatBody(event);
			email.setTextMsg(body);
			client.send(email);
		}
		catch (EmailException e) {
			LogLog.error("Error occured while sending e-mail notification.", e);
		}
		catch (RuntimeException e) {
			LogLog.error("Error occured while sending e-mail notification.", e);
		}
	}

	/**
	 * @return the value of the <b>From</b> option.
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Get the reply addresses.
	 * 
	 * @return reply addresses as comma separated string, may be null.
	 */
	public String getReplyTo() {
		return replyTo;
	}

	/**
	 * @return the value of the <b>Subject</b> option.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The <b>From</b> option takes a string value which should be a e-mail address of the sender.
	 * 
	 * @param from the from address
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Set the e-mail addresses to which replies should be directed.
	 * 
	 * @param addresses reply addresses as comma separated string, may be null.
	 */
	public void setReplyTo(final String addresses) {
		this.replyTo = addresses;
	}

	/**
	 * The <b>Subject</b> option takes a string value which should be a the subject of the e-mail
	 * message.
	 * 
	 * @param subject the mail subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * The <b>SMTPHost</b> option takes a string value which should be a the host name of the SMTP
	 * server that will send the e-mail message.
	 * 
	 * @param smtpHost the smtp server
	 */
	public void setSMTPHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * @return the value of the <b>SMTPHost</b> option.
	 */
	public String getSMTPHost() {
		return smtpHost;
	}

	/**
	 * The <b>To</b> option takes a string value which should be a comma separated list of e-mail
	 * address of the recipients.
	 * 
	 * @param to the to address
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Set the cc recipient addresses.
	 * 
	 * @param addresses recipient addresses as comma separated string, may be null.
	 */
	public void setCc(final String addresses) {
		this.cc = addresses;
	}

	/**
	 * Get the cc recipient addresses.
	 * 
	 * @return recipient addresses as comma separated string, may be null.
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * Set the bcc recipient addresses.
	 * 
	 * @param addresses recipient addresses as comma separated string, may be null.
	 */
	public void setBcc(final String addresses) {
		this.bcc = addresses;
	}

	/**
	 * Get the bcc recipient addresses.
	 * 
	 * @return recipient addresses as comma separated string, may be null.
	 */
	public String getBcc() {
		return bcc;
	}

	/**
	 * The <b>SmtpPassword</b> option takes a string value which should be the password required to
	 * authenticate against the mail server.
	 * 
	 * @param password password, may be null.
	 */
	public void setSMTPPassword(final String password) {
		this.smtpPassword = password;
	}

	/**
	 * The <b>SmtpUsername</b> option takes a string value which should be the username required to
	 * authenticate against the mail server.
	 * 
	 * @param username user name, may be null.
	 */
	public void setSMTPUsername(final String username) {
		this.smtpUsername = username;
	}

	/**
	 * Setting the <b>SmtpDebug</b> option to true will cause the mail session to log its server
	 * interaction to stdout. This can be useful when debuging the appender but should not be used
	 * during production because username and password information is included in the output.
	 * 
	 * @param debug debug flag.
	 */
	public void setSMTPDebug(final boolean debug) {
		this.smtpDebug = debug;
	}

	/**
	 * @param ssl ssl flag.
	 */
	public void setSMTPSsl(final boolean ssl) {
		this.smtpSsl = ssl;
	}

	/**
	 * Get SMTP password.
	 * 
	 * @return SMTP password, may be null.
	 */
	public String getSMTPPassword() {
		return smtpPassword;
	}

	/**
	 * Get SMTP user name.
	 * 
	 * @return SMTP user name, may be null.
	 */
	public String getSMTPUsername() {
		return smtpUsername;
	}

	/**
	 * Get SMTP debug.
	 * 
	 * @return SMTP debug flag.
	 */
	public boolean getSMTPDebug() {
		return smtpDebug;
	}

	/**
	 * Get SMTP ssl.
	 * 
	 * @return SMTP ssl flag.
	 */
	public boolean getSMTPSsl() {
		return smtpSsl;
	}

	/**
	 * Get port.
	 * 
	 * @return port, negative values indicate use of default ports for protocol.
	 */
	public final int getSMTPPort() {
		return smtpPort;
	}

	/**
	 * Set port.
	 * 
	 * @param val port, negative values indicate use of default ports for protocol.
	 */
	public final void setSMTPPort(final int val) {
		smtpPort = val;
	}
}
