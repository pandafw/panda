package panda.tool.mail;

import java.io.File;

import org.apache.commons.cli.CommandLine;

import panda.io.FileNames;
import panda.io.Files;
import panda.lang.Charsets;
import panda.lang.Numbers;
import panda.net.mail.Email;
import panda.net.mail.EmailAttachment;
import panda.net.mail.EmailClient;
import panda.net.mail.EmailException;
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

			addCommandLineOption("host", "SMTP Host");
			addCommandLineOption("port", "SMTP Port");
			addCommandLineOption("user", "SMTP Username");
			addCommandLineOption("pass", "SMTP Password");
			addCommandLineOption("cotm", "SMTP Connection Timeout (ms)");
			addCommandLineOption("dftm", "SMTP Default Timeout (ms)");
			addCommandLineFlag("ssl", "Enable SMTP SSL");

			addCommandLineOption("from", "FROM", true);
			addCommandLineOption("to", "TO", true);
			addCommandLineOption("cc", "CC");
			addCommandLineOption("bcc", "BCC");
			addCommandLineOption("subject", "Mail subject");
			addCommandLineOption("text", "Text message");
			addCommandLineOption("textf", "Text message file");
			addCommandLineOption("html", "HTML message");
			addCommandLineOption("htmlf", "HTML message file");
			addCommandLineOption("attach", "Attachment");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);

			setOptionParam(cl, "host");
			setOptionParam(cl, "port");
			setOptionParam(cl, "user");
			setOptionParam(cl, "pass");
			setOptionFlag(cl, "ssl");
			
			setOptionParam(cl, "from");
			setOptionParams(cl, "to");
			setOptionParams(cl, "cc");
			setOptionParams(cl, "bcc");
			setOptionParam(cl, "subject");
			setOptionParam(cl, "text");
			setOptionParam(cl, "html");

			if (cl.hasOption("textf")) {
				String f = cl.getOptionValue("textf").trim();
				String m = Files.readFileToString(new File(f), Charsets.CS_UTF_8);
				setParameter("text", m);
			}

			if (cl.hasOption("htmlf")) {
				String f = cl.getOptionValue("htmlf").trim();
				String m = Files.readFileToString(new File(f), Charsets.CS_UTF_8);
				setParameter("htmlf", m);
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
	protected EmailClient client = new EmailClient();
	protected Email email = new Email();

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		client.setHost(host);
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		client.setPort(Numbers.toInt(port, 0));
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		client.setUsername(user);
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		client.setPassword(pass);
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(boolean ssl) {
		client.setSsl(ssl);
	}

	/**
	 * @param cotm the connection timeout to set
	 */
	public void setCotm(String cotm) {
		client.setConnectTimeout(Numbers.toInt(cotm, 0));
	}

	/**
	 * @param dftm the default timeout to set
	 */
	public void setDftm(String dftm) {
		client.setDefaultTimeout(Numbers.toInt(dftm, 0));
	}

	/**
	 * @param from the from to set
	 * @throws EmailException 
	 */
	public void setFrom(String from) throws EmailException {
		email.setFrom(from);
	}

	/**
	 * @param tos the tos to set
	 * @throws EmailException 
	 */
	public void setTo(String[] tos) throws EmailException {
		for (String s : tos) {
			email.addTo(s);
		}
	}

	/**
	 * @param ccs the ccs to set
	 * @throws EmailException 
	 */
	public void setCc(String[] ccs) throws EmailException {
		for (String s : ccs) {
			email.addCc(s);
		}
	}

	/**
	 * @param bccs the bccs to set
	 * @throws EmailException 
	 */
	public void setBcc(String[] bccs) throws EmailException {
		for (String s : bccs) {
			email.addBcc(s);
		}
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		email.setSubject(subject);
	}

	/**
	 * @param message the message to set
	 */
	public void setText(String message) {
		email.setTextMsg(message);
	}

	/**
	 * @param message the html message to set
	 */
	public void setHtml(String message) {
		email.setHtmlMsg(message);
	}

	/**
	 * @param attachs attachment files
	 */
	public void setAttachments(String[] attachs) {
		for (String a : attachs) {
			EmailAttachment ea = new EmailAttachment(FileNames.getName(a), a);
			email.addAttachment(ea);
		}
	}

	/**
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		client.send(email);
	}
}
