package panda.tool.mail;

import java.io.File;
import java.util.List;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import panda.io.Files;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.lang.reflect.Fields;
import panda.log.Log;
import panda.log.Logs;
import panda.net.MXLookup;
import panda.util.tool.AbstractCommandTool;

/**
 * a class for send mail
 */
public class JavaMail {
	private static Log log = Logs.getLog(JavaMail.class);

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
		
		List<String> hosts;
		try {
			hosts = MXLookup.lookup(ss[1]);
		}
		catch (NamingException e) {
			throw new EmailException(e);
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
	
	//--------------------------------------------------
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	public static class Main extends AbstractCommandTool {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main main = new Main();
			
			Object cg = new SendMail();

			main.execute(cg, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("f", "from", "FROM", true);
			
			addCommandLineOption("t", "to", "TO", true);

			addCommandLineOption("c", "cc", "CC");

			addCommandLineOption("b", "bcc", "BCC");
			
			addCommandLineOption("s", "subject", "Mail subject");
			
			addCommandLineOption("m", "message", "Mail message");
			
			addCommandLineOption("i", "input", "Mail message file");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("f")) {
				setParameter("from", cl.getOptionValue("f").trim());
			}
			
			if (cl.hasOption("t")) {
				setParameter("to", cl.getOptionValue("t").trim());
			}

			if (cl.hasOption("s")) {
				setParameter("subject", cl.getOptionValue("s").trim());
			}

			if (cl.hasOption("m")) {
				setParameter("message", cl.getOptionValue("m").trim());
			}

			if (cl.hasOption("i")) {
				String f = cl.getOptionValue("i").trim();
				String m = Files.readFileToString(new File(f), Charsets.CS_UTF_8);
				setParameter("message", m);
			}
		}
	}
	
	/**
	 * Constructor
	 */
	public JavaMail() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String from;
	protected String to;
	protected String cc;
	protected String bcc;
	protected String subject = "";
	protected String message = "";


	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}


	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}


	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}


	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}


	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}


	/**
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}


	/**
	 * @return the bcc
	 */
	public String getBcc() {
		return bcc;
	}


	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
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
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		Email email = new SimpleEmail();
		
//		email.setHostName("smtp.gmail.com");
//		email.setSmtpPort(465);
//		email.setSSL(true);
//		email.setAuthentication("", "");
		email.setDebug(true);
		email.setCharset(Charsets.UTF_8);
		email.setFrom(from);
		email.addTo(to);
		email.setSubject(subject);
		email.setMsg(message);

//		email.send();
		send(email);
	}
}
