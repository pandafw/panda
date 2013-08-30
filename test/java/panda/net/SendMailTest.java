package panda.net;

import junit.framework.TestCase;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import panda.net.SendMail;


/**
 * a test class for SendMail
 * @see SendMail
 */
public class SendMailTest extends TestCase {

	/**
	 */
	public void testSend() {
		try {
			Email email = new SimpleEmail();

			email.setDebug(true);
			email.setCharset("UTF-8");
			email.setFrom("test@gmail.com");
			email.setSubject("test subject");
//			email.addTo("fireswan@hotmail.com", "frank.wang"); // banned by microsoft
			email.addTo("squirrels.gallery@gmail.com", "frank.wang");
			email.setMsg("this is a test email");

//			email.buildMimeMessage();
//			email.getMimeMessage();

//			email.send();
			SendMail.send(email);
		}
		catch (EmailException e) {
			e.printStackTrace();
			fail();
		}
	}

}
