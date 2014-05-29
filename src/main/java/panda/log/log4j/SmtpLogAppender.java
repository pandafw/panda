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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import org.apache.log4j.xml.UnrecognizedElementHandler;
import org.w3c.dom.Element;

import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.net.MXLookup;

public class SmtpLogAppender extends AppenderSkeleton implements UnrecognizedElementHandler {
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
	private int bufferSize = 512;
	private boolean locationInfo = false;
	private boolean sendOnClose = false;

	protected CyclicBuffer cb = new CyclicBuffer(bufferSize);
	protected Message msg;

	protected String prefix;
	protected Properties props;
	protected Map<InternetAddress, String> hosts = new HashMap<InternetAddress, String>();

	protected TriggeringEventEvaluator evaluator;

	static class DefaultEvaluator implements TriggeringEventEvaluator {
		/**
		 * Is this <code>event</code> the e-mail triggering event?
		 * <p>
		 * This method returns <code>true</code>, if the event level has ERROR level or higher.
		 * Otherwise it returns <code>false</code>.
		 */
		public boolean isTriggeringEvent(LoggingEvent event) {
			return event.getLevel().isGreaterOrEqual(Level.ERROR);
		}
	}

	/**
	 * Construct
	 */
	public SmtpLogAppender() {
		this(new DefaultEvaluator());
	}

	/**
	 * Construct
	 */
	public SmtpLogAppender(TriggeringEventEvaluator evaluator) {
		this.evaluator = evaluator;
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

		if (evaluator instanceof OptionHandler) {
			((OptionHandler)evaluator).activateOptions();
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
	public void append(LoggingEvent event) {

		if (!checkEntryConditions()) {
			return;
		}

		event.getThreadName();
		event.getNDC();
		event.getMDCCopy();
		if (locationInfo) {
			event.getLocationInformation();
		}
		event.getRenderedMessage();
		event.getThrowableStrRep();
		cb.add(event);
		if (evaluator.isTriggeringEvent(event)) {
			sendBuffer();
		}
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

		if (this.evaluator == null) {
			errorHandler.error("No TriggeringEventEvaluator is set for appender [" + name + "].");
			return false;
		}

		if (this.layout == null) {
			errorHandler.error("No layout set for appender named [" + name + "].");
			return false;
		}
		return true;
	}

	synchronized public void close() {
		this.closed = true;
		if (sendOnClose && cb.length() > 0) {
			sendBuffer();
		}
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
	 * The <code>SMTPAppender</code> requires a {@link org.apache.log4j.Layout layout}.
	 */
	public boolean requiresLayout() {
		return true;
	}

	/**
	 * Layout body of email message.
	 */
	protected String formatBody() {
		// Note: this code already owns the monitor for this
		// appender. This frees us from needing to synchronize on 'cb'.

		StringBuffer sbuf = new StringBuffer();
		String t = layout.getHeader();
		if (t != null)
			sbuf.append(t);
		int len = cb.length();
		for (int i = 0; i < len; i++) {
			// sbuf.append(MimeUtility.encodeText(layout.format(cb.get())));
			LoggingEvent event = cb.get();
			sbuf.append(layout.format(event));
			if (layout.ignoresThrowable()) {
				String[] s = event.getThrowableStrRep();
				if (s != null) {
					for (int j = 0; j < s.length; j++) {
						sbuf.append(s[j]);
						sbuf.append(Layout.LINE_SEP);
					}
				}
			}
		}
		t = layout.getFooter();
		if (t != null) {
			sbuf.append(t);
		}

		return sbuf.toString();
	}

	/**
	 * Send the contents of the cyclic buffer as an e-mail message.
	 */
	protected void sendBuffer() {
		try {
			if (subjectLayout != null) {
				if (cb.length() > 0) {
					StringBuilder sb = new StringBuilder();
					LoggingEvent event = cb.get(cb.length() - 1);
					sb.append(subjectLayout.format(event));
					if (!setMsgSubject(sb.toString())) {
						setMsgSubject(getSubject());
					}
				}
				else {
					setMsgSubject(getSubject());
				}
			}

			String s = formatBody();
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
					StringBuffer sbuf = new StringBuffer(s);
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
	 * Returns value of the <b>EvaluatorClass</b> option.
	 */
	public String getEvaluatorClass() {
		return evaluator == null ? null : evaluator.getClass().getName();
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
	 * The <b>BufferSize</b> option takes a positive integer representing the maximum number of
	 * logging events to collect in a cyclic buffer. When the <code>BufferSize</code> is reached,
	 * oldest events are deleted as new events are added to the buffer. By default the size of the
	 * cyclic buffer is 512 events.
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
		cb.resize(bufferSize);
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
	 * Returns value of the <b>BufferSize</b> option.
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * The <b>EvaluatorClass</b> option takes a string value representing the name of the class
	 * implementing the {@link TriggeringEventEvaluator} interface. A corresponding object will be
	 * instantiated and assigned as the triggering event evaluator for the SMTPAppender.
	 */
	public void setEvaluatorClass(String value) {
		evaluator = (TriggeringEventEvaluator)OptionConverter.instantiateByClassName(value,
			TriggeringEventEvaluator.class, evaluator);
	}

	/**
	 * The <b>LocationInfo</b> option takes a boolean value. By default, it is set to false which
	 * means there will be no effort to extract the location information related to the event. As a
	 * result, the layout that formats the events as they are sent out in an e-mail is likely to
	 * place the wrong location information (if present in the format).
	 * <p>
	 * Location information extraction is comparatively very slow and should be avoided unless
	 * performance is not a concern.
	 */
	public void setLocationInfo(boolean locationInfo) {
		this.locationInfo = locationInfo;
	}

	/**
	 * Returns value of the <b>LocationInfo</b> option.
	 */
	public boolean getLocationInfo() {
		return locationInfo;
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
	 * Sets triggering evaluator.
	 * 
	 * @param trigger triggering event evaluator.
	 */
	public final void setEvaluator(final TriggeringEventEvaluator trigger) {
		if (trigger == null) {
			throw new NullPointerException("trigger");
		}
		this.evaluator = trigger;
	}

	/**
	 * Get triggering evaluator.
	 * 
	 * @return triggering event evaluator.
	 */
	public final TriggeringEventEvaluator getEvaluator() {
		return evaluator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public boolean parseUnrecognizedElement(final Element element, final Properties props) throws Exception {
		if ("triggeringPolicy".equals(element.getNodeName())) {
			Object triggerPolicy = org.apache.log4j.xml.DOMConfigurator.parseElement(element, props,
				TriggeringEventEvaluator.class);
			if (triggerPolicy instanceof TriggeringEventEvaluator) {
				setEvaluator((TriggeringEventEvaluator)triggerPolicy);
			}
			return true;
		}

		return false;
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

	/**
	 * Get sendOnClose.
	 * 
	 * @return if true all buffered logging events will be sent when the appender is closed.
	 */
	public final boolean getSendOnClose() {
		return sendOnClose;
	}

	/**
	 * Set sendOnClose.
	 * 
	 * @param val if true all buffered logging events will be sent when appender is closed.
	 */
	public final void setSendOnClose(final boolean val) {
		sendOnClose = val;
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
