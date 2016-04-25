package panda.net.smtp;

import java.io.PrintWriter;
import java.io.Writer;

import panda.lang.Randoms;
import panda.net.MXLookup;
import panda.net.PrintCommandListener;

public class SMTPClientTest {
	public static void main(String[] args) throws Exception {
		SMTPClientTest sct = new SMTPClientTest();
		sct.sendEmail();
	}
	
	public void sendEmail() throws Exception {
		String hostname = MXLookup.lookup("gmail.com").get(0);
		int port = 25;

		String from = "test@example.com";

		String subject = "This is a test subject";
		String text = "This is a test message";

		AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();
		try {
			String to = "squirrels.gallery@gmail.com";
			
			client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

			// optionally set a timeout to have a faster feedback on errors
			client.setDefaultTimeout(10 * 1000);
			// you connect to the SMTP server
			client.connect(hostname, port);
			if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
				client.disconnect();
				System.err.println("SMTP server refused connection.");
				System.exit(1);
			}

			// you say ehlo and you specify the host you are connecting from, could be anything
			client.ehlo("127.0.0.1");
			// if your host accepts STARTTLS, we're good everything will be encrypted, otherwise
			// we're done here
//			if (!client.execTLS()) {
//				System.out.println("STARTTLS was not accepted " + client.getReply() + client.getReplyString());
//			}
			
			//client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, login, password);
			//checkReply(client);

			client.setSender(from);
			checkReply(client);

			client.addRecipient(to);
			checkReply(client);

			Writer writer = client.sendMessageData();

			if (writer != null) {
				SMTPHeader header = new SMTPHeader();
				header.set(SMTPHeader.FROM, "test <test@example.com>");
				header.set(SMTPHeader.TO, "oh test <squirrels.gallery@gmail.com>");
				header.set(SMTPHeader.SUBJECT, subject);
				header.set("MIME-Version", "1.0");
				header.set("Content-Type", "text/plain; charset=UTF-8");
				header.set("Content-Transfer-Encoding", "7bit");
				header.set("Message-ID", Randoms.randUUID());
				System.out.println(header.toString());
				writer.write(header.toString());
				writer.write(text);
				writer.close();
				if (!client.completePendingCommand()) {// failure
					throw new Exception("Failure to send the email " + client.getReply() + client.getReplyString());
				}
			}
			else {
				throw new Exception("Failure to send the email " + client.getReply() + client.getReplyString());
			}
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			try {
				client.logout();
				client.disconnect();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void checkReply(SMTPClient sc) throws Exception {
		if (SMTPReply.isNegativeTransient(sc.getReplyCode())) {
			throw new Exception("Transient SMTP error " + sc.getReply() + sc.getReplyString());
		}
		else if (SMTPReply.isNegativePermanent(sc.getReplyCode())) {
			throw new Exception("Permanent SMTP error " + sc.getReply() + sc.getReplyString());
		}
	}
}
