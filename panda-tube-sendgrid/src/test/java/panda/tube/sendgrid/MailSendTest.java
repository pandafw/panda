package panda.tube.sendgrid;

import org.junit.Assume;
import org.junit.Test;

import panda.lang.Strings;
import panda.tube.sendgrid.Content;
import panda.tube.sendgrid.Email;
import panda.tube.sendgrid.Mail;
import panda.tube.sendgrid.SendGrid;

public class MailSendTest {

	@Test
	public void testSendText() throws Exception {
		String apikey = System.getenv("SENDGRID_API_KEY");
		if (Strings.isEmpty(apikey)) {
			Assume.assumeTrue(false);
		}
		
		Email from = new Email("test@example.com");
		String subject = "Sending with SendGrid is Fun";

		Email to = new Email("test@example.com");
		Content content = new Content("text/plain", "and easy to do anywhere, even with Java");

		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid(apikey);
		sg.mailSend(mail);
	}
}
