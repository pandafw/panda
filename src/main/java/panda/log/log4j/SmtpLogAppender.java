package panda.log.log4j;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.NamingException;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.net.MXLookup;

public class SmtpLogAppender extends AbstractAppender {
	/** subject layout */
	protected Layout subjectLayout;

	/** to */
	private String to;
	/**
	 * Comma separated list of cc recipients.
	 */
	private String cc;
	/**
	 * Comma separated list of bcc recipients.
	 */
	private String bcc;
	private String from;
	/**
	 * Comma separated list of replyTo addresses.
	 */
	private String replyTo;
	private String subject;
	private String smtpHost;
	private String smtpUsername;
	private String smtpPassword;
	private String smtpProtocol;
	private int smtpPort = -1;
	private boolean smtpDebug = false;

	protected Message msg;

	protected String prefix;
	protected Properties props;
	protected Map<InternetAddress, String> hosts = new HashMap<InternetAddress, String>();

	/**
	 * Construct
	 */
	public SmtpLogAppender() {
	}

	protected void resolveHosts() {
		if (Strings.isNotEmpty(smtpHost)) {
			return;
		}
		
		try {
			hosts.clear();
			for (Address a : msg.getAllRecipients()) {
				String[] ss = ((InternetAddress)a).getAddress().split("@");
				if (ss.length != 2) {
					LogLog.error("Invalid internet address: " + a);
				}
				else {
					List<String> hs = MXLookup.lookup(ss[1]);
					if (Collections.isEmpty(hs)) {
						LogLog.error("Could not lookup MX record for " + ss[1]);
					}
					else {
						hosts.put((InternetAddress)a, hs.get(0));
					}
				}
			}
		}
		catch (NamingException e) {
			LogLog.error("Could not lookup MX records.", e);
		}
		catch (MessagingException e) {
			LogLog.error("Could not getAllRecipients.", e);
		}
	}

	/**
	 * Activate the specified options, such as the smtp host, the recipient, from, etc.
	 */
	public void activateOptions() {
		super.activateOptions();
		
		Session session = createSession();
		msg = new MimeMessage(session);

		try {
			addressMessage(msg);
			if (subject != null) {
				setMsgSubject(subject);
			}
		}
		catch (MessagingException e) {
			LogLog.error("Could not activate SMTPAppender options.", e);
		}
	}

	/**
	 * Address message.
	 * 
	 * @param msg message, may not be null.
	 * @throws MessagingException thrown if error addressing message.
	 */
	protected void addressMessage(final Message msg) throws MessagingException {
		if (from != null) {
			msg.setFrom(getAddress(from));
		}
		else {
			msg.setFrom();
		}

		// Add ReplyTo addresses if defined.
		if (replyTo != null && replyTo.length() > 0) {
			msg.setReplyTo(parseAddress(replyTo));
		}

		if (to != null && to.length() > 0) {
			msg.setRecipients(Message.RecipientType.TO, parseAddress(to));
		}

		// Add CC receipients if defined.
		if (cc != null && cc.length() > 0) {
			msg.setRecipients(Message.RecipientType.CC, parseAddress(cc));
		}

		// Add BCC receipients if defined.
		if (bcc != null && bcc.length() > 0) {
			msg.setRecipients(Message.RecipientType.BCC, parseAddress(bcc));
		}
	}

	/**
	 * Create mail session.
	 * 
	 * @return mail session, may not be null.
	 */
	protected Session createSession() {
		try {
			props = new Properties(System.getProperties());
		}
		catch (SecurityException ex) {
			props = new Properties();
		}

		prefix = "mail.smtp";
		if (smtpProtocol != null) {
			props.put("mail.transport.protocol", smtpProtocol);
			prefix = "mail." + smtpProtocol;
		}
		if (smtpHost != null) {
			props.put(prefix + ".host", smtpHost);
		}
		if (smtpPort > 0) {
			props.put(prefix + ".port", String.valueOf(smtpPort));
		}

		Authenticator auth = null;
		if (smtpPassword != null && smtpUsername != null) {
			props.put(prefix + ".auth", "true");
			auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUsername, smtpPassword);
				}
			};
		}
		Session session = Session.getInstance(props, auth);
		if (smtpProtocol != null) {
			session.setProtocolForAddress("rfc822", smtpProtocol);
		}
		if (smtpDebug) {
			session.setDebug(smtpDebug);
		}
		return session;
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
		if (this.msg == null) {
			errorHandler.error("Message object not configured.");
			return false;
		}

		return super.checkEntryConditions();
	}

	InternetAddress getAddress(String addressStr) {
		try {
			return new InternetAddress(addressStr);
		}
		catch (AddressException e) {
			errorHandler.error("Could not parse address [" + addressStr + "].", e, ErrorCode.ADDRESS_PARSE_FAILURE);
			return null;
		}
	}

	InternetAddress[] parseAddress(String addressStr) {
		try {
			return InternetAddress.parse(addressStr, true);
		}
		catch (AddressException e) {
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
	 * Returns value of the <b>To</b> option.
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Layout body of email message.
	 */
	protected String formatBody(LoggingEvent event) {
		// Note: this code already owns the monitor for this
		// appender. This frees us from needing to synchronize on 'cb'.

		StringBuilder sb = new StringBuilder();
		String t = layout.getHeader();
		if (t != null) {
			sb.append(t);
		}
		
		outputLogEvent(sb, event);
		sb.append(Layout.LINE_SEP);
		
		t = layout.getFooter();
		if (t != null) {
			sb.append(t);
		}

		return sb.toString();
	}

	/**
	 * Send the contents of the cyclic buffer as an e-mail message.
	 */
	protected void sendMail(LoggingEvent event) {
		try {
			if (subjectLayout != null) {
				String s = subjectLayout.format(event);
				if (!setMsgSubject(s)) {
					setMsgSubject(getSubject());
				}
			}
			else {
				setMsgSubject(getSubject());
			}

			String s = formatBody(event);
			boolean allAscii = true;
			for (int i = 0; i < s.length() && allAscii; i++) {
				allAscii = s.charAt(i) <= 0x7F;
			}
			
			MimeBodyPart part;
			if (allAscii) {
				part = new MimeBodyPart();
				part.setContent(s, layout.getContentType());
			}
			else {
				try {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					Writer writer = new OutputStreamWriter(MimeUtility.encode(os, "quoted-printable"), "UTF-8");
					writer.write(s);
					writer.close();
					InternetHeaders headers = new InternetHeaders();
					headers.setHeader("Content-Type", layout.getContentType() + "; charset=UTF-8");
					headers.setHeader("Content-Transfer-Encoding", "quoted-printable");
					part = new MimeBodyPart(headers, os.toByteArray());
				}
				catch (Exception ex) {
					StringBuilder sbuf = new StringBuilder(s);
					for (int i = 0; i < sbuf.length(); i++) {
						if (sbuf.charAt(i) >= 0x80) {
							sbuf.setCharAt(i, '?');
						}
					}
					part = new MimeBodyPart();
					part.setContent(sbuf.toString(), layout.getContentType());
				}
			}

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(part);
			msg.setContent(mp);

			msg.setSentDate(new Date());
			
			if (Strings.isEmpty(smtpHost)) {
				resolveHosts();
				for (Entry<InternetAddress, String> en : hosts.entrySet()) {
					props.put(prefix + ".host", en.getValue());
					Transport.send(msg, new InternetAddress[] { en.getKey() });
				}
			}
			else {
				Transport.send(msg);
			}
		}
		catch (MessagingException e) {
			LogLog.error("Error occured while sending e-mail notification.", e);
		}
		catch (RuntimeException e) {
			LogLog.error("Error occured while sending e-mail notification.", e);
		}
	}

	/**
	 * Returns value of the <b>From</b> option.
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
	 * Returns value of the <b>Subject</b> option.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The <b>From</b> option takes a string value which should be a e-mail address of the sender.
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
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * The <b>SMTPHost</b> option takes a string value which should be a the host name of the SMTP
	 * server that will send the e-mail message.
	 */
	public void setSMTPHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * Returns value of the <b>SMTPHost</b> option.
	 */
	public String getSMTPHost() {
		return smtpHost;
	}

	/**
	 * The <b>To</b> option takes a string value which should be a comma separated list of e-mail
	 * address of the recipients.
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
	 * Get transport protocol. Typically null or "smtps".
	 * 
	 * @return transport protocol, may be null.
	 */
	public final String getSMTPProtocol() {
		return smtpProtocol;
	}

	/**
	 * Set transport protocol. Typically null or "smtps".
	 * 
	 * @param val transport protocol, may be null.
	 */
	public final void setSMTPProtocol(final String val) {
		smtpProtocol = val;
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

	protected boolean setMsgSubject(String subject) {
		try {
			msg.setSubject("");
			msg.setSubject(MimeUtility.encodeText(subject, Charsets.UTF_8, null));
			return true;
		}
		catch (UnsupportedEncodingException ex) {
			LogLog.error("Unable to encode SMTP subject", ex);
			return false;
		}
		catch (MessagingException ex) {
			LogLog.error("Unable to set SMTP subject", ex);
			return false;
		}
	}
}
