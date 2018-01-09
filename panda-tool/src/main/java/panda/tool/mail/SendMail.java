package panda.tool.mail;

import java.io.File;
import java.io.IOException;

import panda.args.Argument;
import panda.args.Option;
import panda.io.FileNames;
import panda.io.Files;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.net.mail.Email;
import panda.net.mail.EmailAddress;
import panda.net.mail.EmailAttachment;
import panda.net.mail.EmailClient;
import panda.net.mail.EmailException;
import panda.tool.AbstractCommandTool;

/**
 */
public class SendMail extends AbstractCommandTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new SendMail().execute(args);
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
	@Option(opt='h', option="host", arg="HOST", usage="SMTP Host")
	public void setHost(String host) {
		client.setHost(host);
	}

	/**
	 * @param port the port to set
	 */
	@Option(opt='P', option="port", arg="PORT", usage="SMTP Port")
	public void setPort(String port) {
		client.setPort(Numbers.toInt(port, 0));
	}

	/**
	 * @param user the user to set
	 */
	@Option(opt='u', option="user", arg="USER", usage="SMTP Username")
	public void setUser(String user) {
		client.setUsername(user);
	}

	/**
	 * @param pass the pass to set
	 */
	@Option(opt='p', option="pass", arg="PASS", usage="SMTP Password")
	public void setPass(String pass) {
		client.setPassword(pass);
	}

	/**
	 * @param ssl the ssl to set
	 */
	@Option(opt='S', option="ssl", usage="Enable SMTP SSL")
	public void setSsl(boolean ssl) {
		client.setSsl(ssl);
	}

	/**
	 * @param coto the connection timeout to set
	 */
	@Option(opt='C', option="ctime", arg="MS", usage="SMTP Connection Timeout (ms)")
	public void setCoto(String coto) {
		client.setConnectTimeout(Numbers.toInt(coto, 0));
	}

	/**
	 * @param dfto the default timeout to set
	 */
	@Option(opt='D', option="dtime", arg="MS", usage="SMTP Default Timeout (ms)")
	public void setDfto(String dfto) {
		client.setDefaultTimeout(Numbers.toInt(dfto, 0));
	}

	/**
	 * @param from the from to set
	 * @throws EmailException if an error occurs
	 */
	@Option(opt='f', option="from", arg="FROM", usage="From Address")
	public void setFrom(String from) throws EmailException {
		email.setFrom(from);
	}

	/**
	 * @param tos the tos to set
	 * @throws EmailException if an error occurs
	 */
	@Option(opt='t', option="to", arg="TO", usage="To Address")
	public void setTo(String tos) throws EmailException {
		email.setTos(EmailAddress.parseList(tos));
	}

	/**
	 * @param ccs the ccs to set
	 * @throws EmailException if an error occurs
	 */
	@Option(opt='c', option="cc", arg="CC", usage="Cc Address")
	public void setCc(String ccs) throws EmailException {
		email.setCcs(EmailAddress.parseList(ccs));
	}

	/**
	 * @param bccs the bccs to set
	 * @throws EmailException if an error occurs
	 */
	@Option(opt='b', option="bcc", arg="BCC", usage="Bcc Address")
	public void setBcc(String bccs) throws EmailException {
		email.setBccs(EmailAddress.parseList(bccs));
	}

	/**
	 * @param subject the subject to set
	 */
	@Option(opt='s', option="subject", arg="TEXT", usage="Mail subject")
	public void setSubject(String subject) {
		email.setSubject(subject);
	}

	/**
	 * @param message the message to set
	 */
	@Option(opt='m', option="message", arg="TEXT", usage="Mail message")
	public void setMessage(String message) {
		email.setMessage(message);
	}

	/**
	 * @param file the message file to set
	 * @throws IOException if an IO error occurred
	 */
	@Option(opt='M', option="file", arg="FILE", usage="Mail message file")
	public void setMessageFile(String file) throws IOException {
		String msg = Files.readFileToString(new File(file), Charsets.CS_UTF_8);
		email.setMessage(msg);
	}

	/**
	 * @param html the html message to set
	 */
	@Option(opt='H', option="html", usage="HTML message")
	public void setHtml(boolean html) {
		email.setHtml(html);
	}

	/**
	 * @param attachs attachment files
	 * @throws EmailException if an error occurs
	 */
	@Argument(name="attachments", usage="Attachments")
	public void setAttachments(String[] attachs) throws EmailException {
		for (String a : attachs) {
			EmailAttachment ea = new EmailAttachment(FileNames.getName(a), a);
			email.addAttachment(ea);
		}
	}

	/**
	 * execute
	 */
	public void execute() {
		try {
			client.send(email);
		}
		catch (EmailException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
