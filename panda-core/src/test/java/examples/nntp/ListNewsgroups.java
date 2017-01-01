package examples.nntp;

import java.io.IOException;

import panda.net.nntp.NNTPClient;
import panda.net.nntp.NewsgroupInfo;

/***
 * This is a trivial example using the NNTP package to approximate the Unix newsgroups command. It
 * merely connects to the specified news server and issues fetches the list of newsgroups stored by
 * the server. On servers that store a lot of newsgroups, this command can take a very long time
 * (listing upwards of 30,000 groups).
 ***/

public final class ListNewsgroups {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: newsgroups newsserver [pattern]");
			return;
		}

		NNTPClient client = new NNTPClient();
		String pattern = args.length >= 2 ? args[1] : "";

		try {
			client.connect(args[0]);

			int j = 0;
			try {
				for (String s : client.iterateNewsgroupListing(pattern)) {
					j++;
					System.out.println(s);
				}
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println(j);

			j = 0;
			for (NewsgroupInfo n : client.iterateNewsgroups(pattern)) {
				j++;
				System.out.println(n.getNewsgroup());
			}
			System.out.println(j);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (client.isConnected()) {
					client.disconnect();
				}
			}
			catch (IOException e) {
				System.err.println("Error disconnecting from server.");
				e.printStackTrace();
				System.exit(1);
			}
		}

	}

}
