package panda.net;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import panda.lang.Fields;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

/**
 * a class for send mail
 * @author yf.frank.wang@gmail.com
 */
public class SendMail {
	private static Log log = Logs.getLog(SendMail.class);
	private static Map<String, List<String>> hostsCache = new WeakHashMap<String, List<String>>();

	/**
	 * @param email email
	 * @return email content
	 */
	public static String getMailContent(Email email) {
		String content = Strings.EMPTY;
		
		try {
			if (email instanceof SimpleEmail) {
				content = (String)Fields.readField(email, "content", true);
			}
			else if (email instanceof HtmlEmail) {
				content = (String)Fields.readField(email, "html", true);
			}
		}
		catch (Throwable e) {
		}
		
		return content;
	}

	private static String toString(InternetAddress ia) {
		return ia.getPersonal() + '<' + ia.getAddress() + '>';
	}
	
	private static String toString(List<InternetAddress> ias) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (int i = 0; i < ias.size(); i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(toString(ias.get(i)));
		}
		sb.append(" ]");
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static void log(Email email) {
		if (log.isDebugEnabled()) {
			log.debug("\n"
					+ "============SEND EMAIL================================\n"
					+ "FROM   : " + toString(email.getFromAddress()) + "\n"
					+ "TO     : " + toString(email.getToAddresses()) + "\n"
					+ "CC     : " + toString(email.getCcAddresses()) + "\n"
					+ "BCC    : " + toString(email.getBccAddresses()) + "\n"
					+ "SUBJECT: " + email.getSubject() + "\n"
					+ "MESSAGE: " + getMailContent(email) + "\n"
					+ "=======================================================\n");
		}
	}

	/**
	 * @param email email
	 * @throws EmailException if an error occurs
	 */
	@SuppressWarnings("unchecked")
	public static void send(Email email) throws EmailException {
		try {
			log(email);

			if (Strings.isEmpty(email.getHostName())) {
				send(email.getToAddresses(), email);
				send(email.getCcAddresses(), email);
				send(email.getBccAddresses(), email);
			}
			else {
				email.send();
			}
		}
		catch (EmailException e) {
			throw e;
		}
		catch (Throwable t) {
			throw new EmailException(t);
		}
	}

	/**
	 * send email to the specified address list
	 *
	 * @param ias address list
	 * @param email email
	 * @throws EmailException if an error occurs
	 */
	public static void send(List<InternetAddress> ias, Email email) throws EmailException {
		if (ias != null && !ias.isEmpty()) {
			for (InternetAddress ia : ias) {
				send(ia, email);
			}
		}
	}

	/**
	 * send email to the specified address
	 *
	 * @param ia address
	 * @param email email
	 * @throws EmailException if an error occurs
	 */
	public static void send(InternetAddress ia, Email email) throws EmailException {
		EmailException ee = new EmailException("Invalid email address: " + ia.getAddress());

		String[] ss = ia.getAddress().split("@");
		if (ss.length != 2) {
			throw ee;
		}

		List<String> hosts = hostsCache.get(ss[1]);
		if (hosts == null || hosts.isEmpty()) {
			try {
				hosts = MXLookup.lookup(ss[1]);
				hostsCache.put(ss[1], hosts);
			}
			catch (NamingException e) {
				throw new EmailException(e);
			}
		}
		
		for (String host : hosts) {
			try {
				Fields.writeField(email, "session", null, true);
			}
			catch (Exception e) {
				throw new EmailException("failed to clear session", e);
			}

			try {
				email.setHostName(host);
				email.buildMimeMessage();

				MimeMessage message = email.getMimeMessage();

				try {
					Transport.send(message, new InternetAddress[] { ia });
				}
				catch (Throwable t) {
					String msg = "Sending the email to the following server failed : "
							+ email.getHostName() + ":" + email.getSmtpPort();
					throw new EmailException(msg, t);
				}
				return;
			}
			catch (EmailException e) {
				ee = e;
			}
		}
		throw ee;
	}
}
