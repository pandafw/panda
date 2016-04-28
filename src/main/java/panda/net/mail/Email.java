package panda.net.mail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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
import panda.lang.Regexs;
import panda.lang.Strings;
import panda.net.Mimes;

public class Email {
	private String charset = Charsets.UTF_8;
	private EmailAddress from;
	private Set<EmailAddress> rcpts;
	private List<EmailAddress> tos;
	private List<EmailAddress> ccs;
	private List<EmailAddress> bccs;
	private String subject = "";
	private String message = "";
	private boolean html;
	private List<EmailAttachment> attachments;

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
	 * @return the from address
	 */
	public String getSender() {
		return from.getAddress();
	}
	
	/**
	 * @return the from
	 */
	public EmailAddress getFrom() {
		return from;
	}
	
	public String getEncodedFrom() throws EmailException {
		return Email.toEncodedAddress(Arrays.asList(from), charset);
	}

	/**
	 * @param addr the from to set
	 * @throws EmailException 
	 */
	public void setFrom(String addr) throws EmailException {
		setFrom(addr, null);
	}

	/**
	 * @param addr the from address to set
	 * @param name the from name
	 * @throws EmailException 
	 */
	public void setFrom(String addr, String name) throws EmailException {
		this.from = toEmailAddress(addr, name);
	}

	/**
	 * add to
	 * @param addr to address
	 */
	public void addTo(String addr) throws EmailException {
		addTo(addr, null);
	}

	/**
	 * add to
	 * @param addr address
	 * @param name name
	 */
	public void addTo(String addr, String name) throws EmailException {
		EmailAddress ea = toEmailAddress(addr, name);
		if (tos == null) {
			tos = new ArrayList<EmailAddress>();
		}
		
		tos.add(ea);
		addRcpt(ea);
	}
	
	/**
	 * @return the tos
	 */
	public List<EmailAddress> getTos() {
		return Collections.unmodifiableList(tos);
	}
	
	public String getEncodedTos() throws EmailException {
		return Email.toEncodedAddress(tos, charset);
	}

	/**
	 * add cc
	 * @param addr cc address
	 * @throws EmailException 
	 */
	public void addCc(String addr) throws EmailException {
		addCc(addr, null);
	}

	/**
	 * add cc
	 * @param addr address
	 * @param name name
	 * @throws EmailException 
	 */
	public void addCc(String addr, String name) throws EmailException {
		EmailAddress ea = toEmailAddress(addr, name);
		if (ccs == null) {
			ccs = new ArrayList<EmailAddress>();
		}
		
		ccs.add(ea);
		addRcpt(ea);
	}

	/**
	 * @return the ccs
	 */
	public List<EmailAddress> getCcs() {
		return Collections.unmodifiableList(ccs);
	}
	
	public String getEncodedCcs() throws EmailException {
		return Email.toEncodedAddress(ccs, charset);
	}

	/**
	 * add bcc
	 * @param addr bcc address
	 * @throws EmailException 
	 */
	public void addBcc(String addr) throws EmailException {
		addBcc(addr, null);
	}

	/**
	 * add bcc
	 * @param addr address
	 * @param name name
	 * @throws EmailException 
	 */
	public void addBcc(String addr, String name) throws EmailException {
		EmailAddress ea = toEmailAddress(addr, name);
		if (bccs == null) {
			bccs = new ArrayList<EmailAddress>();
		}
		
		bccs.add(ea);
		addRcpt(ea);
	}

	/**
	 * @return the bccs
	 */
	public List<EmailAddress> getBccs() {
		return Collections.unmodifiableList(bccs);
	}
	
	/**
	 * @return the rcpts
	 */
	public Set<EmailAddress> getRcpts() {
		return Collections.unmodifiableSet(rcpts);
	}

	public Map<String, List<EmailAddress>> getRcptsByDomain() {
		Map<String, List<EmailAddress>> m = new HashMap<String, List<EmailAddress>>();
		for (EmailAddress r : rcpts) {
			String d = getMailDomain(r.getAddress());
			List<EmailAddress> rs = m.get(d);
			if (rs == null) {
				rs = new ArrayList<EmailAddress>();
				m.put(d, rs);
			}
			rs.add(r);
		}
		return m;
	}

	private void addRcpt(EmailAddress addr) {
		if (rcpts == null) {
			rcpts = new HashSet<EmailAddress>();
		}
		rcpts.add(addr);
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
	
	public static EmailAddress toEmailAddress(String addr, String name) throws EmailException {
		if (Strings.isEmpty(addr)) {
			throw new EmailException("Empty mail address");
		}

		if (Strings.containsAny(name, "\"<>")) {
			throw new EmailException("Empty mail name: " + name);
		}
		
		if (!Regexs.isEmail(addr)) {
			throw new EmailException("Invalid email address: " + addr);
		}
		
		return new EmailAddress(addr, name);
	}
	
	public static String getMailAddress(String rcpt) {
		int a = rcpt.indexOf('<');
		if (a >= 0) {
			int b = rcpt.indexOf('>', a);
			rcpt = rcpt.substring(a + 1, b);
		}
		return rcpt.toLowerCase();
	}

	public static String getMailDomain(String addr) {
		return Strings.substringAfter(addr, '@');
	}
	
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * @return the encoded subject
	 * @throws UnsupportedEncodingException 
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
	 * @param text the text message to set
	 */
	public void setTextMsg(String text) {
		this.message = Strings.defaultString(text);
		this.html = false;
	}
	
	/**
	 * @param html the html message to set
	 */
	public void setHtmlMsg(String html) {
		this.message = Strings.defaultString(html);
		this.html = true;
	}
	
	/**
	 * @return the html
	 */
	public boolean isHtml() {
		return html;
	}

	/**
	 * @return the attachments
	 */
	public List<EmailAttachment> getAttachments() {
		return Collections.unmodifiableList(attachments);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Charset: ").append(charset).append(Streams.LINE_SEPARATOR);
		sb.append("Html   : ").append(html).append(Streams.LINE_SEPARATOR);
		sb.append("FROM   : ").append(from).append(Streams.LINE_SEPARATOR);
		sb.append("TO     : ").append(tos).append(Streams.LINE_SEPARATOR);
		sb.append("CC     : ").append(ccs).append(Streams.LINE_SEPARATOR);
		sb.append("BCC    : ").append(bccs).append(Streams.LINE_SEPARATOR);
		sb.append("SUBJECT: ").append(subject).append(Streams.LINE_SEPARATOR);
		sb.append("MESSAGE: ").append(message).append(Streams.LINE_SEPARATOR);
		return sb.toString();
	}
}
