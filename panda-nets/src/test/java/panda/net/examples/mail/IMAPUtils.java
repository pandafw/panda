package panda.net.examples.mail;

import java.io.IOException;
import java.net.URI;

import panda.net.ProtocolCommandListener;
import panda.net.imap.IMAPClient;
import panda.net.imap.IMAPSClient;

/**
 * Utility class for shared IMAP utilities
 */

class IMAPUtils {

	/**
	 * Parse the URI and use the details to connect to the IMAP(S) server and login.
	 * 
	 * @param uri the URI to use, e.g. imaps://user:pass@imap.mail.yahoo.com/folder or
	 *            imaps://user:pass@imap.googlemail.com/folder
	 * @param defaultTimeout initial timeout (in milliseconds)
	 * @param listener for tracing protocol IO (may be null)
	 * @return the IMAP client - connected and logged in
	 * @throws IOException if any problems occur
	 */
	static IMAPClient imapLogin(URI uri, int defaultTimeout, ProtocolCommandListener listener) throws IOException {
		final String userInfo = uri.getUserInfo();
		if (userInfo == null) {
			throw new IllegalArgumentException("Missing userInfo details");
		}

		String[] userpass = userInfo.split(":");
		if (userpass.length != 2) {
			throw new IllegalArgumentException("Invalid userInfo details: '" + userpass + "'");
		}

		String username = userpass[0];
		String password = userpass[1]; // TODO enable reading this secretly

		final IMAPClient imap;

		final String scheme = uri.getScheme();
		if ("imaps".equalsIgnoreCase(scheme)) {
			System.out.println("Using secure protocol");
			imap = new IMAPSClient(true); // implicit
		}
		else if ("imap".equalsIgnoreCase(scheme)) {
			imap = new IMAPClient();
		}
		else {
			throw new IllegalArgumentException("Invalid protocol: " + scheme);
		}
		final int port = uri.getPort();
		if (port != -1) {
			imap.setDefaultPort(port);
		}

		imap.setDefaultTimeout(defaultTimeout);

		if (listener != null) {
			imap.addProtocolCommandListener(listener);
		}

		final String server = uri.getHost();
		System.out.println("Connecting to server " + server + " on " + imap.getDefaultPort());

		try {
			imap.connect(server);
			System.out.println("Successfully connected");
		}
		catch (IOException e) {
			throw new RuntimeException("Could not connect to server.", e);
		}

		if (!imap.login(username, password)) {
			imap.disconnect();
			throw new RuntimeException("Could not login to server. Check login details.");
		}

		return imap;
	}
}
