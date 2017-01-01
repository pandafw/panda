package panda.net.mail;

import panda.log.Log;
import panda.log.Logs;

/**
 * a class for send mail
 */
public class EmailClient extends MailClient {
	private static Log log = Logs.getLog(EmailClient.class);

	public EmailClient() {
		super();
		setLog(log);
	}
}
