package panda.net;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;


/**
 * a test class for SendMail
 * @see SendMail
 */
public class SendMailTest {

	/**
	 */
	@Test
	public void testSend() throws Exception {
		Email email = new SimpleEmail();

		email.setDebug(true);
		email.setCharset("UTF-8");
		email.setFrom("test@gmail.com");
		email.setSubject("test subject");
//			email.addTo("fireswan@hotmail.com", "fire"); // banned by microsoft
		email.addTo("squirrels.gallery@gmail.com", "oh test");
		email.setMsg("this is a test email");

//			email.buildMimeMessage();
//			email.getMimeMessage();

//			email.send();
		SendMail.send(email);
	}

}
