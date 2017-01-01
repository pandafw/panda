package examples.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import panda.net.PrintCommandListener;
import panda.net.pop3.POP3Client;
import panda.net.pop3.POP3MessageInfo;
import panda.net.pop3.POP3SClient;

/**
 * This is an example program demonstrating how to use the POP3[S]Client class. This program
 * connects to a POP3[S] server and retrieves the message headers of all the messages, printing the
 * From: and Subject: header entries for each message.
 * <p>
 * Usage: POP3Mail <pop3[s] server hostname> <username> <password> [secure protocol, e.g. TLS]
 */
public final class POP3Mail {

	public static final void printMessageInfo(BufferedReader reader, int id) throws IOException {
		String from = "";
		String subject = "";
		String line;
		while ((line = reader.readLine()) != null) {
			String lower = line.toLowerCase(Locale.ENGLISH);
			if (lower.startsWith("from: ")) {
				from = line.substring(6).trim();
			}
			else if (lower.startsWith("subject: ")) {
				subject = line.substring(9).trim();
			}
		}

		System.out.println(Integer.toString(id) + " From: " + from + "  Subject: " + subject);
	}

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Usage: POP3Mail <pop3 server hostname> <username> <password> [TLS [true=implicit]]");
			System.exit(1);
		}

		String server = args[0];
		String username = args[1];
		String password = args[2];

		String proto = args.length > 3 ? args[3] : null;
		boolean implicit = args.length > 4 ? Boolean.parseBoolean(args[4]) : false;

		POP3Client pop3;

		if (proto != null) {
			System.out.println("Using secure protocol: " + proto);
			pop3 = new POP3SClient(proto, implicit);
		}
		else {
			pop3 = new POP3Client();
		}
		System.out.println("Connecting to server " + server + " on " + pop3.getDefaultPort());

		// We want to timeout if a response takes longer than 60 seconds
		pop3.setDefaultTimeout(60000);

		// suppress login details
		pop3.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

		try {
			pop3.connect(server);
		}
		catch (IOException e) {
			System.err.println("Could not connect to server.");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			if (!pop3.login(username, password)) {
				System.err.println("Could not login to server.  Check password.");
				pop3.disconnect();
				System.exit(1);
			}

			POP3MessageInfo[] messages = pop3.listMessages();

			if (messages == null) {
				System.err.println("Could not retrieve message list.");
				pop3.disconnect();
				return;
			}
			else if (messages.length == 0) {
				System.out.println("No messages");
				pop3.logout();
				pop3.disconnect();
				return;
			}

			for (POP3MessageInfo msginfo : messages) {
				BufferedReader reader = (BufferedReader)pop3.retrieveMessageTop(msginfo.number, 0);

				if (reader == null) {
					System.err.println("Could not retrieve message header.");
					pop3.disconnect();
					System.exit(1);
				}
				printMessageInfo(reader, msginfo.number);
			}

			pop3.logout();
			pop3.disconnect();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
