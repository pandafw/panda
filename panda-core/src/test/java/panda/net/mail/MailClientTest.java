package panda.net.mail;

import org.junit.Test;

import panda.lang.Randoms;


public class MailClientTest {
	public static void main(String[] args) throws Exception {
		MailClientTest sct = new MailClientTest();
		sct.sendHtmlEmailA();
	}

	protected void sendEmail(Email email) throws Exception {
		email.setFrom("test@example.com", "testテスター");
		email.addTo("squirrels.gallery@gmail.com", "oh おう");
		email.setSubject("test subject あいうえお " + Randoms.randInt());

		MailClient client = new SmtpMailClient();
//		client.setHost("localhost");
//		client.setPort(25);
//		client.setUsername("");
//		client.setPassword("");
		client.send(email);
	}

	@Test
	public void sendTextEmail() throws Exception {
		Email email = new Email();

		email.setTextMsg(".\nthis is a test email " + Randoms.randInt() + " from example.com. あいうえお");

		sendEmail(email);
	}

	@Test
	public void sendTextEmailA() throws Exception {
		Email email = new Email();

		email.setTextMsg(".\nthis is a test email " + Randoms.randInt() + " from example.com. あいうえお");
		email.addAttachment(new EmailAttachment("test.txt", "abcdefg"));
		email.addAttachment(new EmailAttachment("test2.txt", "11113322423423abcdefg"));

		sendEmail(email);
	}

	@Test
	public void sendHtmlEmail() throws Exception {
		Email email = new Email();

		email.setHtmlMsg("<pre><font color=red>.\nthis is a test email " + Randoms.randInt() + " from example.com. あいうえお</font></pre>");

		sendEmail(email);
	}

	@Test
	public void sendHtmlEmailA() throws Exception {
		Email email = new Email();

		email.setHtmlMsg("<pre><IMG src=\"cid:panda.png\"> <font color=red>.\nthis is a test email " + Randoms.randInt() + " from example.com. あいうえお</font></pre>");
		email.addAttachment(new EmailAttachment("test.txt", "abcdefg"));
		email.addAttachment(new EmailAttachment("panda.png", "panda.png", MailClientTest.class.getResourceAsStream("panda.png")));

		sendEmail(email);
	}
}
