package panda.net.mail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.net.Mimes;

public class Email {
	private String msgId;
	private Date date;
	private String charset = Charsets.UTF_8;
	private EmailAddress sender;
	private EmailAddress from;
	private List<EmailAddress> tos;
	private List<EmailAddress> ccs;
	private List<EmailAddress> bccs;
	private List<EmailAddress> replyTos;
	private String subject = "";
	private String message = "";
	private boolean html;
	private List<EmailAttachment> attachments;

	private String dkimDomain;
	private String dkimSelector;
	private String dkimPrivateKey;
	
	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}
	
	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the sender address
	 */
	public String getSenderAddress() {
		return (sender == null ? from : sender).getAddress();
	}

	/**
	 * @return the sender address
	 */
	public EmailAddress getSender() {
		return sender;
	}
	
	/**
	 * @param addr the sender address to set
	 * @throws EmailException if an error occurs
	 */
	public void setSender(String addr) throws EmailException {
		this.sender = EmailAddress.parse(addr);
	}

	/**
	 * @param addr the sender address to set
	 * @param name the sender name
	 * @throws EmailException if an error occurs
	 */
	public void setSender(String addr, String name) throws EmailException {
		this.sender = new EmailAddress(addr, name);
	}

	/**
	 * @param addr the sender to set
	 * @throws EmailException if an error occurs
	 */
	public void setSender(EmailAddress addr) throws EmailException {
		this.sender = addr;
	}
	
	/**
	 * @return the encoded from
	 * @throws EmailException if an error occurs
	 */
	public String getEncodedFrom() throws EmailException {
		return Email.toEncodedAddress(Arrays.asList(from), charset);
	}

	/**
	 * @return the from
	 */
	public EmailAddress getFrom() {
		return from;
	}
	
	/**
	 * @param from the from address to set
	 * @throws EmailException if an error occurs
	 */
	public void setFrom(String from) throws EmailException {
		this.from = EmailAddress.parse(from);
	}

	/**
	 * @param addr the from address to set
	 * @param name the from name
	 * @throws EmailException if an error occurs
	 */
	public void setFrom(String addr, String name) throws EmailException {
		this.from = new EmailAddress(addr, name);
	}

	/**
	 * @param addr the from to set
	 * @throws EmailException if an error occurs
	 */
	public void setFrom(EmailAddress addr) throws EmailException {
		this.from = addr;
	}

	/**
	 * add to
	 * @param ea to email address
	 * @throws EmailException if an error occurs
	 */
	public void addTo(EmailAddress ea) throws EmailException {
		if (ea == null) {
			return;
		}

		if (tos == null) {
			tos = new ArrayList<EmailAddress>();
		}
		
		tos.add(ea);
	}

	/**
	 * add to
	 * @param to the to address
	 * @throws EmailException if an error occurs
	 */
	public void addTo(String to) throws EmailException {
		addTo(EmailAddress.parse(to));
	}

	/**
	 * add to
	 * @param addr address
	 * @param name name
	 * @throws EmailException if an error occurs
	 */
	public void addTo(String addr, String name) throws EmailException {
		addTo(new EmailAddress(addr, name));
	}

	/**
	 * add to
	 * @param tos to address array
	 * @throws EmailException if an error occurs
	 */
	public void addTos(String... tos) throws EmailException {
		if (Arrays.isEmpty(tos)) {
			return;
		}
		for (String to : tos) {
			addTo(to);
		}
	}

	/**
	 * @param tos the tos to set
	 */
	public void setTos(List<EmailAddress> tos) {
		this.tos = tos;
	}

	/**
	 * @return the tos
	 */
	public List<EmailAddress> getTos() {
		return tos;
	}
	
	/**
	 * @return the encoded tos
	 * @throws EmailException if an error occurs
	 */
	public String getEncodedTos() throws EmailException {
		return Email.toEncodedAddress(tos, charset);
	}

	/**
	 * add cc
	 * @param ea cc email address
	 */
	public void addCc(EmailAddress ea) {
		if (ea == null) {
			return;
		}

		if (ccs == null) {
			ccs = new ArrayList<EmailAddress>();
		}
		
		ccs.add(ea);
	}

	/**
	 * add cc
	 * @param cc the cc address
	 * @throws EmailException if an error occurs
	 */
	public void addCc(String cc) throws EmailException {
		addCc(EmailAddress.parse(cc));
	}

	/**
	 * add cc
	 * @param addr address
	 * @param name name
	 * @throws EmailException if an error occurs
	 */
	public void addCc(String addr, String name) throws EmailException {
		addCc(new EmailAddress(addr, name));
	}

	/**
	 * add cc
	 * @param ccs cc address array
	 * @throws EmailException if an error occurs
	 */
	public void addCcs(String... ccs) throws EmailException {
		if (Arrays.isEmpty(ccs)) {
			return;
		}
		for (String cc : ccs) {
			addCc(cc);
		}
	}

	/**
	 * @param ccs the ccs to set
	 */
	public void setCcs(List<EmailAddress> ccs) {
		this.ccs = ccs;
	}

	/**
	 * @return the ccs
	 */
	public List<EmailAddress> getCcs() {
		return ccs;
	}

	/**
	 * @return the encoded ccs
	 * @throws EmailException if an error occurs
	 */
	public String getEncodedCcs() throws EmailException {
		return Email.toEncodedAddress(ccs, charset);
	}

	/**
	 * add bcc
	 * @param ea bcc email address
	 */
	public void addBcc(EmailAddress ea) {
		if (ea == null) {
			return;
		}

		if (bccs == null) {
			bccs = new ArrayList<EmailAddress>();
		}
		
		bccs.add(ea);
	}

	/**
	 * add bcc
	 * @param bcc  the bcc address
	 * @throws EmailException if an error occurs
	 */
	public void addBcc(String bcc) throws EmailException {
		addBcc(EmailAddress.parse(bcc));
	}

	/**
	 * add bcc
	 * @param addr address
	 * @param name name
	 * @throws EmailException if an error occurs
	 */
	public void addBcc(String addr, String name) throws EmailException {
		addBcc(new EmailAddress(addr, name));
	}

	/**
	 * add bcc
	 * @param bccs bcc address array
	 * @throws EmailException if an error occurs
	 */
	public void addBccs(String... bccs) throws EmailException {
		if (Arrays.isEmpty(bccs)) {
			return;
		}
		for (String bcc : bccs) {
			addBcc(bcc);
		}
	}

	/**
	 * @param bccs the bccs to set
	 */
	public void setBccs(List<EmailAddress> bccs) {
		this.bccs = bccs;
	}

	/**
	 * @return the bccs
	 */
	public List<EmailAddress> getBccs() {
		return bccs;
	}

	/**
	 * @return the encoded bccs
	 * @throws EmailException if an error occurs
	 */
	public String getEncodedBccs() throws EmailException {
		return Email.toEncodedAddress(bccs, charset);
	}
	
	/**
	 * add replyTo
	 * @param ea replyTo email address
	 * @throws EmailException if an error occurs
	 */
	public void addReplyTo(EmailAddress ea) throws EmailException {
		if (ea == null) {
			return;
		}

		if (replyTos == null) {
			replyTos = new ArrayList<EmailAddress>();
		}
		
		replyTos.add(ea);
	}

	/**
	 * add replyTo
	 * @param addr replyTo address
	 * @throws EmailException if an error occurs
	 */
	public void addReplyTo(String addr) throws EmailException {
		addReplyTo(EmailAddress.parse(addr));
	}

	/**
	 * add replyTo
	 * @param addr address
	 * @param name name
	 * @throws EmailException if an error occurs
	 */
	public void addReplyTo(String addr, String name) throws EmailException {
		addReplyTo(new EmailAddress(addr, name));
	}

	/**
	 * @param replyTos the replyTos to set
	 */
	public void setReplyTos(List<EmailAddress> replyTos) {
		this.replyTos = replyTos;
	}

	/**
	 * @return the replyTos
	 */
	public List<EmailAddress> getReplyTos() {
		return replyTos;
	}

	/**
	 * @return the encoded replyTos
	 * @throws EmailException if an error occurs
	 */
	public String getEncodedReplyTos() throws EmailException {
		return Email.toEncodedAddress(replyTos, charset);
	}
	
	/**
	 * @return the recipients
	 */
	public Set<EmailAddress> getRecipients() {
		Set<EmailAddress> rcpts = new HashSet<EmailAddress>();
		if (tos != null) {
			rcpts.addAll(tos);
		}
		if (ccs != null) {
			rcpts.addAll(ccs);
		}
		if (bccs != null) {
			rcpts.addAll(bccs);
		}
		return rcpts;
	}

	public Map<String, List<EmailAddress>> getRecipientsByDomain() {
		Map<String, List<EmailAddress>> m = new HashMap<String, List<EmailAddress>>();
		Set<EmailAddress> rcpts = getRecipients();
		for (EmailAddress r : rcpts) {
			String d = r.getDomain();
			List<EmailAddress> rs = m.get(d);
			if (rs == null) {
				rs = new ArrayList<EmailAddress>();
				m.put(d, rs);
			}
			rs.add(r);
		}
		return m;
	}

	public static String toEncodedAddress(Collection<EmailAddress> eas, String charset) throws EmailException {
		StringBuilder sb = new StringBuilder();
		Iterator<EmailAddress> it = eas.iterator();
		while (it.hasNext()) {
			EmailAddress ea = it.next();
			if (Strings.isNotEmpty(ea.getPersonal())) {
				try {
					String n = Mimes.encodeWord(ea.getPersonal(), charset);
					n = EmailAddress.quote(n);
					sb.append(n);
				}
				catch (UnsupportedEncodingException e) {
					throw new EmailException(e.getMessage(), e);
				}
			}
			sb.append('<').append(ea.getAddress()).append('>');
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * @return the encoded subject
	 * @exception UnsupportedEncodingException if the encoding fails
	 */
	public String getEncodedSubject() throws UnsupportedEncodingException {
		return Mimes.encodeWord(subject, charset);
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = Strings.defaultString(subject);
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
		this.message = Strings.defaultString(message);
	}

	/**
	 * @param text the text message to set
	 */
	public void setTextMsg(String text) {
		setMessage(text);
		setHtml(false);
	}
	
	/**
	 * @param html the html message to set
	 */
	public void setHtmlMsg(String html) {
		setMessage(html);
		setHtml(true);
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

	/**
	 * @return the attachments
	 */
	public List<EmailAttachment> getAttachments() {
		return attachments;
	}
	
	public boolean hasAttachments() {
		return Collections.isNotEmpty(attachments);
	}

	public void addAttachment(EmailAttachment ea) {
		if (attachments == null) {
			attachments = new ArrayList<EmailAttachment>();
		}
		attachments.add(ea);
	}

	/**
	 * @return the dkimDomain
	 */
	public String getDkimDomain() {
		return dkimDomain;
	}

	/**
	 * @param dkimDomain the dkimDomain to set
	 */
	public void setDkimDomain(String dkimDomain) {
		this.dkimDomain = Strings.stripToNull(dkimDomain);
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
	 * Primes this email for signing with a DKIM domain key. Actual signing is done when sending using a <code>Mailer</code>.
	 * <p/>
	 * Also see:
	 * <pre><ul>
	 *     <li>https://postmarkapp.com/guides/dkim</li>
	 *     <li>https://github.com/markenwerk/java-utils-mail-dkim</li>
	 *     <li>http://www.gettingemaildelivered.com/dkim-explained-how-to-set-up-and-use-domainkeys-identified-mail-effectively</li>
	 *     <li>https://en.wikipedia.org/wiki/DomainKeys_Identified_Mail</li>
	 * </ul></pre>
	 *
	 * @param privateKey                DKIM private key content used to sign for the sending party.
	 * @param domain                    The domain being authorized to send.
	 * @param selector                  Additional domain specifier.
	 */
	public void signWithDomainKey(final String domain, final String selector, final String privateKey) {
		setDkimDomain(domain);
		setDkimSelector(selector);
		setDkimPrivateKey(privateKey);
	}

	public boolean isApplyDKIMSignature() {
		return Strings.isNotEmpty(dkimDomain) && Strings.isNotEmpty(dkimSelector) && Strings.isNotEmpty(dkimPrivateKey);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (Strings.isNotEmpty(msgId)) {
			sb.append("MSGID  : ").append(Strings.defaultString(msgId)).append(Streams.EOL);
		}
		if (date != null) {
			sb.append("DATE   : ").append(date).append(Streams.EOL);
		}
		if (sender != null) {
			sb.append("SENDER : ").append(sender.getAddress()).append(Streams.EOL);
		}
		if (from != null) {
			sb.append("FROM   : ").append(from).append(Streams.EOL);
		}
		if (Collections.isNotEmpty(tos)) {
			sb.append("TO     : ").append(tos).append(Streams.EOL);
		}
		if (Collections.isNotEmpty(ccs)) {
			sb.append("CC     : ").append(ccs).append(Streams.EOL);
		}
		if (Collections.isNotEmpty(bccs)) {
			sb.append("BCC    : ").append(bccs).append(Streams.EOL);
		}
		if (Collections.isNotEmpty(replyTos)) {
			sb.append("REPLYTO: ").append(replyTos).append(Streams.EOL);
		}
		if (html) {
			sb.append("HTML   : ").append(html).append(Streams.EOL);
		}
		if (Strings.isNotEmpty(charset)) {
			sb.append("CHARSET: ").append(charset).append(Streams.EOL);
		}
		if (Strings.isNotEmpty(charset)) {
			sb.append("SUBJECT: ").append(subject).append(Streams.EOL);
		}
		if (Strings.isNotEmpty(charset)) {
			sb.append("MESSAGE: ").append(message).append(Streams.EOL);
		}
		return sb.toString();
	}
}
