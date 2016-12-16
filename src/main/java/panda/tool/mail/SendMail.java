package panda.tool.mail;

import java.io.File;
import java.io.IOException;

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

			addCommandLineOption("H", "host", "SMTP Host");
			addCommandLineOption("P", "port", "SMTP Port");
			addCommandLineOption("U", "user", "SMTP Username");
			addCommandLineOption("W", "pass", "SMTP Password");
			addCommandLineOption("CT", "coto", "SMTP Connection Timeout (ms)");
			addCommandLineOption("DT", "dfto", "SMTP Default Timeout (ms)");
			addCommandLineFlag("ssl", "Enable SMTP SSL");

			addCommandLineOption("f", "from", "FROM", true);
			addCommandLineOption("t", "to", "TO", true);
			addCommandLineOption("c", "cc", "CC");
			addCommandLineOption("b", "bcc", "BCC");
			addCommandLineOption("s", "subject", "Mail subject");
			addCommandLineOption("t", "text", "Text message");
			addCommandLineOption("tf", "textfile", "Text message file");
			addCommandLineOption("h", "html", "HTML message");
			addCommandLineOption("hf", "htmlfile", "HTML message file");
			addCommandLineOption("a", "attach", "Attachment");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);

			setOptionParam(cl, "host");
			setOptionParam(cl, "port");
			setOptionParam(cl, "user");
			setOptionParam(cl, "pass");
			setOptionParam(cl, "coto");
			setOptionParam(cl, "dfto");
			setOptionFlag(cl, "ssl");
			
			setOptionParam(cl, "from");
			setOptionParams(cl, "to");
			setOptionParams(cl, "cc");
			setOptionParams(cl, "bcc");
			setOptionParam(cl, "subject");
			setOptionParam(cl, "text");
			setOptionParam(cl, "textfile");
			setOptionParam(cl, "html");
			setOptionParam(cl, "htmlfile");
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
	 * @param coto the connection timeout to set
	 */
	public void setCoto(String coto) {
		client.setConnectTimeout(Numbers.toInt(coto, 0));
	}

	/**
	 * @param dfto the default timeout to set
	 */
	public void setDfto(String dfto) {
		client.setDefaultTimeout(Numbers.toInt(dfto, 0));
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
	 * @param file the text file to set
	 */
	public void setTextFile(String file) throws IOException {
		String msg = Files.readFileToString(new File(file), Charsets.CS_UTF_8);
		email.setTextMsg(msg);
	}

	/**
	 * @param message the html message to set
	 */
	public void setHtml(String message) {
		email.setHtmlMsg(message);
	}

	/**
	 * @param file the html file to set
	 */
	public void setHtmlFile(String file) throws IOException {
		String msg = Files.readFileToString(new File(file), Charsets.CS_UTF_8);
		email.setHtmlMsg(msg);
	}

	/**
	 * @param attachs attachment files
	 * @throws EmailException 
	 */
	public void setAttachments(String[] attachs) throws EmailException {
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
