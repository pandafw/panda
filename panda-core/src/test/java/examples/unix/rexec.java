package examples.unix;

import java.io.IOException;

import panda.net.bsd.RExecClient;

import examples.util.IOUtil;

/***
 * This is an example program demonstrating how to use the RExecClient class. This program connects
 * to an rexec server and requests that the given command be executed on the server. It then reads
 * input from stdin (this will be line buffered on most systems, so don't expect character at a time
 * interactivity), passing it to the remote process and writes the process stdout and stderr to
 * local stdout.
 * <p>
 * Example: java rexec myhost myusername mypassword "ps -aux"
 * <p>
 * Usage: rexec <hostname> <username> <password> <command>
 ***/

// This class requires the IOUtil support class!
public final class rexec {

	public static void main(String[] args) {
		String server, username, password, command;
		RExecClient client;

		if (args.length != 4) {
			System.err.println("Usage: rexec <hostname> <username> <password> <command>");
			System.exit(1);
			return; // so compiler can do proper flow control analysis
		}

		client = new RExecClient();

		server = args[0];
		username = args[1];
		password = args[2];
		command = args[3];

		try {
			client.connect(server);
		}
		catch (IOException e) {
			System.err.println("Could not connect to server.");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			client.rexec(username, password, command);
		}
		catch (IOException e) {
			try {
				client.disconnect();
			}
			catch (IOException f) {/* ignored */
			}
			e.printStackTrace();
			System.err.println("Could not execute command.");
			System.exit(1);
		}

		IOUtil.readWrite(client.getInputStream(), client.getOutputStream(), System.in, System.out);

		try {
			client.disconnect();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}

}
