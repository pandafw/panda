package examples.nntp;

import java.io.IOException;
import java.io.PrintWriter;

import panda.net.PrintCommandListener;
import panda.net.nntp.Article;
import panda.net.nntp.NNTPClient;
import panda.net.nntp.NewsgroupInfo;

/**
 * Simple class showing some of the extended commands (AUTH, XOVER, LIST ACTIVE)
 */
public class ExtendedNNTPOps {

	NNTPClient client;

	public ExtendedNNTPOps() {
		client = new NNTPClient();
		client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
	}

	private void demo(String host, String user, String password) {
		try {
			client.connect(host);

			// AUTHINFO USER/AUTHINFO PASS
			if (user != null && password != null) {
				boolean success = client.authenticate(user, password);
				if (success) {
					System.out.println("Authentication succeeded");
				}
				else {
					System.out.println("Authentication failed, error =" + client.getReplyString());
				}
			}

			// XOVER
			NewsgroupInfo testGroup = new NewsgroupInfo();
			client.selectNewsgroup("alt.test", testGroup);
			long lowArticleNumber = testGroup.getFirstArticleLong();
			long highArticleNumber = lowArticleNumber + 100;
			Iterable<Article> articles = client.iterateArticleInfo(lowArticleNumber, highArticleNumber);

			for (Article article : articles) {
				if (article.isDummy()) { // Subject will contain raw response
					System.out.println("Could not parse: " + article.getSubject());
				}
				else {
					System.out.println(article.getSubject());
				}
			}

			// LIST ACTIVE
			NewsgroupInfo[] fanGroups = client.listNewsgroups("alt.fan.*");
			for (NewsgroupInfo fanGroup : fanGroups) {
				System.out.println(fanGroup.getNewsgroup());
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ExtendedNNTPOps ops;

		int argc = args.length;
		if (argc < 1) {
			System.err.println("usage: ExtendedNNTPOps nntpserver [username password]");
			System.exit(1);
		}

		ops = new ExtendedNNTPOps();
		ops.demo(args[0], argc >= 3 ? args[1] : null, argc >= 3 ? args[2] : null);
	}

}

/*
 * Emacs configuration Local variables: ** mode: java ** c-basic-offset: 4 ** indent-tabs-mode: nil
 * ** End: **
 */
