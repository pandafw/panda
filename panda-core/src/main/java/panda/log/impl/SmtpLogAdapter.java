package panda.log.impl;

import java.util.List;
import java.util.Map;

import panda.lang.Booleans;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogFormat;
import panda.log.LogFormat.SimpleLogFormat;
import panda.log.LogLevel;
import panda.log.LogLog;
import panda.log.Logs;
import panda.net.mail.Email;
import panda.net.mail.EmailAddress;
import panda.net.mail.EmailException;
import panda.net.mail.JavaMailClient;
import panda.net.mail.MailClient;
import panda.net.mail.SmtpMailClient;


public class SmtpLogAdapter extends AbstractLogAdapter {

	/**
	 * mail subject format
	 */
	private LogFormat subject = new SimpleLogFormat("[%p] %c - %m");

	/**
	 * From recipient.
	 */
	private EmailAddress from;

	/**
	 * Comma separated list of to recipients.
	 */
	private List<EmailAddress> tos;

	/**
	 * Comma separated list of cc recipients.
	 */
	private List<EmailAddress> ccs;

	/**
	 * Comma separated list of bcc recipients.
	 */
	private List<EmailAddress> bccs;

	/**
	 * Comma separated list of replyTo addresses.
	 */
	private List<EmailAddress> replyTos;

	private String smtpClient;
	private String smtpHost;
	private int smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	private boolean smtpSsl;
	private boolean smtpStartTls;
	private boolean smtpDebug;

	private MailClient client;

	@Override
	protected Log getLogger(String name) {
		return new SmtpLog(this, name);
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("from".equalsIgnoreCase(name)) {
			setFrom(value);
		}
		else if ("to".equalsIgnoreCase(name)) {
			setTos(value);
		}
		else if ("cc".equalsIgnoreCase(name)) {
			setCcs(value);
		}
		else if ("bcc".equalsIgnoreCase(name)) {
			setBccs(value);
		}
		else if ("replyTo".equalsIgnoreCase(name)) {
			setReplyTos(value);
		}
		else if ("subject".equalsIgnoreCase(name)) {
			setSubject(value);
		}
		else if ("smtpClient".equalsIgnoreCase(name)) {
			setSmtpClient(value);
		}
		else if ("smtpHost".equalsIgnoreCase(name)) {
			setSmtpHost(value);
		}
		else if ("smtpPort".equalsIgnoreCase(name)) {
			setSmtpPort(Numbers.toInt(value, -1));
		}
		else if ("smtpUsername".equalsIgnoreCase(name)) {
			setSmtpUsername(value);
		}
		else if ("smtpPassword".equalsIgnoreCase(name)) {
			setSmtpPassword(value);
		}
		else if ("smtpSSL".equalsIgnoreCase(name)) {
			setSmtpSsl(Booleans.toBoolean(value));
		}
		else if ("smtpStartTls".equalsIgnoreCase(name)) {
			setSmtpStartTls(Booleans.toBoolean(value));
		}
		else if ("smtpDebug".equalsIgnoreCase(name)) {
			setSmtpDebug(Booleans.toBoolean(value));
		}
		else {
			super.setProperty(name, value);
		}
	}

	@Override
	public void init(Logs logs, String name, Map<String, String> props) {
		super.init(logs, name, props);
		
		client = getMailClient();
	}

	/**
	 * Create mail client.
	 * 
	 * @return mail client, may not be null.
	 */
	private MailClient getMailClient() {
		MailClient mc = null;
		
		if (Strings.equalsIgnoreCase("java", smtpClient)) {
			mc = new JavaMailClient();
		}
		else if (Strings.equalsIgnoreCase("smtp", smtpClient)) {
			mc = new SmtpMailClient();
		}
		else {
			mc = Systems.IS_OS_APPENGINE ? new JavaMailClient() : new SmtpMailClient();
		}
		
		mc.setHost(smtpHost);
		if (smtpPort > 0) {
			mc.setPort(smtpPort);
		}
		mc.setUsername(smtpUsername);
		mc.setPassword(smtpPassword);
		mc.setSsl(smtpSsl);
		if (smtpDebug) {
			ConsoleLog log = new ConsoleLog(logs, getClass().getName());
			log.setLogLevel(LogLevel.DEBUG);
			mc.setLog(log);

			mc.setDebug(smtpDebug);
		}
		else {
			mc.setLog(null);
		}

		return mc;
	}

	private EmailAddress getAddress(String addressStr) {
		try {
			return EmailAddress.parse(addressStr);
		}
		catch (EmailException e) {
			LogLog.error("Could not parse address [" + addressStr + "].", e);
			return null;
		}
	}

	private List<EmailAddress> parseAddress(String ass) {
		if (ass != null && ass.length() > 0) {
			try {
				return EmailAddress.parseList(ass);
			}
			catch (EmailException e) {
				LogLog.error("Could not parse address [" + ass + "].", e);
			}
		}
		return null;
	}

	protected void write(LogEvent event) {
		String sub = subject.format(event);

		StringBuilder msg = new StringBuilder();
		msg.append(format.format(event));

		if (event.getError() != null) {
			msg.append(Exceptions.getStackTrace(event.getError()));
		}

		sendMail(sub, msg.toString());
	}


	/**
	 * Send the contents of the cyclic buffer as an e-mail message.
	 * @param subject the mail subject
	 * @param message the mail content
	 */
	protected void sendMail(String subject, String message) {
		try {
			Email email = new Email();
			email.setFrom(from);
			email.setTos(tos);
			email.setCcs(ccs);
			email.setBccs(bccs);
			email.setReplyTos(replyTos);
			email.setSubject(subject);
			email.setTextMsg(message);
			client.send(email);
		}
		catch (Throwable e) {
			LogLog.error("Error occured while sending e-mail notification.", e);
		}
	}

	/**
	 * @return the value of the <b>From</b> option.
	 */
	public EmailAddress getFrom() {
		return from;
	}

	/**
	 * The <b>From</b> option takes a string value which should be a e-mail address of the sender.
	 * 
	 * @param from the from address
	 */
	public void setFrom(String from) {
		this.from = getAddress(from);
	}

	/**
	 * @return value of the <b>To</b> option.
	 */
	public List<EmailAddress> getTos() {
		return tos;
	}

	/**
	 * The <b>To</b> option takes a string value which should be a comma separated list of e-mail
	 * address of the recipients.
	 * 
	 * @param to the to address
	 */
	public void setTos(String to) {
		tos = parseAddress(to);
	}

	/**
	 * Get the reply addresses.
	 * 
	 * @return reply addresses as comma separated string, may be null.
	 */
	public List<EmailAddress> getReplyTos() {
		return replyTos;
	}

	/**
	 * Set the e-mail addresses to which replies should be directed.
	 * 
	 * @param addresses reply addresses as comma separated string, may be null.
	 */
	public void setReplyTos(final String addresses) {
		replyTos = parseAddress(addresses);
	}

	/**
	 * Get the cc recipient addresses.
	 * 
	 * @return recipient addresses as comma separated string, may be null.
	 */
	public List<EmailAddress> getCcs() {
		return ccs;
	}

	/**
	 * Set the cc recipient addresses.
	 * 
	 * @param addresses recipient addresses as comma separated string, may be null.
	 */
	public void setCcs(final String addresses) {
		ccs = parseAddress(addresses);
	}

	/**
	 * Get the bcc recipient addresses.
	 * 
	 * @return recipient addresses as comma separated string, may be null.
	 */
	public List<EmailAddress> getBccs() {
		return bccs;
	}

	/**
	 * Set the bcc recipient addresses.
	 * 
	 * @param addresses recipient addresses as comma separated string, may be null.
	 */
	public void setBccs(final String addresses) {
		bccs = parseAddress(addresses);
	}

	/**
	 * The <b>Subject</b> option takes a string value which should be a the subject of the e-mail
	 * message.
	 * 
	 * @param subject the mail subject
	 */
	public void setSubject(String subject) {
		this.subject = new SimpleLogFormat(subject);
	}

	/**
	 * @return the smtpClient
	 */
	public String getSmtpClient() {
		return smtpClient;
	}

	/**
	 * @param smtpClient the smtpClient to set
	 */
	public void setSmtpClient(String smtpClient) {
		this.smtpClient = smtpClient;
	}

	/**
	 * @return the value of the <b>SmtpHost</b> option.
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * The <b>SmtpHost</b> option takes a string value which should be a the host name of the Smtp
	 * server that will send the e-mail message.
	 * 
	 * @param smtpHost the smtp server
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * Get port.
	 * 
	 * @return port, negative values indicate use of default ports for protocol.
	 */
	public final int getSmtpPort() {
		return smtpPort;
	}

	/**
	 * Set port.
	 * 
	 * @param val port, negative values indicate use of default ports for protocol.
	 */
	public final void setSmtpPort(final int val) {
		smtpPort = val;
	}

	/**
	 * Get Smtp user name.
	 * 
	 * @return Smtp user name, may be null.
	 */
	public String getSmtpUsername() {
		return smtpUsername;
	}

	/**
	 * The <b>SmtpUsername</b> option takes a string value which should be the username required to
	 * authenticate against the mail server.
	 * 
	 * @param username user name, may be null.
	 */
	public void setSmtpUsername(final String username) {
		this.smtpUsername = username;
	}

	/**
	 * Get Smtp password.
	 * 
	 * @return Smtp password, may be null.
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}

	/**
	 * The <b>SmtpPassword</b> option takes a string value which should be the password required to
	 * authenticate against the mail server.
	 * 
	 * @param password password, may be null.
	 */
	public void setSmtpPassword(final String password) {
		this.smtpPassword = password;
	}

	/**
	 * Get Smtp ssl.
	 * 
	 * @return Smtp ssl flag.
	 */
	public boolean getSmtpSsl() {
		return smtpSsl;
	}

	/**
	 * @param ssl ssl flag.
	 */
	public void setSmtpSsl(final boolean ssl) {
		this.smtpSsl = ssl;
	}

	/**
	 * @return the smtpStartTls
	 */
	public boolean isSmtpStartTls() {
		return smtpStartTls;
	}

	/**
	 * @param smtpStartTls the smtpStartTls to set
	 */
	public void setSmtpStartTls(boolean smtpStartTls) {
		this.smtpStartTls = smtpStartTls;
	}

	/**
	 * Get Smtp debug.
	 * 
	 * @return Smtp debug flag.
	 */
	public boolean getSmtpDebug() {
		return smtpDebug;
	}

	/**
	 * Setting the <b>SmtpDebug</b> option to true will cause the mail session to log its server
	 * interaction to stdout. This can be useful when debuging the appender but should not be used
	 * during production because username and password information is included in the output.
	 * 
	 * @param debug debug flag.
	 */
	public void setSmtpDebug(final boolean debug) {
		this.smtpDebug = debug;
	}

	/**
	 * Smtp log
	 */
	protected static class SmtpLog extends AbstractLog {
		protected SmtpLogAdapter adapter;
		
		protected SmtpLog(SmtpLogAdapter adapter, String name) {
			super(adapter.logs, name, adapter.threshold);
			this.adapter = adapter;
		}

		@Override
		protected void write(LogEvent event) {
			adapter.write(event);
		}
	}
}
