package panda.net.examples.nntp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import panda.net.PrintCommandListener;
import panda.net.nntp.NNTPClient;
import panda.net.nntp.NewsgroupInfo;

/**
 * Sample program demonstrating the use of article header and body retrieval
 */
public class ArticleReader {

	public static void main(String[] args) throws SocketException, IOException {

		if (args.length != 2 && args.length != 3 && args.length != 5) {
			System.out
				.println("Usage: MessageThreading <hostname> <groupname> [<article specifier> [<user> <password>]]");
			return;
		}

		String hostname = args[0];
		String newsgroup = args[1];
		// Article specifier can be numeric or Id in form <m.n.o.x@host>
		String articleSpec = args.length >= 3 ? args[2] : null;

		NNTPClient client = new NNTPClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
		client.connect(hostname);

		if (args.length == 5) { // Optional auth
			String user = args[3];
			String password = args[4];
			if (!client.authenticate(user, password)) {
				System.out.println("Authentication failed for user " + user + "!");
				System.exit(1);
			}
		}

		NewsgroupInfo group = new NewsgroupInfo();
		client.selectNewsgroup(newsgroup, group);

		BufferedReader brHdr;
		String line;
		if (articleSpec != null) {
			brHdr = (BufferedReader)client.retrieveArticleHeader(articleSpec);
		}
		else {
			long articleNum = group.getLastArticleLong();
			brHdr = client.retrieveArticleHeader(articleNum);
		}
		if (brHdr != null) {
			while ((line = brHdr.readLine()) != null) {
				System.out.println(line);
			}
			brHdr.close();
		}
		BufferedReader brBody;
		if (articleSpec != null) {
			brBody = (BufferedReader)client.retrieveArticleBody(articleSpec);
		}
		else {
			long articleNum = group.getLastArticleLong();
			brBody = client.retrieveArticleBody(articleNum);
		}
		if (brBody != null) {
			while ((line = brBody.readLine()) != null) {
				System.out.println(line);
			}
			brBody.close();
		}
	}

}
