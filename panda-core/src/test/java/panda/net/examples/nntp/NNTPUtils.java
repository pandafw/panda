package panda.net.examples.nntp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import panda.net.nntp.Article;
import panda.net.nntp.NNTPClient;

/**
 * Some convenience methods for NNTP example classes.
 */
public class NNTPUtils {

	/**
	 * Given an {@link NNTPClient} instance, and an integer range of messages, return an array of
	 * {@link Article} instances.
	 * 
	 * @param client the client to use
	 * @param lowArticleNumber low number
	 * @param highArticleNumber high number
	 * @return Article[] An array of Article
	 * @throws IOException on error
	 */
	public static List<Article> getArticleInfo(NNTPClient client, long lowArticleNumber, long highArticleNumber)
			throws IOException {
		List<Article> articles = new ArrayList<Article>();
		Iterable<Article> arts = client.iterateArticleInfo(lowArticleNumber, highArticleNumber);
		for (Article article : arts) {
			articles.add(article);
		}
		return articles;
	}
}
