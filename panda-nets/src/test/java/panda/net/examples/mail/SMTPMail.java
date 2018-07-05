package panda.net.examples.mail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import panda.net.PrintCommandListener;
import panda.net.io.Util;
import panda.net.smtp.SMTPClient;
import panda.net.smtp.SMTPHeader;
import panda.net.smtp.SMTPReply;

/***
 * This is an example program using the SMTP package to send a message to the specified recipients.
 * It prompts you for header information and a filename containing the message.
 ***/

public final class SMTPMail {

	public static void main(String[] args) {
		String sender, recipient, subject, filename, server, cc;
		List<String> ccList = new ArrayList<String>();
		BufferedReader stdin;
		FileReader fileReader = null;
		Writer writer;
		SMTPHeader header;
		SMTPClient client;

		if (args.length < 1) {
			System.err.println("Usage: mail smtpserver");
			System.exit(1);
		}

		server = args[0];

		stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print("From: ");
			System.out.flush();

			sender = stdin.readLine();

			System.out.print("To: ");
			System.out.flush();

			recipient = stdin.readLine();

			System.out.print("Subject: ");
			System.out.flush();

			subject = stdin.readLine();

			header = new SMTPHeader();
			header.set(SMTPHeader.FROM, sender);
			header.set(SMTPHeader.TO, recipient);
			header.set(SMTPHeader.SUBJECT, subject);

			while (true) {
				System.out.print("CC <enter one address per line, hit enter to end>: ");
				System.out.flush();

				cc = stdin.readLine();

				if (cc == null || cc.length() == 0) {
					break;
				}

				header.add(SMTPHeader.CC, cc.trim());
				ccList.add(cc.trim());
			}

			System.out.print("Filename: ");
			System.out.flush();

			filename = stdin.readLine();

			try {
				fileReader = new FileReader(filename);
			}
			catch (FileNotFoundException e) {
				System.err.println("File not found. " + e.getMessage());
			}

			client = new SMTPClient();
			client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

			client.connect(server);

			if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
				client.disconnect();
				System.err.println("SMTP server refused connection.");
				System.exit(1);
			}

			client.login();

			client.setSender(sender);
			client.addRecipient(recipient);

			for (String recpt : ccList) {
				client.addRecipient(recpt);
			}

			writer = client.sendMessageData();

			if (writer != null) {
				writer.write(header.toString());
				Util.copyReader(fileReader, writer);
				writer.close();
				client.completePendingCommand();
			}

			if (fileReader != null) {
				fileReader.close();
			}

			client.logout();

			client.disconnect();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
