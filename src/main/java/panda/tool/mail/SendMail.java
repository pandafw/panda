package panda.tool.mail;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import panda.util.tool.AbstractCommandTool;

/**
 */
public class SendMail {
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
		}
	}
	
	/**
	 * Constructor
	 */
	public SendMail() {
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
		email.setFrom(from);
		email.addTo(to);
		email.setSubject(subject);
		email.setMsg(message);
		email.setDebug(true);

//		email.send();
		panda.net.SendMail.send(email);
	}
}
