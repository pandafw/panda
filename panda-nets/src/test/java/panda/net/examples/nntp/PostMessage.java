package panda.net.examples.nntp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import panda.net.PrintCommandListener;
import panda.net.io.Util;
import panda.net.nntp.NNTPClient;
import panda.net.nntp.NNTPReply;
import panda.net.nntp.SimpleNNTPHeader;

/***
 * This is an example program using the NNTP package to post an article to the specified
 * newsgroup(s). It prompts you for header information and a filename to post.
 ***/

public final class PostMessage {

	public static void main(String[] args) {
		String from, subject, newsgroup, filename, server, organization;
		String references;
		BufferedReader stdin;
		FileReader fileReader = null;
		SimpleNNTPHeader header;
		NNTPClient client;

		if (args.length < 1) {
			System.err.println("Usage: post newsserver");
			System.exit(1);
		}

		server = args[0];

		stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print("From: ");
			System.out.flush();

			from = stdin.readLine();

			System.out.print("Subject: ");
			System.out.flush();

			subject = stdin.readLine();

			header = new SimpleNNTPHeader(from, subject);

			System.out.print("Newsgroup: ");
			System.out.flush();

			newsgroup = stdin.readLine();
			header.addNewsgroup(newsgroup);

			while (true) {
				System.out.print("Additional Newsgroup <Hit enter to end>: ");
				System.out.flush();

				// Of course you don't want to do this because readLine() may be null
				newsgroup = stdin.readLine().trim();

				if (newsgroup.length() == 0) {
					break;
				}

				header.addNewsgroup(newsgroup);
			}

			System.out.print("Organization: ");
			System.out.flush();

			organization = stdin.readLine();

			System.out.print("References: ");
			System.out.flush();

			references = stdin.readLine();

			if (organization != null && organization.length() > 0) {
				header.addHeaderField("Organization", organization);
			}

			if (references != null && references.length() > 0) {
				header.addHeaderField("References", references);
			}

			header.addHeaderField("X-Newsreader", "NetComponents");

			System.out.print("Filename: ");
			System.out.flush();

			filename = stdin.readLine();

			try {
				fileReader = new FileReader(filename);
			}
			catch (FileNotFoundException e) {
				System.err.println("File not found. " + e.getMessage());
				System.exit(1);
			}

			client = new NNTPClient();
			client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

			client.connect(server);

			if (!NNTPReply.isPositiveCompletion(client.getReplyCode())) {
				client.disconnect();
				System.err.println("NNTP server refused connection.");
				System.exit(1);
			}

			if (client.isAllowedToPost()) {
				Writer writer = client.postArticle();

				if (writer != null) {
					writer.write(header.toString());
					Util.copyReader(fileReader, writer);
					writer.close();
					client.completePendingCommand();
				}
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
